package com.royalitpark.employeemanagement.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.royalitpark.employeemanagement.ui.dashboard.DashBoardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.royalitpark.employeemanagement.R
import org.json.JSONException
import java.lang.System.currentTimeMillis

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        lateinit var sharedPreferences: SharedPreferences

        private val TAG = "MyFirebaseToken"
        private lateinit var notificationManager: NotificationManager
        private lateinit var title: String
        private lateinit var notification_body: String
        private lateinit var userid: String
        var token: String? = ""
        var body: String? = ""
        var id: String? = "1"
        lateinit var context: Context



    }


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("onMessageReceived: ", p0.data.toString())
        Log.e("onMessageReceived: ", p0.notification.toString())
        p0.notification!!.body?.let { Log.e("onMessageReceived: ", it) }


        if (p0.data.isNotEmpty()) {
            try {

                /*val params: Map<String?, String?> = p0.data
                val json = JSONObject(params)
                Log.e("json","json ${json.toString()}")
                val message = json.getString("message")
                var obj = JSONObject(message)
                val jsonString = message.substring(1, message.length - 1).replace("\\", "")
                val jsonObject = JSONTokener(message).nextValue() as JSONObject

                id = jsonObject.getString("id")
                title = jsonObject.getString("title")
                body = jsonObject.getString("body")*/
                title= p0.notification!!.title.toString()
                body=   p0.notification!!.body



                //  handleDataMessage(json)
            } catch (e: JSONException) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }


        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//        Log.e("tilte", "" + title)
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("id", id)

        /* var pendingIntent = PendingIntent.getActivity(
             applicationContext, 0,
             intent,
             PendingIntent.FLAG_MUTABLE
         )*/
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, intent, PendingIntent.FLAG_IMMUTABLE
            )
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            checkNotificationChannel("1")
        }


        val notification = NotificationCompat.Builder(applicationContext, "1")
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color = ContextCompat.getColor(this, R.color.red)
            notification.setColor(
                Color.RED

            )
//            notification.setColor(ContextCompat.getColor(this, R.color.red))


        } else {
            val color = ContextCompat.getColor(this, R.color.red)
            notification.setColor(
                Color.RED


            )
//            notification.setColor(ContextCompat.getColor(this, R.color.red))
        }
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("MotherChoice")

            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.MessagingStyle(title!!)
                    .setGroupConversation(false)
                    .addMessage(body, currentTimeMillis(), title)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(defaultSound)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(CHANNEL_ID: String) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Mother Choice",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = body
        notificationChannel.enableLights(true)

        notificationChannel.enableVibration(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onNewToken(p0: String) {
        token = p0
        super.onNewToken(p0)
    }
}