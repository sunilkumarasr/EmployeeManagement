package com.royalitpark.employeemanagement.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref.Companion.employeeData
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.BankDetails
import com.google.gson.Gson
import com.royalitpark.employeemanagement.databinding.ActivityBankAccountDetailsBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankAccountDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityBankAccountDetailsBinding
     var bankId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBankAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBankDetails()
    }

    fun onBack(view: View) {
        finish()
    }
    fun savedetails(view: View) {
        saveUpdateBankDetails()
    }

    fun getBankDetails()
    {
        var hashMap = HashMap<String, String> (1)
        hashMap.put("employee_id", employeeData?.id.toString());


        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@BankAccountDetailsActivity,true)
        RetrofitClient.apiInterface.getBankDetails(hashMap).enqueue(object : Callback<Any> {

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

                    val jsonArray=jsobObj.getJSONArray("bank_details");
                    if(jsonArray.length()>0)
                    {
                        val bankDetaila=Gson().fromJson(jsobObj.getJSONArray("bank_details").get(0).toString(),BankDetails::class.java)
                        binding.editBankName.setText(bankDetaila.bankName.toString())
                        binding.editAccountNumber.setText(bankDetaila.accountNumber.toString())
                        binding.editIfsc.setText(bankDetaila.ifscCode.toString())
                        binding.editEmpid.setText(bankDetaila.accountHolderName.toString())
                        bankId= bankDetaila.id.toString()
                    }

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
    }

    lateinit var call:Call<Any>
    fun saveUpdateBankDetails()
    {
        val bankName=binding.editBankName.text.toString()
        val bankAccNo=binding.editAccountNumber.text.toString()
        val bankAccNname=binding.editEmpid.text.toString()
        val bankifdc=binding.editIfsc.text.toString()

        if(bankName.isEmpty()){
            Utils.showMessage("Enter Bank Name", applicationContext)
            return
        }
        if(bankAccNo.isEmpty()){
            Utils.showMessage("Enter Account Number", applicationContext)
            return
        }
        val requiredLength = 12
        if (bankAccNo.length != requiredLength) {
            Utils.showMessage("Account Number must be $requiredLength digits long", applicationContext)
            return
        }
        if(bankifdc.isEmpty()){
            Utils.showMessage("Enter IFSC Code", applicationContext)
            return
        }
        if(bankAccNname.isEmpty()){
            Utils.showMessage("Enter Account Holder Name", applicationContext)
            return
        }

        var hashMap = HashMap<String, String> (6)
        hashMap.put("employee_id", employeeData?.id.toString());
        hashMap.put("id", bankId.toString());
        hashMap.put("bank_name",bankName);
        hashMap.put("account_number",bankAccNo);
        hashMap.put("ifsc_code",bankifdc);
        hashMap.put("account_holder_name",bankAccNname);

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@BankAccountDetailsActivity,true)

        if(bankId==null||bankId.isEmpty())
       call=  RetrofitClient.apiInterface.saveBankDetails(hashMap)
        else
       call=  RetrofitClient.apiInterface.updateBankDetails(hashMap)
        call.enqueue(object : Callback<Any> {

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
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("asdfghj_",t.message.toString())
                customDialog.closeDialog()
                finish()
                Utils.showMessage("Success", applicationContext)
            }

        }
        )
    }


}