package com.datingonline.meet._core

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.datingonline.meet.BuildConfig
import com.datingonline.meet.R
import com.google.firebase.database.*


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/13/19.
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var appEventsLogger: AppEventsLogger
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        appEventsLogger = AppEventsLogger.newLogger(this)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initUI()
        setUI()
    }

    @LayoutRes
    abstract fun getContentView(): Int

    abstract fun initUI()

    abstract fun setUI()


    @Deprecated(message = "use getValueFromDatabase")
    fun fetchRemoteConfig(
        onTaskSuccessful: (FirebaseRemoteConfig) -> Unit,
        onFailure: () -> Unit = {}
    ) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        val cacheExpiration: Long = 0

        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.default_parameters)

        remoteConfig.fetch(cacheExpiration).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activateFetched()
                onTaskSuccessful(remoteConfig)
            }
        }.addOnFailureListener { onFailure() }
    }

    fun getValuesFromDatabase(
        onTaskSuccessful: (DataSnapshot) -> Unit,
        onFailure: () -> Unit = {}
    ) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                onTaskSuccessful(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onFailure()
            }
        }

        myRef.addListenerForSingleValueEvent(postListener)
    }

    /**
     * method logs event(firebase and facebook analytics)
     */
    fun logEvent(event: String, bundle: Bundle? = null) {
        if (bundle != null) {
            appEventsLogger.logEvent(event, bundle)
            firebaseAnalytics.logEvent(event, bundle)
        } else {
            appEventsLogger.logEvent(event, null)
            firebaseAnalytics.logEvent(event, null)
        }
    }

    fun logEvent(event: String) {
        appEventsLogger.logEvent(event)
        firebaseAnalytics.logEvent(event, null)
    }
}