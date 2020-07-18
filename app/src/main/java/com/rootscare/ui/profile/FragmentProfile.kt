package com.rootscare.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.dialog.CommonDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.interfaces.DropDownDialogCallBack
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.patientprofilerequest.PatientProfileRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilelifestylerequest.ProfileLifestyleUpdateRequest
import com.rootscare.data.model.api.request.patientprofilerequest.updateprofilemedicalrequest.ProfileMedicalUpdateRequest
import com.rootscare.data.model.api.response.nationalityresponse.NationalityResponse
import com.rootscare.data.model.api.response.patientprofileresponse.PatientProfileResponse
import com.rootscare.databinding.FragmentProfileBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.login.subfragment.registrationfragment.FragmentRegistration
import com.rootscare.utils.ManagePermissions
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

class FragmentProfile : BaseFragment<FragmentProfileBinding, FragmentProfileViewModel>(),
    FragmentProfileNavigator {
    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private var fragmentProfileViewModel: FragmentProfileViewModel? = null

    var nationalityDropdownlist:ArrayList<String?>?=null

    var monthstr: String=""
    var dayStr: String=""
    var imagefilename=""
    var selectGender="Female"
//


    var imageFile: File? = null


    private val PICK_IMAGE_REQUEST = 1

    var thumbnail: Bitmap? = null
    var bytes: ByteArrayOutputStream? = null

    private val REQUEST_CAMERA = 0
    private  var SELECT_FILE:Int = 1
    var REQUEST_ID_MULTIPLE_PERMISSIONS = 123
    private val ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0
    var requested = false
    private lateinit var managePermissions: ManagePermissions
    private val PermissionsRequestCode = 123

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_profile
    override val viewModel: FragmentProfileViewModel
        get() {
            fragmentProfileViewModel =
                ViewModelProviders.of(this).get(FragmentProfileViewModel::class.java!!)
            return fragmentProfileViewModel as FragmentProfileViewModel
        }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        val TAG = FragmentRegistration::class.java.simpleName
        fun newInstance(): FragmentProfile {
            val args = Bundle()
            val fragment = FragmentProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentProfileBinding = viewDataBinding
        setUpTabLayout()
        fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.setText("1")
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()



        if(isNetworkConnected){
            baseActivity?.showLoading()
            var patientProfileRequest= PatientProfileRequest()
            patientProfileRequest?.userId=fragmentProfileViewModel?.appSharedPref?.userId

//            patientProfileRequest?.userId="11"
            fragmentProfileViewModel?.apipatientprofile(patientProfileRequest)
            fragmentProfileViewModel?.apinationality()

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalDob?.setOnClickListener(View.OnClickListener {
            // TODO Auto-generated method stub
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this!!.activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                if((monthOfYear+1)<10){
                    monthstr= "0" + (monthOfYear+1)
                }else{
                    monthstr=(monthOfYear+1).toString()
                }

                if(dayOfMonth<10){
                    dayStr="0"+dayOfMonth

                }else{
                    dayStr=dayOfMonth.toString()
                }
//                fragmentSeedStockedBinding?.txtLsfSeedstockDateofStocking?.setText("" + year + "-" + monthstr + "-" + dayStr)
                fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalDob?.setText("" + year + "-" + monthstr + "-" + dayStr)

            }, year, month, day)

            dpd.show()
        })

        radioButtonClickEvenOfMedical()

        fragmentProfileBinding?.edtProfileImage?.setOnClickListener({
//            showPictureDialog()
            // captureImage()


            if (Build.VERSION.SDK_INT >= 23) {
                val granted = checkAndRequestPermissionsTest()
                println("granted===>$granted")
                if (granted == true) {
                    if(checkAndRequestPermissionsTest()){
                        captureImage()
                    }

                }
            } else {
                if(checkAndRequestPermissionsTest()){
                    captureImage()
                }

            }
        })

        fragmentProfileBinding?.layoutProfilePersonal?.radioProfileMale?.setOnClickListener(View.OnClickListener {
            selectGender="Male"
        })

        fragmentProfileBinding?.layoutProfilePersonal?.radioProfileFemale?.setOnClickListener(View.OnClickListener {
            selectGender="Female"
        })


        fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalNationality?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Nationality",nationalityDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalNationality?.setText(text)
                }

            })
        })



        fragmentProfileBinding?.layoutProfileMedical?.btnPatientProfileMedical?.setOnClickListener(
            View.OnClickListener {

                if(isNetworkConnected){
                    baseActivity?.showLoading()
                    var profileMedicalUpdateRequest= ProfileMedicalUpdateRequest()
                    if (fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.allergies="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.allergies="no"
                        fragmentProfileBinding?.layoutProfileMedical?.edtAllergiesData?.setText("")
                    }

                    profileMedicalUpdateRequest?.allergiesData=fragmentProfileBinding?.layoutProfileMedical?.edtAllergiesData?.text?.toString()

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.isChecked!!){
                        profileMedicalUpdateRequest?.currentMedication="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.isChecked!!){
                        profileMedicalUpdateRequest?.currentMedication="no"
                        fragmentProfileBinding?.layoutProfileMedical?.edtCurrentMedicationData?.setText("")
                    }
                    profileMedicalUpdateRequest?.currentMedicationData=fragmentProfileBinding?.layoutProfileMedical?.edtCurrentMedicationData?.text?.toString()

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.isChecked!!){
                        profileMedicalUpdateRequest?.pastMedication="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.isChecked!!){
                        profileMedicalUpdateRequest?.pastMedication="no"
                        fragmentProfileBinding?.layoutProfileMedical?.edtPastmedicationData?.setText("")
                    }
                    profileMedicalUpdateRequest?.pastMedicationData=fragmentProfileBinding?.layoutProfileMedical?.edtPastmedicationData?.text?.toString()

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.injuries="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.injuries="no"
                        fragmentProfileBinding?.layoutProfileMedical?.edtInjuriesData?.setText("")
                    }

                    profileMedicalUpdateRequest?.injuriesData=fragmentProfileBinding?.layoutProfileMedical?.edtInjuriesData?.text?.toString()

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.surgeries="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.surgeries="no"
                        fragmentProfileBinding?.layoutProfileMedical?.edtSurgeriesData?.setText("")
                    }
                    profileMedicalUpdateRequest?.surgeriesData=fragmentProfileBinding?.layoutProfileMedical?.edtSurgeriesData?.text?.toString()

                    if (fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.isChecked!!){
                        profileMedicalUpdateRequest?.chronicDiseases="yes"
                    }else if(fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.isChecked!!){
                        profileMedicalUpdateRequest?.chronicDiseases="no"
                        fragmentProfileBinding?.layoutProfileMedical?.edtChronicdisessesData?.setText("")
                    }
                    profileMedicalUpdateRequest?.chronicDiseasesData=fragmentProfileBinding?.layoutProfileMedical?.edtChronicdisessesData?.text?.toString()
                    profileMedicalUpdateRequest?.userId=fragmentProfileViewModel?.appSharedPref?.userId

                    fragmentProfileViewModel?.apieditpatientprofilemedical(profileMedicalUpdateRequest)

                }else{
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }

            })

        fragmentProfileBinding?.layoutProfileLifestyle?.btnPatientProfileLifestyle?.setOnClickListener(
            View.OnClickListener {
                if(checkFieldValidation()){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        var profileLifestyleUpdateRequest= ProfileLifestyleUpdateRequest()
                        profileLifestyleUpdateRequest?.smoking=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.text?.toString()
                        profileLifestyleUpdateRequest?.alcohol=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.text?.toString()
                        profileLifestyleUpdateRequest?.bloodGroup=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.text?.toString()
                        profileLifestyleUpdateRequest?.activityLevel=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.text?.toString()
                        profileLifestyleUpdateRequest?.foodPreference=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.text?.toString()
                        profileLifestyleUpdateRequest?.occupation=fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.text?.toString()
                        profileLifestyleUpdateRequest?.userId=fragmentProfileViewModel?.appSharedPref?.userId
                        fragmentProfileViewModel?.apieditpatientprofilestyle(profileLifestyleUpdateRequest)
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }

            })

        fragmentProfileBinding?.layoutProfilePersonal?.btnPatientProfilePersonal?.setOnClickListener(
            View.OnClickListener {
                if(checkFieldValidationForPersonalDetails()){
                    if(isNetworkConnected){
                        baseActivity?.showLoading()
                        apieditpatientprofilepersonalApiCall()
                    }else{
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        //Life Style Smooking Habits Dropdown Click

        lifeStyleDropdownClick()

        personalDropdownClick()


    }

    private fun setUpTabLayout() {
        val tabTitles: MutableList<String> =
            ArrayList()
        tabTitles.add("Personal")
        tabTitles.add("Medical")
        tabTitles.add("Life style")
        for (i in tabTitles.indices) {
            fragmentProfileBinding?.tablayout?.addTab(
                fragmentProfileBinding?.tablayout?.newTab()?.setText(
                    tabTitles[i]
                )!!, i
            )
        }
        //        activityOrderBinding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (i in 0 until fragmentProfileBinding?.tablayout?.getTabCount()!!) {
            val view: View =
                LayoutInflater.from(activity).inflate(R.layout.tab_item_layout, null)
            val tab_item_tv = view.findViewById<TextView>(R.id.tab_item_tv)
            tab_item_tv.text = tabTitles[i]
            if (i == 0) {
                tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
                view.background = resources.getDrawable(R.drawable.tab_background_selected)
            } else {
                tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
//                tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                view.background = resources.getDrawable(R.drawable.tab_background_unselected)
            }
            fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView = view
        }
        fragmentProfileBinding?.tablayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until fragmentProfileBinding?.tablayout?.getTabCount()!!) {
//                    val view: View = fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView!!
                    val view: View = Objects.requireNonNull<View>(
                        fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView
                    )

                    val tab_item_tv =
                        Objects.requireNonNull(view)
                            .findViewById<TextView>(R.id.tab_item_tv)
                    if (i == tab.position) {
                        tab_item_tv.setTextColor(resources.getColor(R.color.background_white))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_selected)
                    } else {
                        tab_item_tv.setTextColor(ContextCompat.getColor(activity!!,R.color.background_white))
//                        tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_unselected)
                    }
                }
                if (tab.position == 0) {
                    fragmentProfileBinding?.llProfilePersonal?.visibility=View.VISIBLE
                    fragmentProfileBinding?.llProfileMedical?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileLifestyle?.visibility=View.GONE
//                    addFragment(
//                        PendingFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                } else if (tab.position == 1) {
                    fragmentProfileBinding?.llProfilePersonal?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileMedical?.visibility=View.VISIBLE
                    fragmentProfileBinding?.llProfileLifestyle?.visibility=View.GONE
//                    addFragment(
//                        ConfirmFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                } else if (tab.position == 2) {
                    fragmentProfileBinding?.llProfilePersonal?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileMedical?.visibility=View.GONE
                    fragmentProfileBinding?.llProfileLifestyle?.visibility=View.VISIBLE
//                    addFragment(
//                        DeliveredFragment.newInstance(Bundle()),
//                        com.medsflick.activity.OrderListingActivity.TAG,
//                        activityOrderBinding.containerLayout.getId()
//                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        fragmentProfileBinding?.llProfilePersonal?.visibility=View.VISIBLE
        fragmentProfileBinding?.llProfileMedical?.visibility=View.GONE
        fragmentProfileBinding?.llProfileLifestyle?.visibility=View.GONE

    }

    override fun successPatientProfileResponse(patientProfileResponse: PatientProfileResponse?) {
        baseActivity?.hideLoading()
        if(patientProfileResponse?.code.equals("200")){
            fragmentProfileBinding?.txtProfileName?.setText(patientProfileResponse?.result?.firstName+" "+patientProfileResponse?.result?.lastName)
            fragmentProfileBinding?.txtProfileEmail?.setText(patientProfileResponse?.result?.email)
            fragmentProfileBinding?.txtProfileContactnumner?.setText(patientProfileResponse?.result?.phoneNumber)
            Toast.makeText(activity, patientProfileResponse?.message, Toast.LENGTH_SHORT).show()
            fragmentProfileViewModel?.appSharedPref?.userName=patientProfileResponse?.result?.firstName+" "+patientProfileResponse?.result?.lastName
            fragmentProfileViewModel?.appSharedPref?.userEmail=patientProfileResponse?.result?.email
            fragmentProfileViewModel?.appSharedPref?.userImage=patientProfileResponse?.result?.image
            defaultPersonalDetailsSetup(patientProfileResponse)
            defaultMedicalDetailsSetup(patientProfileResponse)
            defaultRadioButtonSetup()
            defaultLifeStylelDetailsSetup(patientProfileResponse)

        }else{
            Toast.makeText(activity, patientProfileResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun successNationalityResponse(nationalityResponse: NationalityResponse?) {
        baseActivity?.hideLoading()
        if (nationalityResponse?.code.equals("200")){
            nationalityDropdownlist=ArrayList<String?>()
            if (nationalityResponse?.result!=null && nationalityResponse?.result.size>0){
                for (i in 0 until nationalityResponse?.result?.size!!) {
                    nationalityDropdownlist?.add(nationalityResponse?.result?.get(i)?.nationality)
                }

            }

        }else{

        }
    }

    override fun errorPatientProfileResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentProfile.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun defaultPersonalDetailsSetup(patientProfileResponse: PatientProfileResponse?){

        if(!patientProfileResponse?.result?.firstName.equals("") || patientProfileResponse?.result?.firstName!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalFname?.setText(patientProfileResponse?.result?.firstName)
        }

        if(!patientProfileResponse?.result?.lastName.equals("") || patientProfileResponse?.result?.lastName!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLname?.setText(patientProfileResponse?.result?.lastName)
        }

        if(!patientProfileResponse?.result?.phoneNumber.equals("") || patientProfileResponse?.result?.phoneNumber!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalPhonenumber?.setText(patientProfileResponse?.result?.phoneNumber)
        }

        if(!patientProfileResponse?.result?.email.equals("") || patientProfileResponse?.result?.email!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalEmail?.setText(patientProfileResponse?.result?.email)
        }

        if(!patientProfileResponse?.result?.location.equals("") || patientProfileResponse?.result?.location!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLocation?.setText(patientProfileResponse?.result?.location)
        }

        if(!patientProfileResponse?.result?.dob.equals("") || patientProfileResponse?.result?.dob!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalDob?.setText(patientProfileResponse?.result?.dob)
        }

        if(!patientProfileResponse?.result?.age.equals("") || patientProfileResponse?.result?.age!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalAge?.setText(patientProfileResponse?.result?.age)
        }

        if(!patientProfileResponse?.result?.address.equals("") || patientProfileResponse?.result?.address!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalAddress?.setText(patientProfileResponse?.result?.address)
        }

        if(patientProfileResponse?.result?.gender!=null){
            if(!patientProfileResponse?.result?.gender.equals("")){
                if(patientProfileResponse?.result?.gender.equals("Female")){
                    fragmentProfileBinding?.layoutProfilePersonal?.radioProfileFemale?.isChecked=true
                }else if(patientProfileResponse?.result?.gender.equals("Male")){
                    fragmentProfileBinding?.layoutProfilePersonal?.radioProfileMale?.isChecked=true
                }
            }
        }

        if(patientProfileResponse?.result?.nationality!=null && !patientProfileResponse?.result?.nationality.equals("")){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalNationality?.setText(patientProfileResponse?.result?.nationality)
        }

        if (patientProfileResponse?.result?.maritalStatus!=null && !patientProfileResponse?.result?.maritalStatus.equals("")){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalMaterialStatus?.setText(patientProfileResponse?.result?.maritalStatus)
        }

        if (patientProfileResponse?.result?.height!=null && !patientProfileResponse?.result?.height.equals("")){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalHeight?.setText(patientProfileResponse?.result?.height)
        }

        if (patientProfileResponse?.result?.weight!=null && !patientProfileResponse?.result?.weight.equals("")){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalWeight?.setText(patientProfileResponse?.result?.weight)
        }

        if(!patientProfileResponse?.result?.idNumber.equals("") || patientProfileResponse?.result?.idNumber!=null){
            fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalIdnumber?.setText(patientProfileResponse?.result?.idNumber)
        }

//
        if(!patientProfileResponse?.result?.image.equals("") || patientProfileResponse?.result?.image!=null){
            imagefilename= patientProfileResponse?.result?.image!!
            Glide.with(this!!.activity!!)
                .load(activity?.getString(R.string.api_base)+"uploads/images/" + (patientProfileResponse?.result?.image))
                .into(fragmentProfileBinding?.imgRootscareProfileImage!!)
        }else{
            imagefilename=""
            Glide.with(this!!.activity!!)
                .load(  this.getResources().getDrawable(R.drawable.profile_no_image))
                .into(fragmentProfileBinding?.imgRootscareProfileImage!!)
        }


    }

    private fun defaultMedicalDetailsSetup(patientProfileResponse: PatientProfileResponse?){
        if(patientProfileResponse?.result?.allergies.equals("") || patientProfileResponse?.result?.allergies!=null){
            if(patientProfileResponse?.result?.allergies.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.allergies.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.isChecked=true
            }
        }

        if (patientProfileResponse?.result?.allergiesData!=null && !patientProfileResponse?.result?.allergiesData.equals("")){
            fragmentProfileBinding?.layoutProfileMedical?.edtAllergiesData?.setText(patientProfileResponse?.result?.allergiesData)
        }

        if(patientProfileResponse?.result?.currentMedication.equals("") || patientProfileResponse?.result?.currentMedication!=null){
            if(patientProfileResponse?.result?.currentMedication.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.isChecked=true
            }else if(patientProfileResponse?.result?.currentMedication.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.isChecked=true
            }
        }
        if (patientProfileResponse?.result?.currentMedicationData!=null && !patientProfileResponse?.result?.currentMedicationData.equals("")){
            fragmentProfileBinding?.layoutProfileMedical?.edtCurrentMedicationData?.setText(patientProfileResponse?.result?.currentMedicationData)
        }


        if(patientProfileResponse?.result?.pastMedication.equals("") || patientProfileResponse?.result?.pastMedication!=null){
            if(patientProfileResponse?.result?.pastMedication.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.isChecked=true
            }else if(patientProfileResponse?.result?.pastMedication.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.isChecked=true
            }
        }

        if (patientProfileResponse?.result?.pastMedicationData!=null && !patientProfileResponse?.result?.pastMedicationData.equals("")){
            fragmentProfileBinding?.layoutProfileMedical?.edtPastmedicationData?.setText(patientProfileResponse?.result?.pastMedicationData)
        }


        if(patientProfileResponse?.result?.injuries.equals("") || patientProfileResponse?.result?.injuries!=null){
            if(patientProfileResponse?.result?.injuries.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.injuries.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.isChecked=true
            }
        }

        if (patientProfileResponse?.result?.injuriesData!=null && !patientProfileResponse?.result?.injuriesData.equals("")){
            fragmentProfileBinding?.layoutProfileMedical?.edtInjuriesData?.setText(patientProfileResponse?.result?.injuriesData)
        }



        if(patientProfileResponse?.result?.surgeries.equals("") || patientProfileResponse?.result?.surgeries!=null){
            if(patientProfileResponse?.result?.surgeries.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.surgeries.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.isChecked=true
            }
        }

        if (patientProfileResponse?.result?.surgeriesData!=null && !patientProfileResponse?.result?.surgeriesData.equals("")){
            fragmentProfileBinding?.layoutProfileMedical?.edtSurgeriesData?.setText(patientProfileResponse?.result?.surgeriesData)
        }

        if(patientProfileResponse?.result?.chronicDiseases.equals("") || patientProfileResponse?.result?.chronicDiseases!=null){
            if(patientProfileResponse?.result?.chronicDiseases.equals("yes")){
                fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.isChecked=true
            }else if(patientProfileResponse?.result?.chronicDiseases.equals("no")){
                fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.isChecked=true
            }
        }

        if (patientProfileResponse?.result?.chronicDiseasesData!=null && !patientProfileResponse?.result?.chronicDiseasesData.equals("")){
            fragmentProfileBinding?.layoutProfileMedical?.edtChronicdisessesData?.setText(patientProfileResponse?.result?.chronicDiseasesData)
        }

    }

    private fun defaultRadioButtonSetup(){
        if (fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.GONE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.VISIBLE
        }
        if (fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.isChecked!!){
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.GONE
        }
    }

    private  fun radioButtonClickEvenOfMedical(){
        fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioAllergiesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddAllergies?.visibility=View.GONE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioCurrentMedicationNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddCurrentMedication?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioPastMedicationNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddPastMedication?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioInjuriesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddInjuries?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioSurgeriesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddSurgeries?.visibility=View.GONE
        })

        fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesYes?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.VISIBLE
        })
        fragmentProfileBinding?.layoutProfileMedical?.radioChronicdisessesNo?.setOnClickListener(View.OnClickListener {
            fragmentProfileBinding?.layoutProfileMedical?.llAddChronicdisesses?.visibility=View.GONE
        })
    }

    private fun defaultLifeStylelDetailsSetup(patientProfileResponse: PatientProfileResponse?){
        if(!patientProfileResponse?.result?.smoking.equals("") || patientProfileResponse?.result?.smoking!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.setText(patientProfileResponse?.result?.smoking)
        }
        if(!patientProfileResponse?.result?.alcohol.equals("") || patientProfileResponse?.result?.alcohol!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.setText(patientProfileResponse?.result?.alcohol)
        }
        if(!patientProfileResponse?.result?.bloodGroup.equals("") || patientProfileResponse?.result?.bloodGroup!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.setText(patientProfileResponse?.result?.bloodGroup)
        }
        if(!patientProfileResponse?.result?.activityLevel.equals("") || patientProfileResponse?.result?.activityLevel!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.setText(patientProfileResponse?.result?.activityLevel)
        }
        if(!patientProfileResponse?.result?.foodPreference.equals("") || patientProfileResponse?.result?.foodPreference!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.setText(patientProfileResponse?.result?.foodPreference)
        }
        if(!patientProfileResponse?.result?.occupation.equals("") || patientProfileResponse?.result?.occupation!=null){
            fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.setText(patientProfileResponse?.result?.occupation)
        }

    }




    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    // Do the task now
                    goToImageIntent()
                    Toast.makeText(activity, "Permissions granted.", Toast.LENGTH_SHORT).show()
                    //  toast("Permissions granted.")
                } else {
                    Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_SHORT).show()
                    // toast("Permissions denied.")
                }
                return
            }
        }
    }



    private fun checkFieldValidation(): Boolean {
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.text?.toString())) {
            Toast.makeText(activity, "Please enter your smoking habits", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.text?.toString())) {
            Toast.makeText(activity, "Please enter your alcohol habits", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.text?.toString())) {
            Toast.makeText(activity, "Please enter your blood group", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.text?.toString())) {
            Toast.makeText(activity, "Please enter your activity level", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.text?.toString())) {
            Toast.makeText(activity, "Please enter your foodPre ference", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.text?.toString())) {
            Toast.makeText(activity, "Please enter your occupation", Toast.LENGTH_SHORT).show()
            return false
        }
//


        return true
    }

    private fun checkFieldValidationForPersonalDetails(): Boolean {
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalFname?.text?.toString())) {
            Toast.makeText(activity, "Please enter your first name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLname?.text?.toString())) {
            Toast.makeText(activity, "Please enter your last name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalIdnumber?.text?.toString())) {
            Toast.makeText(activity, "Please enter your id number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())) {
            Toast.makeText(activity, "Please select your accout status", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun apieditpatientprofilepersonalApiCall(){
        baseActivity?.showLoading()
        val userId = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileViewModel!!.appSharedPref!!.userId)
        val first_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalFname?.text?.toString())
        val last_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalLname?.text?.toString())
        val id_number = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalIdnumber?.text?.toString())
        val age = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalAge?.text?.toString())
        val address = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalAddress?.text?.toString())
        val gender = RequestBody.create(MediaType.parse("multipart/form-data"), selectGender)
        val nationality = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalNationality?.text?.toString())
        val height = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalHeight?.text?.toString())
        val weight = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalWeight?.text?.toString())
        val marital_status = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalMaterialStatus?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"),"1")

        if (imageFile != null) {
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            var multipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,multipartBody,age,address,gender,nationality,height,weight,marital_status)

        } else{
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            var multipartBody = MultipartBody.Part.createFormData("image", imagefilename, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,multipartBody,age,address,gender,nationality,height,weight,marital_status)
            //Toast.makeText(activity, "Image can not be blank", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lifeStyleDropdownClick(){
        var smokingHabitsDropdownlist= ArrayList<String?>()
        smokingHabitsDropdownlist.add("Yes")
        smokingHabitsDropdownlist.add("No")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Smoking Habits",smokingHabitsDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleSmokinghabits?.setText(text)
                }

            })
        })

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Alcohol Habits",smokingHabitsDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleAlcoholHabits?.setText(text)
                }

            })
        })

        var bloodGroupDropdownlist= ArrayList<String?>()
        bloodGroupDropdownlist.add("A+")
        bloodGroupDropdownlist.add("O+")
        bloodGroupDropdownlist.add("B+")
        bloodGroupDropdownlist.add("AB+")
        bloodGroupDropdownlist.add("A-")
        bloodGroupDropdownlist.add("O-")
        bloodGroupDropdownlist.add("B-")
        bloodGroupDropdownlist.add("AB-")
        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Blood Group",bloodGroupDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleBloodGroup?.setText(text)
                }

            })
        })
        var activityLevelDropdownlist= ArrayList<String?>()
        activityLevelDropdownlist.add("Extremely inactive")
        activityLevelDropdownlist.add("Sedentary")
        activityLevelDropdownlist.add("Moderately active")
        activityLevelDropdownlist.add("Vigorously active")
        activityLevelDropdownlist.add("Extremely active")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Activity Level",activityLevelDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleActivityLevel?.setText(text)
                }

            })
        })

        var foodPreferanceDropdownlist= ArrayList<String?>()
        foodPreferanceDropdownlist.add("Standard")
        foodPreferanceDropdownlist.add("Pescetarian")
        foodPreferanceDropdownlist.add("Vegetarian")
        foodPreferanceDropdownlist.add("Lacto-vegetarian")
        foodPreferanceDropdownlist.add("Vegan")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Food Preference",foodPreferanceDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleFoodPreference?.setText(text)
                }

            })
        })

        var occupationDropdownlist= ArrayList<String?>()
        occupationDropdownlist.add("Technician")
        occupationDropdownlist.add("Teacher")
        occupationDropdownlist.add("Machinist")
        occupationDropdownlist.add("Radiographer")
        occupationDropdownlist.add("Technologist")
        occupationDropdownlist.add("Electrician")
        occupationDropdownlist.add("Engineering technician")
        occupationDropdownlist.add("Actuary")
        occupationDropdownlist.add("Tradesman")
        occupationDropdownlist.add("Medical laboratory scientist")
        occupationDropdownlist.add("Quantity surveyor")
        occupationDropdownlist.add("Prosthetist")
        occupationDropdownlist.add("Paramedic")
        occupationDropdownlist.add("Bricklayer")
        occupationDropdownlist.add("Special Education Teacher")
        occupationDropdownlist.add("Lawyer")
        occupationDropdownlist.add("Physician")
        occupationDropdownlist.add("Other")

        fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Occupation",occupationDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfileLifestyle?.edtLifestyleOccupation?.setText(text)
                }

            })
        })
    }
    private fun personalDropdownClick(){

        var materialStatusDropdownlist= ArrayList<String?>()
        materialStatusDropdownlist.add("Married")
        materialStatusDropdownlist.add("Widowed")
        materialStatusDropdownlist.add("Separated")
        materialStatusDropdownlist.add("Divorced")
        materialStatusDropdownlist.add("Single")

        fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalMaterialStatus?.setOnClickListener(View.OnClickListener {
            CommonDialog.showDialogForDropDownList(this!!.activity!!,"Material Status",materialStatusDropdownlist,object:
                DropDownDialogCallBack {
                override fun onConfirm(text: String) {
                    fragmentProfileBinding?.layoutProfilePersonal?.edtProfilePersonalMaterialStatus?.setText(text)
                }

            })
        })


    }



    ///New Image Upload Section








    //NEW IMAGE UPLOAD


    //image upload*********************************************************************************************************************************************
    private fun checkAndRequestPermissionsTest(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            val permissionText = " "
            val permissioncamera = ContextCompat.checkSelfPermission(
                this!!.activity!!,
                Manifest.permission.CAMERA
            )
            val permissionwriteexternalstorage = ContextCompat.checkSelfPermission(
                this!!.activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val listPermissionsNeeded: MutableList<String> =
                ArrayList()
            if (permissioncamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (permissionwriteexternalstorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (!listPermissionsNeeded.isEmpty()) {
                requested = true
                /*ActivityCompat.requestPermissions(getActivity(),
                                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                                    REQUEST_ID_MULTIPLE_PERMISSIONS);*/requestPermissions(
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
                )
                false
            } else {
                true
            }
        } else {
            requested = false
            true
        }
    }


    private fun captureImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_CAMERA)
            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT //
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    private fun OpenPictureEditActivity() {
        if (!TextUtils.isEmpty(imageFile?.getPath()) && File(imageFile?.getPath())
                .exists()
        ) {
            CropImage.activity(Uri.fromFile(File(imageFile?.getPath())))
                .start(this!!.activity!!)
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        if(data!=null){
            thumbnail = data.extras!!["data"] as Bitmap?
            bytes = ByteArrayOutputStream()

            thumbnail?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            imageFile = File(
                Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg"
            )
            val fo: FileOutputStream
            try {
                imageFile?.createNewFile()
                fo = FileOutputStream(imageFile)
                fo.write(bytes?.toByteArray())
                fo.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            OpenPictureEditActivity()
        }




        /*      im_upload.setImageBitmap(thumbnail);
        im_editbutton.setVisibility(View.GONE);
        im_holder.setVisibility(View.GONE);*/
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data.data)
                bytes = ByteArrayOutputStream()
                thumbnail?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        imageFile = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        val fo: FileOutputStream
        try {
            imageFile?.createNewFile()
            fo = FileOutputStream(imageFile)
            fo.write(bytes?.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        OpenPictureEditActivity()
        /*     im_upload.setImageBitmap(thumbnail);
        im_editbutton.setVisibility(View.GONE);
        im_holder.setVisibility(View.GONE);*/
    }


    fun goToImageIntent() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }




    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if(resultCode != Activity.RESULT_CANCELED){


            if (requestCode == REQUEST_CAMERA){

                try {
                    onCaptureImageResult(data!!)
                } catch (e: Exception) {
                    println("Exception===>${e.toString()}")
                }

            }
            else if (requestCode == SELECT_FILE) {
                if(data!=null){
                    onSelectFromGalleryResult(data)
                }

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                if(data!=null){
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                 //   Picasso.get().load(resultUri).into(fragmentProfileBinding?.imgRootscareProfileImage)
                    imageFile = File(result.uri.path)

                    baseActivity?.showLoading()
                    val userId = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileViewModel!!.appSharedPref!!.userId)

//        val status = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"),"1")

                    if (imageFile != null) {
                        val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
                        var multipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
                        fragmentProfileViewModel?.apieditpatientprofileimage(userId,multipartBody)

                    } else{
                        val image = RequestBody.create(MediaType.parse("multipart/form-data"), "")
                        var multipartBody = MultipartBody.Part.createFormData("image", imagefilename, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
                        fragmentProfileViewModel?.apieditpatientprofileimage(userId,multipartBody)
                        //Toast.makeText(activity, "Image can not be blank", Toast.LENGTH_SHORT).show()
                    }
                    println("resultUri===>$resultUri")
                }

            }
        }

    }


}
//End image upload*********************************************************************************************************************************************


//
//






