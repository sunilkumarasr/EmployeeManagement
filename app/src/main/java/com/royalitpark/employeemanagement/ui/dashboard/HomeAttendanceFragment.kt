package com.royalitpark.employeemanagement.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bookiron.itpark.utils.Utils.Companion.getTodayThMonth
import com.royalitpark.employeemanagement.R
import com.royalitpark.employeemanagement.databinding.FragmentHomeAttendanceBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeAttendanceFragment : Fragment() {
    lateinit var binding:FragmentHomeAttendanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeAttendanceBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view=layoutInflater.inflate(R.layout.layout_notification_permission_alert,null)
        val dialog=AlertDialog.Builder(activity)
            .setView(view)
            .create()
        val txtClose=view.findViewById<View>(R.id.txt_close)
        txtClose.setOnClickListener {
            dialog.dismiss()
        }
        val btnAllow=view.findViewById<View>(R.id.btn_allow)
        btnAllow.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


        binding.txtDate.setText(getTodayThMonth())

        binding.btnRequest.setOnClickListener {

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeAttendanceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeAttendanceFragment().apply {

            }
    }

}