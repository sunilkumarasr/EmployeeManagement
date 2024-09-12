package com.royalitpark.employeemanagement.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookiron.itpark.utils.MyPref
import com.royalitpark.employeemanagement.ui.attendance.ViewAttendanceActivity
import com.royalitpark.employeemanagement.ui.documents.DocumentsListActivity
import com.royalitpark.employeemanagement.ui.holidays.HolidaysListActivity
import com.royalitpark.employeemanagement.ui.leaves.LeaveHistoryActivity
import com.royalitpark.employeemanagement.ui.notifications.NotificationsActivity
import com.royalitpark.employeemanagement.ui.profile.SettingsActivity
import com.royalitpark.employeemanagement.databinding.FragmentProfileBinding
import com.royalitpark.employeemanagement.ui.attendance.ViewAttendanceNewActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
lateinit var binding:FragmentProfileBinding
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
        binding=FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userdata= MyPref.getUser(requireActivity())

        /*if(userdata!=null&&userdata.length>10) {
            val gson = Gson()
            MyPref.employeeData = gson.fromJson(userdata, EmployeeData::class.java)
            binding.txtName.setText(employeeData?.username.toString())
        }
        try{
            val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a")
            val dateFormat2 = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a")

            val d=dateFormat2.parse(dateFormat.format(Calendar.getInstance().time))
            d.hours
            val timeOfDay: Int = d.hours
            if(timeOfDay >= 0 && timeOfDay < 12){
                binding.txtWish.setText("Good Morning");

            }else if(timeOfDay >= 12 && timeOfDay < 16){
                binding.txtWish.setText("Good Afternoon");
            }else if(timeOfDay >= 16 && timeOfDay < 21){
                binding.txtWish.setText("Good Evening");
            }else if(timeOfDay >= 21 && timeOfDay < 24){
                binding.txtWish.setText("Good Night");
            }
        }catch (e:Exception){
            binding.txtWish.setText("Hello")
        }*/


        binding.viewSettings.setOnClickListener {
            startActivity(Intent(activity,SettingsActivity::class.java))
        }
        binding.viewLeave.setOnClickListener {
            startActivity(Intent(activity,LeaveHistoryActivity::class.java))
        }
        binding.viewAttendance.setOnClickListener {
            startActivity(Intent(activity,ViewAttendanceNewActivity::class.java))
        }
        binding.viewDocuments.setOnClickListener {
            startActivity(Intent(activity, DocumentsListActivity::class.java))
        }
        binding.viewNotify.setOnClickListener {
            startActivity(Intent(activity, NotificationsActivity::class.java))
        }

        binding.viewHoliday.setOnClickListener {
            startActivity(Intent(activity, HolidaysListActivity::class.java))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}