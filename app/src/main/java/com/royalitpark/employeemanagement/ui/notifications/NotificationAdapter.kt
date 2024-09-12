package com.royalitpark.employeemanagement.ui.notifications

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.royalitpark.employeemanagement.models.Notifications
import com.royalitpark.employeemanagement.R

class NotificationAdapter():RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var notificationList: ArrayList<Notifications>? =null

    init {
        notificationList= ArrayList<Notifications>();
    }
    fun setNotificationLists(notificationLists:ArrayList<Notifications>)
    {
        notificationList!!.clear()
        notificationList!!.addAll(notificationLists)
        notifyDataSetChanged()
        Log.e("Onnotification","Onnotification update ${notificationList!!.size}")
    }
    class NotificationViewHolder(view:View):ViewHolder(view){

        val txt_not_title=view.findViewById<TextView>(R.id.txt_not_title)
        val txt_not_date=view.findViewById<TextView>(R.id.txt_not_date)
        val txt_not_msg=view.findViewById<TextView>(R.id.txt_not_msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_notification_item,parent,false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.e("Onnotification","Onnotification ${notificationList!!.size}")
        return notificationList!!.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        holder.txt_not_title.text="${notificationList!!.get(position).title}"
        holder.txt_not_date.text="${notificationList!!.get(position).created_at}"
        holder.txt_not_msg.text="${notificationList!!.get(position).body}"
    }
}