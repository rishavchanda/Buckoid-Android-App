package com.rishav.buckoid

import android.app.KeyguardManager
import android.content.*
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.rishav.buckoid.databinding.ActivityMainBinding
import java.util.*

import android.content.Intent


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding:ActivityMainBinding
    lateinit var mNightModeSwitch: SwitchCompat
    lateinit var userDetails:SharedPreferences
    private var appUpdate: AppUpdateManager? = null
    var isNight:Boolean = false

    //finger print
    var isFingerPrintEnabled:Boolean = false
    private var cancellationSignal:CancellationSignal?=null
    private val authenticationCallback:BiometricPrompt.AuthenticationCallback
    get() =
        @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                finish()
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
            }
        }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            userDetails = this.getSharedPreferences("UserDetails", MODE_PRIVATE)
            isFingerPrintEnabled = userDetails.getBoolean("fingerprint_enabled", false)
            if (isFingerPrintEnabled) {
                fingerPrintSensor()
            }
        }
            setTheme(R.style.Theme_TrackBack)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            nightMode()
            inAppUpdater()
            binding.navigationView.setNavigationItemSelectedListener(this)
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            binding.bottomNavigation.setupWithNavController(navController)

    }

    fun nightMode(){
        // Configure night-mode switch
        isNight = userDetails.getBoolean("nightMode",true)
        val actionLayout:View = binding.navigationView.getMenu().findItem(R.id.dark_mode).getActionView()
        mNightModeSwitch = actionLayout.findViewById(R.id.night_switch_compat)
        if (isNight) {
            mNightModeSwitch.setChecked(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            mNightModeSwitch.setChecked(false)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        mNightModeSwitch.setOnCheckedChangeListener { buttonView, isChecked -> applyNightMode(isChecked) }

    }

    private fun applyNightMode(checked: Boolean) {
        if (checked) {
            isNight = true
            saveSettingsBoolean("nightMode",true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            isNight = false
            saveSettingsBoolean("nightMode",false)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //restartActivityInvalidateBackstack(this)
    }
    private fun saveSettingsBoolean(mode: String, isNight: Boolean) {
        val editor: SharedPreferences.Editor = userDetails.edit()
        editor.putBoolean(mode, isNight)
        editor.apply()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if(id == R.id.dark_mode){
            mNightModeSwitch.performClick()
        }else if(id == R.id.nav_share){
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "@string/app_name")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id="+ this@MainActivity.getPackageName()
                )
                startActivity(Intent.createChooser(intent, "Share With"))
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Unable to share at this moment.." + e.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else if(id == R.id.nav_RateUs){
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }else if(id == R.id.nav_aboutUs){
            Toast.makeText(
                this,
                "Working on this wait for update",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true
    }

    private fun inAppUpdater() {
        appUpdate = AppUpdateManagerFactory.create(this)

        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if(updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                appUpdate!!.startUpdateFlowForResult(updateInfo,AppUpdateType.IMMEDIATE,this,100)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun inProgressUpdate() {
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if(updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdate!!.startUpdateFlowForResult(updateInfo,AppUpdateType.IMMEDIATE,this,100)
            }

        }
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun fingerPrintSensor() {
        checkBiometricSupport()
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setDeviceCredentialAllowed(true)
            .setTitle("Authentication Required")
            .setDescription("Please enter your PIN / password to continue")
            .build()

        biometricPrompt.authenticate(getCancellationSignal(),mainExecutor,authenticationCallback)


    }

    private fun getCancellationSignal(): CancellationSignal{
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")

        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager : KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure){
            notifyUser("Finger print not enabled in settings")
            return false
        }

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.USE_BIOMETRIC)!= PackageManager.PERMISSION_GRANTED){
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true

    }

    private fun notifyUser(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }



}