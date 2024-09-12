package com.royalitpark.employeemanagement.ui.documents

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.royalitpark.employeemanagement.models.EmpDocuments
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.royalitpark.employeemanagement.databinding.ActivityDocumentsListBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

class DocumentsListActivity : AppCompatActivity() {
    lateinit var binding:ActivityDocumentsListBinding
    lateinit var documentListAdapter:DocumentListAdapter
    var documentsLIst=ArrayList<EmpDocuments>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDocumentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        documentListAdapter= DocumentListAdapter(this@DocumentsListActivity)
        binding.recyclerDocument.layoutManager=LinearLayoutManager(applicationContext)
        binding.recyclerDocument.adapter=documentListAdapter
        getDocuments()
    }

    fun onBack(view: View) {
        finish()
    }

    fun addDocument(view: View) {
        startActivityForResult(Intent(applicationContext,AddDocumentActivity::class.java),100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getDocuments()
    }
    fun getDocuments()
    {
        var hashMap = HashMap<String, String> (1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());


        if(!Utils.checkConnectivity(applicationContext))
        {
            Utils.showMessage("Please check your connection ", applicationContext)
            return
        }
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@DocumentsListActivity,true)
        RetrofitClient.apiInterface.getDocuments(hashMap).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                customDialog.closeDialog()
                var strRes= response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj= JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ",strRes.toString());
                //Utils.showMessage(jsobObj.getString("message")!!, applicationContext)
                binding.txtNoDocs.visibility=View.GONE
                if(jsobObj.getInt("status")==200)
                {


                    val documentLIstType: Type = object : TypeToken<ArrayList<EmpDocuments>>() {}.type

                    documentsLIst= Gson().fromJson(jsobObj.getJSONArray("emp_documents").toString(),documentLIstType)
                    documentListAdapter.setDocList(documentsLIst)

                }
                if(documentsLIst.size<=0)
                {
                    binding.txtNoDocs.visibility=View.VISIBLE

                }else
                {
                    binding.txtNoDocs.visibility=View.GONE
                }
                /* val gson=Gson()
                val loginResponse= gson.fromJson<LoginResponse>(strRes, LoginResponse::class.java)
               */

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", applicationContext)
                if(documentsLIst.size<=0)
                {
                    binding.txtNoDocs.visibility=View.VISIBLE
                }else
                {
                    binding.txtNoDocs.visibility=View.GONE
                }
            }

        }
        )
    }
    val myExecutor = Executors.newSingleThreadExecutor()
    val myHandler = Handler(Looper.getMainLooper())
    var mImage: Bitmap? = null
    fun downloadImage(mWebPath:String)
    {
        val customDialog= CustomDialog(applicationContext);
        customDialog.showDialog(this@DocumentsListActivity,true)

        myExecutor.execute {
            mImage = mLoad(mWebPath)
            customDialog.closeDialog()
            myHandler.post {

                if(mImage!=null){
                    mSaveMediaToStorage(mImage)
                }
            }
        }
    }
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Document downloaded" , Toast.LENGTH_SHORT).show()
        }
    }
}