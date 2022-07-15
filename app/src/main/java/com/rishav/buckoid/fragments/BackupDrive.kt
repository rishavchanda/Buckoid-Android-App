package com.rishav.buckoid.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.common.Scopes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import com.rishav.buckoid.Database.TransactionDatabase
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.FragmentBackupDriveBinding
import com.rishav.buckoid.Model.Profile
import com.rishav.buckoid.Model.Transaction
import com.rishav.buckoid.ViewModel.TransactionViewModel
import java.io.*
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class BackupDrive : Fragment() {

    lateinit var binding:FragmentBackupDriveBinding
    lateinit var profileModel:Profile
    private val dbPath = "/data/data/com.rishav.buckoid/databases/Transaction"
    private val dbPathWal = "/data/data/com.rishav.buckoid/databases/Transaction-wal"
    private val dbPathShm = "/data/data/com.rishav.buckoid/databases/Transaction-shm"

    private val STORAGE_PERMISSION_CODE = 100
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding =  FragmentBackupDriveBinding.inflate(inflater, container, false)
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNav.visibility = View.GONE

        setData()
        return binding.root
    }

    private fun setData() {
        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_backupDrive_to_profile)
        }

        binding.backupBtn.setOnClickListener{
            //backupdata()
            Toast.makeText(requireActivity(),"Coming Soon wait for an Update.", Toast.LENGTH_SHORT).show()
            //notifyUser("Coming Soon wait for an Update.")
        }
    }

    private fun notifyUser(message: String) {
        Toast.makeText(requireActivity(),message, Toast.LENGTH_SHORT).show()
    }

    private fun backupdata() {
        profileModel = Profile(requireContext())
        val googleSignInAccount = profileModel.account
            val credential =
            GoogleAccountCredential.usingOAuth2(requireContext(), Collections.singleton(Scopes.DRIVE_FILE))
        credential.selectedAccount = googleSignInAccount!!.account
        val googleDriveService = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName(getString(com.rishav.buckoid.R.string.app_name))
            .build()
        Thread {
            upload(googleDriveService)
        }.start()
    }

    private fun upload(googleDriveDevices:Drive){
        val storageFile:com.google.api.services.drive.model.File =com.google.api.services.drive.model.File()
        //storageFile.setParents(Collections.singletonList("appDataFolder"))
        storageFile.setName("Transaction")

        val storageFileShm:com.google.api.services.drive.model.File = com.google.api.services.drive.model.File()
        //storageFile.setParents(Collections.singletonList("appDataFolder"))
        storageFile.setName("Transaction-shm")

        val storageFileWal:com.google.api.services.drive.model.File = com.google.api.services.drive.model.File()
        //storageFile.setParents(Collections.singletonList("appDataFolder"))
        storageFile.setName("Transaction-wal")

        val filePath:java.io.File = java.io.File(dbPath)
        //val filePathShm:java.io.File = java.io.File(dbPathShm)
        //val filePathWal:java.io.File = java.io.File(dbPathWal)
        val mediaContent:FileContent = FileContent("",filePath)
        //val mediaContentShm:FileContent = FileContent("",filePathShm)
        //val mediaContentWal:FileContent = FileContent("",filePathWal)
        try {
            val file: com.google.api.services.drive.model.File? = googleDriveDevices.files().create(storageFile,mediaContent).execute();
            if (file != null) {
                Log.w("@@@","Filename: %s File ID: %s ${file.getName()}, ${file.getId()}")
            }

            /*val fileWal: com.google.api.services.drive.model.File? = googleDriveDevices.files().create(storageFile,mediaContentWal).execute();
            if (fileWal != null) {
                Log.w("@@@","Filename: %s File ID: %s ${fileWal.getName()}, ${fileWal.getId()}")
            }
            val fileShm: com.google.api.services.drive.model.File? = googleDriveDevices.files().create(storageFileShm,mediaContentShm).execute();
            if (fileShm != null) {
                Log.w("@@@","Filename: %s File ID: %s ${fileShm.getName()}, ${fileShm.getId()}")
            }*/

        }
        catch(e: UserRecoverableAuthIOException){
            Log.w("@@@","errorAuthIO:"+e.message.toString())
        }
        catch (e:Exception) {
            Log.w("@@@","error:"+e.message.toString())
        }
    }


    private fun Download(googleDriveService:Drive) {
        try {
            val dir = java.io.File("/data/data/com.rishav.buckoid/databases")
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    Log.e("@@@", "Found file: ${children[i]}")
                    java.io.File(dir, children[i]).delete()
                }
                //Log.e("@@@", "Found file: ${children}")

                val files: FileList = googleDriveService.files().list()
                //.setSpaces("appDataFolder")
                .setFields("nextPageToken, files(id, name, createdTime, size)")
                .setPageSize(10)
                .execute()
            if (files.files.size == 0) Log.e("@@@", "No DB file exists in Drive")
            for (file in files.files) {
                Log.e("@@@", "Found file: ${file.name}, ${file.id}, ${file.createdTime}, ${file.size}")
                if (file.name.equals("Transaction")) {
                    val outputStream: OutputStream = FileOutputStream(dbPath)
                    googleDriveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)
                }else if(file.getName().equals("Transaction-shm")){
                    val outputStream: OutputStream = FileOutputStream(dbPathShm)
                    googleDriveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)
                }
                else if(file.getName().equals("Transaction-wal")){
                    val outputStream: OutputStream = FileOutputStream(dbPathWal)
                    googleDriveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)
                }
            }
                Log.e("@@@", "Found file: ${children}")
            }
        } catch (e: IOException) {
            Log.w("@@@","error:"+e.message.toString())
        }
    }




    /*private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

            try{
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", requireActivity().packageName, null )
                intent.setData(uri)
                startActivityForResult(intent,STORAGE_PERMISSION_CODE)

            } catch (Exception err){
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent,STORAGE_PERMISSION_CODE)

            }
        }else{
            ActivityCompat.requestPermissions(this.requireActivity(),Manifest.permission)
        }
    }*/

   /* private fun exportCSV(){
        val folder = File(Environment.getExternalStorageDirectory().path+"/"+"RoomDbBackup");

        var isFolderCreated = false;
        if(!folder.exists()){
            isFolderCreated = folder.mkdir();
        }

        val csvFileName = "RoomDB_Backup.csv";
        val filePathAndName: String = folder.toString()+"/"+csvFileName;

        var recordList = LiveData<List<Transaction>>()
        var db = Room.databaseBuilder(requireContext(),TransactionDatabase::class.java,"Transaction").allowMainThreadQueries().build()
        var transactionDao = db.myTransactionDao()
        var transactions = transactionDao.getTransaction()
        recordList = transactions
        try{
                val fileWriter = FileWriter(filePathAndName)
                for(i in recordList) {
                    fileWriter.append("" + i.id)
                    fileWriter.append(",")
                    fileWriter.append("" + i.type)
                    fileWriter.append(",")
                    fileWriter.append("" + i.title)
                    fileWriter.append(",")
                    fileWriter.append("" + i.amount)
                    fileWriter.append(",")
                    fileWriter.append("" + i.note)
                    fileWriter.append(",")
                    fileWriter.append("" + i.date)
                    fileWriter.append(",")
                    fileWriter.append("" + i.day)
                    fileWriter.append(",")
                    fileWriter.append("" + i.month)
                    fileWriter.append(",")
                    fileWriter.append("" + i.year)
                    fileWriter.append(",")
                    fileWriter.append("" + i.category)
                    fileWriter.append("\n")

                }
                fileWriter.flush();
                fileWriter.close();
                Toast.makeText(requireContext(),"backup exported to " + filePathAndName,Toast.LENGTH_SHORT).show()

            }catch (e: Exception){
                Toast.makeText(requireContext(),""+e.message,Toast.LENGTH_LONG).show()
            }

    }
*/

}