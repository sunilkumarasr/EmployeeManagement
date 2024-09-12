package com.royalitpark.employeemanagement.ui.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bookiron.itpark.services.RetrofitClient
import com.bookiron.itpark.utils.MyPref
import com.bookiron.itpark.utils.Utils
import com.royalitpark.employeemanagement.customviews.CustomDialog
import com.google.gson.Gson
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.FragmentHomeBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    lateinit var viewBinding: FragmentHomeBinding
    var base64Image = "";
    var imageName = "";
    var punchStatus = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.lnrPunchin.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("Confirmation")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                        if (punchStatus == "out"){
                            addWorkReport()
                        }
                        else{
                            takePhoto()
                        }

                    }

                })
                .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                    }
                })
                .show()
        }
        //viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        viewBinding.imgCameraFlip.setOnClickListener {
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            else
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateFormat = SimpleDateFormat("dd MMM")
        viewBinding.txtDate.setText("${dateFormat.format(Calendar.getInstance().time)}")
        getPunchStatus()

    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        imageName = name + ".jpeg";
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                requireActivity().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults) {
                    var imageStream: InputStream? = null
                    try {
                        val customDialog = CustomDialog(requireActivity());
                        customDialog.showDialog(activity, true)
                        imageStream = output.savedUri?.let {
                            requireActivity().getContentResolver().openInputStream(
                                it
                            )
                        }
                        val selectImage =
                            getResizedBitmap(BitmapFactory.decodeStream(imageStream), 250)
                        val baos = ByteArrayOutputStream()
                        selectImage.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                        val b = baos.toByteArray()
                        base64Image = Base64.encodeToString(b, Base64.DEFAULT)
                        customDialog.closeDialog()

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val msg = "Photo capture succeeded: ${output.savedUri}"

                    updatePunchStatus()
                }
            }
        )
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun captureVideo() {}

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default


            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {

        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    requireActivity(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }

    fun getPunchStatus() {
        viewBinding.lnrPunchin.visibility = View.INVISIBLE
        var hashMap = HashMap<String, String>(1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());
        Log.e("emp_id_", MyPref.employeeData?.id.toString())

        if (!Utils.checkConnectivity(requireActivity())) {
            Utils.showMessage("Please check your connection ", requireActivity())
            return
        }
        val customDialog = CustomDialog(requireActivity());
        customDialog.showDialog(activity, true)
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
                    viewBinding.lnrPunchin.visibility = View.VISIBLE
                    punchStatus = jsobObj.getString("activity")
                    val punch_display_status = jsobObj.getString("punch_display_status")
                    if (punch_display_status.equals("0") || punch_display_status.equals("0.0")) {
                        viewBinding.lnrPunchin.visibility = View.INVISIBLE
                        //val punch_out=jsobObj.getString("punch_out")

                        viewBinding.txtStatus.setTextColor(Color.parseColor("#26CD0B"))
                        viewBinding.relCam.visibility = View.GONE
                        if (punchStatus.equals("over")) {
                            val punch_out = jsobObj.getString("punch_out")
                            viewBinding.txtStatus.setText("Punch Out at ${punch_out}")
                            viewBinding.lnrComplete.visibility = View.VISIBLE
                        } else {
                            viewBinding.txtStatus.setText("${jsobObj.getString("message")}")
                            viewBinding.lnrComplete.visibility = View.GONE
                        }
                        return
                    }
                    (activity as DashBoardActivity).updateStatus(punchStatus)
                    if (punchStatus.isEmpty() || punchStatus.equals("in")) {
                        viewBinding.btnPunchin.setText("Punch In Now")
                        viewBinding.txtStatus.setText("Absent")
                        viewBinding.txtStatus.setTextColor(Color.parseColor("#FFFF00"))
                    } else if (punchStatus.equals("out")) {
                        val punch_in = jsobObj.getString("punch_in")
                        viewBinding.btnPunchin.setText("Punch Out Now")
                        viewBinding.txtStatus.setText("Punch In at ${punch_in}")
                        viewBinding.txtStatus.setTextColor(Color.parseColor("#FFB233"))
                    } else {
                        viewBinding.lnrPunchin.visibility = View.INVISIBLE
                        val punch_out = jsobObj.getString("punch_out")
                        viewBinding.txtStatus.setText("Punch Out at ${punch_out}")
                        viewBinding.txtStatus.setTextColor(Color.parseColor("#26CD0B"))
                        viewBinding.relCam.visibility = View.GONE
                        viewBinding.lnrComplete.visibility = View.VISIBLE
                    }
                    return
                }
                //Utils.showMessage(jsobObj.getString("message")!!, requireActivity())


            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", requireActivity())
            }

        }
        )
    }

    lateinit var call: Call<Any>

    var enteredValue = ""
    fun addWorkReport() {

        val view = layoutInflater.inflate(R.layout.logout_popup, null)
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(view)
            .create()
        val edit_msg = view.findViewById<AppCompatEditText>(R.id.edit_msg)
        val btn_submit = view.findViewById<AppCompatButton>(R.id.btn_submit)
        btn_submit.setOnClickListener {

            enteredValue = edit_msg.text.toString()

            if (enteredValue.isEmpty()) {
                Utils.showMessage("Enter Work Report", requireContext())
            } else {
                dialog.dismiss()
                takePhoto()
                // workReport(enteredValue)
            }

        }

        dialog.show()
    }

    fun updatePunchStatus() {
        viewBinding.lnrPunchin.visibility = View.INVISIBLE
        var hashMap = HashMap<String, String>(1)
        hashMap.put("emp_id", MyPref.employeeData?.id.toString());

        if (!Utils.checkConnectivity(requireActivity())) {
            Utils.showMessage("Please check your connection ", requireActivity())
            return
        }
        val customDialog = CustomDialog(requireActivity());
        customDialog.showDialog(activity, true)

        if (punchStatus.isEmpty() || punchStatus == "in") {
            hashMap.put("punch_in_image_name", imageName);
            hashMap.put("punch_in_image_file", base64Image);
            call = RetrofitClient.apiInterface.punchIn(hashMap)
        } else if (punchStatus == "out") {
            call = RetrofitClient.apiInterface.punchOut(hashMap)
            hashMap.put("punch_out_image_name", imageName);
            hashMap.put("punch_out_image_file", base64Image);
            hashMap.put("report", enteredValue);
        }

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                customDialog.closeDialog()
                var strRes = response.body().toString();
                val gson = Gson()
                val successResponse = gson.toJson(response.body())
                val jsobObj = JSONObject(successResponse)
                //strRes=strRes.replace("!!","")
                Log.d("strRes ", strRes.toString());

                if (jsobObj.getInt("status") == 200) {
                    getPunchStatus()
                    return
                }
                Utils.showMessage(jsobObj.getString("message")!!, requireActivity())
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                customDialog.closeDialog()
                Utils.showMessage("Try again", requireActivity())
            }

        }
        )
    }
}