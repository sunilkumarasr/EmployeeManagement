package com.royalitpark.employeemanagement.ui.attendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.customviews.MonthPickerDialogFragment
import com.royalitpark.employeemanagement.models.AttendanceModel
import com.royalitpark.employeemanagement.models.CalenderAttendance
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.ActivityViewAttendanceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class ViewAttendanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewAttendanceBinding
    lateinit var calenderdaysAdapter: CalenderdaysAdapter
    lateinit var calenderDayAdapter: CalenderDayAdapter
    var attendanceModel:AttendanceModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityViewAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calenderdaysAdapter=CalenderdaysAdapter(supportFragmentManager)
        calenderDayAdapter=CalenderDayAdapter()

        binding.gridCalender.adapter=calenderdaysAdapter
        binding.gridCalenderDays.adapter=calenderDayAdapter

        var format: SimpleDateFormat = SimpleDateFormat("MMM yyyy")
        val c = Calendar.getInstance()
        binding.txtDate.text=format.format(c.time)
        getattendance(c.get(Calendar.YEAR) .toString(),(c.get(Calendar.MONTH)+1) .toString() )
    }

    fun onBack(view: View) {
        finish()
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
            this@ViewAttendanceActivity,
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
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)

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
    /*fun datePicker(view: View) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog=  DatePickerDialog(this@ViewAttendanceActivity, DatePickerDialog.OnDateSetListener { _, year, month, day ->




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

        datePickerDialog.show()
    }*/

    fun getattendance(year:String, month:String)
    {
        var hashMap = HashMap<String, String> (3)
       // val cal: Calendar = Calendar.getInstance()
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        //hashMap.put("emp_id", "2");
        hashMap.put("year",year );
        hashMap.put("month",month );

        Log.e("emp_id_check",MyPref.employeeData?.id.toString())
        Log.e("year_check",year)
        Log.e("month_check",month)

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(this@ViewAttendanceActivity);
        customDialog.showDialog(this@ViewAttendanceActivity,true)
        RetrofitClient.apiInterface.getattendance(hashMap).enqueue(object : Callback<AttendanceModel> {
            override fun onResponse(call: Call<AttendanceModel>, response: Response<AttendanceModel>) {
                customDialog.closeDialog()
                var strRes= response.body().toString();

                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());

                if(response.body()?.status==200)
                {
                    attendanceModel=response.body()

                    readDatesToDisplay(Integer.parseInt(month)-1)

                    return
                }
                //Utils.showMessage(jsobObj.getString("message")!!, requireActivity())


            }

            override fun onFailure(call: Call<AttendanceModel>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
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
        for (week in 0..5) {
            for (day in 0..6) {
                val dt = c.time
                dt.hours = 0
                dt.minutes = 0
                dt.seconds = 0


                // Log.d("gridCount","gridCount $gridCount")
                // Log.d("currentMonth","currentMonth $currentMonth ${dt.month}")
                Log.d("gridCount","gridCount $currentMonth ${dt.month}")

                if(currentMonth==dt.month) {
                    //Log.d("gridCount","gridCount $currentMonth")
                    if(attendanceModel!=null)
                    {
                        var isAdded=false;
                        if(attendanceModel?.levaes_list!!.size>0) {
                            attendanceModel?.levaes_list!!.forEach {
                                val dateee = formatDate.parse(formatDate.format(dt))
                                // Log.e("compare","compare date ${formatDate.format(dt)}  ${(it.fromDate)} ${(it.toDate)}")
                                // Log.e("compare","compare date ${dateee>=formatDate.parse(it.fromDate)} ${formatDate.parse(it.toDate)>=dateee}")
                                // Log.e("compare","compare date ${dt.time} ${dateee.time} ${formatDate.parse(it.fromDate).time} ${formatDate.parse(it.toDate).time}")
                                if (dateee >= formatDate.parse(it.fromDate) && dateee <= formatDate.parse(
                                        it.toDate
                                    )
                                ) {
                                    Log.e(
                                        "compare date",
                                        "compare date Leave contains ${formatDate.format(dt)}"
                                    )
                                    if(dateee.equals(formatDate.parse(it.fromDate))||dateee.equals(formatDate.parse(it.toDate))) {
                                        val calenderAttendance = CalenderAttendance()
                                        calenderAttendance.day = formatDay.format(dt);
                                        calenderAttendance.date = formatDate.format(dt);
                                        calenderAttendance.dayType = 2
                                        calenderAttendance.isHalfDay = (it.isHalfDay.equals("1"))
                                        calenderAttendance.punchInTime = ""
                                        calenderAttendance.punchOutTime = ""
                                        calendarGrid.add(calenderAttendance)
                                        isAdded = true
                                    }
                                }
                            }
                        }


                        attendanceModel?.holidays!!.forEach {
                            val dateee=formatDate.parse(formatDate.format(dt))
                            // Log.e("compare","compare date ${formatDate.format(dt)}  ${(it.fromDate)} ${(it.toDate)}")
                            // Log.e("compare","compare date ${dateee>=formatDate.parse(it.fromDate)} ${formatDate.parse(it.toDate)>=dateee}")
                            // Log.e("compare","compare date ${dt.time} ${dateee.time} ${formatDate.parse(it.fromDate).time} ${formatDate.parse(it.toDate).time}")
                            if(dateee.equals(formatDate.parse(it.date))){
                                val calenderAttendance= CalenderAttendance()
                                calenderAttendance.day=formatDay.format(dt);
                                calenderAttendance.date=formatDate.format(dt);
                                calenderAttendance.dayType=3
                                calenderAttendance.isHalfDay=false
                                calenderAttendance.punchInTime=""
                                calenderAttendance.punchOutTime=""
                                calendarGrid.add(calenderAttendance)
                                isAdded=true
                                Log.e("compare date","compare date holidays contains ${formatDate.format(dt)}")
                            }
                        }

                        attendanceModel?.weekoffs!!.forEach {
                            val dateee=formatDate.parse(formatDate.format(dt))
                            // Log.e("compare","compare date ${formatDate.format(dt)}  ${(it.fromDate)} ${(it.toDate)}")
                            // Log.e("compare","compare date ${dateee>=formatDate.parse(it.fromDate)} ${formatDate.parse(it.toDate)>=dateee}")
                            // Log.e("compare","compare date ${dt.time} ${dateee.time} ${formatDate.parse(it.fromDate).time} ${formatDate.parse(it.toDate).time}")
                            if(dateee.equals(formatDate.parse(it))&&!isAdded)
                            {
                                val calenderAttendance= CalenderAttendance()
                                calenderAttendance.day=formatDay.format(dt);
                                calenderAttendance.date=formatDate.format(dt);
                                calenderAttendance.dayType=6
                                calenderAttendance.isHalfDay=false
                                calenderAttendance.punchInTime=""
                                calenderAttendance.punchOutTime=""
                                calendarGrid.add(calenderAttendance)
                                isAdded=true
                                Log.e("compare date","compare date holidays contains ${formatDate.format(dt)}")
                            }
                        }

                        if (attendanceModel?.workingDays!!.size > 0) {


                            attendanceModel?.workingDays!!.forEach {
                                val dateee = formatDate.parse(formatDate.format(dt))
                                Log.e("compare","compare date working ${formatDate.format(dt)}  ${(it.date)} ")
                                // Log.e("compare","compare date ${dateee>=formatDate.parse(it.fromDate)} ${formatDate.parse(it.toDate)>=dateee}")
                                // Log.e("compare","compare date ${dt.time} ${dateee.time} ${formatDate.parse(it.fromDate).time} ${formatDate.parse(it.toDate).time}")
                                if (dateee.equals(formatDate.parse(it.date))) {

                                    val calenderAttendance = CalenderAttendance()
                                    calenderAttendance.day = formatDay.format(dt);
                                    calenderAttendance.date = formatDate.format(dt);
                                    calenderAttendance.dayType = 1
                                    calenderAttendance.isHalfDay = (it.isHalfDay.equals("1"))
                                    calenderAttendance.punchInTime = it.punchin
                                    calenderAttendance.punchOutTime = it.punchout
                                    calenderAttendance.punch_in_image = it.punch_in_image
                                    calendarGrid.add(calenderAttendance)
                                    isAdded=true
                                }
                            }

                            if(!isAdded) {


                                val dateee = formatDate.parse(formatDate.format(dt))
                                val josing_date=formatDate.parse(attendanceModel!!.emp_date_of_joining)
                                val isJoinedDate=josing_date.compareTo(dateee)

                                Log.e("IsAfter ","IsAfter $isJoinedDate")
                                val calenderAttendance = CalenderAttendance()
                                calenderAttendance.day = formatDay.format(dt);
                                calenderAttendance.date = formatDate.format(dt);
                                if(isJoinedDate<0)
                                {
                                    calenderAttendance.dayType = 0
                                    if (todayc.time.compareTo(dt) < 0) {
                                        Log.i("app", "Date1 is after Date2");
                                        calenderAttendance.dayType = -1
                                    }
                                    }
                                else
                                    if (dateee.equals(josing_date)) {
                                        calenderAttendance.dayType = 0
                                        if (todayc.time.compareTo(dt) < 0) {
                                            Log.i("app", "Date1 is after Date2");
                                            calenderAttendance.dayType = -1
                                        }
                                    }
                                else
                                    calenderAttendance.dayType = -2
                                calenderAttendance.isHalfDay = false
                                calenderAttendance.punchInTime = ""
                                calenderAttendance.punchOutTime = ""
                                calendarGrid.add(calenderAttendance)


                            }
                        }else{
                            if(!isAdded) {
                                val dateee = formatDate.parse(formatDate.format(dt))
                                val josing_date=formatDate.parse(attendanceModel!!.emp_date_of_joining)
                                val isJoinedDate=josing_date.compareTo(dateee)
                                val calenderAttendance = CalenderAttendance()
                                calenderAttendance.day = formatDay.format(dt);
                                calenderAttendance.date = formatDate.format(dt);
                                if(isJoinedDate<0)
                                    calenderAttendance.dayType = 0
                                else
                                    if (dateee.equals(josing_date))
                                        calenderAttendance.dayType = 0
                                    else
                                    calenderAttendance.dayType = -2
                                if (todayc.time.compareTo(dt) < 0) {
                                    Log.i("app", "Date1 is after Date2");
                                    calenderAttendance.dayType = -1
                                }
                                calenderAttendance.isHalfDay = false
                                calenderAttendance.punchInTime = ""
                                calenderAttendance.punchOutTime = ""
                                calendarGrid.add(calenderAttendance)
                            }
                        }


                        var count=0.0
                        count=count+attendanceModel?.weekoffs!!.size
                        count=count+attendanceModel?.holidays!!.size
                        /*if(attendanceModel?.half_day_leaves_count!=null&&!attendanceModel?.half_day_leaves_count.equals("0"))
                        {
                            var half_day_leaves_count=attendanceModel?.half_day_leaves_count?.toFloat()
                            var halfdays_leave_to_add=(half_day_leaves_count?.toFloat()!!/2)
                            count=count+halfdays_leave_to_add
                        }*/

                        if(attendanceModel?.full_day_leaves_count!=null&&!attendanceModel?.full_day_leaves_count.equals("0"))
                        {
                            var full_day_leaves_count=attendanceModel?.full_day_leaves_count?.toFloat()

                            count=count+ full_day_leaves_count!!
                        }

                        //count=count+attendanceModel?.levaes_list!!.size

                        binding.txtPresent.setText("${attendanceModel?.present_days_count!!}")
                        binding.txtAbsent.setText("${attendanceModel?.absent_days_count!!}")
                        binding.txtHalfDays.setText("${attendanceModel?.half_day_leaves_count!!}")
                        binding.txtPaidLeaves.setText("${count}")
                    }else{
                        val calenderAttendance= CalenderAttendance()
                        calenderAttendance.day=formatDay.format(dt);
                        calenderAttendance.date="";
                        calenderAttendance.dayType=-1
                        calenderAttendance.isHalfDay=false
                        calenderAttendance.punchInTime=""
                        calenderAttendance.punchOutTime=""
                        calendarGrid.add(calenderAttendance)
                        binding.txtPresent.setText("0")
                        binding.txtAbsent.setText("0")
                        binding.txtHalfDays.setText("0")
                        binding.txtPaidLeaves.setText("0")
                    }
                }
                else {
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
        calenderdaysAdapter.setCalenderData(calendarGrid)
    }

}