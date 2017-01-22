package ru.mail.sporttogether.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.util.Log


object AppIsRunningHelper {
    val PACKAGE_NAME = "ru.mail.sporttogether"
    val PACKAGE_NAME_DEBUG = "ru.mail.sporttogether.debug"

    fun isAppRunning(context: Context): Boolean {
        val service = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val procInfos = service.runningAppProcesses
        for (i in procInfos.indices) {
            val processName = procInfos[i].processName
            Log.d("#MY ", "proccess name is $processName")
            if (processName == PACKAGE_NAME || processName == PACKAGE_NAME_DEBUG) {
                return true
            }
        }
        return false
    }
}