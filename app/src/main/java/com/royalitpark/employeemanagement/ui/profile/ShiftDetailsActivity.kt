package com.royalitpark.employeemanagement.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.EmployeeShiftDetailsResponse
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.ActivityShiftDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShiftDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityShiftDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShiftDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getShiftDetails()
    }

    fun onBack(view: View) {
        finish()
    }
    fun getShiftDetails(){
        var hashMap = HashMap<String, String> (3)
        // val cal: Calendar = Calendar.getInstance()
        hashMap.put("employee_id", MyPref.employeeData?.id.toString());
        val customDialog= CustomDialog(this@ShiftDetailsActivity);
        customDialog.showDialog(this@ShiftDetailsActivity,true)
        RetrofitClient.apiInterface.shiftDetails(hashMap).enqueue(object :
            Callback<EmployeeShiftDetailsResponse> {




            override fun onResponse(call: Call<EmployeeShiftDetailsResponse>, response: Response<EmployeeShiftDetailsResponse>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();

                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());
        val employeeShiftDetailsResponse=response.body()
                if(employeeShiftDetailsResponse?.status==200)
                {
                   if(employeeShiftDetailsResponse.shiftDetails!=null&& employeeShiftDetailsResponse.shiftDetails.size>0)
                   {
                       binding.txtStartTime.setText(employeeShiftDetailsResponse.shiftDetails[0].start)
                       binding.txtEndTime.setText(employeeShiftDetailsResponse.shiftDetails[0].end)
                   }else
                   {
                       binding.txtStartTime.setText("00")
                       binding.txtEndTime.setText("00")
                   }
                    if(employeeShiftDetailsResponse.assignedWorkingDays!=null&& employeeShiftDetailsResponse.assignedWorkingDays.size>0)
                    {
                        employeeShiftDetailsResponse.assignedWorkingDays.forEach {
                            if(it.id.equals("1"))
                            {
                                binding.txtMonday.setBackgroundResource(R.drawable.circle_right)
                            }
                            if(it.id.equals("2"))
                            {
                                binding.txtTuesday.setBackgroundResource(R.drawable.circle_right)
                            }
                            if(it.id.equals("3"))
                            {
                                binding.txtWed.setBackgroundResource(R.drawable.circle_right)
                            }
                            if(it.id.equals("4"))
                            {
                                binding.txtThur.setBackgroundResource(R.drawable.circle_right)
                            }
                            if(it.id.equals("5"))
                            {
                                binding.txtFri.setBackgroundResource(R.drawable.circle_right)
                            }
                            if(it.id.equals("6"))
                            {
                                binding.txtSat.setBackgroundResource(R.drawable.circle_right)
                            }
                            if(it.id.equals("7"))
                            {
                                binding.txtSuund.setBackgroundResource(R.drawable.circle_right)
                            }
                        }
                    }else
                    {
                        binding.txtMonday.setBackgroundResource(R.drawable.circle_red)
                        binding.txtTuesday.setBackgroundResource(R.drawable.circle_red)
                        binding.txtWed.setBackgroundResource(R.drawable.circle_red)
                        binding.txtThur.setBackgroundResource(R.drawable.circle_red)
                        binding.txtFri.setBackgroundResource(R.drawable.circle_red)
                        binding.txtSat.setBackgroundResource(R.drawable.circle_red)
                        binding.txtSuund.setBackgroundResource(R.drawable.circle_red)
                    }

                    return
                }
                //Utils.showMessage(jsobObj.getString("message")!!, requireActivity())


            }

            override fun onFailure(call: Call<EmployeeShiftDetailsResponse>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }
}