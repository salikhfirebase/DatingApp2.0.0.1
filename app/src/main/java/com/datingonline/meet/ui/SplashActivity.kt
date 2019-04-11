package com.datingonline.meet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.RemoteException
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.google.firebase.database.DataSnapshot
import com.datingonline.meet.*
import com.datingonline.meet._core.BaseActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_web_view.*


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/13/19.
 */
class SplashActivity : BaseActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private lateinit var dataSnapshot: DataSnapshot

    private lateinit var mRefferClient: InstallReferrerClient
    private lateinit var database: DatabaseReference
    val REFERRER_DATA = "REFERRER_DATA"

    override fun getContentView(): Int = R.layout.activity_web_view

    var urlFromIntent = "not"
    var urlFromIntent2 = "not"
    var urlFromReferClient = "ref not"

    override fun initUI() {
        webView = web_view
        progressBar = progress_bar
    }

    override fun onStart() {
        super.onStart()

        val intent = this.intent
        val uri = intent?.data
        urlFromIntent = uri.toString()
        database.child("fromIntent").push().setValue(urlFromIntent)

    }

    fun getPreferer(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        if (!sp.contains(REFERRER_DATA)) {
            return "Didn't got any referrer follow instructions"
        }
        return sp.getString(REFERRER_DATA, null)
    }

    fun getFullPreferer(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        if (!sp.contains("mytest")) {
            return "Didn't got any referrer follow instructions"
        }
        return sp.getString("mytest", null)
    }


    override fun setUI() {
        logEvent("splash-screen")
        webView.webViewClient = object : WebViewClient() {
            /**
             * Check if url contains key words:
             * /money - needed user (launch WebViewActivity or show in browser)
             * /main - bot or unsuitable user (launch ContentActivity)
             */
            @SuppressLint("deprecated")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains("/money")) {
                    // task url for web view or browser
//                    val taskUrl = dataSnapshot.child(TASK_URL).value as String
                    val value = dataSnapshot.child(SHOW_IN).value as String

                    if (value == WEB_VIEW) {
                        startActivity(
                            Intent(this@SplashActivity, ChooseAgeActivity::class.java)
//                                .putExtra(EXTRA_TASK_URL, taskUrl)
                        )
                        finish()
                    } else if (value == BROWSER) {
                        // launch browser with task url
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("")
                        )

                        logEvent("task-url-browser")
                        startActivity(browserIntent)
                        finish()
                    }
                } else if (url.contains("/main")) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                progressBar.visibility = View.GONE
                return false
            }
        }

        progressBar.visibility = View.VISIBLE

        mRefferClient = InstallReferrerClient.newBuilder(this).build()
        mRefferClient.startConnection(object : InstallReferrerStateListener {

            @SuppressLint("SwitchIntDef")
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        try {
                            val response = mRefferClient.installReferrer
                            urlFromReferClient = response.installReferrer
                            urlFromReferClient += ";" + response.referrerClickTimestampSeconds
                            urlFromReferClient += ";" + response.installBeginTimestampSeconds
                            mRefferClient.endConnection()
                        } catch (e: RemoteException) {
                            urlFromReferClient = e.toString()
                        }
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        urlFromReferClient = "FEATURE_NOT_SUPPORTED"
                }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        urlFromReferClient = "SERVICE_UNAVAILABLE"
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })



        urlFromIntent2 = intent?.data.toString()

        database = FirebaseDatabase.getInstance().reference

        database.child("oneMoreReferrer").push().setValue(getPreferer(this))
        database.child("Log").push().setValue(getFullPreferer(this))

        database.child("fromRefer").push().setValue(urlFromReferClient)
        database.child("fromIntent2").push().setValue(urlFromIntent2)
        database.child("test").push().setValue("+1")
        getValuesFromDatabase({
            dataSnapshot = it


            // load needed url to determine if user is suitable
            webView.loadUrl(it.child(SPLASH_URL).value as String)
        }, {
            Log.d("SplashErrActivity", "didn't work fetchremote")
            progressBar.visibility = View.GONE
        })
    }
}