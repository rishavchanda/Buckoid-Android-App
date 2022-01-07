package com.rishav.buckoid

import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.internal.e
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.rishav.buckoid.databinding.ActivityUserDetailsBinding
import java.io.File
import java.util.*

class UserDetails : AppCompatActivity() {
    lateinit var binding:ActivityUserDetailsBinding
    lateinit var userDetails: SharedPreferences
    var isFirstTime:Boolean = false



    lateinit var client: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TrackBack)
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account!=null){
            Log.w("@@@@@@@",account?.displayName.toString())
            val credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(Scopes.DRIVE_FILE))
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
        setData()
    }

    private fun setData() {
        userDetails = this.getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        isFirstTime = userDetails.getBoolean("isFirstTime",true)
        if (!isFirstTime){
            //goToNextScreen()
            signIn()
        }else {
            binding.next.setOnClickListener {
                //saveUserData()
                signIn()
            }
        }
    }

    private fun goToNextScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveUserData() {
        val name = binding.editName.text.toString()
        val monthly_budget = binding.editMoney.text.toString()
        val yearly_budget = binding.editYearMoney
        if(name.equals("") || monthly_budget.equals("") || yearly_budget.text.toString().equals("")) {
            Toast.makeText(this, "Enter all details to continue...", Toast.LENGTH_SHORT).show()
        }else{
            val editor: SharedPreferences.Editor = userDetails.edit()
            editor.putBoolean("isFirstTime", false)
            editor.putString("Name", name)
            editor.putString("MonthlyBudget", monthly_budget)
            editor.putString("YearlyBudget", yearly_budget.text.toString())
            editor.apply()
            goToNextScreen()
        }
    }





    fun googleCall(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        client = GoogleSignIn.getClient(this@UserDetails, gso)
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
                    .usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE))
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
        val storageFile:com.google.api.services.drive.model.File ?= null
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
        }
    }


}