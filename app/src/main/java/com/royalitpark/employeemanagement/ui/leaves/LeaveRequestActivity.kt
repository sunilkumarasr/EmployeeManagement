package com.royalitpark.employeemanagement.ui.leaves

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.bumptech.glide.Glide
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.LeaveTypes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.royalitpark.employeemanagement.databinding.ActivityLeaveRequestBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Calendar

class LeaveRequestActivity : AppCompatActivity(),OnItemSelectedListener {
    lateinit var binding:ActivityLeaveRequestBinding
    var startDate=""
    var endDate=""
    var reason=""
    var emp_leave_type_id=""
    var is_half_day=0
    var dateformat= SimpleDateFormat("E, dd MMM")
    var dateformat2=SimpleDateFormat("yyyy-MM-dd")

    var startCalendar:Calendar=Calendar.getInstance()
    var endCalendar:Calendar=Calendar.getInstance()
    var base64Image=""
    var imageName=""
    lateinit var leaveTypesList: ArrayList<LeaveTypes>
    lateinit var leaveTypeAdapter: LeaveTypeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLeaveRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgLeaveDocDelete.setOnClickListener {
            base64Image=""
            imageName=""
            binding.imgLeaveDoc.setImageBitmap(null)
            binding.relLeaveDoc.visibility=View.GONE
        }
        leaveTypeAdapter= LeaveTypeAdapter()
        binding.spinner.adapter=leaveTypeAdapter
        binding.spinner.onItemSelectedListener=this@LeaveRequestActivity
        binding.checkHalfDay.setOnCheckedChangeListener { buttonView, isChecked -> {


        } }
        leaveTypesList= ArrayList()
        getLeaveTypes()

    }

    fun onBack(view: View) {
        finish()
    }
    fun fromDate(view: View) {

        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog=  DatePickerDialog(this@LeaveRequestActivity, DatePickerDialog.OnDateSetListener { _, year, month, day ->



                val pickedDateTime = Calendar.getInstance()
                var monthsdate="${month+1}"
                if(monthsdate.length==1)
                    monthsdate="0$monthsdate"

                var daydate="$day"
                if(daydate.length==1)
                    daydate="0$daydate"
            startCalendar.set(year,month+1,day,0,0)
                 startDate="$year-$monthsdate-$daydate"

                binding.inputPickip.setText(dateformat.format(dateformat2.parse(startDate)))




        }, startYear, startMonth, startDay)
        datePickerDialog.datePicker.minDate=Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }
    fun toDate(view: View) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog=  DatePickerDialog(this@LeaveRequestActivity, DatePickerDialog.OnDateSetListener { _, year, month, day ->



            endCalendar.set(year,month+1,day,0,0)
            if(startCalendar.timeInMillis>endCalendar.timeInMillis)
            {
                Utils.showMessage("To date should be after start date",applicationContext)
                endDate=""
                return@OnDateSetListener
            }
            var monthsdate="${month+1}"
            if(monthsdate.length==1)
                monthsdate="0$monthsdate"

            var daydate="$day"
            if(daydate.length==1)
                daydate="0$daydate"

            endDate="$year-$monthsdate-$daydate"

            binding.inputDrop.setText(dateformat.format(dateformat2.parse(endDate)))




        }, startYear, startMonth, startDay)
        datePickerDialog.datePicker.minDate=Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data==null)
            return
        val uri: Uri? = data?.getData()
        // Initialize bitmap
        // Initialize bitmap
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            // initialize byte stream
            val stream = ByteArrayOutputStream()
            // compress Bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            // Initialize byte array
            val bytes = stream.toByteArray()
            // get base64 encoded string
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)


            val returnCursor: Cursor = contentResolver.query(uri!!, null, null, null, null)!!
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor!!.moveToFirst()
            imageName = returnCursor!!.getString(nameIndex)
            returnCursor!!.close()

binding.relLeaveDoc.visibility=View.VISIBLE
            binding.relLeaveDoc.visibility=View.VISIBLE
            Glide.with(applicationContext)
                .load(bitmap)
                .into(binding.imgLeaveDoc);

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        // set type
        // set type
        intent.type = "image/*"
        // start activity result
        // start activity result
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
    }

    fun requestLeave(view: View) {


        reason=binding.editReason.text.toString().trim()
        if(startDate.isEmpty())
        {
            Utils.showMessage("Please add leave from date ", applicationContext)

            return
        }
        if(endDate.isEmpty())
        {
            Utils.showMessage("Please add leave end date ", applicationContext)

            return
        }
        if(emp_leave_type_id.isEmpty())
        {
            Utils.showMessage("Please select leave type ", applicationContext)

            return
        }
        if(reason.isEmpty())
        {
            Utils.showMessage("Please write your reason ", applicationContext)

            return
        }
       /* if(base64Image.isEmpty())
        {
            Utils.showMessage("Please add your document ", applicationContext)

            return
        }*/
        if(binding.checkHalfDay.isChecked)
            is_half_day=1
        else
            is_half_day=0

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        var hashMap = HashMap<String, String> (4)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());

        if(is_half_day==1)
        {
            Log.e("dateformat2.parse","dateformat2.parse(startDate).date ${dateformat2.parse(startDate).equals(dateformat2.parse(endDate))}")
            Log.e("dateformat2.parse","dateformat2.parse(startDate).date $is_half_day")

            if(!dateformat2.parse(startDate).equals(dateformat2.parse(endDate)))
            {
                Utils.showMessage("Slected more than 1 day, uncheck Half day checkbox ", applicationContext)
                return
            }
        }
        hashMap.put("from_date",startDate);
        hashMap.put("to_date",endDate);
        hashMap.put("is_half_day",is_half_day.toString());
        hashMap.put("reason",reason);
        hashMap.put("emp_leave_type_id",emp_leave_type_id);
       // hashMap.put("document_name",imageName);
        //hashMap.put("document_file",base64Image);
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@LeaveRequestActivity,true)
        RetrofitClient.apiInterface.addLeaveRequest(hashMap).enqueue(object : Callback<Any> {




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
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }
    fun getLeaveTypes() {

        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            finish()
            return
        }
        var hashMap = HashMap<String, String> (4)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());

        hashMap.put("from_date",startDate);
        hashMap.put("to_date",endDate);
        hashMap.put("is_half_day",is_half_day.toString());
       // hashMap.put("document_name",imageName);
        //hashMap.put("document_file",base64Image);
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@LeaveRequestActivity,true)
        RetrofitClient.apiInterface.getLeaveTypes(hashMap).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                var strRes= response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj= JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes dfdsf ",strRes.toString());
                if(jsobObj.getInt("status")==200)
                {
                    val leaveLIstType: Type = object : TypeToken<ArrayList<LeaveTypes>>() {}.type
                    leaveTypesList=Gson().fromJson<ArrayList<LeaveTypes>>(jsobObj.getJSONArray("leave_types").toString(),leaveLIstType)
                    leaveTypeAdapter.addLeaveTypes(leaveTypesList)
                }
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                val gson = Gson()
                val successResponse = gson.toJson("{\"status\":200,\"message\":\"Data fetched successfully!!\",\"leave_types\":[{\"id\":\"1\",\"leave_type\":\"Casual Leave\"},{\"id\":\"2\",\"leave_type\":\"Sick Leave\"}]}")
                val jsobObj= JSONObject("{\"status\":200,\"message\":\"Data fetched successfully!!\",\"leave_types\":[{\"id\":\"1\",\"leave_type\":\"Casual Leave\"},{\"id\":\"2\",\"leave_type\":\"Sick Leave\"}]}")
                //strRes=strRes.replace("!!","")
                Log.d("strRes dfdsf ",successResponse.toString());

                if(jsobObj.getInt("status")==200)
                {
                    val leaveLIstType: Type = object : TypeToken<ArrayList<LeaveTypes>>() {}.type
                    leaveTypesList=Gson().fromJson<ArrayList<LeaveTypes>>("[{\"id\":\"1\",\"leave_type\":\"Casual Leave\"},{\"id\":\"2\",\"leave_type\":\"Sick Leave\"}]",leaveLIstType)
                    leaveTypeAdapter.addLeaveTypes(leaveTypesList)
                }
               // Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if(position>0)
        {
            emp_leave_type_id=leaveTypeAdapter.leaveTypeList.get(position).id.toString()
        }else
        {
            emp_leave_type_id=""
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        emp_leave_type_id=""
    }
}