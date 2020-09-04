package com.rootscare.ui.caregiver.caregiverseealllisting.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interfaces.OnClickWithTwoButton
import com.rootscare.R
import com.rootscare.data.model.api.response.caregiverallresponse.allcaregiverlistingresponse.ResultCareItem
import com.rootscare.data.model.api.response.doctorallapiresponse.alldoctorlistingresponse.ResultItem
import com.rootscare.databinding.ItemSeeAllCaregiverListRecyclerviewBinding
import com.rootscare.databinding.ItemSeeAllDoctorByGridRecyclerviewBinding
import com.rootscare.databinding.ItemSeeAllNursesByGridRecyclerviewBinding
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import com.rootscare.ui.nurses.adapter.AdapterSeeAllNursesByGridRecyclerView
import com.rootscare.ui.seealldoctorbygrid.adapter.AdapterSeeAllDoctorByGridRecyclerView
import kotlinx.android.synthetic.main.item_see_all_caregiver_list_recyclerview.view.*
import kotlinx.android.synthetic.main.item_see_all_doctor_by_grid_recyclerview.view.*
import kotlinx.android.synthetic.main.item_see_all_nurses_by_grid_recyclerview.view.*

class AdapterCaregiverSeeAllListRecyclerview  (val allDoctorList: ArrayList<ResultCareItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterCaregiverSeeAllListRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterCaregiverSeeAllListRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickWithTwoButton

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemSeeAllCaregiverListRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_see_all_caregiver_list_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return allDoctorList!!.size
//        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemSeeAllCaregiverListRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.crdview_seeall_caregiverlisting?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onFirstItemClick(1)
            })
            itemView?.root?.crdview_seeall_caregiverlisting?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onSecondItemClick(allDoctorList?.get(local_position)?.userId?.toInt()!!)
            })


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            itemView?.rootView?.txt_caregiver_name?.setText(allDoctorList?.get(pos)?.firstName+" "+allDoctorList?.get(pos)?.lastName)

            itemView?.rootView?.txt_qualification?.setText(allDoctorList?.get(pos)?.qualification)
            itemView?.rootView?.txt_description?.setText(allDoctorList?.get(pos)?.description)
            Glide.with(context)
                .load(context.getString(R.string.api_base)+"uploads/images/" + (allDoctorList?.get(pos)?.image))
                .into(itemView?.rootView?.image_caragiver!!)
        }
    }

}