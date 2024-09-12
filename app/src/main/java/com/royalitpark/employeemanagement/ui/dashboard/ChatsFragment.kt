package com.royalitpark.employeemanagement.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.customviews.MonthPickerDialogFragment
import com.royalitpark.employeemanagement.models.CalanderModelMainRespon
import com.royalitpark.employeemanagement.models.CalenderAttendance
import com.royalitpark.employeemanagement.ui.attendance.CalenderDayAdapter
import com.royalitpark.employeemanagement.ui.attendance.CalenderdaysAdapter
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.FragmentChatsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var calenderdaysAdapter: CalenderdaysAdapter
    lateinit var calenderDayAdapter: CalenderDayAdapter
    lateinit var binding:FragmentChatsBinding
    var CalanderModelMainRespon:CalanderModelMainRespon?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calenderdaysAdapter= CalenderdaysAdapter(supportFragmentManager = childFragmentManager)
        calenderDayAdapter= CalenderDayAdapter()

        binding.gridCalender.adapter=calenderdaysAdapter
        binding.gridCalenderDays.adapter=calenderDayAdapter

        var format: SimpleDateFormat = SimpleDateFormat("MMM yyyy")
        val c = Calendar.getInstance()
        binding.txtDate.text=format.format(c.time)
        getattendance(c.get(Calendar.YEAR) .toString(),(c.get(Calendar.MONTH)+1) .toString() )

        binding.lnrDateSelector.setOnClickListener {
            datePicker(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    var format: SimpleDateFormat = SimpleDateFormat("MMM yyyy")
    var format2: SimpleDateFormat = SimpleDateFormat("MM yyyy")
    fun datePicker(view: View) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        /* val datePickerDialog=  DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { _, year, month, day ->




             var monthsdate="${month+1}"
             if(monthsdate.length==1)
                 monthsdate="0$monthsdate"

             var daydate="$day"
             if(daydate.length==1)
                 daydate="0$daydate"



             getattendance(year .toString(),(monthsdate) .toString() )
             // binding.inputDrop.setText(dateformat.format(dateformat2.parse(endDate)))




         }, startYear, startMonth, startDay)

         datePickerDialog.datePicker.maxDate=Calendar.getInstance().timeInMillis
        // datePickerDialog.datePicker.findViewById<View> (Resources.getSystem().getIdentifier("day","id","android")).visibility = View.GONE


         datePickerDialog.show()*/


        val dialog = MonthYearPickerDialog.Builder(
            requireActivity(),
            R.style.Style_MonthYearPickerDialog_Orange,
            { year, month ->
                var monthsdate="${month+1}"
                if(monthsdate.length==1)
                    monthsdate="0$monthsdate"





                getattendance(year .toString(),(monthsdate) .toString() )
                binding.txtDate .setText(format.format(format2.parse("$monthsdate $year")))
            },
            startYear,
            startMonth
        )
            .setNegativeButton("Cancel")
            .setPositiveButton("Set")
            .build()

        dialog.setTitle("Select month and year")
        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(childFragmentManager, MonthPickerDialogFragment::class.java.simpleName)

        /*SingleDateAndTimePickerDialog.Builder(this)
            .bottomSheet()
            .curved()
            .displayMinutes(false)
            .displayHours(false)
            .displayDays(false)
            .displayMonth(true)
            .displayYears(true)
            .displayDaysOfMonth(false)
            .display()*/
    }
    fun getattendance(year:String, month:String)
    {

        var hashMap = HashMap<String, String> (3)
        // val cal: Calendar = Calendar.getInstance()
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        //hashMap.put("emp_id", "2");
        hashMap.put("year",year );
        hashMap.put("month",month );


        if(!Utils.checkConnectivity(requireActivity()))
        {
            Utils.showMessage("Please check your connection ", requireActivity())
            return
        }
        val customDialog= CustomDialog(requireActivity());
        customDialog.showDialog(requireActivity(),true)
        RetrofitClient.apiInterface.getattendanceNew(hashMap).enqueue(object : Callback<CalanderModelMainRespon> {




            override fun onResponse(call: Call<CalanderModelMainRespon>, response: Response<CalanderModelMainRespon>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();

                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());

                if(response.body()?.status==200)
                {
                    CalanderModelMainRespon=response.body()

                    readDatesToDisplay(Integer.parseInt(month)-1)
                    return
                }
                //Utils.showMessage(jsobObj.getString("message")!!, requireActivity())


            }

            override fun onFailure(call: Call<CalanderModelMainRespon>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", requireActivity())
            }

        }
        )
    }
    fun readDatesToDisplay(selectedMonth:Int)
    {

        val c = Calendar.getInstance()
        val todayc = Calendar.getInstance()

        var formatDay: SimpleDateFormat = SimpleDateFormat("dd")
        var formatDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

        c[c[Calendar.YEAR], c[Calendar.MONTH]] = 1
        c.set(Calendar.MONTH,selectedMonth)
        val currentMonth=c[Calendar.MONTH];
        var startOfWeek = c[Calendar.DAY_OF_WEEK]
        Log.e("fdfdfdf", "" + startOfWeek)
        Log.e("-1 vagar nu", "" + c[Calendar.DAY_OF_WEEK])
        //startOfWeek=startOfWeek-2
        c.add(Calendar.DATE, -(startOfWeek-1))
        val  calendarGrid = ArrayList<CalenderAttendance>(6 * 7)
        var gridCount = 0
        var paidLeaveCount=0
        var josing_date:Date?=null
        if(CalanderModelMainRespon!=null) {
            josing_date= formatDate.parse(CalanderModelMainRespon!!.emp_date_of_joining)
        }

        for (week in 0..5) {
            for (day in 0..6) {
                val dt = c.time
                dt.hours = 0
                dt.minutes = 0
                dt.seconds = 0


                // Log.d("gridCount","gridCount $gridCount")
                // Log.d("currentMonth","currentMonth $currentMonth ${dt.month}")
                Log.d("gridCount","gridCount $currentMonth ${dt.month}")
                val dateee=formatDate.parse(formatDate.format(dt))
                if(currentMonth==dt.month) {
                    if(CalanderModelMainRespon!=null&& CalanderModelMainRespon!!.data.size>0) {
                        CalanderModelMainRespon!!.data.forEach {
                            if(dateee.equals(formatDate.parse(it.date))){
                                val calenderAttendance = CalenderAttendance()
                                calenderAttendance.day = formatDay.format(formatDate.parse(it.date));
                                calenderAttendance.date = it.date;
                                calenderAttendance.dayType = -1
                                calenderAttendance.isHalfDay = false
                                if(it.status=="weekoff")
                                {
                                    calenderAttendance.dayType=6
                                    if(formatDate.parse(it.date).before(todayc.time)||formatDate.parse(it.date).equals(todayc.time))
                                        paidLeaveCount=paidLeaveCount+1
                                }else if(it.status=="full day leave")
                                {
                                    calenderAttendance.dayType = 2
                                    calenderAttendance.isHalfDay = false
                                    paidLeaveCount=paidLeaveCount+1
                                }else if(it.status=="half day leave")
                                {
                                    calenderAttendance.dayType = 2
                                    calenderAttendance.isHalfDay = true

                                }else if(it.status=="absent")
                                {
                                    calenderAttendance.dayType = 0
                                }else if(it.status=="present")
                                {
                                    calenderAttendance.dayType = 1

                                }else if(it.status=="holiday")
                                {
                                    calenderAttendance.dayType=3
                                    if(formatDate.parse(it.date).before(todayc.time)||formatDate.parse(it.date).equals(todayc.time))
                                        paidLeaveCount=paidLeaveCount+1
                                }else if(it.status=="future date")
                                {
                                    calenderAttendance.dayType=-1
                                }else if(it.status=="today")
                                {
                                    calenderAttendance.dayType=11
                                }



                                calenderAttendance.punchInTime = "${it.punch_in}"
                                calenderAttendance.punchOutTime = "${it.punch_out}"
                                calendarGrid.add(calenderAttendance)
                            }
                        }
                    }
                }else{

                    val calenderAttendance= CalenderAttendance()
                    calenderAttendance.day="";
                    calenderAttendance.date="";
                    calenderAttendance.dayType=-1
                    calenderAttendance.isHalfDay=false
                    calenderAttendance.punchInTime=""
                    calenderAttendance.punchOutTime=""
                    calendarGrid.add(calenderAttendance)
                }




                c.add(Calendar.DATE, 1)
            }
        }

        if(CalanderModelMainRespon!=null) {
            binding.txtPresent.setText("${CalanderModelMainRespon?.presentDayCount!!}")
            binding.txtAbsent.setText("${CalanderModelMainRespon?.absentCount!!}")
            binding.txtHalfDays.setText("${CalanderModelMainRespon?.halfDayLeaveCount!!}")
            binding.txtPaidLeaves.setText("${paidLeaveCount}")
        }else
        {

        }
        calenderdaysAdapter.setCalenderData(calendarGrid)
    }

}