package com.dreams.best

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReferrerDataReceiver: BroadcastReceiver() {

    val ACTION_UPDATE_DATA = "ACTION_UPDATE_DATA"
    val ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER"
    val KEY_REFERRER = "referrer"

    val REFERRER_DATA = "REFERRER_DATA"

    private lateinit var database: DatabaseReference

    override fun onReceive(context: Context?, intent: Intent?) {
        database = FirebaseDatabase.getInstance().reference

        if (intent == null) {
            database.child("Log").push().setValue("RefererDataReciever: Intent is null")
            return
        }
        if (ACTION_INSTALL_REFERRER != intent.action){
            database.child("Log").push().setValue("RefererDataReciever: Wrong action! Expected:  + $ACTION_INSTALL_REFERRER but was: ${intent.action}")
            return
        }
        val extras = intent.extras
        if (intent.extras == null) {
            database.child("Log").push().setValue("RefererDataReciever: No data in intent")
            return
        }

        val sp = PreferenceManager.getDefaultSharedPreferences(context?.applicationContext)
        if (!sp.contains(REFERRER_DATA)) {
            sp.edit().putString(REFERRER_DATA, extras?.get(KEY_REFERRER) as String).apply()
        }
        if (!sp.contains("mytest")) {
            sp.edit().putString("mytest", extras?.toString()).apply()
        }

        if (context != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(ACTION_UPDATE_DATA))
        }
    }

}