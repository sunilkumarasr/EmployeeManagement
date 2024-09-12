package com.royalitpark.employeemanagement.ui.attendance

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.models.CalenderAttendance
import com.royalitpark.employeemanagement.R
import java.text.SimpleDateFormat
import java.util.Calendar


class CalenderdaysAdapter(val supportFragmentManager: FragmentManager) : BaseAdapter() {
    lateinit var calendarGrid: ArrayList<CalenderAttendance>
    lateinit var mCal: Calendar
    lateinit var today: String

    var format: SimpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
    var formatDay: SimpleDateFormat = SimpleDateFormat("dd")
    init {
        mCal = Calendar.getInstance();
        // CalendarView.dateStore.clear();
        // initCalendar(mCal);
        calendarGrid= ArrayList()
    }

    private fun initCalendar(cal: Calendar) {

        val c = Calendar.getInstance()
        c.time = cal.time
        c[c[Calendar.YEAR], c[Calendar.MONTH]] = 1
        c.set(Calendar.MONTH,9)
        val currentMonth=c[Calendar.MONTH];
        var startOfWeek = c[Calendar.DAY_OF_WEEK]
        Log.e("fdfdfdf", "" + startOfWeek)
        Log.e("-1 vagar nu", "" + c[Calendar.DAY_OF_WEEK])
        //startOfWeek=startOfWeek-2
        c.add(Calendar.DATE, -(startOfWeek-1))
        // calendarGrid = ArrayList<String>(6 * 7)
        var gridCount = 0
        for (week in 0..5) {
            for (day in 0..6) {
                val dt = c.time
                dt.hours = 0
                dt.minutes = 0
                dt.seconds = 0
                Log.d("gridCount","gridCount $gridCount")
                Log.d("currentMonth","currentMonth $currentMonth ${dt.month}")
                // if(currentMonth==dt.month)
                //  calendarGrid.add(formatDay.format(dt))
                // else calendarGrid.add("")
                c.add(Calendar.DATE, 1)
            }
        }
    }
    override fun getCount(): Int {

        return  calendarGrid.size
    }

    override fun getItem(position: Int): Any {
        return calendarGrid.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val context=parent!!.context
        val view=LayoutInflater.from(context).inflate(R.layout.layout_calender_day_item,parent,false)

        val txt_day=view.findViewById<TextView>(R.id.txt_day)

        val calenderAttendance=calendarGrid.get(position)
        txt_day.setText(calenderAttendance.day)
        if(calenderAttendance.day!!.isEmpty())
            txt_day.visibility=View.GONE
        else
            txt_day.visibility=View.VISIBLE
        view.setOnClickListener {
            if(calenderAttendance.dayType==3)
            {
                Utils.showMessage("Holiday",context)
                return@setOnClickListener
            }
           else if(calenderAttendance.dayType==6)
            {
                Utils.showMessage("WeekOff",context)
                return@setOnClickListener
            } else if(calenderAttendance.dayType==-1)
            {
                Utils.showMessage("Future day selected",context)
                return@setOnClickListener
            }
            else if(calenderAttendance.dayType==-2)
            {
                Utils.showMessage("Not Joined on this date",context)
                return@setOnClickListener
            }

            else if(calenderAttendance.dayType==11&&calenderAttendance.punchInTime!!.isEmpty())
            {
                Utils.showMessage("Today & Not punched yet",context)
                return@setOnClickListener
            }
            onDateSelection(calenderAttendance)
        }
        if(calenderAttendance.dayType==0)
        {
            view.setBackgroundColor(context.resources.getColor(R.color.color_absent))
        }else
            if(calenderAttendance.dayType==1)
            {
                if(calenderAttendance.isHalfDay)
                    view.setBackgroundColor(context.resources.getColor(R.color.color_halfday))
                else
                view.setBackgroundColor(context.resources.getColor(R.color.color_present))
            }else  if(calenderAttendance.dayType==2)
            {
                 if(calenderAttendance.isHalfDay)
                 view.setBackgroundColor(context.resources.getColor(R.color.color_halfday))
                 else
                view.setBackgroundColor(context.resources.getColor(R.color.color_paidleave))
            }else  if(calenderAttendance.dayType==3||calenderAttendance.dayType==6)
            {
                view.setBackgroundColor(context.resources.getColor(R.color.color_paidleave))
            }
            else  if(calenderAttendance.dayType==-2)
            {
                view.setBackgroundColor(context.resources.getColor(R.color.color_order_date))
                txt_day.setText("NA")
            }
            else
            {
                view.setBackgroundResource(R.drawable.rectangle_edittext_bank)
            }

        return view

    }
    fun  onDateSelection(calenderAttendance: CalenderAttendance)
    {

        val bottomSheetDialogFragment=ShowAttendanceDetailsFragment(calenderAttendance)
        bottomSheetDialogFragment.show(supportFragmentManager,"")
    }
    fun setCalenderData(calendarGrids:ArrayList<CalenderAttendance>)
    {
        calendarGrid.clear()
        calendarGrid.addAll(calendarGrids)
        notifyDataSetChanged()
    }
}