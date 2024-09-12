package com.royalitpark.employeemanagement.ui.attendance


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.royalitpark.employeemanagement.models.CalenderAttendance
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.royalitpark.employeemanagement.databinding.FragmentShowAttendanceDetailsBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShowAttendanceDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowAttendanceDetailsFragment(val calenderAttendance: CalenderAttendance) : BottomSheetDialogFragment() {
lateinit var  binding:FragmentShowAttendanceDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,  container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        binding=FragmentShowAttendanceDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View,  savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("calenderAttendance","calenderAttendance ${calenderAttendance.dayType}${calenderAttendance.punchInTime}")
        if(calenderAttendance!=null)
        {
            binding.txtDate.setText(calenderAttendance.date)
            if(calenderAttendance.dayType==2)
            {
                binding.lnrLeavesDisplay.visibility=View.VISIBLE
                binding.lnrPresent.visibility=View.GONE
                binding.lnrPunhcInDetails.visibility=View.GONE
                binding.lnrFullPresent.visibility=View.GONE
                /*if( calenderAttendance.isHalfDay)
                {

                }*/

            }else if(calenderAttendance.dayType==1){
                binding.lnrLeavesDisplay.visibility=View.GONE
                    binding.lnrPunhcInDetails.visibility=View.VISIBLE
                binding.lnrFullPresent.visibility=View.GONE
                binding.txtNotDate.setText(calenderAttendance.punchInTime)
                if(calenderAttendance.punchOutTime!=null&& calenderAttendance.punchOutTime!!.isNotEmpty()) {
                    binding.txtPunchOutTime.setText("${calenderAttendance.punchOutTime}")
                    binding.lnrPunchOut.visibility=View.VISIBLE
                }
                if(calenderAttendance.punch_in_image!=null&& calenderAttendance.punch_in_image!!.isNotEmpty()) {
                    binding.txtNotMsg.setText(calenderAttendance.punch_in_image)
                }else  binding.txtNotMsg.setText("")

                binding.radioPresent.isChecked=true
            }else if(calenderAttendance.dayType==0){
                binding.lnrLeavesDisplay.visibility=View.GONE
                binding.lnrPresent.visibility=View.VISIBLE
                    binding.lnrPunhcInDetails.visibility=View.GONE
                binding.lnrFullPresent.visibility=View.GONE

                binding.lnrFullPresent.visibility=View.GONE
                binding.lnrAbsent.visibility=View.VISIBLE
                binding.radioAbsent.isChecked=true
                binding.radioPresent.visibility=View.GONE

            }else if(calenderAttendance.dayType==3){
//                binding.lnrLeavesDisplay.visibility=View.GONE
//                binding.lnrPresent.visibility=View.GONE
//                binding.lnrPunhcInDetails.visibility=View.GONE
//                binding.lnrFullPresent.visibility=View.GONE
//                binding.lnrFullPresent.visibility=View.GONE

                binding.lnrLeavesDisplay.visibility=View.GONE
                binding.lnrPresent.visibility=View.VISIBLE
                binding.lnrFullPresent.visibility=View.GONE
                binding.lnrPunhcInDetails.visibility=View.GONE

            }else
            {
                binding.lnrLeavesDisplay.visibility=View.GONE
                binding.lnrPresent.visibility=View.VISIBLE
                binding.lnrFullPresent.visibility=View.GONE
                binding.lnrPunhcInDetails.visibility=View.GONE
            }

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    fun onClick(view: View) {
        val tvSelected = view as TextView

    }
}