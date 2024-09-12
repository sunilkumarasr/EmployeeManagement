package com.royalitpark.employeemanagement.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.IDCardResponse
import com.royalitpark.employeemanagement.databinding.ActivityIdCardDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IdCardDetails : AppCompatActivity() {
    lateinit var binding:ActivityIdCardDetailsBinding
    var idcard=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIdCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webview.loadData("<html><body><h2>Loading...</h2></body></html>","text/html","utf-8")
        binding.webview.settings.setSupportZoom(true)
        binding.webview.settings.builtInZoomControls=true
        getIDCard()
    }

    fun onBack(view: View) {
        finish()
    }

    fun shareBio(view: View) {
        if(idcard==null||idcard.isEmpty())
        {
            Toast.makeText(applicationContext,"IDCard Not found",Toast.LENGTH_SHORT).show()
            return
        }
        try{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "$idcard")
            startActivity(Intent.createChooser(intent, "Share with:"))
        }catch (e:Exception)
        {

        }
    }

    fun getIDCard(){
        var hashMap = HashMap<String, String> (3)
        // val cal: Calendar = Calendar.getInstance()
        hashMap.put("employee_id", MyPref.employeeData?.id.toString());
        val customDialog= CustomDialog(this@IdCardDetails);
        customDialog.showDialog(this@IdCardDetails,true)
        RetrofitClient.apiInterface.getIDCard(hashMap).enqueue(object : Callback<IDCardResponse> {
            override fun onResponse(call: Call<IDCardResponse>, response: Response<IDCardResponse>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();

                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());
                val IDCardResponse=response.body()
                if(IDCardResponse?.status==200)
                {

                    if(IDCardResponse.idCard!=null&& IDCardResponse.idCard!!.isNotEmpty())
                    {
                        idcard= IDCardResponse.id_card_full_path!!
                        binding.webview.loadData("<html><body><img src='${IDCardResponse.id_card_full_path}' alt='IDCArd'/></body></html>","text/html","utf-8")

                    }else{
                        binding.webview.loadData("<html><body><img src='https://crm.rajasreetownship.in/assets/admin/assets/images/users/1.jpg' alt='IDCArd'/></body></html>","text/html","utf-8")

                    }

                    return
                }
                binding.webview.loadData("<html><body><h2>Error</h2></body></html>","text/html","utf-8")
                //Utils.showMessage(jsobObj.getString("message")!!, requireActivity())


            }

            override fun onFailure(call: Call<IDCardResponse>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
                binding.webview.loadData("<html><body><h2>Error</h2></body></html>","text/html","utf-8")

            }

        }
        )
    }
}