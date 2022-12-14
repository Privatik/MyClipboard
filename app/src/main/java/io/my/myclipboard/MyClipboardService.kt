package io.my.myclipboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyClipboardService: Service() {

    private val intentFilter by lazy { IntentFilter("clipboard") }
    private val receiver: BroadcastReceiver by lazy { MyClipboardBroadcast() }

    private var isInclude: Boolean = false
    private val notificationId = Constants.NOTIFICATION_ID
    private val channelNotification = Constants.CHANNEL_FOR_NOTIFICATION

    private val clipboard by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    private val notificationBuilder: NotificationCompat.Builder by lazy(LazyThreadSafetyMode.NONE) {
        val activityPendingIntent = PendingIntent.getActivity(
            this,
            1,
            packageManager.getLaunchIntentForPackage(applicationContext.packageName)!!,
            PendingIntent.FLAG_IMMUTABLE
        )

        NotificationCompat.Builder(this.applicationContext, channelNotification)
            .setContentTitle("Clipboard Service")
            .setContentIntent(activityPendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
    }

    override fun onBind(intent: Intent?): IBinder = StateBinder()

    override fun onCreate() {
        super.onCreate()
        registerReceiver(receiver, intentFilter)
        subscribeOnClipboardManager()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action){
            START_ACTION -> {
                createForegroundNotification()
                isInclude = true
                return START_STICKY
            }
            STOP_ACTION -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                isInclude = false
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createForegroundNotification(){
        startForeground(notificationId, notificationBuilder.build(), )
    }

    private fun subscribeOnClipboardManager() {
        val clipboard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private fun unSubscribeOnClipboardManager() {
        val clipboard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        unSubscribeOnClipboardManager()
        super.onDestroy()
    }


    inner class StateBinder: Binder() {
        fun isInclude(): Boolean = isInclude
    }

    companion object{
        const val START_ACTION = "data.service.START_ACTION"
        const val STOP_ACTION = "data.service.STOP_ACTION"
    }

}