package com.royalitpark.employeemanagement.ui.leaves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.royalitpark.employeemanagement.models.LeaveTypes
import com.royalitpark.employeemanagement.R

class LeaveTypeAdapter:BaseAdapter() {
    lateinit var leaveTypeList:ArrayList<LeaveTypes>
    init {
        leaveTypeList= ArrayList()
        leaveTypeList.add(LeaveTypes("-1","Select"))
    }
    override fun getCount(): Int {

        return  leaveTypeList.size
    }

    override fun getItem(position: Int): Any {
        return leaveTypeList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view=LayoutInflater.from(parent?.context).inflate(R.layout.layout_leave_type_item,parent,false)
        val txt_type=view.findViewById<TextView>(R.id.txt_type)
        txt_type.setText(leaveTypeList.get(position).leaveType)
        return view
    }

    fun addLeaveTypes(leaveTypesList: ArrayList<LeaveTypes>) {
        leaveTypeList.clear()
        leaveTypeList.add(LeaveTypes("-1","Select"))

        // Filter out "Punch Activity Leave" item
        val filteredList = leaveTypesList.filter { it.leaveType != "Punch Activity Leave" }

        leaveTypeList.addAll(filteredList)
        notifyDataSetChanged()
    }
}