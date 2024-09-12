package com.royalitpark.employeemanagement.ui.profile

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
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.services.RetrofitClient.PROFILE_IMAGE_PATH
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.ActivitySettingsBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Calendar

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    var base64Image = ""
    var imageName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //profile details
        fetchProfile()


        binding.imgEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            // set type
            // set type
            intent.type = "image/*"
            // start activity result
            // start activity result
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
        }
        binding.profileImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            // set type
            // set type
            intent.type = "image/*"
            // start activity result
            // start activity result
            startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
        }

        binding.imgEditProfile.setOnClickListener {
            startActivity(Intent(applicationContext, BasicDetailsActivity::class.java))
        }

    }

    fun fetchProfile() {
        var hashMap = HashMap<String, String>(1)
        hashMap.put("employee_id", MyPref.employeeData?.id.toString());
        Log.e("employee_id_", MyPref.employeeData?.id.toString())

        if (!Utils.checkConnectivity(applicationContext)) {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog = CustomDialog(applicationContext);
        customDialog.showDialog(this@SettingsActivity, true)
        RetrofitClient.apiInterface.getProfile(hashMap).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                var strRes = response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj = JSONObject(successResponse)
                if (jsobObj.getInt("status") == 200) {
                    //save profile
                    MyPref.setUser(
                        applicationContext,
                        jsobObj.getJSONArray("employee_data").get(0).toString()
                    )

                    val employeeDataArray = jsobObj.getJSONArray("employee_data")
                    val firstEmployeeObject = employeeDataArray.getJSONObject(0)
                    val profileImage = firstEmployeeObject.getString("profile_image")

                    binding.txtName.setText("${firstEmployeeObject.getString("full_name")}")
                    binding.txtMobile.setText("${firstEmployeeObject.getString("mobile")}")

                    if (profileImage.isNotEmpty()) {
                        Glide.with(applicationContext).load(PROFILE_IMAGE_PATH + profileImage)
                            .apply(
                                RequestOptions().placeholder(R.drawable.img_profile_avatar)
                                    .error(R.drawable.img_profile_avatar)
                            )
                            .into(binding.profileImg)
                    }
                    return
                }
                Utils.showMessage(jsobObj.getString("message"), applicationContext)
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }

    fun onIDCard(view: View) {
        startActivity(Intent(applicationContext, IdCardDetails::class.java))

    }

    fun onBack(view: View) {
        finish()
    }

    fun pickImage(view: View) {}
    fun onBasicDetails(view: View) {
        startActivity(Intent(applicationContext, BasicDetailsActivity::class.java))
    }

    fun onAttenDetails(view: View) {
        startActivity(Intent(applicationContext, ShiftDetailsActivity::class.java))
    }

    fun onBankDetails(view: View) {
        startActivity(Intent(applicationContext, BankAccountDetailsActivity::class.java))
    }

    fun onUserPermission(view: View) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null)
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
            uploadIMage()


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun uploadIMage() {
        if (base64Image.isEmpty()) {
            Utils.showMessage("Please try again", applicationContext)
            return
        }
        if (imageName.isEmpty()) {
            imageName = "${Calendar.getInstance().timeInMillis}.jpeg"
        }

        var hashMap = HashMap<String, String>()
        hashMap.put("id", MyPref.employeeData?.id.toString());
        hashMap.put("profile_image_name", imageName);
        hashMap.put("profile_image_file", base64Image);

        if (!Utils.checkConnectivity(applicationContext)) {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog = CustomDialog(applicationContext);
        customDialog.showDialog(this@SettingsActivity, true)
        RetrofitClient.apiInterface.uploadProfileImage(hashMap).enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                var strRes = response.body();
                val jsonObject: JsonObject? = strRes?.let { Utils.getJsonObject(it) }

                Utils.showMessage(jsonObject!!.get("message").asString, applicationContext)
                if (jsonObject!!.get("status").asString == "200") {
                    fetchProfile()
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