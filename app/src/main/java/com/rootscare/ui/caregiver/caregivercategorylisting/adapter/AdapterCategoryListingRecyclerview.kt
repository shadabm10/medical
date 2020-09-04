package com.rootscare.ui.caregiver.caregivercategorylisting.adapter

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
import com.rootscare.data.model.api.response.caregiverallresponse.caregiverlist.CaregiverResultItem
import com.rootscare.data.model.api.response.nurses.nurselist.ResultItem
import com.rootscare.databinding.ItemCaregiverCategoryListingRecyclerviewBinding
import com.rootscare.databinding.ItemNursesCategorylistingRecyclerviewBinding
import com.rootscare.databinding.ItemSeeAllCaregiverListRecyclerviewBinding
import com.rootscare.ui.caregiver.caregiverseealllisting.adapter.AdapterCaregiverSeeAllListRecyclerview
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_caregiver_category_listing_recyclerview.view.*
import kotlinx.android.synthetic.main.item_nurses_categorylisting_recyclerview.view.*
import kotlinx.android.synthetic.main.item_nurses_categorylisting_recyclerview.view.btn_rootscare_booking_nurses
import kotlinx.android.synthetic.main.item_see_all_caregiver_list_recyclerview.view.*
import kotlinx.android.synthetic.main.item_see_all_doctor_by_grid_recyclerview.view.*

class AdapterCategoryListingRecyclerview  (val allDoctorList: ArrayList<CaregiverResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterCategoryListingRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickWithTwoButton
    var nurseFirstName=""
    var nurseLastName=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemCaregiverCategoryListingRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_caregiver_category_listing_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return allDoctorList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemCaregiverCategoryListingRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.btn_rootscare_booking_caregiver?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onFirstItemClick(allDoctorList?.get(local_position)?.userId?.toInt()!!)
            })
//
            itemView?.root?.crdview_caregiver_category_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onSecondItemClick(allDoctorList?.get(local_position)?.userId?.toInt()!!)
            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            if(allDoctorList?.get(pos)?.firstName!=null && !allDoctorList?.get(pos)?.firstName.equals("")){
                nurseFirstName= allDoctorList?.get(pos)?.firstName!!
            }else{
                nurseFirstName=""
            }
            if (allDoctorList?.get(pos)?.lastName!=null && !allDoctorList?.get(pos)?.lastName.equals("")){
                nurseLastName=allDoctorList?.get(pos)?.lastName!!
            }else{
                nurseLastName=""
            }

            itemView?.rootView?.txt_caregiver_list_name?.setText(nurseFirstName+" "+nurseLastName)
            if (allDoctorList?.get(pos)?.qualification!=null && !allDoctorList?.get(pos)?.qualification.equals("")){
                itemView?.rootView?.txt_caregive_qualification?.setText(allDoctorList?.get(pos)?.qualification)
            }else{
                itemView?.rootView?.txt_caregive_qualification?.setText("")
            }
            if(allDoctorList?.get(pos)?.description!=null && !allDoctorList?.get(pos)?.description.equals("")){
                itemView?.rootView?.txt_caregiver_desc?.setText(allDoctorList?.get(pos)?.description)
            }else{
                itemView?.rootView?.txt_caregiver_desc?.setText("")
            }

            if(allDoctorList?.get(pos)?.avgRating!=null && !allDoctorList?.get(pos)?.avgRating.equals("")) {
                itemView?.rootView?.ratingcaregiverdetailseview?.rating =
                    allDoctorList?.get(pos)?.avgRating?.toFloat()!!
            }
            if (allDoctorList?.get(pos)?.totalReviewRating!=null && !allDoctorList?.get(pos)?.totalReviewRating.equals("")){
                itemView?.rootView?.txt_caregiverdetails_noofreview?.setText(allDoctorList?.get(pos)?.totalReviewRating+" "+"reviews")
            }else{
                itemView?.rootView?.txt_caregiverdetails_noofreview?.setText("")
            }
            if (allDoctorList?.get(pos)?.image!=null && !allDoctorList?.get(pos)?.image.equals("")){
                Glide.with(context)
                    .load(context.getString(R.string.api_base)+"uploads/images/" + (allDoctorList?.get(pos)?.image))
                    .into(itemView?.rootView?.img_caregiver_profilephoto!!)
            }else{
                Glide.with(context)
                    .load(R.drawable.no_image)
                    .into(itemView?.rootView?.img_caregiver_profilephoto!!)
            }

        }
    }


}