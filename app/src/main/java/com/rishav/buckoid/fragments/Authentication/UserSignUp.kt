package com.rishav.buckoid.fragments.Authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.FragmentUserSignUpBinding
import java.util.*

class UserSignUp : Fragment() {
    lateinit var binding:FragmentUserSignUpBinding
    lateinit var client: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=  FragmentUserSignUpBinding.inflate(inflater, container, false)
        //setUpSignUp()
        return binding.root
    }

    private fun setUpSignUp() {
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (account!=null){
            Log.w("@@@@@@@",account?.displayName.toString())
            val credential =
                GoogleAccountCredential.usingOAuth2(requireActivity(), Collections.singleton(Scopes.DRIVE_FILE))
            credential.selectedAccount = account.getAccount()
            val googleDriveService = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory(),
                credential)
                .setApplicationName(getString(R.string.app_name))
                .build()
            Thread(Runnable {
                upload(googleDriveService)
            }).start()

        }
        googleCall()
        //setData()
    }



    fun googleCall(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        client = GoogleSignIn.getClient(requireActivity(), gso)
    }
    private fun signIn() {
        val signInIntent: Intent = client.signInIntent
        getResult.launch(signInIntent)
    }
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                handleSignInIntent(it.data)
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
        }
    }

    private fun handleSignInIntent(data: Intent?) {

        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnSuccessListener {
                val credential: GoogleAccountCredential = GoogleAccountCredential
                    .usingOAuth2(requireActivity(), Collections.singleton(DriveScopes.DRIVE_FILE))
                credential.setSelectedAccount(it.account)
                val googleDriveDevices: Drive = Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential)
                    .setApplicationName("Buckoid")
                    .build()

                Thread(Runnable {
                    // upload(googleDriveDevices)
                }).start()
            }
            .addOnFailureListener{

            }

    }

    private fun upload(googleDriveDevices:Drive){
        /*val storageFile:com.google.api.services.drive.model.File ?= null
        storageFile?.setParents(Collections.singletonList("appDataFolder"))
        storageFile?.setName("Transaction")

        val db = getDatabasePath("Transaction").getAbsolutePath()

        val filePath:java.io.File = java.io.File(db);
        val mediaContent: FileContent = FileContent("",filePath);
        try {
            val file: com.google.api.services.drive.model.File? = googleDriveDevices.files().create(storageFile,mediaContent).execute();
            if (file != null) {

                Log.w("@@@","Filename: %s File ID: %s ${file.getName()}, ${file.getId()}")
            }
        }
        catch (e:Exception) {
            Log.w("@@@","error:"+e.message.toString())
        }*/
    }


}