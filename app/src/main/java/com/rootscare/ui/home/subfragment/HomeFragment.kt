package com.rootscare.ui.home.subfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.homesearch.HomeSearchRequest
import com.rootscare.data.model.api.request.nurse.searchbyname.NurseSearchByNameRequest
import com.rootscare.data.model.api.response.patienthome.*
import com.rootscare.databinding.FragmentHomeBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.babysitter.babysitterseealllist.FragmentBabysitterSeeallListingByGrid
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.caregiver.caregiverseealllisting.FragmentCaregiverSeeAllListingByGrid
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.adapter.*
import com.rootscare.ui.hospital.FragmentSeeAllHospitalList
import com.rootscare.ui.nurses.FragmentNursesListByGrid
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails
import com.rootscare.ui.physiotherapy.FragmentSeeAllPhysiotherapyListing
import com.rootscare.ui.seealldoctorbygrid.FragmentSeeAllDoctorByGrid

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewmodel>(), HomeFragmentNavigator {
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private var homeFragmentViewmodel: HomeFragmentViewmodel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModel: HomeFragmentViewmodel
        get() {
            homeFragmentViewmodel = ViewModelProviders.of(this).get(
                HomeFragmentViewmodel::class.java!!)
            return homeFragmentViewmodel as HomeFragmentViewmodel
        }

    companion object {
        val TAG = HomeFragment::class.java.simpleName
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeFragmentViewmodel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = viewDataBinding

        fragmentHomeBinding?.llHomeDoctorSeeAll?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSeeAllDoctorByGrid.newInstance())
        })

        fragmentHomeBinding?.llHomeNurseSeeAll?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentNursesListByGrid.newInstance())
        })
        fragmentHomeBinding?.llHomeHospitalSeeAll?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSeeAllHospitalList.newInstance())
        })
        fragmentHomeBinding?.llHomePhsiotherapySeeAll?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentSeeAllPhysiotherapyListing.newInstance())
        })

        fragmentHomeBinding?.llHomeCaregiverSeeAll?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentCaregiverSeeAllListingByGrid.newInstance())
        })

        fragmentHomeBinding?.llHomeBabysitterSeeAll?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentBabysitterSeeallListingByGrid.newInstance())
        })
        if(isNetworkConnected){
            baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
            homeFragmentViewmodel?.apipatienthome()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }


        fragmentHomeBinding?.edtHomeSearch?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable) { // you can call or do what you want with your EditText here
// yourEditText...
                if(s.toString().length==0){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
                        homeFragmentViewmodel?.apipatienthome()

                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if(s.toString().length>2){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        var homeSearchRequest= HomeSearchRequest()
                        homeSearchRequest?.searchContent=fragmentHomeBinding?.edtHomeSearch?.text?.toString()
                        homeFragmentViewmodel?.apipatienthomesearch(homeSearchRequest)
                    }
                }
            }
        })

    }


    // Set up recycler view for service listing if available
    private fun setUpHospitalListing(hospitalList: ArrayList<HospitalItem?>?) {
        assert(fragmentHomeBinding!!.recyclerViewRootCareHospitalList != null)
        val recyclerView = fragmentHomeBinding!!.recyclerViewRootCareHospitalList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterHospitalRecyclerviw(hospitalList,context!!)
        recyclerView.adapter = contactListAdapter


    }
    // Set up recycler view for service listing if available
    private fun setUpDoctorListing( doctorList: ArrayList<DoctorItem?>?) {
        assert(fragmentHomeBinding!!.recyclerViewRootCareDoctorList != null)
        val recyclerView = fragmentHomeBinding!!.recyclerViewRootCareDoctorList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterDoctorRecyclerviw(doctorList,context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorListingDetails.newInstance(id.toString()))
            }

        }


    }

    // Set up recycler view for service listing if available
    private fun setUpNursesListing(nurseList: ArrayList<NurseItem?>?) {
        assert(fragmentHomeBinding!!.recyclerViewRootCareNursesList != null)
        val recyclerView = fragmentHomeBinding!!.recyclerViewRootCareNursesList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterNursesRecyclerviw(nurseList,context!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentNursesListingDetails.newInstance(id.toString()))
            }

        }


    }
    // Set up recycler view for service listing if available
    private fun setUphysiotherapyListing(physiotherapyList: ArrayList<PhysiotherapyItem?>?) {
        assert(fragmentHomeBinding!!.recyclerViewRootCarePhysiotherapyList != null)
        val recyclerView = fragmentHomeBinding!!.recyclerViewRootCarePhysiotherapyList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterPhysiotherapyRecyclerView(physiotherapyList,context!!)
        recyclerView.adapter = contactListAdapter


    }

    // Set up recycler view for service listing if available
    private fun setUpCaregiverListing(caregiverList: ArrayList<CaregiverItem?>?) {
        assert(fragmentHomeBinding!!.recyclerViewRootCareCareGiverList != null)
        val recyclerView = fragmentHomeBinding!!.recyclerViewRootCareCareGiverList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterCaregiverRecyclerview(caregiverList,context!!)
        recyclerView.adapter = contactListAdapter


    }

    // Set up recycler view for service listing if available
    private fun setUpBabysitterListing(babysitterList: ArrayList<BabysitterItem?>?) {
        assert(fragmentHomeBinding!!.recyclerViewRootCareBabySitterList != null)
        val recyclerView = fragmentHomeBinding!!.recyclerViewRootCareBabySitterList
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterbabysitterRecyclerview(babysitterList,context!!)
        recyclerView.adapter = contactListAdapter


    }

    override fun successPatientHomeApiResponse(patientHomeApiResponse: PatientHomeApiResponse?) {
        baseActivity?.hideLoading()
        if(patientHomeApiResponse?.code.equals("200")){

            if(patientHomeApiResponse?.result?.hospital!=null && patientHomeApiResponse?.result?.hospital.size>0){
                fragmentHomeBinding?.recyclerViewRootCareHospitalList?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvNoData?.visibility=View.GONE
                setUpHospitalListing(patientHomeApiResponse?.result?.hospital)
            }else{
                fragmentHomeBinding?.recyclerViewRootCareHospitalList?.visibility=View.GONE
                fragmentHomeBinding?.tvNoData?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvNoData?.setText("No Hospital List Found")
            }

            if(patientHomeApiResponse?.result?.doctor!=null && patientHomeApiResponse?.result?.doctor.size>0){
                fragmentHomeBinding?.recyclerViewRootCareDoctorList?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvDoctorsNoData?.visibility=View.GONE
                setUpDoctorListing(patientHomeApiResponse?.result?.doctor)
            }else{
                fragmentHomeBinding?.recyclerViewRootCareDoctorList?.visibility=View.GONE
                fragmentHomeBinding?.tvDoctorsNoData?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvDoctorsNoData?.setText("No Doctor List Found")
            }

            if(patientHomeApiResponse?.result?.nurse!=null && patientHomeApiResponse?.result?.nurse.size>0){
                fragmentHomeBinding?.recyclerViewRootCareNursesList?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvNursesNoData?.visibility=View.GONE
                setUpNursesListing(patientHomeApiResponse?.result?.nurse)

            }else{
                fragmentHomeBinding?.recyclerViewRootCareNursesList?.visibility=View.GONE
                fragmentHomeBinding?.tvNursesNoData?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvDoctorsNoData?.setText("No Nurses List Found")
            }

            if(patientHomeApiResponse?.result?.physiotherapy!=null && patientHomeApiResponse?.result?.physiotherapy.size>0){
                fragmentHomeBinding?.recyclerViewRootCarePhysiotherapyList?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvPhysiotherapyNoData?.visibility=View.GONE
                setUphysiotherapyListing(patientHomeApiResponse?.result?.physiotherapy)

            }else{
                fragmentHomeBinding?.recyclerViewRootCarePhysiotherapyList?.visibility=View.GONE
                fragmentHomeBinding?.tvPhysiotherapyNoData?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvPhysiotherapyNoData?.setText("No Physiotherapy List Found")
            }

            if(patientHomeApiResponse?.result?.caregiver!=null && patientHomeApiResponse?.result?.caregiver.size>0){
                fragmentHomeBinding?.recyclerViewRootCareCareGiverList?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvCareGiverNoData?.visibility=View.GONE
                setUpCaregiverListing(patientHomeApiResponse?.result?.caregiver)
            }else{
                fragmentHomeBinding?.recyclerViewRootCareCareGiverList?.visibility=View.GONE
                fragmentHomeBinding?.tvCareGiverNoData?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvCareGiverNoData?.setText("No Caregiver List Found")
            }
            if(patientHomeApiResponse?.result?.babysitter!=null && patientHomeApiResponse?.result?.babysitter.size>0){
                fragmentHomeBinding?.recyclerViewRootCareBabySitterList?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvBabySitterNoData?.visibility=View.GONE
                setUpBabysitterListing(patientHomeApiResponse?.result?.babysitter)
            }else{
                fragmentHomeBinding?.recyclerViewRootCareBabySitterList?.visibility=View.GONE
                fragmentHomeBinding?.tvBabySitterNoData?.visibility=View.VISIBLE
                fragmentHomeBinding?.tvBabySitterNoData?.setText("No Babysitter List Found")
            }

        }else{
            Toast.makeText(activity, patientHomeApiResponse?.message, Toast.LENGTH_SHORT).show()

            fragmentHomeBinding?.recyclerViewRootCareHospitalList?.visibility=View.GONE
            fragmentHomeBinding?.tvNoData?.visibility=View.VISIBLE
            fragmentHomeBinding?.tvNoData?.setText("No Hospital List Found")

            fragmentHomeBinding?.recyclerViewRootCareDoctorList?.visibility=View.GONE
            fragmentHomeBinding?.tvDoctorsNoData?.visibility=View.VISIBLE
            fragmentHomeBinding?.tvDoctorsNoData?.setText("No Doctor List Found")

            fragmentHomeBinding?.recyclerViewRootCareNursesList?.visibility=View.GONE
            fragmentHomeBinding?.tvNursesNoData?.visibility=View.VISIBLE
            fragmentHomeBinding?.tvDoctorsNoData?.setText("No Nurses List Found")

            fragmentHomeBinding?.recyclerViewRootCarePhysiotherapyList?.visibility=View.GONE
            fragmentHomeBinding?.tvPhysiotherapyNoData?.visibility=View.VISIBLE
            fragmentHomeBinding?.tvPhysiotherapyNoData?.setText("No Physiotherapy List Found")

            fragmentHomeBinding?.recyclerViewRootCareCareGiverList?.visibility=View.GONE
            fragmentHomeBinding?.tvCareGiverNoData?.visibility=View.VISIBLE
            fragmentHomeBinding?.tvCareGiverNoData?.setText("No Caregiver List Found")

            fragmentHomeBinding?.recyclerViewRootCareBabySitterList?.visibility=View.GONE
            fragmentHomeBinding?.tvBabySitterNoData?.visibility=View.VISIBLE
            fragmentHomeBinding?.tvBabySitterNoData?.setText("No Babysitter List Found")


        }

    }

    override fun errorPatientHomeApiResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}