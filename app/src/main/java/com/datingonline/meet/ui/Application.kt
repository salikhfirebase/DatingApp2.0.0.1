package com.datingonline.meet.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Application: android.app.Application() {

    val FIRST_LAUNCH = "FIRST_LAUNCH"
    val REFERRER_DATE = "REFERRER_DATE"
    val REFERRER_DATA = "REFERRER_DATA"

    override fun onCreate() {
        super.onCreate()
        setFirstLaunch(this)
    }

    fun setFirstLaunch(context: Context) {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        if (!sp.contains(FIRST_LAUNCH)) {
            sp.edit().putLong(FIRST_LAUNCH, Date().time).apply()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getFirstLaunch(context: Context): String {
        val date = Date(PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getLong(FIRST_LAUNCH, Date().time))
        return DateFormat.getDateInstance().format(date) + " - " +  SimpleDateFormat("HH:mm:ss.SSS").format(date)
    }

    fun isReferrerDetected(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext).contains(REFERRER_DATE)
    }

    fun setReferrerDate(context: Context, date: Long) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        if (!sp.contains(REFERRER_DATE)) {
            sp.edit().putLong(REFERRER_DATE, date).apply()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getReferrerDate(context: Context): String {
        val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        if (!sp.contains(REFERRER_DATE)) {
            return "Undefined"
        }

        val date = Date(sp.getLong(REFERRER_DATE, Date().time))
        return DateFormat.getDateInstance().format(date) + " - " + SimpleDateFormat("HH:mm:ss.SSS").format(date)
    }

    fun setReferrerData(context: Context, data: String) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        if (!sp.contains(REFERRER_DATA)) {
            sp.edit().putString(REFERRER_DATA, data).apply()
        }
    }

    fun getReferrerDataRaw(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return if (!sp.contains(REFERRER_DATA)) {
            "Undefined"
        } else sp.getString(REFERRER_DATA, null)
    }

    fun getReferrerDataDecoded(context: Context): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val raw = sp.getString(REFERRER_DATA, null) ?: return null

        try {
            val url = URLDecoder.decode(raw, "utf-8")
            try {
                val url2x = URLDecoder.decode(url, "utf-8")
                return if (raw == url2x) {
                    null
                } else url2x
            } catch (uee: UnsupportedEncodingException) {
                // not URL 2x encoded but URL encoded
                return if (raw == url) {
                    null
                } else url
            }

        } catch (uee: UnsupportedEncodingException) {
            // not URL encoded
        }

        return null
    }

}