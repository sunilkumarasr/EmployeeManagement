package com.royalitpark.employeemanagement.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.MyPref.Companion.employeeData
import com.bookiron.itpark.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.royalitpark.employeemanagement.LoginActivity
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.EmployeeData
import com.royalitpark.employeemanagement.ui.attendance.ViewAttendanceActivity
import com.royalitpark.employeemanagement.ui.documents.DocumentsListActivity
import com.royalitpark.employeemanagement.ui.holidays.HolidaysListActivity
import com.royalitpark.employeemanagement.ui.leaves.LeaveHistoryActivity
import com.royalitpark.employeemanagement.ui.notifications.NotificationsActivity
import com.royalitpark.employeemanagement.ui.profile.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.ActivityDashBoardBinding
import com.royalitpark.employeemanagement.ui.attendance.ViewAttendanceNewActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class DashBoardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userdata = MyPref.getUser(applicationContext)

        if (userdata != null && userdata.length > 10) {
            val gson = Gson()
            employeeData = gson.fromJson(userdata, EmployeeData::class.java)
            binding.txtName.setText(employeeData?.fullName.toString())

            val view = binding.navview.getHeaderView(0)
            var name = view.findViewById<TextView>(R.id.txt_name)
            var txt_mobile = view.findViewById<TextView>(R.id.txt_mobile)
            var profile_img = view.findViewById<ImageView>(R.id.profile_img)
            if (employeeData?.profile_image != null && employeeData?.profile_image!!.isNotEmpty()) {
                Glide.with(applicationContext).load(RetrofitClient.PROFILE_IMAGE_PATH +employeeData?.profile_image)
                    .apply(
                        RequestOptions().placeholder(R.drawable.img_profile_avatar)
                            .error(R.drawable.icon_bottom_profile)
                    )
                    .into(profile_img)
            }
            var txt_view_attendance = view.findViewById<TextView>(R.id.txt_view_attendance)
            var txt_leaves = view.findViewById<TextView>(R.id.txt_leaves)
            var txt_logout = view.findViewById<TextView>(R.id.txt_logout)
            var txt_holidays = view.findViewById<TextView>(R.id.txt_holidays)
            var txt_documents = view.findViewById<TextView>(R.id.txt_documents)
            var txt_settings = view.findViewById<TextView>(R.id.txt_settings)
            var txt_view_home = view.findViewById<TextView>(R.id.txt_view_home)
            name.setText(employeeData?.fullName.toString())
            txt_mobile.setText(employeeData?.mobile.toString())

            profile_img.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(applicationContext, SettingsActivity::class.java))
            }
            txt_view_attendance.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(applicationContext, ViewAttendanceNewActivity::class.java))
            }

            txt_view_home.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                navController.navigate(R.id.profileFragment)
            }
            binding.imgNoti.setOnClickListener {

                startActivity(Intent(applicationContext, NotificationsActivity::class.java))
            }
            txt_leaves.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(applicationContext, LeaveHistoryActivity::class.java))
            }
            txt_holidays.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(applicationContext, HolidaysListActivity::class.java))
            }
            txt_documents.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(applicationContext, DocumentsListActivity::class.java))
            }
            txt_settings.setOnClickListener {
                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(applicationContext, SettingsActivity::class.java))
            }
            txt_logout.setOnClickListener {
//                binding.myDrawerLayout.closeDrawer(GravityCompat.START)
//                MyPref.clear(applicationContext)
//                startActivity(Intent(applicationContext,LoginActivity::class.java))
//                finish()

                logOut()


            }
        }

        binding.imgMenu.setOnClickListener {
            binding.myDrawerLayout.openDrawer(GravityCompat.START);
        }

        try {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a")
            val dateFormat2 = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a")

            val d = dateFormat2.parse(dateFormat.format(Calendar.getInstance().time))
            d.hours
            val timeOfDay: Int = d.hours
            if (timeOfDay >= 0 && timeOfDay < 12) {
                binding.txtWish.setText("Good Morning");
            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                binding.txtWish.setText("Good Afternoon");
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                binding.txtWish.setText("Good Evening");
            } else if (timeOfDay >= 21 && timeOfDay < 24) {
                binding.txtWish.setText("Good Night");
            }
        } catch (e: Exception) {
            binding.txtWish.setText("Hello")
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.e("TAG", "onDestinationChanged: " + destination.label);

            if (destination.label == ("Profile"))
                binding.lnrStatistics.visibility = View.VISIBLE
            else
                binding.lnrStatistics.visibility = View.GONE
        }
        getPunchStatus()
    }

    private fun logOut() {

        binding.myDrawerLayout.closeDrawer(GravityCompat.START)
        MyPref.clear(applicationContext)
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()

        if (true) {
            return;
        }

        val view = layoutInflater.inflate(R.layout.logout_popup, null)
        val dialog = AlertDialog.Builder(this@DashBoardActivity)
            .setView(view)
            .create()
        val edit_msg = view.findViewById<AppCompatEditText>(R.id.edit_msg)
        val btn_submit = view.findViewById<AppCompatButton>(R.id.btn_submit)
        btn_submit.setOnClickListener {

            val enteredValue = edit_msg.text.toString()

            if (enteredValue.isEmpty()) {
                Utils.showMessage("Enter Work Report", applicationContext)
            } else {
                dialog.dismiss()
                workReport(enteredValue)
            }

        }

        dialog.show()
    }

    private fun workReport(enteredValue: String) {
        val empId = employeeData?.id.toString()
        val report = enteredValue
        Log.e("successemployeeData", employeeData?.id.toString())

        if (!Utils.checkConnectivity(applicationContext)) {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }

        //progress loading
        val customDialog = CustomDialog(applicationContext);
        customDialog.showDialog(this@DashBoardActivity, true)
        //api
        RetrofitClient.apiInterface.employeeReport(empId, report).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                val gson = Gson()
                val responseBody = response.body()
                if (responseBody != null) {
                    val successResponse = gson.toJson(responseBody)
                    Log.e("successResponse", successResponse)
                    val jsonObject = JSONObject(successResponse)
                    Utils.showMessage(jsonObject.getString("message")!!, applicationContext)
                    if (jsonObject.getString("status").equals("200")) {
                        binding.myDrawerLayout.closeDrawer(GravityCompat.START)
                        MyPref.clear(applicationContext)
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    }
                } else {
                    Log.e("Error", "Response body is null")
                }


            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }
        }
        )

    }

    override fun onSupportNavigateUp(): Boolean {
        // val navController = findNavController(R.id.nav_host_fragment_content_dash_board)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun onmenu(view: View) {
    }

    fun updateStatus(punchStatus: String) {
        if (punchStatus.isEmpty() || punchStatus.equals("in")) {

            binding.imgStatus.setImageResource(R.drawable.circle_red)
        } else if (punchStatus.equals("out")) {
            binding.imgStatus.setImageResource(R.drawable.circle_green)

        } else {
            binding.imgStatus.setImageResource(R.drawable.circle_red)


        }
    }

    fun getPunchStatus() {

        var hashMap = HashMap<String, String>(1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        Log.e("emp_id_", MyPref.employeeData?.id.toString())
        if (!Utils.checkConnectivity(applicationContext)) {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog = CustomDialog(this@DashBoardActivity);
        customDialog.showDialog(this@DashBoardActivity, true)
        RetrofitClient.apiInterface.getPunchStatus(hashMap).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                var strRes = response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj = JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ", strRes.toString());

                if (jsobObj.getInt("status") == 200) {

                    val punchStatus = jsobObj.getString("activity")
                    var present_days_count = "0"
                    var absent_days_counts = "0"
                    var half_days_count = "0"

                    if (jsobObj.has("present_days_count") && jsobObj.has("present_days_count") != null)
                        present_days_count = jsobObj.getString("present_days_count")

                    if (jsobObj.has("absent_days_counts") && jsobObj.has("absent_days_counts") != null)
                        absent_days_counts = jsobObj.getString("absent_days_counts")

                    if (jsobObj.has("half_days_count") && jsobObj.has("half_days_count") != null)
                        half_days_count = jsobObj.getString("half_days_count")

                    updateStatus(punchStatus)
                    binding.txtP.setText("Present Days - ${present_days_count}")
                    binding.txtA.setText("Absent Days - ${absent_days_counts}")
                    return
                }
                //Utils.showMessage(jsobObj.getString("message")!!, requireActivity())
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
            }

        }
        )
    }

}