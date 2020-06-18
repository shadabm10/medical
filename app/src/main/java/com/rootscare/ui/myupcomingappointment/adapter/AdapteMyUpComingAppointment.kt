package com.rootscare.ui.myupcomingappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.DoctorAppointmentItem
import com.rootscare.databinding.ItemCancellAppointmentBinding
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.cancellappointment.adapter.AdapterCancelMyUpcomingAppiontment
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapteMyUpComingAppointment (val doctorAppointmentList: ArrayList<DoctorAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapteMyUpComingAppointment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemMyUpcomingappointmentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_my_upcomingappointment_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return doctorAppointmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemMyUpcomingappointmentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
//

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            if(doctorAppointmentList?.get(pos)?.orderId!=null && !doctorAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_upcomming_appointment?.setText(doctorAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_upcomming_appointment?.setText("")
            }

            if(doctorAppointmentList?.get(pos)?.patientName!=null && !doctorAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_patient_name?.setText(doctorAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_patient_name?.setText("")
            }

            if(doctorAppointmentList?.get(pos)?.doctorName!=null && !doctorAppointmentList?.get(pos)?.doctorName.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(doctorAppointmentList?.get(pos)?.doctorName)
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

            if(doctorAppointmentList?.get(pos)?.bookingDate!=null && !doctorAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_booking_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",doctorAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_booking_date?.setText("")
            }

            if(doctorAppointmentList?.get(pos)?.appointmentDate!=null && !doctorAppointmentList?.get(pos)?.appointmentDate.equals("")){
                itemView?.rootView?.txt_appointment_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",doctorAppointmentList?.get(pos)?.appointmentDate))
            }else{
                itemView?.rootView?.txt_appointment_date?.setText("")
            }




//

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