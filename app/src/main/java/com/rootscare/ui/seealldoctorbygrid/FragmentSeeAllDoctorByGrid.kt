package com.rootscare.ui.seealldoctorbygrid

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.interfaces.DropDownDialogCallBack
import com.interfaces.DropdownRowItemItemClickOnConfirm
import com.interfaces.OnClickWithTwoButton
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.doctorrequest.doctorlistbydepartmentrequest.DoctorListByDepartmentIdRequest
import com.rootscare.data.model.api.response.doctorallapiresponse.alldoctorlistingresponse.AllDoctorListingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.alldoctorlistingresponse.ResultItem
import com.rootscare.data.model.api.response.doctorallapiresponse.doctordepartmentlistingresponse.DoctorDepartmentListingResponse
import com.rootscare.databinding.FragmentBookingBinding
import com.rootscare.databinding.FragmentProfileBinding
import com.rootscare.databinding.FragmentSeeAllDoctorByGridBinding
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.model.RowItem
import com.rootscare.ui.appointment.subfragment.FragmentAppiontmentDetails
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.doctorcategorieslisting.FragmentDoctorCategoriesListing
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.doctorprofile.FragmentDoctorProfile
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.profile.FragmentProfile
import com.rootscare.ui.profile.FragmentProfileNavigator
import com.rootscare.ui.profile.FragmentProfileViewModel
import com.rootscare.ui.seealldoctorbygrid.adapter.AdapterSeeAllDoctorByGridRecyclerView
import com.rootscare.ui.viewprescription.adapter.AdapterViewPrescriptionRecyclerview

class FragmentSeeAllDoctorByGrid : BaseFragment<FragmentSeeAllDoctorByGridBinding, FragmentSeeAllDoctorByGridViewModel>(),
    FragmentSeeAllDoctorByGridNavigator {
    private var fragmentSeeAllDoctorByGridBinding: FragmentSeeAllDoctorByGridBinding? = null
    private var fragmentSeeAllDoctorByGridViewModel: FragmentSeeAllDoctorByGridViewModel? = null
     var departmentDropdownList:ArrayList<RowItem?>? = null
    var departmentId=""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_see_all_doctor_by_grid
    override val viewModel: FragmentSeeAllDoctorByGridViewModel
        get() {
            fragmentSeeAllDoctorByGridViewModel =
                ViewModelProviders.of(this).get(FragmentSeeAllDoctorByGridViewModel::class.java!!)
            return fragmentSeeAllDoctorByGridViewModel as FragmentSeeAllDoctorByGridViewModel
        }

    companion object {
        fun newInstance(): FragmentSeeAllDoctorByGrid {
            val args = Bundle()
            val fragment = FragmentSeeAllDoctorByGrid()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentSeeAllDoctorByGridViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSeeAllDoctorByGridBinding = viewDataBinding
        fragmentSeeAllDoctorByGridBinding?.btnRootscareMoreDoctor?.setOnClickListener(View.OnClickListener {
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentDoctorCategoriesListing.newInstance())
        })

        //All department api call
        if(isNetworkConnected){
            baseActivity?.showLoading()
            fragmentSeeAllDoctorByGridViewModel?.apidepartmentlist()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        //All Doctor List Api Call
        if(isNetworkConnected){
            baseActivity?.showLoading()
            fragmentSeeAllDoctorByGridViewModel?.apidoctorlist()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
        fragmentSeeAllDoctorByGridBinding?.txtSelectDoctorDepartment?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForRowItemDropDownList(this!!.activity!!,"Select Department",departmentDropdownList,object:
                DropdownRowItemItemClickOnConfirm {
                override fun onConfirm(title_name: String,title_id:String) {
                    fragmentSeeAllDoctorByGridBinding?.txtSelectDoctorDepartment?.setText(title_name)
                    departmentId=title_id
                    apicalldepartmentdoctorlist(departmentId)
                }

            })
        })
    }

    // Set up recycler view for service listing if available
    private fun setUpViewSeeAllDoctorByGridlistingRecyclerview(allDoctorList: ArrayList<ResultItem?>?) {
        assert(fragmentSeeAllDoctorByGridBinding!!.recyclerViewRootscareSeealldoctorsByGrid != null)
        val recyclerView = fragmentSeeAllDoctorByGridBinding!!.recyclerViewRootscareSeealldoctorsByGrid
        val gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter = AdapterSeeAllDoctorByGridRecyclerView(allDoctorList,context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView= object : OnClickWithTwoButton {

            override fun onFirstItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorProfile.newInstance())
            }

            override fun onSecondItemClick(id: Int) {
               (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                   FragmentDoctorListingDetails.newInstance(id.toString()))
            }

        }


    }

    override fun successDoctorDepartmentListingResponse(doctorDepartmentListingResponse: DoctorDepartmentListingResponse?) {
        baseActivity?.hideLoading()
        if(doctorDepartmentListingResponse?.code.equals("200")){
            if(doctorDepartmentListingResponse?.result!=null && doctorDepartmentListingResponse?.result.size>0){
                departmentDropdownList= ArrayList<RowItem?>()
                for (i in 0 until doctorDepartmentListingResponse?.result?.size) {
                    val item = RowItem(doctorDepartmentListingResponse?.result?.get(i)?.title!!,
                        doctorDepartmentListingResponse?.result?.get(i)?.id.toString()
                    )
                    departmentDropdownList?.add(item)
                }


            }
        }else{
            Toast.makeText(activity, doctorDepartmentListingResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successAllDoctorListingResponse(allDoctorListingResponse: AllDoctorListingResponse?) {
        baseActivity?.hideLoading()
        if(allDoctorListingResponse?.code.equals("200")){
            if(allDoctorListingResponse?.result!=null && allDoctorListingResponse?.result.size>0){
                fragmentSeeAllDoctorByGridBinding?.recyclerViewRootscareSeealldoctorsByGrid?.visibility=View.VISIBLE
                fragmentSeeAllDoctorByGridBinding?.tvNoDate?.visibility=View.GONE
                setUpViewSeeAllDoctorByGridlistingRecyclerview(allDoctorListingResponse?.result)
            }  else{
                fragmentSeeAllDoctorByGridBinding?.recyclerViewRootscareSeealldoctorsByGrid?.visibility=View.GONE
                fragmentSeeAllDoctorByGridBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentSeeAllDoctorByGridBinding?.tvNoDate?.setText("No doctor list found")
            }
        }else{
            Toast.makeText(activity, allDoctorListingResponse?.message, Toast.LENGTH_SHORT).show()
        }


    }

    override fun errorDoctorDepartmentListingResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun apicalldepartmentdoctorlist(departmentid:String){
        if(isNetworkConnected){
            baseActivity?.showLoading()
            var doctorListByDepartmentIdRequest=DoctorListByDepartmentIdRequest()
            doctorListByDepartmentIdRequest?.departmentId=departmentid
            fragmentSeeAllDoctorByGridViewModel?.apidepartmentdoctorlist(doctorListByDepartmentIdRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }
}