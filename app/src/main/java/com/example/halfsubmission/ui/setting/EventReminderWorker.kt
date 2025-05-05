package com.example.halfsubmission.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.halfsubmission.R
import com.example.halfsubmission.data.remote.response.EventResponse
import com.example.halfsubmission.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Worker untuk mengirim notifikasi pengingat event yang akan datang.
 *
 * @param context Konteks aplikasi.
 * @param workerParams Parameter worker.
 */
class EventReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private const val TAG = "EventReminderWorker"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "event_channel"
        private const val CHANNEL_NAME = "Event Reminder"
    }

    override fun doWork(): Result {
        fetchUpcomingEvent()
        return Result.success()
    }

    /**
     * Mengambil event yang akan datang dari API.
     */
    private fun fetchUpcomingEvent() {
        ApiConfig.getApiService().getActiveEvents(active = 1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful && response.body()?.event?.isNotEmpty() == true) {
                    response.body()?.event?.firstOrNull()?.let {
                        showNotification(it.name, it.beginTime ?: "Time unavailable")
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get event data: ${t.message}")
            }
        })
    }

    /**
     * Menampilkan notifikasi.
     */
    private fun showNotification(title: String, time: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_event)
            .setContentTitle("Upcoming Event: $title")
            .setContentText("Date: $time")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH))
        }

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
