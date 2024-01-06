package com.example.lockapp.service


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.lockapp.R
import com.example.lockapp.notes.NotesActivity
import java.util.SortedMap
import java.util.TreeMap


class ForegroundService : Service() {

    private lateinit var notificationManager: NotificationManager

    private var isStarted = false
    private val isMonitoring = true

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isStarted) {
            makeForeground()
            Thread { monitorForegroundApp() }.start()
            isStarted = true
        }

        return START_STICKY
    }

    private fun monitorForegroundApp() {
        val usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val interval: Long = 5000 // Set your desired monitoring interval
        while (isMonitoring) {
            val currentTime = System.currentTimeMillis()
            val usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, currentTime - interval, currentTime
            )
            if (usageStatsList != null && usageStatsList.isNotEmpty()) {
                val sortedMap: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in usageStatsList) {
                    sortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (!sortedMap.isEmpty()) {
                    val currentPackageName = sortedMap[sortedMap.lastKey()]!!.packageName

                    Log.e("TAG", "monitorForegroundApp: $currentPackageName")

                    if (true) {
                    }
                }
            }
            try {
                Thread.sleep(interval)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }


    private fun makeForeground() {
        val intent = Intent(this, NotesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        createServiceNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Running in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }


    private fun createServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) {
            return
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val ONGOING_NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "1001"


        fun startService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            context.stopService(intent)
        }

    }
}