package com.royalitpark.employeemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.ui.dashboard.DashBoardActivity
import com.google.gson.Gson
import com.royalitpark.employeemanagement.databinding.ActivityOtpactivityBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpactivityBinding
    lateinit var mobile:String
    lateinit var user_id:String
    lateinit var otp:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobile= intent.getStringExtra("mobile").toString()
        user_id= intent.getStringExtra("user_id").toString()
        otp= intent.getStringExtra("otp").toString()

        binding.txtOTP.text = otp;
        binding.txtOTP.visibility=View.GONE
        /*binding.lnrBack.setOnClickListener {
            onBackPressed()
        }*/

        binding.otpOne.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0)
                    binding.otpTwo.requestFocus()
            }

        })

        binding.otpTwo.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0)
                    binding.otpThree.requestFocus()
                else
                    binding.otpOne.requestFocus()
            }

        })

        binding.otpThree.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0)
                    binding.otpFour.requestFocus()
                else  binding.otpTwo.requestFocus()
            }

        })
        binding.otpFour.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0)
                {}
                else  binding.otpThree.requestFocus()
            }

        })


       /* binding.otpFive.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0)
                    binding.otpSix.requestFocus()
                else  binding.otpFour.requestFocus()
            }
        })


        binding.otpSix.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().length>0)
                {}
                else  binding.otpFive.requestFocus()
            }

        })*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext,LoginActivity::class.java))
        finish()
    }

    fun signin(view: View) {
        var otp=binding.otpOne.text.toString();
        otp=otp+binding.otpTwo.text.toString();
        otp=otp+binding.otpThree.text.toString();
        otp=otp+binding.otpFour.text.toString();

        if(otp.length<4)
        {
            Utils.showMessage("Please enter OTP", applicationContext)
            return
        }

        var hashMap = HashMap<String, String> (2)
        hashMap.put("employee_id",user_id);
        hashMap.put("login_otp",otp);

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@OTPActivity,true)
        RetrofitClient.apiInterface.verifyOtp(hashMap).enqueue(object : Callback<Any> {
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
                    MyPref.setUser(applicationContext,jsobObj.getJSONArray("employee_data").get(0).toString())
                    val intent=Intent(applicationContext,DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                    return
                }
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }

        }
        )
      //  startActivity(Intent(applicationContext,DashBoardActivity::class.java))
       // finish()
    }
}