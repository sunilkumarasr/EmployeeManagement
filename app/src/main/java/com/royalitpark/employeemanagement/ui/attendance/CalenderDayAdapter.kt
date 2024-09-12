package com.royalitpark.employeemanagement.ui.attendance

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.royalitpark.employeemanagement.R

class CalenderDayAdapter:BaseAdapter() {
    lateinit var list:ArrayList<String>
    init {
        list=ArrayList()
        list.add("Sun")
        list.add("Mon")
        list.add("Tue")
        list.add("Wed")
        list.add("Thur")
        list.add("Fri")
        list.add("Sat")

    }
    override fun getCount(): Int {
        return  7
    }

    override fun getItem(position: Int): Any {

return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view= LayoutInflater.from(parent!!.context).inflate(R.layout.layout_calender_days_item,parent,false)

        val txt_day=view.findViewById<TextView>(R.id.txt_day)
        txt_day.text=list.get(position)
        txt_day.setBackgroundColor(Color.parseColor("#FFFFFF"))
        return view;
    }
}