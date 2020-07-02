package com.rootscare.ui.nurses.nurseslistingdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.nurse.nursedetailsrequest.NurseDetailsRequest
import com.rootscare.data.model.api.response.nurses.nursedetails.*
import com.rootscare.databinding.FragmentNursesCategorylistingBinding
import com.rootscare.databinding.FragmentNursesListingDetailsBinding
import com.rootscare.databinding.FragmentSeeAllNursesListByGridBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.doctorlistingdetails.adapter.AdapterDoctordetailsEducationListRecyclerview
import com.rootscare.ui.doctorlistingdetails.adapter.AdapterDoctordetailsReviewListRecyclerview
import com.rootscare.ui.doctorlistingdetails.adapter.AdapterDoctordetailsSpecilityListRecyclerview
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.nurses.FragmentNursesListByGridNavigator
import com.rootscare.ui.nurses.FragmentNursesListByGridViewModel
import com.rootscare.ui.nurses.adapter.AdapterSeeAllNursesByGridRecyclerView
import com.rootscare.ui.nurses.nursesbookingappointment.FragmentNursesBookingAppointment
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListingViewModel
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNurseEducationListRecyclerView
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNurseReviewrecyclerview
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNurseSpecilityListRecyclerview
import com.rootscare.ui.nurses.nurseslistingdetails.adapter.AdapterNursesFeesListingRecyclerView
import com.rootscare.ui.physiotherapy.submitreviewforservice.FragmentSubmitReview
import com.rootscare.ui.profile.FragmentProfile
import kotlinx.android.synthetic.main.item_see_all_nurses_by_grid_recyclerview.view.*

class FragmentNursesListingDetails : BaseFragment<FragmentNursesListingDetailsBinding, FragmentNursesListingDetailsViewModel>(),
    FragmentNursesListingDetailsNavigator {
    private var fragmentNursesListingDetailsBinding: FragmentNursesListingDetailsBinding? = null
    private var fragmentNursesListingDetailsViewModel: FragmentNursesListingDetailsViewModel? = null
    var nurseFirstName=""
    var nurseLastName=""
    var nurseId=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_listing_details
    override val viewModel: FragmentNursesListingDetailsViewModel
        get() {
            fragmentNursesListingDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentNursesListingDetailsViewModel::class.java!!)
            return fragmentNursesListingDetailsViewModel as FragmentNursesListingDetailsViewModel
        }
    companion object {
        fun newInstance(nurseid: String): FragmentNursesListingDetails {
            val args = Bundle()
            args.putString("nurseid",nurseid)
            val fragment = FragmentNursesListingDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesListingDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesListingDetailsBinding = viewDataBinding
        if (arguments!=null && arguments?.getString("nurseid")!=null){
            nurseId = arguments?.getString("nurseid")!!
            Log.d("Nurse ID", ": " + nurseId )
        }
//        setUpViewSeeAllNursesCategorieslistingRecyclerview()
//        setUpViewNursesFeeslistingRecyclerview()
//Api hit for nurse details
        apiHitForNurseDetails()
        fragmentNursesListingDetailsBinding?.btnRootscareBookingNurses?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesBookingAppointment.newInstance())
        })

        fragmentNursesListingDetailsBinding?.btnBookingAppointment?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesBookingAppointment.newInstance())
        })


//        fragmentNursesListingDetailsBinding?.txtWriteYourReview?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentSubmitReview.newInstance())
//        })
    }

    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview(hourlyRatesList: ArrayList<HourlyRatesItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewRootscareNursesfeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterNursesFeesListingRecyclerView(hourlyRatesList,context!!)
        recyclerView.adapter = contactListAdapter

    }

    override fun successNurseDetailsResponse(nurseDetailsResponse: NurseDetailsResponse?) {
        baseActivity?.hideLoading()
        if(nurseDetailsResponse?.code.equals("200")){
            if (nurseDetailsResponse?.result?.firstName!=null && !nurseDetailsResponse?.result?.firstName.equals("")){
                nurseFirstName=nurseDetailsResponse?.result?.firstName
            }else{
                nurseFirstName=""
            }

            if (nurseDetailsResponse?.result?.lastName!=null && !nurseDetailsResponse?.result?.lastName.equals("")){
                nurseLastName=nurseDetailsResponse?.result?.lastName
            }else{
                nurseLastName=""
            }

            fragmentNursesListingDetailsBinding?.txtNursedetaisName?.setText(nurseFirstName+" "+nurseLastName)

            if(nurseDetailsResponse?.result?.qualification!=null && !nurseDetailsResponse?.result?.qualification.equals("")){
                fragmentNursesListingDetailsBinding?.txtNursedetaisQualification?.setText(nurseDetailsResponse?.result?.qualification)
            }else{
                fragmentNursesListingDetailsBinding?.txtNursedetaisQualification?.setText("")
            }

            if (nurseDetailsResponse?.result?.avgRating!=null && !nurseDetailsResponse?.result?.avgRating.equals("")){
                fragmentNursesListingDetailsBinding?.ratingBardoctordetailseview?.rating=nurseDetailsResponse?.result?.avgRating.toFloat()
            }else{

            }
            if (nurseDetailsResponse?.result?.reviewRating!=null && nurseDetailsResponse?.result?.reviewRating.size>0){
                fragmentNursesListingDetailsBinding?.txtNursedetailsNoofreview?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.txtNursedetailsNoofreview?.setText(nurseDetailsResponse?.result?.reviewRating.size.toString()+" "+"reviews")
            }else{
                fragmentNursesListingDetailsBinding?.txtNursedetailsNoofreview?.visibility=View.GONE
            }
            if (nurseDetailsResponse?.result?.description!=null && !nurseDetailsResponse?.result?.description.equals("")){
                fragmentNursesListingDetailsBinding?.txtNursedetaisAddressDescription?.setText(nurseDetailsResponse?.result?.description)
            }else{
                fragmentNursesListingDetailsBinding?.txtNursedetaisAddressDescription?.setText("")
            }

            if(nurseDetailsResponse?.result?.hourlyRates!=null && nurseDetailsResponse?.result?.hourlyRates.size>0){
             fragmentNursesListingDetailsBinding?.recyclerViewRootscareNursesfeesListing?.visibility=View.VISIBLE
             fragmentNursesListingDetailsBinding?.tvNoDateNursefeeslist?.visibility=View.GONE
                setUpViewNursesFeeslistingRecyclerview(nurseDetailsResponse?.result?.hourlyRates)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewRootscareNursesfeesListing?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNoDateNursefeeslist?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNoDateNursefeeslist?.setText("No fees found.")
            }

            if(nurseDetailsResponse?.result?.qualificationData!=null && nurseDetailsResponse?.result?.qualificationData.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsEducation?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsEducationNoDate?.visibility=View.GONE
                setQualificationDataListing(nurseDetailsResponse?.result?.qualificationData)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsEducation?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNursedetailsEducationNoDate?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsEducationNoDate?.setText("No qualification data found.")
            }

            if(nurseDetailsResponse?.result?.department!=null && nurseDetailsResponse?.result?.department.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsSpecility?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsSpecilityNoDate?.visibility=View.GONE
                setSpecilityDataListing(nurseDetailsResponse?.result?.department)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNursedetailsSpecility?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNursedetailsSpecilityNoDate?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNursedetailsSpecilityNoDate?.setText("No specility data found.")
            }

            if(nurseDetailsResponse?.result?.reviewRating!=null && nurseDetailsResponse?.result?.reviewRating.size>0){
                fragmentNursesListingDetailsBinding?.recyclerViewNurselistingReview?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNurselistingReviewNoDate?.visibility=View.GONE
                setReviewRatingListing(nurseDetailsResponse?.result?.reviewRating)
            }else{
                fragmentNursesListingDetailsBinding?.recyclerViewNurselistingReview?.visibility=View.GONE
                fragmentNursesListingDetailsBinding?.tvNurselistingReviewNoDate?.visibility=View.VISIBLE
                fragmentNursesListingDetailsBinding?.tvNurselistingReviewNoDate?.setText("No review found.")
            }

            if(nurseDetailsResponse?.result?.image!=null && !nurseDetailsResponse?.result?.image.equals("")){

                    Glide.with(this!!.activity!!)
                        .load(this!!.activity!!.getString(R.string.api_base)+"uploads/images/" + (nurseDetailsResponse?.result?.image))
                        .into(fragmentNursesListingDetailsBinding?.imgNursedetailsProfilephoto!!)
            }else{
                Glide.with(this!!.activity!!)
                    .load(R.drawable.no_image)
                    .into(fragmentNursesListingDetailsBinding?.imgNursedetailsProfilephoto!!)
            }


        }else{
            Toast.makeText(activity, nurseDetailsResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorNurseDetailsResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentProfile.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    // Set up recycler view for service listing if available
    private fun setQualificationDataListing(qualificationDataList: ArrayList<QualificationDataItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsEducation != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsEducation
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterNurseEducationListRecyclerView(qualificationDataList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setSpecilityDataListing(departmentList: ArrayList<DepartmentItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsSpecility != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNursedetailsSpecility
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterNurseSpecilityListRecyclerview(departmentList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setReviewRatingListing(reviewRatingList: ArrayList<ReviewRatingItem?>?) {
        assert(fragmentNursesListingDetailsBinding!!.recyclerViewNurselistingReview != null)
        val recyclerView = fragmentNursesListingDetailsBinding!!.recyclerViewNurselistingReview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterNurseReviewrecyclerview(reviewRatingList,context!!)
        recyclerView.adapter = contactListAdapter
    }

    fun apiHitForNurseDetails(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var nurseDetailsRequest= NurseDetailsRequest()
            nurseDetailsRequest.id=nurseId.toInt()
            nurseDetailsRequest.userId=fragmentNursesListingDetailsViewModel?.appSharedPref?.userId?.toInt()

            fragmentNursesListingDetailsViewModel?.apinursedetails(nurseDetailsRequest)
        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }
}