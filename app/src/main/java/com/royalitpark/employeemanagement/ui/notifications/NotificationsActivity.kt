package com.royalitpark.employeemanagement.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.Notifications
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.royalitpark.employeemanagement.databinding.ActivityNotificationsBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class NotificationsActivity : AppCompatActivity() {
    lateinit var binding:ActivityNotificationsBinding
    lateinit var notificationAdapter: NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notificationAdapter= NotificationAdapter()
        binding.recyclerNotifications.layoutManager=LinearLayoutManager(applicationContext)
        binding.recyclerNotifications.adapter=notificationAdapter
        getNotificationList()
    }

    fun onBack(view: View) {
        finish()
    }

    fun getNotificationList()
    {
        var hashMap = HashMap<String, String> (1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@NotificationsActivity,true)
        RetrofitClient.apiInterface.getNotificationList(hashMap).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                var strRes= response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj= JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());
                //Utils.showMessage(jsobObj.getString("message")!!, applicationContext)
                binding.txtNoLeaves.visibility=View.GONE
                if(jsobObj.getInt("status")==200)
                {

                    val notificationlistType: Type = object : TypeToken<ArrayList<Notifications>>() {}.type

                  val  notificationlist= Gson().fromJson<ArrayList<Notifications>>(jsobObj.getJSONArray("notification_list").toString(),notificationlistType)
                    notificationAdapter.setNotificationLists(notificationlist)
                }
                if(notificationAdapter.notificationList!!.size<=0)
                {
                    binding.txtNoLeaves.visibility=View.VISIBLE
                }
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
                if(notificationAdapter.notificationList!!.size<=0)
                {
                    binding.txtNoLeaves.visibility=View.VISIBLE
                }
            }

        }
        )
    }
}