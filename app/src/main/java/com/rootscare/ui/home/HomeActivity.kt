package com.rootscare.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dialog.CommonDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.adapter.DrawerAdapter
import com.rootscare.databinding.ActivityHomeBinding
import com.rootscare.databinding.FragmentSeeAllDoctorByGridBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClickListener
import com.rootscare.model.DrawerDatatype
import com.rootscare.ui.appointment.FragmentAppointment
import com.rootscare.ui.base.BaseActivity
import com.rootscare.ui.bookingappointment.FragmentBookingAppointment
import com.rootscare.ui.bookingappointment.subfragment.FragmentAddPatientForDoctorBooking
import com.rootscare.ui.bookingcart.FragmentBookingCart
import com.rootscare.ui.cancellappointment.FragmentCancellMyUcomingAppointment
import com.rootscare.ui.doctorlistingdetails.FragmentDoctorListingDetails
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.login.LoginActivity
import com.rootscare.ui.medicalrecords.FragmentMedicalRecords
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointment
import com.rootscare.ui.notification.FragmentNotification
import com.rootscare.ui.nurses.FragmentNursesListByGrid
import com.rootscare.ui.nurses.nursescategorylisting.FragmentNursesCategoryListing
import com.rootscare.ui.nurses.nurseslistingdetails.FragmentNursesListingDetails
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNow
import com.rootscare.ui.patientprofilrsetting.FragmentPatientProfileSetting
import com.rootscare.ui.paymenthistory.FragmentPaymentHistory
import com.rootscare.ui.profile.FragmentProfile
import com.rootscare.ui.reviewandrating.FragmentReviewAndRating
import com.rootscare.ui.seealldoctorbygrid.FragmentSeeAllDoctorByGrid
import com.rootscare.ui.submitfeedback.FragmentSubmitReview
import com.rootscare.ui.viewprescription.FragmnetViewPrespriction
import com.rootscare.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), HomeActivityNavigator {
    private var activityHomeBinding: ActivityHomeBinding? = null
    private var homeViewModel: HomeActivityViewModel? = null
    private var drawerAdapter: DrawerAdapter? = null
    private var check_for_close = false

    private var useramne: String = ""
    private var useremail: String = ""
    private var userimage: String = ""

    companion object {

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, HomeActivity::class.java)
        }


        private var fragment_open_container: Int? = null
    }

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_home

    override val viewModel: HomeActivityViewModel
        get() {
            homeViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)
            return homeViewModel as HomeActivityViewModel
        }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                checkFragmentInBackstackAndOpen(HomeFragment.newInstance())
                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
                return@OnNavigationItemSelectedListener true
            }



            R.id.navigation_booking -> {
                checkFragmentInBackstackAndOpen(FragmentAppointment.newInstance())
                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_cart -> {
                checkFragmentInBackstackAndOpen(FragmentBookingCart.newInstance())
                Handler().postDelayed({ showSelectionOfBottomNavigationItem() }, 100)
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_media -> return@OnNavigationItemSelectedListener true
//            R.id.navigation_message -> return@OnNavigationItemSelectedListener true
//            R.id.navigation_profile -> return@OnNavigationItemSelectedListener true
//            R.id.navigation_contact -> return@OnNavigationItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel!!.navigator = this
        activityHomeBinding = viewDataBinding
        setupPermissions()

        //        activityHomeBinding.navigation.enableAnimation(false);
        //        activityHomeBinding.navigation.enableShiftingMode(false);
        //        activityHomeBinding.navigation.enableItemShiftingMode(false);

        BottomNavigationViewHelper.removeShiftMode(activityHomeBinding!!.navigation)

        drawerNavigationMenu()
        setDataAndSelectOptionInDrawerNavigation()
        val bottomNavigationView = activityHomeBinding!!.navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //        activityHomeBinding.navigation.setSelectedItemId(R.id.navigation_home);

        // Open Default fragment when app is open ed for 1st time
        //checkFragmentInBackstackAndOpen(AdmissionFragmentPageOne.newInstance(), activityHomeBinding!!.appBarHomepage.layoutContainer.id)
        checkFragmentInBackstackAndOpen(HomeFragment.newInstance())

//
//        activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarProfile.setOnClickListener {
//            //a checkFragmentInBackstackAndOpen(ProfileFragment.newInstance())
//            CommonDialog.showDialog(this, object : DialogClickCallback {
//                override fun onDismiss() {
//                }
//
//                override fun onConfirm() {
//                    homeViewModel?.appSharedPref?.deleteUserId()
//                    homeViewModel?.appSharedPref?.deleteUserName()
//                    homeViewModel?.appSharedPref?.deleteUserEmail()
//                    homeViewModel?.appSharedPref?.deleteUserImage()
//                    homeViewModel?.appSharedPref?.deleteUserType()
//                    homeViewModel?.appSharedPref?.deleteStudentLogInPassword()
//                    startActivity(ActivityLogInOption.newIntent(this@HomeActivity))
//                    finishAffinity()
//
//                }
//
//            }, "Logout", "Are you sure you want to logout?")
//
//        }

//        activityHomeBinding!!.drawerLayout.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


    }

    /*private void checkFragmentInBackstackAndOpen(FragmentStudentAttendanceRecord fragment) {
        String name_fragment_in_backstack = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.layout_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(name_fragment_in_backstack);
        ft.commit();
        //  showTextInToolbarRelativeToFragment(fragment);
    }*/

    private fun drawerNavigationMenu() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle.text = resources.getString(R.string.roots_care)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(false)
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.parent_layout)
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, activityHomeBinding!!.drawerLayout,
            toolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

                hideKeyboard()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                if (homeViewModel?.appSharedPref?.userName != null && !homeViewModel?.appSharedPref?.userEmail.equals("")) {
                    useramne = homeViewModel?.appSharedPref?.userName!!
                } else {
                    useramne = ""
                }
                if (homeViewModel?.appSharedPref?.userEmail != null && !homeViewModel?.appSharedPref?.userEmail.equals("")) {
                    useremail = homeViewModel?.appSharedPref?.userEmail!!
                } else {
                    useremail = ""
                }

                if (homeViewModel?.appSharedPref?.userImage != null && !homeViewModel?.appSharedPref?.userImage.equals("")) {
                    userimage = homeViewModel?.appSharedPref?.userImage!!
                } else {
                    userimage = ""
                }
//
//
                if (useramne != null && !useramne.equals("")) {
                    activityHomeBinding?.txtSidemenuName?.setText(useramne)
                }

                if (useremail != null && !useremail.equals("")) {
                    activityHomeBinding?.txtSidemenueEmail?.setText(useremail)
                }
                if (userimage != null && !userimage.equals("")) {
                    Glide.with(this@HomeActivity)
                        .load(getString(R.string.api_base) + "uploads/images/" + (userimage))
                        .into(activityHomeBinding?.profileImage!!)
                }else{
                    Glide.with(this@HomeActivity)
                        .load(  getResources().getDrawable(R.drawable.profile_no_image))
                        .into(activityHomeBinding?.profileImage!!)
                }
                hideKeyboard()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val moveFactor = activityHomeBinding!!.navView.width * slideOffset
                constraintLayout.translationX = moveFactor
            }

        }



        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        //        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.header_menu, HomeActivity.this.getTheme());
        //        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        activityHomeBinding!!.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarAddMemberIvBack.setOnClickListener {
            if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }


    //Set up drawer side menu item

//    private fun setDataAndSelectOptionInDrawerNavigation() {
//        // updateNavigationDrawerprofile();
//        val linearLayoutManager = LinearLayoutManager(this@HomeActivity)
//        activityHomeBinding!!.navigationDrawerRecyclerview.layoutManager = linearLayoutManager
//        activityHomeBinding!!.navigationDrawerRecyclerview.setHasFixedSize(true)
//        //     SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(HomeActivity.this);
//        //  NotificationDatatype notificationDatatype = new Gson().fromJson(sharedPrefManager.getNotification(), NotificationDatatype.class);
//        val strings = LinkedList<DrawerDatatype>()
////        strings.add(DrawerDatatype("My Profile", 0, 0))
////        strings.add(DrawerDatatype("My Schedule", 1, 0))
////        strings.add(DrawerDatatype("Online Fees Payment", 2, 0))
////        strings.add(DrawerDatatype("Student Exam Portal", 3, 0))
////        strings.add(DrawerDatatype("Student Result Portal", 4, 0))
////        strings.add(DrawerDatatype("Student Attendance", 5, 0))
////        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
////        strings.add(DrawerDatatype("Student Online Conversation", 7, 0))
////        strings.add(DrawerDatatype("Student Media Access", 8, 0))
////        strings.add(DrawerDatatype("Student Feedback Module", 9, 0))
////        strings.add(DrawerDatatype("Student GK Portal", 10, 0))
////        strings.add(DrawerDatatype("Refer and Earn", 11, 0))
//
//        strings.add(DrawerDatatype("My Upcoming Appointment", 0,R.drawable.checked))
//        strings.add(DrawerDatatype("Cancel My Upcoming Appointment", 1, R.drawable.checked))
//        strings.add(DrawerDatatype("Appointment History", 2, R.drawable.checked))
//        strings.add(DrawerDatatype("Medical Records", 3,R.drawable.checked))
//        strings.add(DrawerDatatype("View Prescription", 4, R.drawable.checked))
//        strings.add(DrawerDatatype("Payment History", 5, R.drawable.checked))
////        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
//        strings.add(DrawerDatatype("Review and Rating", 6,R.drawable.checked))
//        strings.add(DrawerDatatype("Setting", 7, R.drawable.checked))
//
//        drawerAdapter = DrawerAdapter(this@HomeActivity, strings)
//        activityHomeBinding!!.navigationDrawerRecyclerview.adapter = drawerAdapter
//        drawerAdapter!!.setonClickListener(object : OnItemClickListener {
//            override fun onItemClick(position: Int, view: View) {
//                val menu = activityHomeBinding!!.navigation.menu
//                for (i in 0 until menu.size()) {
//                    val menuItem = menu.getItem(i)
////                    var isChecked = menuItem.getItemId() == item.getItemId();
//                    menuItem.isCheckable = false
//                    menuItem.isChecked = false
//                }
////                val menu = activityHomeBinding!!.navigation.menu
////                for (i in 0 until menu.size()) {
////                    val menuItem = menu.getItem(i)
////                    /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
////                    //            menuItem.setCheckable(false);
////                    menuItem.isChecked = false
////                }
//
//
//                // Uncheck all menu item
//                // Uncheck all menu item
//           //     val menu: Menu = navigation.getMenu()
////                for (i in 0 until menu.size()) {
////                    val menuItem = menu.getItem(i)
////                    /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/menuItem.isChecked =
////                        false
////                }
//
//                menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect)
//                menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect)
//                menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect)
//                menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_deselect)
//
//
//                if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                    activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
//                } else {
//                    activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
//                }
//                when (position) {
////                    0 -> checkFragmentInBackstackAndOpen(ProfileFragment.newInstance())
////                    1 -> checkFragmentInBackstackAndOpen(MyScheduleFragment.newInstance())
//                    2 -> checkFragmentInBackstackAndOpen(FragmentAppointment.newInstance())
////                    3 -> checkFragmentInBackstackAndOpen(ExamPortalFragment.newInstance())
////                    4 -> checkFragmentInBackstackAndOpen(ResultPortalFragment.newInstance())
////                    5 -> checkFragmentInBackstackAndOpen(AttendanceFragment.newInstance())
////                    6 -> {
////                    }
////                    7 -> {
////                    }
////                    8 -> checkFragmentInBackstackAndOpen(FragmentMedia.newInstance())
////                    9 -> checkFragmentInBackstackAndOpen(FragmentFeedBack.newInstance())
////                    10 -> checkFragmentInBackstackAndOpen(FragmentStudentGKProtal.newInstance())
//
////                    0 -> checkFragmentInBackstackAndOpen(ProfileFragment.newInstance())
////                    1 -> checkFragmentInBackstackAndOpen(MyScheduleFragment.newInstance())
////                    2 -> checkFragmentInBackstackAndOpen(FragmentOnlineFeesPayment.newInstance())
////                    3 -> checkFragmentInBackstackAndOpen(ExamPortalFragment.newInstance())
////                    4 -> checkFragmentInBackstackAndOpen(ResultPortalFragment.newInstance())
////                    5 -> checkFragmentInBackstackAndOpen(AttendanceFragment.newInstance())
////                    6 -> checkFragmentInBackstackAndOpen(FragmentChatContact.newInstance())
////                    7 -> checkFragmentInBackstackAndOpen(FragmentMedia.newInstance())
////                    8 -> checkFragmentInBackstackAndOpen(FragmentFeedBack.newInstance())
////                    9 -> checkFragmentInBackstackAndOpen(FragmentStudentGKProtal.newInstance())
////                    10 -> checkFragmentInBackstackAndOpen(FragmentReferAndEarn.newInstance())
//
//
//                }
//            }
//
//        })
//    }


    //Set up drawer side menu item

    private fun setDataAndSelectOptionInDrawerNavigation() {
        // updateNavigationDrawerprofile();
        val linearLayoutManager = LinearLayoutManager(this@HomeActivity)
        activityHomeBinding!!.navigationDrawerRecyclerview.layoutManager = linearLayoutManager as RecyclerView.LayoutManager?
        activityHomeBinding!!.navigationDrawerRecyclerview.setHasFixedSize(true)
        //     SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(HomeActivity.this);
        //  NotificationDatatype notificationDatatype = new Gson().fromJson(sharedPrefManager.getNotification(), NotificationDatatype.class);
        val strings = LinkedList<DrawerDatatype>()
//        strings.add(DrawerDatatype("My Profile", 0, 0))
//        strings.add(DrawerDatatype("My Schedule", 1, 0))
//        strings.add(DrawerDatatype("Online Fees Payment", 2, 0))
//        strings.add(DrawerDatatype("Student Exam Portal", 3, 0))
//        strings.add(DrawerDatatype("Student Result Portal", 4, 0))
//        strings.add(DrawerDatatype("Student Attendance", 5, 0))
//        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
//        strings.add(DrawerDatatype("Student Online Conversation", 7, 0))
//        strings.add(DrawerDatatype("Student Media Access", 8, 0))
//        strings.add(DrawerDatatype("Student Feedback Module", 9, 0))
//        strings.add(DrawerDatatype("Student GK Portal", 10, 0))
//        strings.add(DrawerDatatype("Refer and Earn", 11, 0))

//        Cancelled Appointment
//        Cancel My Upcoming Appointment

        strings.add(DrawerDatatype("My Upcoming Appointment", 0,R.drawable.my_appointment))
        strings.add(DrawerDatatype("Cancelled Appointment", 1, R.drawable.cancel_appointment))
        strings.add(DrawerDatatype("Appointment History", 2, R.drawable.appointment_history))
        strings.add(DrawerDatatype("Medical Records", 3,R.drawable.medical_records))
        strings.add(DrawerDatatype("View Prescription", 4, R.drawable.view_prescription))
        strings.add(DrawerDatatype("Payment History", 5, R.drawable.payment_history))
//        strings.add(DrawerDatatype("Student LIVE Status", 6, 0))
        strings.add(DrawerDatatype("Review and Rating", 6,R.drawable.review_and_rating))
        strings.add(DrawerDatatype("Setting", 7, R.drawable.setting))
        strings.add(DrawerDatatype("Logout", 8, R.drawable.logout_icon))
        drawerAdapter = DrawerAdapter(this@HomeActivity, strings)
        activityHomeBinding!!.navigationDrawerRecyclerview.adapter = drawerAdapter
        drawerAdapter!!.setonClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val menu = activityHomeBinding!!.navigation.menu
                for (i in 0 until menu.size()) {
                    val menuItem = menu.getItem(i)
                    /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
                    menuItem.isCheckable = false
                    menuItem.isChecked = false
                }
                menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect)
                menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect)
                menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect)
                menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_deselect)
              //  menu.findItem(R.id.navigation_contact).setIcon(R.drawable.profile_deselect)
                if (activityHomeBinding!!.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    activityHomeBinding!!.drawerLayout.openDrawer(GravityCompat.START)
                }
                when (position) {
//                    0 -> checkFragmentInBackstackAndOpen(ProfileFragment.newInstance())
//                    1 -> checkFragmentInBackstackAndOpen(MyScheduleFragment.newInstance())
//                    2 -> {
//                    }
//                    3 -> checkFragmentInBackstackAndOpen(ExamPortalFragment.newInstance())
//                    4 -> checkFragmentInBackstackAndOpen(ResultPortalFragment.newInstance())
//                    5 -> checkFragmentInBackstackAndOpen(AttendanceFragment.newInstance())
//                    6 -> {
//                    }
//                    7 -> {
//                    }
//                    8 -> checkFragmentInBackstackAndOpen(FragmentMedia.newInstance())
//                    9 -> checkFragmentInBackstackAndOpen(FragmentFeedBack.newInstance())
//                    10 -> checkFragmentInBackstackAndOpen(FragmentStudentGKProtal.newInstance())

                    0 -> checkFragmentInBackstackAndOpen(FragmentMyUpCommingAppointment.newInstance())
                    1 -> checkFragmentInBackstackAndOpen(FragmentCancellMyUcomingAppointment.newInstance())
                    2 -> checkFragmentInBackstackAndOpen(FragmentAppointment.newInstance())
                    3 -> checkFragmentInBackstackAndOpen(FragmentMedicalRecords.newInstance())
                    4 -> checkFragmentInBackstackAndOpen(FragmnetViewPrespriction.newInstance())
                    5 -> checkFragmentInBackstackAndOpen(FragmentPaymentHistory.newInstance())
                    6 -> checkFragmentInBackstackAndOpen(FragmentReviewAndRating.newInstance())
                    7 -> checkFragmentInBackstackAndOpen(FragmentPatientProfileSetting.newInstance())
                    8 -> logout()
//                    9 -> checkFragmentInBackstackAndOpen(FragmentStudentGKProtal.newInstance())
//                    10 -> checkFragmentInBackstackAndOpen(FragmentReferAndEarn.newInstance())


                }
            }

        })
    }




    override fun onBackPressed() {
        if (activityHomeBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.findFragmentById(R.id.layout_container) is HomeFragment) {
                if (check_for_close) {
                    finish()
                }
                check_for_close = true
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ check_for_close = false }, 2000)
            } else {
                super.onBackPressed()
                showSelectionOfBottomNavigationItem()
            }
        }
    }


//    private fun showSelectionOfBottomNavigationItem() {
//        val fragment = supportFragmentManager.findFragmentById(R.id.layout_container)
//        // Uncheck all menu item
//        val menu = activityHomeBinding!!.navigation.menu
//        for (i in 0 until menu.size()) {
//            val menuItem = menu.getItem(i)
//            /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
//                        menuItem.setCheckable(false);
//            menuItem.isChecked = false
//        }
//        // Check only desired item and select the item and unselect other items
//        if (fragment is HomeFragment) {
//            menu.findItem(R.id.navigation_home).isChecked = true
//            /*menu.findItem(R.id.navigation_home).setIcon(R.drawable.specials_icon_selected);
//            menu.findItem(R.id.navigation_collection).setIcon(R.drawable.collections_icon);
//            menu.findItem(R.id.navigation_search).setIcon(R.drawable.search_icon);
//            menu.findItem(R.id.navigation_events).setIcon(R.drawable.event_icon);
//            menu.findItem(R.id.navigation_favourites).setIcon(R.drawable.favourites_icon);*/
//        }
//
////        else if (fragment is FragmentMedia) {
////            menu.findItem(R.id.navigation_media).isChecked = true
////
////        }
//        else if (fragment is FragmentProfile) {
//            menu.findItem(R.id.navigation_profile).isChecked = true
//
//        }
//
////        else if (fragment is FragmentAppointment) {
////
//////            menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect)
//////            menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect)
//////            menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect)
//////            menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_deselect)
////        }
////        else if (fragment is FragmentChatContact) {
////            menu.findItem(R.id.navigation_message).isChecked = true
////        } else if (fragment is FragmentContact) {
////            menu.findItem(R.id.navigation_contact).isChecked = true
////        }
//        // For select or unselect the item in drawer navigation menu
//        /*if (fragment instanceof MyAccountFragment) {
//            drawerAdapter.setDraweritemPositionTobeActivated(0);
//        } else if (fragment instanceof NotificationFragment) {
//            drawerAdapter.setDraweritemPositionTobeActivated(3);
//        } else {
//            drawerAdapter.setDraweritemPositionTobeActivated(-1);
//        }*/
//        showTextInToolbarRelativeToFragment(fragment!!)
//    }


    private fun showSelectionOfBottomNavigationItem() {
        val fragment = supportFragmentManager.findFragmentById(R.id.layout_container)
        // Uncheck all menu item
        val menu = activityHomeBinding!!.navigation.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            /*boolean isChecked = menuItem.getItemId() == item.getItemId();*/
            //            menuItem.setCheckable(false);
            menuItem.isChecked = false
        }
        // Check only desired item and select the item and unselect other items
        if (fragment is HomeFragment) {
            menu.findItem(R.id.navigation_home).isChecked = true
            /*menu.findItem(R.id.navigation_home).setIcon(R.drawable.specials_icon_selected);
            menu.findItem(R.id.navigation_collection).setIcon(R.drawable.collections_icon);
            menu.findItem(R.id.navigation_search).setIcon(R.drawable.search_icon);
            menu.findItem(R.id.navigation_events).setIcon(R.drawable.event_icon);
            menu.findItem(R.id.navigation_favourites).setIcon(R.drawable.favourites_icon);*/
        } else if (fragment is FragmentProfile) {
            menu.findItem(R.id.navigation_profile).isChecked = true
//            menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_deselect);
//            menu.findItem(R.id.navigation_booking).setIcon(R.drawable.booking_deselect);
//            menu.findItem(R.id.navigation_cart).setIcon(R.drawable.cart_deselect);
//            menu.findItem(R.id.navigation_profile).setIcon(R.drawable.profile_select);
        }else if (fragment is FragmentAppointment) {
            menu.findItem(R.id.navigation_booking).isChecked = true
        }
        else if (fragment is FragmentBookingCart) {
            menu.findItem(R.id.navigation_cart).isChecked = true
        }
//        else if (fragment is FragmentPatientbookPayNow) {
//            menu.findItem(R.id.navigation_booking).isChecked = true
//        }



//        else if (fragment is ProfileFragment) {
//            menu.findItem(R.id.navigation_profile).isChecked = true
//        } else if (fragment is FragmentChatContact) {
//            menu.findItem(R.id.navigation_message).isChecked = true
//        } else if (fragment is FragmentContact) {
//            menu.findItem(R.id.navigation_contact).isChecked = true
//        }
        // For select or unselect the item in drawer navigation menu
        /*if (fragment instanceof MyAccountFragment) {
            drawerAdapter.setDraweritemPositionTobeActivated(0);
        } else if (fragment instanceof NotificationFragment) {
            drawerAdapter.setDraweritemPositionTobeActivated(3);
        } else {
            drawerAdapter.setDraweritemPositionTobeActivated(-1);
        }*/
        showTextInToolbarRelativeToFragment(fragment!!)
    }


    private fun showTextInToolbarRelativeToFragment(fragment: Fragment) {

        val tootbar_text=activityHomeBinding!!.appBarHomepage.toolbarLayout.toolbarTitle
        val tootlebar_profile=activityHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarProfile
        val tootlebar_notification=activityHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarNotification
        val toolbar_logout=activityHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarLogout
        val toolbar_back=activityHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarBack
        val toolbar_menu=activityHomeBinding?.appBarHomepage?.toolbarLayout?.toolbarAddMemberIvBack



        if (fragment is HomeFragment) {
            tootbar_text.text = resources.getString(R.string.roots_care)
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.GONE
            toolbar_menu?.visibility=View.VISIBLE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
               logout()
            })
//            tootbar_text.setTextColor(resources.getColor(android.R.color.white))
         //   drawerAdapter!!.selectItem(-1)
        }

        else if (fragment is FragmentProfile) {
         //   drawerAdapter!!.selectItem(0)
            tootbar_text.text = resources.getString(R.string.roots_care)
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.GONE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE

            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })
        }

        else if (fragment is FragmentBookingAppointment) {
           // drawerAdapter!!.selectItem(0)
            tootbar_text.text = resources.getString(R.string.roots_care)
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })
        }
        else if (fragment is FragmentAppointment) {
        //    drawerAdapter!!.selectItem(2)
            tootbar_text.text ="Appointment History"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentCancellMyUcomingAppointment) {
            ///drawerAdapter!!.selectItem(1)
            tootbar_text.text ="Cancel Appointment"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentPatientbookPayNow) {
            //drawerAdapter!!.selectItem(1)
            tootbar_text.text ="Roots Care"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentMyUpCommingAppointment) {
           // drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Roots Care"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentMedicalRecords) {
            // drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Roots Care"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }
        else if (fragment is FragmnetViewPrespriction) {
         //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="View Prescription"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentPaymentHistory) {
         //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Payment History"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }
        else if (fragment is FragmentPatientProfileSetting) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Profile Setting"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentReviewAndRating) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Review and Rating"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }
        else if (fragment is FragmentAddPatientForDoctorBooking) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Add Patient"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentBookingCart) {
            //   drawerAdapter!!.selectItem(3)

            tootbar_text.text ="Cart Item"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentSubmitReview) {
            //   drawerAdapter!!.selectItem(3)

            tootbar_text.text ="Submit Review"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })
            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }



        else if (fragment is FragmentNotification) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Notification List"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.GONE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
//            tootlebar_notification?.setOnClickListener(View.OnClickListener {
//                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
//            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })

            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentDoctorListingDetails) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Doctor Details"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })

            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }
        else if (fragment is FragmentSeeAllDoctorByGrid) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Doctor List"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })

            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }
        else if (fragment is FragmentNursesListByGrid) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Nurse List"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })

            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentNursesListingDetails) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Nurse Details"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })

            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }

        else if (fragment is FragmentNursesCategoryListing) {
            //   drawerAdapter!!.selectItem(3)
            tootbar_text.text ="Nurse List"
            tootbar_text.setTextColor(ContextCompat.getColor(this@HomeActivity, android.R.color.white))
            toolbar_profile?.visibility=View.VISIBLE
            tootlebar_notification?.visibility=View.VISIBLE
            toolbar_back?.visibility=View.VISIBLE
            toolbar_menu?.visibility=View.GONE
            tootlebar_profile?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentProfile.newInstance())
            })
            tootlebar_notification?.setOnClickListener(View.OnClickListener {
                checkFragmentInBackstackAndOpen(FragmentNotification.newInstance())
            })
            toolbar_logout?.setOnClickListener(View.OnClickListener {
                logout()
            })

            toolbar_back?.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

        }


    }

    fun checkFragmentInBackstackAndOpen(fragment: Fragment) {
        val name_fragment_in_backstack = fragment.javaClass.name
        if (fragment_open_container == null && activityHomeBinding!!.appBarHomepage.layoutContainer.id != null) {
            fragment_open_container = activityHomeBinding!!.appBarHomepage.layoutContainer.id
        }
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(name_fragment_in_backstack, 0)
        val ft = manager.beginTransaction()
        if (!fragmentPopped && manager.findFragmentByTag(name_fragment_in_backstack) == null) { //fragment not in back stack, create it.
            ft.replace(fragment_open_container!!, fragment, name_fragment_in_backstack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(name_fragment_in_backstack)
            ft.commit()
        } else if (manager.findFragmentByTag(name_fragment_in_backstack) != null) {
            /*String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();*/
            val currentFragment = manager.findFragmentByTag(name_fragment_in_backstack)
            ft.replace(fragment_open_container!!, currentFragment!!, name_fragment_in_backstack)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(name_fragment_in_backstack)
            ft.commit()
        }
        showTextInToolbarRelativeToFragment(fragment)
    }

    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_CONTACTS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            RECORD_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fragment=supportFragmentManager.findFragmentById(activityHomeBinding?.appBarHomepage?.layoutContainer?.id!!)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }



    private fun logout(){
        CommonDialog.showDialog(this@HomeActivity, object :
            DialogClickCallback {
            override fun onDismiss() {
            }

            override fun onConfirm() {
                homeViewModel?.appSharedPref?.deleteUserId()
                homeViewModel?.appSharedPref?.deleteIsLogINRemember()
                homeViewModel?.appSharedPref?.deleteIsAppointmentType()
                startActivity(LoginActivity.newIntent(this@HomeActivity))
                finishAffinity()

            }

        }, "Logout", "Are you sure you want to logout?")
    }

    override fun reloadPageAfterConnectedToInternet() {
        showSelectionOfBottomNavigationItem()
    }


    private fun init() {

    }
}
