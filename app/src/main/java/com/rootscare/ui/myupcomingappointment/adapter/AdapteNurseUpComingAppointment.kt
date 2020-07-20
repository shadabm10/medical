package com.rootscare.ui.myupcomingappointment.adapter

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.interfaces.OnNurseAppointmentCancel
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.NurseAppointmentItem
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import com.rootscare.utils.AppConstants
import kotlinx.android.synthetic.main.item_appointmentlist_recyclerview.view.*
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.*
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_appointment_date
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_booking_date
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_doctor_name
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.txt_patient_name
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapteNurseUpComingAppointment  (val pathologyAppointmentList: ArrayList<NurseAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapteNurseUpComingAppointment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnNurseAppointmentCancel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemMyUpcomingappointmentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_my_upcomingappointment_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return pathologyAppointmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemMyUpcomingappointmentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.btn_appointment_cancel?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onCancelBtnClick(pathologyAppointmentList?.get(local_position)?.id!!)
            })

            itemView?.root?.btn_appointment_reschedule?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onRescheduleBtnClick(pathologyAppointmentList?.get(local_position)!!,local_position.toString())
            })

//


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(pathologyAppointmentList?.get(pos)?.acceptanceStatus.equals("Pending")){
                itemView?.rootView?.btn_appointment_cancel?.visibility=View.VISIBLE
                itemView?.rootView?.btn_appointment_reschedule?.visibility=View.VISIBLE
                itemView?.rootView?.btn_accepted?.visibility=View.GONE
            }else{
                itemView?.rootView?.btn_appointment_cancel?.visibility=View.GONE
                itemView?.rootView?.btn_appointment_reschedule?.visibility=View.GONE
                itemView?.rootView?.btn_accepted?.visibility=View.VISIBLE
                itemView?.rootView?.btn_accepted?.setEnabled(false);
                itemView?.rootView?.btn_accepted?.setText(pathologyAppointmentList?.get(pos)?.acceptanceStatus)
            }

            if(pathologyAppointmentList?.get(pos)?.orderId!=null && !pathologyAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_upcomming_appointment?.setText(pathologyAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_upcomming_appointment?.setText("")
            }
            if(pos==AppConstants.NurserescheculeClickPosation){
                if(AppConstants.IS_NURSE_RESCHEDULE){
                    val handler = Handler()
                    handler.postDelayed({
                        AppConstants.IS_NURSE_RESCHEDULE=false
                        itemView?.rootView?.txt_appointment_date?.setBackgroundColor(Color.parseColor("#ffffff"))
//                   itemView?.rootView?.txt_appointment_date?.
                        // Execute a comethode in the intervel 3sec
                    }, 3000)
//                itemView?.rootView?.txt_appointment_date?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.border_with_green_bg) )

                    itemView?.rootView?.txt_appointment_date?.setBackgroundColor(Color.parseColor("#D2F2F5"))
                }else{
                    itemView?.rootView?.txt_appointment_date?.setBackgroundColor(Color.parseColor("#ffffff"))

                }
            }else{
                itemView?.rootView?.txt_appointment_date?.setBackgroundColor(Color.parseColor("#ffffff"))
            }



            if(pathologyAppointmentList?.get(pos)?.patientName!=null && !pathologyAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_patient_name?.setText(pathologyAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_patient_name?.setText("")
            }
            itemView?.rootView?.txt_name_header?.setText("Nurse Name :")
            if(pathologyAppointmentList?.get(pos)?.nurseName!=null && !pathologyAppointmentList?.get(pos)?.nurseName.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(pathologyAppointmentList?.get(pos)?.nurseName)
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.bookingDate!=null && !pathologyAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_booking_date?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.fromDate!=null && !pathologyAppointmentList?.get(pos)?.fromDate.equals("")){
                itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.fromDate))
            }else{
                itemView?.rootView?.txt_appointment_date?.setText("")
            }

            if (pathologyAppointmentList?.get(pos)?.patientContact!=null && !pathologyAppointmentList?.get(pos)?.patientContact.equals("")){
                itemView?.rootView?.txt_upcoming_appointmentphone_no?.setText(pathologyAppointmentList?.get(pos)?.patientContact)
            }else{
                itemView?.rootView?.txt_upcoming_appointmentphone_no?.setText("")
            }
            itemView?.rootView?.txt_upappointment_status?.setText(pathologyAppointmentList?.get(pos)?.appointmentStatus)
            itemView?.rootView?.txt_upappointment_acceptance?.setText(pathologyAppointmentList?.get(pos)?.acceptanceStatus)

        }

        fun formateDateFromstring(inputFormat: String?, outputFormat: String?, inputDate: String?): String? {
            var parsed: Date? = null
            var outputDate = ""
            val df_input =
                SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output =
                SimpleDateFormat(outputFormat, Locale.getDefault())
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: ParseException) {
            }
            return outputDate
        }
    }

}