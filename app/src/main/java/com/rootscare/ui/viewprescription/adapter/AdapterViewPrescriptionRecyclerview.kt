package com.rootscare.ui.viewprescription.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.R
import com.rootscare.data.model.api.response.patientprescription.ResultItem
import com.rootscare.databinding.ItemMyUpcomingappointmentRecyclerviewBinding
import com.rootscare.databinding.ItemViewPresprictionRecyclerviewBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import com.rootscare.ui.myupcomingappointment.adapter.AdapteMyUpComingAppointment
import kotlinx.android.synthetic.main.item_view_prespriction_recyclerview.view.*

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

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if (prescriptionList?.get(pos)?.prescriptionNumber!=null && !prescriptionList?.get(pos)?.prescriptionNumber.equals("")){
                itemView?.txt_description?.setText(prescriptionList?.get(pos)?.prescriptionNumber)
            }else{
                itemView?.txt_description?.setText("")
            }



        }
    }

}