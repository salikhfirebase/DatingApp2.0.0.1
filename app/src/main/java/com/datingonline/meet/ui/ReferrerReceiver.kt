package com.datingonline.meet.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.FacebookSdk.getApplicationContext
import java.util.*


class ReferrerReceiver: BroadcastReceiver() {

    val ACTION_UPDATE_DATA = "ACTION_UPDATE_DATA"
    val ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER"
    val KEY_REFERRER = "referrer"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            Log.e("ReferrerReceiver", "Intent is null")
            return
        }
        if (ACTION_INSTALL_REFERRER != intent.action) {
            Log.e("ReferrerReceiver", "Wrong action! Expected: " + ACTION_INSTALL_REFERRER + " but was: " + intent.action)
            return
        }
        val extras: Bundle? = intent.extras
        if (intent.extras == null) {
            Log.e("ReferrerReceiver", "No data in intent")
            return
        }

        context?.applicationContext?.let { Application.setReferrerDate(it, Date().time) }
        if (context != null) {
            Application.setReferrerData(context.applicationContext, extras?.get(KEY_REFERRER) as String)
        }

        if (context != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(ACTION_UPDATE_DATA))
        }
    }

}