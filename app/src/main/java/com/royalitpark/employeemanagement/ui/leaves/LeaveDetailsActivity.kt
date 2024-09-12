package com.royalitpark.employeemanagement.ui.leaves

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bookiron.itpark.utils.MyPref
import com.royalitpark.employeemanagement.databinding.ActivityLeaveDetailsBinding
import java.text.SimpleDateFormat

class LeaveDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityLeaveDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLeaveDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val type=intent.getStringExtra("type")
        val date=intent.getStringExtra("date")
        val from=intent.getStringExtra("from")
        val to=intent.getStringExtra("to")
        val reson=intent.getStringExtra("reson")
        val status=intent.getStringExtra("status")
        val is_half_day=intent.getStringExtra("is_half_day")

        binding.txtName.setText(MyPref.employeeData?.fullName.toString())
        binding.type.setText("$type")
        binding.txtLeaveReason.setText("$reson")
        binding.txtLeaveApplyDate.setText("$date")
        binding.btnAllow.setText("$status")
        if(is_half_day!=null&&is_half_day.equals("1"))
            binding.checkHalfDay.isChecked=true
        else
            binding.checkHalfDay.isChecked=false

        try{
            Log.d("from","from $from")
            val format=SimpleDateFormat("yyyy-MM-dd")
            val format2=SimpleDateFormat("dd MMM yyyy")
            binding.txtLeaveOnDate.setText("From: ${format2.format(format.parse(from))}\nTo: ${format2.format(format.parse(to))}")
            binding.txtLeaveApplyDate.setText("${format2.format(format.parse(date))}")

            val startDate=format.parse(from)
            val endDate=format.parse(to)

            val different: Long = endDate.getTime() - startDate.getTime()

            System.out.println("startDate : $startDate")
            System.out.println("endDate : $endDate")
            println("different : $different")


            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = different / daysInMilli
            binding.txtLeaveDuration.setText("${elapsedDays+1} days")
        }catch (e:Exception)
        {

        }
    }

    fun onBack(view: View) {
        finish()
    }
}