package com.royalitpark.employeemanagement.ui.holidays

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.royalitpark.employeemanagement.models.HolidayList
import com.royalitpark.employeemanagement.R

class HolidaysAdapter :RecyclerView.Adapter<HolidaysAdapter.HolidayViewHolder>() {
    lateinit var holidayList: ArrayList<HolidayList>
    init {
        holidayList= ArrayList()
    }
    class HolidayViewHolder(view:View):ViewHolder(view){

        val txt_holiday=view.findViewById<TextView>(R.id.txt_holiday)
        val txt_title=view.findViewById<TextView>(R.id.txt_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_holiday_item,parent,false)
        return HolidayViewHolder(view)
    }

    override fun getItemCount(): Int {

        return holidayList.size
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {

        holder.txt_holiday.setText("${holidayList.get(position).date}")
        holder.txt_title.setText(" ${holidayList.get(position).reason}")
    }

    fun setHolidayData(holidayLists: ArrayList<HolidayList>) {
        holidayList.clear()
        holidayList.addAll(holidayLists)
        notifyDataSetChanged()
    }
}