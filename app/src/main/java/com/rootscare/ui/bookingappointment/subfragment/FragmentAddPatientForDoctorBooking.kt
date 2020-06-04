package com.rootscare.ui.bookingappointment.subfragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.addpatientresponse.AddPatientResponse
import com.rootscare.databinding.FragmentAddPatientForDoctorBookingBinding
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.utils.ManagePermissions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

class FragmentAddPatientForDoctorBooking : BaseFragment<FragmentAddPatientForDoctorBookingBinding, FragmentAddPatientForDoctorBookingViewModel>(), FragmentAddPatientForDoctorBookingNavigator  {
    private var fragmentAddPatientForDoctorBookingBinding: FragmentAddPatientForDoctorBookingBinding? = null
    private var fragmentAddPatientForDoctorBookingViewModel: FragmentAddPatientForDoctorBookingViewModel? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    private var selectedGender="Female"
    var imageFile: File? = null
    private var doctorId:String=""


    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_patient_for_doctor_booking
    override val viewModel: FragmentAddPatientForDoctorBookingViewModel
        get() {
            fragmentAddPatientForDoctorBookingViewModel =
                ViewModelProviders.of(this).get(FragmentAddPatientForDoctorBookingViewModel::class.java!!)
            return fragmentAddPatientForDoctorBookingViewModel as FragmentAddPatientForDoctorBookingViewModel
        }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
        fun newInstance(doctorid: String): FragmentAddPatientForDoctorBooking {
            val args = Bundle()
            args.putString("doctorid",doctorid)
            val fragment = FragmentAddPatientForDoctorBooking()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAddPatientForDoctorBookingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddPatientForDoctorBookingBinding = viewDataBinding
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this!!.activity!!, list, PermissionsRequestCode)

        //check permissions states

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()


        if (arguments!=null && arguments?.getString("doctorid")!=null){
            doctorId = arguments?.getString("doctorid")!!
            Log.d("Doctor Id", ": " + doctorId )
        }
        fragmentAddPatientForDoctorBookingBinding?.edtPatientProfileImage?.setOnClickListener(View.OnClickListener {
            showPictureDialog()
        })
        fragmentAddPatientForDoctorBookingBinding?.radioPatientGenderFemale?.setOnClickListener(View.OnClickListener {
            selectedGender="Female"
        })

        fragmentAddPatientForDoctorBookingBinding?.radioPatientGenderMale?.setOnClickListener(View.OnClickListener {
            selectedGender="Male"
        })

        fragmentAddPatientForDoctorBookingBinding?.btnRootscareAddpatientForDoctorBooking?.setOnClickListener(
            View.OnClickListener {
                if(checkFieldValidationForPersonalDetails()){
                    apiinsertpatientfamilyApiCall()
                }

            })


    }


    //IMAGE SELECTION AND GET IMAGE PATH

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    bitmapToFile(bitmap)
                    Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()

                    fragmentAddPatientForDoctorBookingBinding?.imgRootscarePatientProfileImage?.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            fragmentAddPatientForDoctorBookingBinding?.imgRootscarePatientProfileImage?.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            bitmapToFile(thumbnail)
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + FragmentAddPatientForDoctorBooking.IMAGE_DIRECTORY)

        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            //     File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");


            f.createNewFile()
            // imageFile=f

            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())


            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
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
    // Method to save an bitmap to a file
    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(activity)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        imageFile = file
        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //updateProfileImageApiCall(imageFile!!)
        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)


    }

    private fun checkFieldValidationForPersonalDetails(): Boolean {
        if (TextUtils.isEmpty(fragmentAddPatientForDoctorBookingBinding?.edtAddpatientFname?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient first name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentAddPatientForDoctorBookingBinding?.edtAddpatientLname?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient last name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentAddPatientForDoctorBookingBinding?.edtAddpatientEmail?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(fragmentAddPatientForDoctorBookingBinding?.edtAddpatientPhonenumber?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient phone number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(fragmentAddPatientForDoctorBookingBinding?.edtAddpatientAge?.text?.toString())) {
            Toast.makeText(activity, "Please enter patient age", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private fun apiinsertpatientfamilyApiCall(){
        baseActivity?.showLoading()
        val patient_id = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentAddPatientForDoctorBookingViewModel!!.appSharedPref!!.userId)
        val first_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentAddPatientForDoctorBookingBinding?.edtAddpatientFname?.text?.toString())
        val last_name = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentAddPatientForDoctorBookingBinding?.edtAddpatientLname?.text?.toString())
        val email = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentAddPatientForDoctorBookingBinding?.edtAddpatientEmail?.text?.toString())
        val phone_number = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentAddPatientForDoctorBookingBinding?.edtAddpatientPhonenumber?.text?.toString())
        val gender = RequestBody.create(MediaType.parse("multipart/form-data"), selectedGender)
        val age = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentAddPatientForDoctorBookingBinding?.edtAddpatientAge?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"), fragmentProfileBinding?.layoutProfilePersonal?.txtProfilePersonalStatus?.text?.toString())
//        val status = RequestBody.create(MediaType.parse("multipart/form-data"),"1")
        if (imageFile != null) {
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            var multipartBody = MultipartBody.Part.createFormData("image", imageFile?.name, image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentAddPatientForDoctorBookingViewModel?.apiinsertpatientfamily(patient_id,first_name,last_name,multipartBody,email,phone_number,gender,age)

        } else{
            val image = RequestBody.create(MediaType.parse("multipart/form-data"), "")
            var multipartBody = MultipartBody.Part.createFormData("image", "", image)
//            fragmentProfileViewModel?.apieditpatientprofilepersonal(userId,first_name,last_name,id_number,status,multipartBody)
            fragmentAddPatientForDoctorBookingViewModel?.apiinsertpatientfamily(patient_id,first_name,last_name,multipartBody,email,phone_number,gender,age)
            //Toast.makeText(activity, "Image can not be blank", Toast.LENGTH_SHORT).show()
        }
    }

    override fun successAddPatientResponse(addPatientResponse: AddPatientResponse?) {
        baseActivity?.hideLoading()
        if(addPatientResponse?.code.equals("200")){
            Toast.makeText(activity, addPatientResponse?.message, Toast.LENGTH_SHORT).show()
            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                FragmentBookingAppointment.newInstance(doctorId))

        }else{
            Toast.makeText(activity, addPatientResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorAddPatientResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }




    // Callback with the request from calling requestPermissions(...)



}