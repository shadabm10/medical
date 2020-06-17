package com.rootscare.ui.myupcomingappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.appointmenthistoryresponse.PhysiotherapyAppointmentItem
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_my_upcomingappointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdaptePhysiotherapylUpComingAppointment  (val pathologyAppointmentList: ArrayList<PhysiotherapyAppointmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdaptePhysiotherapylUpComingAppointment.ViewHolder>() {
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
        return pathologyAppointmentList?.size!!
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
//            itemView?.root?.btn_view_trainner_profile?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(trainerList?.get(local_position)?.id?.toInt()!!)
//            })

//            itemView.root?.img_bid?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_bid,local_position)
//                    //serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_bid,it1) }
//                }
//            }
//
//            itemView.root?.img_details?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_details,local_position)
//                    // serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_details,it1) }
//                }
//            }


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if(pathologyAppointmentList?.get(pos)?.orderId!=null && !pathologyAppointmentList?.get(pos)?.orderId.equals("")){
                itemView?.rootView?.txt_upcomming_appointment?.setText(pathologyAppointmentList?.get(pos)?.orderId)
            }else{
                itemView?.rootView?.txt_upcomming_appointment?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.patientName!=null && !pathologyAppointmentList?.get(pos)?.patientName.equals("")){
                itemView?.rootView?.txt_patient_name?.setText(pathologyAppointmentList?.get(pos)?.patientName)
            }else{
                itemView?.rootView?.txt_patient_name?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.physiotherapistName!=null && !pathologyAppointmentList?.get(pos)?.physiotherapistName.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(pathologyAppointmentList?.get(pos)?.physiotherapistName)
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.bookingDate!=null && !pathologyAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

            if(pathologyAppointmentList?.get(pos)?.bookingDate!=null && !pathologyAppointmentList?.get(pos)?.bookingDate.equals("")){
                itemView?.rootView?.txt_doctor_name?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",pathologyAppointmentList?.get(pos)?.bookingDate))
            }else{
                itemView?.rootView?.txt_doctor_name?.setText("")
            }

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