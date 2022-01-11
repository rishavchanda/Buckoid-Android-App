package com.rishav.buckoid.Model

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class Profile(context:Context){
    val mContext:Context = context
    val name = getData()?.displayName.toString()
    val profilePic = getData()?.photoUrl
    val email = getData()?.email
    val account = getData()

    fun getData(): GoogleSignInAccount? {
        val acct = GoogleSignIn.getLastSignedInAccount(mContext)
        return acct
    }

}