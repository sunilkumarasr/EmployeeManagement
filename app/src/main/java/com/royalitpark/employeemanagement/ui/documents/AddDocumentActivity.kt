package com.royalitpark.employeemanagement.ui.documents

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
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.bumptech.glide.Glide
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.google.gson.Gson
import com.royalitpark.employeemanagement.databinding.ActivityAddDocumentBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddDocumentActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddDocumentBinding
    var base64Image=""
    var imageName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgLeaveDocDelete.setOnClickListener {
            base64Image=""
            imageName=""
            binding.imgLeaveDoc.setImageBitmap(null)
            binding.relLeaveDoc.visibility=View.GONE
        }

    }

    fun onBack(view: View) {
        finish()
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
            Glide.with(applicationContext)
                .load(bitmap)
            .into(binding.imgLeaveDoc);



        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Image pick error","image Pick Error ${e.printStackTrace()}")
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
        var display_name=""


    fun uploadDoc(view: View) {

        display_name=binding.editDocName.text.toString()


        if(display_name.isEmpty())
        {
            Utils.showMessage("Please add document name ", applicationContext)

            return
        }
        if(base64Image.isEmpty())
        {
            Utils.showMessage("Please add your document ", applicationContext)

            return
        }


        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        var hashMap = HashMap<String, String> (4)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        hashMap.put("display_name",display_name);
        hashMap.put("document_name",imageName);
        hashMap.put("document_file",base64Image);
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@AddDocumentActivity,true)
        RetrofitClient.apiInterface.addDocuments(hashMap).enqueue(object : Callback<Any> {




            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj= JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());
                Utils.showMessage(jsobObj.getString("message")!!, applicationContext)
                if(jsobObj.getInt("status")==200||jsobObj.getInt("status")==201)
                {
                    finish()

                }
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                //Utils.showMessage("Try again", applicationContext)
            }

        }
        )
    }
}