package com.rootscare.ui.viewprescription.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rootscare.R
import com.rootscare.data.model.api.response.patientprescription.ResultItem
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.databinding.ItemViewPresprictionRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import com.rootscare.ui.myupcomingappointment.adapter.AdapteMyUpComingAppointment
import com.rootscare.ui.showimagelarger.TransaprentPopUpActivityForImageShow
import com.rootscare.utils.AnimUtils
import com.rootscare.utils.AppConstants
import kotlinx.android.synthetic.main.item_home_caregiver_recyclerviewlist.view.*
import kotlinx.android.synthetic.main.item_medical_records_file_upload.view.*
import kotlinx.android.synthetic.main.item_view_prespriction_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterViewPrescriptionRecyclerview (val prescriptionList: ArrayList<ResultItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterViewPrescriptionRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemViewPresprictionRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_view_prespriction_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return prescriptionList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemViewPresprictionRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
//

            if (prescriptionList?.get(local_position)?.ePrescription?.toLowerCase(Locale.ROOT)!!.contains("pdf")) {

                itemView?.root?.crdview_view_prescription?.setOnClickListener(View.OnClickListener {
                    AnimUtils.animateIntent(context as HomeActivity, itemView?.root?.crdview_view_prescription, null,
                        TransaprentPopUpActivityForImageShow.newIntent(context as HomeActivity,
                            "http://166.62.54.122/rootscare/uploads/images/"+prescriptionList?.get(local_position)?.ePrescription!!, "pdf"),
                        AppConstants.REQUEST_RESULT_CODE_FOR_TRANSITION_ANIM_LABLISTING_TO_POPUPIMAGESHOW)
                })

            }else{
                itemView?.root?.crdview_view_prescription?.setOnClickListener(View.OnClickListener {
                    AnimUtils.animateIntent(context as HomeActivity, itemView?.root?.crdview_view_prescription, null,
                        TransaprentPopUpActivityForImageShow.newIntent(context as HomeActivity,
                            "http://166.62.54.122/rootscare/uploads/images/"+prescriptionList?.get(local_position)?.ePrescription!!, "image"),
                        AppConstants.REQUEST_RESULT_CODE_FOR_TRANSITION_ANIM_LABLISTING_TO_POPUPIMAGESHOW)
                })
            }

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if (prescriptionList?.get(pos)?.prescriptionNumber!=null && !prescriptionList?.get(pos)?.prescriptionNumber.equals("")){
                itemView?.txt_description?.setText(prescriptionList?.get(pos)?.prescriptionNumber)
            }else{
                itemView?.txt_description?.setText("")
            }
            if (prescriptionList?.get(pos)?.createdDate!=null && !prescriptionList?.get(pos)?.createdDate.equals("")){
                itemView?.txt_prescription_date?.setText(formateDateFromstring("yyyy-MM-dd","dd MMM yyyy",prescriptionList?.get(pos)?.createdDate))
            }else{
                itemView?.txt_prescription_date?.setText("")
            }
            if (!prescriptionList?.get(local_position)?.ePrescription?.toLowerCase(Locale.ROOT)!!.contains("pdf")) {
                Glide.with(context)
                    .load(context.getString(R.string.api_base)+"uploads/images/" + (prescriptionList?.get(local_position)?.ePrescription))
                    .into(itemView?.rootView?.img_contentimage!!)
            }else{
                Glide.with(context)
                    .load(R.drawable.pdf_file_logo)
                    .into(itemView?.rootView?.img_contentimage!!)
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