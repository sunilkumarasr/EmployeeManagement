package com.royalitpark.employeemanagement.ui.profile


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.UserProfile
import com.google.gson.Gson
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.ActivityBasicDetailsBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class BasicDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityBasicDetailsBinding
     var gender:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBasicDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.baseCardview.setOnClickListener {
            if (binding.hiddenView.getVisibility() === View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition())
                binding.hiddenView.setVisibility(View.GONE)
                binding.arrowButton.setImageResource(R.drawable.img_arrow_down)
            } else {
                TransitionManager.beginDelayedTransition(binding.baseCardview, AutoTransition())
                binding.hiddenView.setVisibility(View.VISIBLE)
                binding.arrowButton.setImageResource(R.drawable.img_arrow_up)
            }
        }


        binding.baseCardviewCompany.setOnClickListener {
            if (binding.hiddenViewCompany.getVisibility() === View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.baseCardviewCompany, AutoTransition())
                binding.hiddenViewCompany.setVisibility(View.GONE)
                binding.arrowButtonCmpany.setImageResource(R.drawable.img_arrow_down)
            } else {
                TransitionManager.beginDelayedTransition(binding.baseCardviewCompany, AutoTransition())
                binding.hiddenViewCompany.setVisibility(View.VISIBLE)
                binding.arrowButtonCmpany.setImageResource(R.drawable.img_arrow_up)
            }
        }
        binding.radiogrop.setOnCheckedChangeListener { group, checkedId->
                if(checkedId==R.id.radio_female)
                    gender="female"
                else if(checkedId==R.id.radio_male)
                    gender="male"

        }
        fetchProfile()
    }

    fun onIDCard(view: View) {
        startActivity(Intent(applicationContext,IdCardDetails::class.java))
    }
    fun savedetails(view: View) {
        updateProfile()
    }
    fun onBack(view: View) {
        finish()
    }
    fun datePicker(view: View) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog=  DatePickerDialog(this@BasicDetailsActivity, DatePickerDialog.OnDateSetListener { _, year, month, day ->




            var monthsdate="${month+1}"
            if(monthsdate.length==1)
                monthsdate="0$monthsdate"

            var daydate="$day"
            if(daydate.length==1)
                daydate="0$daydate"



             binding.editDob.setText("${year}-$monthsdate-$daydate")




        }, startYear, startMonth, startDay)
        val maxDateTime = Calendar.getInstance()
        maxDateTime.set(Calendar.YEAR,startYear-15)
        datePickerDialog.datePicker.maxDate=maxDateTime.timeInMillis

        datePickerDialog.show()
    }
    fun fetchProfile()
    {
        var hashMap = HashMap<String, String> (1)
        hashMap.put("employee_id", MyPref.employeeData?.id.toString());


        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@BasicDetailsActivity,true)
        RetrofitClient.apiInterface.getProfile(hashMap).enqueue(object : Callback<Any> {




            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj= JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());

                if(jsobObj.getInt("status")==200)
                {

                    val userProfile= Gson().fromJson(jsobObj.getJSONArray("employee_data").get(0).toString(),
                        UserProfile::class.java)
                    binding.editStaffname.setText("${userProfile.fullName}")
                    binding.editMobileNumber.setText("${userProfile.mobile}")
                    binding.editDob.setText("${userProfile.dob}")
                    binding.editAddress.setText("${userProfile.address}")
                    binding.editEmail.setText("${userProfile.email}")
                    binding.editAdhar.setText("${userProfile.aadharNumber}")

                    gender=userProfile.gender.toString()
                    if(gender.toLowerCase()=="male")
                    {
                        binding.radioMale.isChecked=true
                    }else if(gender.toLowerCase()=="female")
                    {
                        binding.radioFemale.isChecked=true
                    }else
                    {

                    }

                    binding.editBranch.setText("${userProfile.company_name}")
                    binding.editEmpid.setText("${userProfile.id}")
                    binding.editJobTitle.setText("${userProfile.designation}")
                    binding.editDoj.setText("${userProfile.joining_date}")
                    /*binding.editBranch.setText(userProfile.accountHolderName.toString())
                    binding.editJobTitle.setText(userProfile.accountHolderName.toString())
                    binding.editDoj.setText(userProfile.accountHolderName.toString())
                    binding.editEmpid.setText(userProfile.accountHolderName.toString())*/

                    return
                }
                Utils.showMessage(jsobObj.getString("message")!!, applicationContext)


            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }
    fun updateProfile()
    {
        val name=binding.editStaffname.text.toString()
        val mobile=binding.editMobileNumber.text.toString()
        val dob=binding.editDob.text.toString()
        val address=binding.editAddress.text.toString()
        val email=binding.editEmail.text.toString()
        val adhar=binding.editAdhar.text.toString()


        if(name.isEmpty()||mobile.isEmpty()||dob.isEmpty()||address.isEmpty()||email.isEmpty()||adhar.isEmpty())
        {
            Utils.showMessage("Please enter all fields ", applicationContext)
            return
        }

        var hashMap = HashMap<String, String> (1)
        hashMap.put("employee_id", MyPref.employeeData?.id.toString());
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        hashMap.put("aadhar_number", adhar);
        hashMap.put("email", email);
        hashMap.put("address", address);
        hashMap.put("dob", dob);
        hashMap.put("gender", gender);
        hashMap.put("mobile", mobile);
        hashMap.put("full_name", name);


        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }


        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@BasicDetailsActivity,true)
        RetrofitClient.apiInterface.updateProfile(hashMap).enqueue(object : Callback<Any> {




            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj= JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());
                Utils.showMessage(jsobObj.getString("message")!!, applicationContext)
                if(jsobObj.getInt("status")==200)
                {
                    finish()
                }


            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }
}