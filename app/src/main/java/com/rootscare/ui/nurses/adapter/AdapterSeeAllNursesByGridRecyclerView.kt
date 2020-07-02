package com.rootscare.ui.nurses.adapter

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
import com.rootscare.databinding.ItemSeeAllDoctorByGridRecyclerviewBinding
import com.rootscare.databinding.ItemSeeAllNursesByGridRecyclerviewBinding
import com.rootscare.ui.home.subfragment.adapter.AdapterHospitalRecyclerviw
import com.rootscare.ui.seealldoctorbygrid.adapter.AdapterSeeAllDoctorByGridRecyclerView
import kotlinx.android.synthetic.main.item_home_nurses_recyclerview.view.*
import kotlinx.android.synthetic.main.item_see_all_doctor_by_grid_recyclerview.view.*
import kotlinx.android.synthetic.main.item_see_all_nurses_by_grid_recyclerview.view.*

class AdapterSeeAllNursesByGridRecyclerView (val nurseList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterSeeAllNursesByGridRecyclerView.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalRecyclerviw::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickWithTwoButton
    var nurseFirstName:String=""
    var nurseLastName:String=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemSeeAllNursesByGridRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_see_all_nurses_by_grid_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return nurseList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemSeeAllNursesByGridRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
            itemView?.root?.crdview_seeall_nurseslisting?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onSecondItemClick(1)
            })
        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            if (nurseList?.get(pos)?.firstName!=null && !nurseList?.get(pos)?.firstName.equals("")){
                nurseFirstName= nurseList?.get(pos)?.firstName!!
            }else{
                nurseFirstName=""
            }
            if (nurseList?.get(pos)?.lastName!=null && !nurseList?.get(pos)?.lastName.equals("")){
                nurseLastName= nurseList?.get(pos)?.lastName!!
            }else{
                nurseLastName=""
            }

            itemView?.rootView?.txt_nurse_name?.text=nurseFirstName+" "+nurseLastName
            if (nurseList?.get(pos)?.qualification!=null && !nurseList?.get(pos)?.qualification.equals("")){
                itemView?.rootView?.txt_nurse_qualification?.setText(nurseList?.get(pos)?.qualification)
            }else{
                itemView?.rootView?.txt_nurse_qualification?.setText("")
            }
            if(nurseList?.get(pos)?.description!=null && !nurseList?.get(pos)?.description.equals("")){
                itemView?.rootView?.txt_nurse_description?.text=nurseList?.get(pos)?.description
            }else{
                itemView?.rootView?.txt_nurse_description?.text=""
            }

            if (nurseList?.get(pos)?.image!=null && !nurseList?.get(pos)?.image.equals("")){
                Glide.with(context)
                    .load(context.getString(R.string.api_base)+"uploads/images/" + (nurseList?.get(pos)?.image))
                    .into(itemView?.rootView?.img_nurse_list!!)
            }else{
                Glide.with(context)
                    .load(R.drawable.no_image)
                    .into(itemView?.rootView?.img_nurse_list!!)
            }



        }
    }

}