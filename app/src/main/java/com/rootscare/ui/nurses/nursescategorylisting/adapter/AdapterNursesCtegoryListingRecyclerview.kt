package com.rootscare.ui.nurses.nursescategorylisting.adapter

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
import com.rootscare.data.model.api.response.nurses.nurselist.ResultItem
import com.rootscare.databinding.ItemNursesCategorylistingRecyclerviewBinding
import com.rootscare.databinding.ItemRootscareDoctorCategorilistingRecyclerviewBinding
import com.rootscare.ui.doctorcategorieslisting.adapter.AdapterDoctorCategoriesLisingRecyclerview
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import kotlinx.android.synthetic.main.item_nurses_categorylisting_recyclerview.view.*
import kotlinx.android.synthetic.main.item_nurses_categorylisting_recyclerview.view.txt_nurse_description
import kotlinx.android.synthetic.main.item_nurses_categorylisting_recyclerview.view.txt_nurse_name
import kotlinx.android.synthetic.main.item_nurses_categorylisting_recyclerview.view.txt_nurse_qualification
import kotlinx.android.synthetic.main.item_rootscare_doctor_categorilisting_recyclerview.view.*
import kotlinx.android.synthetic.main.item_see_all_nurses_by_grid_recyclerview.view.*

class AdapterNursesCtegoryListingRecyclerview (val nurseList: ArrayList<ResultItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterNursesCtegoryListingRecyclerview.ViewHolder>() {
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
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemNursesCategorylistingRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_nurses_categorylisting_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return nurseList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemNursesCategorylistingRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.btn_rootscare_booking_nurses?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onFirstItemClick(nurseList?.get(local_position)?.userId?.toInt()!!)
            })
//
            itemView?.root?.crdview_nurses_category_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onSecondItemClick(nurseList?.get(local_position)?.userId?.toInt()!!)
            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            if(nurseList?.get(pos)?.firstName!=null && !nurseList?.get(pos)?.firstName.equals("")){
                nurseFirstName= nurseList?.get(pos)?.firstName!!
            }else{
                nurseFirstName=""
            }
            if (nurseList?.get(pos)?.lastName!=null && !nurseList?.get(pos)?.lastName.equals("")){
                nurseLastName=nurseList?.get(pos)?.lastName!!
            }else{
                nurseLastName=""
            }

            itemView?.rootView?.txt_nurse_name?.setText(nurseFirstName+" "+nurseLastName)
            if (nurseList?.get(pos)?.qualification!=null && !nurseList?.get(pos)?.qualification.equals("")){
                itemView?.rootView?.txt_nurse_qualification?.setText(nurseList?.get(pos)?.qualification)
            }else{
                itemView?.rootView?.txt_nurse_qualification?.setText("")
            }
            if(nurseList?.get(pos)?.description!=null && !nurseList?.get(pos)?.description.equals("")){
                itemView?.rootView?.txt_nurse_description?.setText(nurseList?.get(pos)?.description)
            }else{
                itemView?.rootView?.txt_nurse_description?.setText("")
            }

            if(nurseList?.get(pos)?.avgRating!=null && !nurseList?.get(pos)?.avgRating.equals("")) {
                itemView?.rootView?.ratingBardoctordetailseview?.rating =
                    nurseList?.get(pos)?.avgRating?.toFloat()!!
            }
            if (nurseList?.get(pos)?.totalReviewRating!=null && !nurseList?.get(pos)?.totalReviewRating.equals("")){
                itemView?.rootView?.txt_nurselist_noofreview?.setText(nurseList?.get(pos)?.totalReviewRating+" "+"reviews")
            }else{
                itemView?.rootView?.txt_nurselist_noofreview?.setText("")
            }
            if (nurseList?.get(pos)?.image!=null && !nurseList?.get(pos)?.image.equals("")){
                Glide.with(context)
                    .load(context.getString(R.string.api_base)+"uploads/images/" + (nurseList?.get(pos)?.image))
                    .into(itemView?.rootView?.img_nurselisting_profilephoto!!)
            }else{
                Glide.with(context)
                    .load(R.drawable.no_image)
                    .into(itemView?.rootView?.img_nurselisting_profilephoto!!)
            }

        }
    }

}