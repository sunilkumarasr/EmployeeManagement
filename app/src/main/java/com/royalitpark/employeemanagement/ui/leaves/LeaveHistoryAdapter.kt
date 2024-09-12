package com.royalitpark.employeemanagement.ui.leaves

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalitpark.employeemanagement.models.Leaves
import com.royalitpark.employeemanagement.R
import java.text.SimpleDateFormat

class LeaveHistoryAdapter : RecyclerView.Adapter<LeaveHistoryAdapter.LeaveHistoryViewHolder>() {
lateinit var leavesList:ArrayList<Leaves>;
lateinit var format:SimpleDateFormat;
lateinit var format2:SimpleDateFormat;
    init {
        leavesList= ArrayList()
         format= SimpleDateFormat("yyyy-MM-dd")
         format2= SimpleDateFormat("dd MMM yyyy")
    }

    class LeaveHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_leave_type=itemView.findViewById<TextView>(R.id.txt_leave_type)
        val txt_leave_apply_date=itemView.findViewById<TextView>(R.id.txt_leave_apply_date)
        val txt_leave_on_date=itemView.findViewById<TextView>(R.id.txt_leave_on_date)
        val txt_leave_status=itemView.findViewById<TextView>(R.id.txt_leave_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveHistoryViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_leave_history_item,parent,false)
        return LeaveHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return leavesList.size
    }

    override fun onBindViewHolder(holder: LeaveHistoryViewHolder, position: Int) {

        var leave=leavesList.get(position)
        holder.itemView.setOnClickListener {

            val intent=Intent(holder.itemView.context,LeaveDetailsActivity::class.java)
            intent.putExtra("type",leave.leave_type)
            intent.putExtra("date",leave.requested_date)
            intent.putExtra("from",leave.fromDate)
            intent.putExtra("to",leave.toDate)
            intent.putExtra("reson",leave.reason)
            intent.putExtra("status",leave.status_name)
            intent.putExtra("is_half_day",leave.isHalfDay)
            holder.itemView.context.startActivity(intent)
        }


        if(leave.requested_date!=null&& leave.requested_date!!.isNotEmpty())
        holder.txt_leave_apply_date.setText("${format2.format(format.parse(leave.requested_date))}")
        else
        holder.txt_leave_apply_date.setText("")
        if(leave.fromDate!=null&& leave.fromDate!!.isNotEmpty())
        holder.txt_leave_on_date.setText("${format2.format(format.parse(leave.fromDate))}")
        else
        holder.txt_leave_on_date.setText("")
        holder.txt_leave_type.setText("${leave.leave_type}")

        holder.txt_leave_status.setText("${leave.status_name}")


    }

    fun setDocList(leavesLists: ArrayList<Leaves>) {
        leavesList.clear()
        leavesList.addAll(leavesLists)
        notifyDataSetChanged()

    }
}