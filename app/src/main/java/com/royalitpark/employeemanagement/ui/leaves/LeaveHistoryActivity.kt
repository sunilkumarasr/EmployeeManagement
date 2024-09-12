package com.royalitpark.employeemanagement.ui.leaves

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.Leaves
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.royalitpark.employeemanagement.databinding.ActivityLeaveHistoryBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LeaveHistoryActivity : AppCompatActivity() {
    lateinit var binding:ActivityLeaveHistoryBinding
    lateinit var leaveHistoryAdapter: LeaveHistoryAdapter
    lateinit var leavesList:ArrayList<Leaves>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLeaveHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        leaveHistoryAdapter= LeaveHistoryAdapter()
        leavesList= ArrayList()
        binding.recyclerLeave.layoutManager=LinearLayoutManager(applicationContext)
        binding.recyclerLeave.adapter=leaveHistoryAdapter
        getLeaves()
    }

    fun onBack(view: View) {
        finish()
    }
    fun addLeave(view: View) {
        startActivityForResult(Intent(applicationContext,LeaveRequestActivity::class.java),100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getLeaves()
    }
    fun getLeaves()
    {
        var hashMap = HashMap<String, String> (1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        Log.e("emp_id_" , MyPref.employeeData?.id.toString())

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@LeaveHistoryActivity,true)
        RetrofitClient.apiInterface.getLeaveList(hashMap).enqueue(object : Callback<Any> {
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
                    val leavesLIstType: Type = object : TypeToken<ArrayList<Leaves>>() {}.type
                    leavesList= Gson().fromJson(jsobObj.getJSONArray("leaves").toString(),leavesLIstType)
                    Log.e("leavesList ","leavesList ${leavesList.size}")
                    leaveHistoryAdapter.setDocList(leavesList)

                }
                if(leaveHistoryAdapter.leavesList.size<=0)
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
                if(leaveHistoryAdapter.leavesList.size<=0)
                {
                    binding.txtNoLeaves.visibility=View.VISIBLE
                }
            }

        }
        )
    }
}