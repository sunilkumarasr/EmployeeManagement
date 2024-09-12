package com.royalitpark.employeemanagement.ui.holidays

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.HolidayList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.ActivityHolidaysListBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class HolidaysListActivity : AppCompatActivity() {
    lateinit var binding:ActivityHolidaysListBinding
    lateinit var holidaysAdapter: HolidaysAdapter
    lateinit var holidayList: ArrayList<HolidayList>
    private val VERTICAL_ITEM_SPACE = 48

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHolidaysListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerHoliday.layoutManager=LinearLayoutManager(applicationContext)
        holidaysAdapter= HolidaysAdapter()
        val itemDecorator = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider)!!)
        binding.recyclerHoliday.adapter=holidaysAdapter
        binding.recyclerHoliday.addItemDecoration( itemDecorator);
        holidayList= ArrayList()

        getHolidays()
    }

    fun onBack(view: View) {
        finish()
    }

    fun getHolidays()
    {
        var hashMap = HashMap<String, String> (1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());


        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@HolidaysListActivity,true)
        RetrofitClient.apiInterface.getHolidays(hashMap).enqueue(object : Callback<Any> {




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


                    val leavesLIstType: Type = object : TypeToken<ArrayList<HolidayList>>() {}.type

                    holidayList= Gson().fromJson(jsobObj.getJSONArray("holiday_list").toString(),leavesLIstType)
                    holidaysAdapter.setHolidayData(holidayList)

                    try {
                        if (holidayList.size > 0) {
                            binding.btnYear.visibility = View.VISIBLE
                            binding.btnYear.setText(
                              "Holiday List - "+  holidayList.get(0).date?.split("-")?.get(0) ?: ""
                            )
                        } else binding.btnYear.visibility = View.GONE
                    }catch (e:Exception)
                    {
                        e.printStackTrace()
                    }

                }
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
                finish()
            }

        }
        )
    }
}