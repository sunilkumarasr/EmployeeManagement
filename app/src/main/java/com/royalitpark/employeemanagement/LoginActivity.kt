package com.royalitpark.employeemanagement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bookiron.itpark.services.RetrofitClient.PRIVACY_URL
import com.bookiron.itpark.services.RetrofitClient.TERMS_CONDITION
import com.bookiron.itpark.services.RetrofitClient.apiInterface
import com.bookiron.itpark.utils.Utils
import com.bookiron.itpark.utils.Utils.Companion.checkConnectivity
import com.bookiron.itpark.utils.Utils.Companion.showMessage
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.royalitpark.employeemanagement.databinding.ActivityLoginBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    var fireBasetoken: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editMobile.setText("")

        fireBaseToken()
    }

    private fun fireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fireBasetoken = task.result
                Log.e("FCMfireBasetoken",fireBasetoken)
                // Now you have the FCM token, you can use it as needed.
            } else {
                Log.e("FCM Token", "Failed to get token ${task.exception}")
            }
        }
    }

    fun signin(view: View) {

        val countryCode=binding.countryCode.selectedCountryCode
        val mobile=binding.editMobile.text?.trim().toString()
        if(countryCode.isEmpty()||mobile.isEmpty()){
            Utils.showMessage("Enter all fileds",applicationContext)
            return
        }
        var hashMap = HashMap<String, String> (1)
        hashMap.put("mobile",mobile);
        hashMap.put("token",fireBasetoken);
        if(!checkConnectivity(applicationContext))
        {
            showMessage("Please check your connection ",applicationContext)
            return
        }else if (!validateMobileNumber(mobile)) {
            // Valid mobile number, you can proceed with further actions
            Utils.showMessage("Enter Valid mobile number",applicationContext)
        }else{
            val customDialog= CustomDialog(applicationContext);
            customDialog.showDialog(this@LoginActivity,true)
            apiInterface.login(hashMap).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    customDialog.closeDialog()
                    var strRes= response.body().toString();
                    val gson = Gson()
                    val successResponse = gson.toJson(response.body())
                    val jsobObj=JSONObject(successResponse)
                    //strRes=strRes.replace("!!","")
                    Log.d("strRes ",strRes.toString());
                    showMessage(jsobObj.getString("message")!!,applicationContext)
                    if(jsobObj.getInt("status")==200){
                        val user_id=jsobObj.getString("employee_id")
                        val otp=jsobObj.getInt("otp")
                        Log.e("otp_",otp.toString())

                        val intent=Intent(applicationContext,OTPActivity::class.java)
                        Log.d("mobile",mobile+"")
                        intent.putExtra("mobile",mobile)
                        intent.putExtra("user_id",user_id)
                        intent.putExtra("otp",otp.toString())
                        startActivity(intent)
                        finish()
                        return
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    customDialog.closeDialog()
                    showMessage("Try again",applicationContext)
                }

            }
            )
        }
        //startActivity(Intent(applicationContext,OTPActivity::class.java))
       // finish()
    }

    private fun validateMobileNumber(mobile: String): Boolean {
        // Regular expression for a simple validation of mobile numbers
        //val mobilePattern = "^[0-9]{10}\$"
        val mobilePattern = "^[6-9][0-9]{9}\$"

        // You can also use Patterns class for more comprehensive validation
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }

    fun termsConditions(view: View) {
        val intent=Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(PRIVACY_URL)
        startActivity(intent)
    }
    fun privacyPolicy(view: View) {
        val intent=Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(TERMS_CONDITION)
        startActivity(intent)
    }

}