package com.rootscare.ui.bookingcart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.interfaces.OnClickOfCartItem
import com.interfaces.OnClickWithTwoButton
import com.interfaces.OnDoctorPrivateSlotClickListner
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.data.model.api.request.cartitemdeleterequest.CartItemDeleteRequest
import com.rootscare.data.model.api.request.checkoutdoctorbookingrequest.CheckoutDoctorBookingRequest
import com.rootscare.data.model.api.request.doctorrequest.bookingcartrequests.BookingCartRequest
import com.rootscare.data.model.api.response.doctorallapiresponse.checkoutdoctorbookingresponse.CheckoutDoctorBookingResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.BookingCartResponse
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.ResultItem
import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.cartitemdeleteresponse.DoctorCartItemDeleteResponse
import com.rootscare.databinding.FragmentBookingCartBinding
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.ui.base.BaseFragment
import com.rootscare.ui.bookingcart.adapter.AdapterBookingCartRecyclerview
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.home.subfragment.HomeFragment
import com.rootscare.ui.myappointment.FragmentMyAppointment
import com.rootscare.ui.myupcomingappointment.FragmentMyUpCommingAppointment
import com.rootscare.ui.notification.FragmentNotification
import com.rootscare.ui.notification.FragmentNotificationViewModel
import com.rootscare.ui.notification.adapter.AdapterNotificationListRecyclerview
import com.rootscare.ui.patientbookpaynow.FragmentPatientbookPayNow

class FragmentBookingCart: BaseFragment<FragmentBookingCartBinding, FragmentBookingCartViewModel>(), FragmentBookingCartNavigator {
    private var fragmentBookingCartBinding: FragmentBookingCartBinding? = null
    private var fragmentBookingCartViewModel: FragmentBookingCartViewModel? = null

    var adapterBookingCartRecyclerview:AdapterBookingCartRecyclerview?=null

    var subTotalPrice:Int=0
    var vatPrice:Int=0
    var totalPaidPrice:Int=0
    var resultCartItemIds=""
    var p=0
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_booking_cart
    override val viewModel: FragmentBookingCartViewModel
        get() {
            fragmentBookingCartViewModel =
                ViewModelProviders.of(this).get(FragmentBookingCartViewModel::class.java!!)
            return fragmentBookingCartViewModel as FragmentBookingCartViewModel
        }
    companion object {
        fun newInstance(): FragmentBookingCart {
            val args = Bundle()
            val fragment = FragmentBookingCart()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBookingCartViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBookingCartBinding = viewDataBinding
//        setUpNotificationRecyclerview()
        getCartItem()
        fragmentBookingCartBinding?.btnCartCheckout?.setOnClickListener(View.OnClickListener {

            CommonDialog.showDialog(this.activity!!, object :
                DialogClickCallback {
                override fun onDismiss() {
                }

                override fun onConfirm() {
                    //adapterBookingCartRecyclerview?.geteCartItemList()
                //    Toast.makeText(activity, "Found DATA.", Toast.LENGTH_SHORT).show()

                    subTotalPrice=0
                    vatPrice=0
                    totalPaidPrice=0
                    var cartIds=""
                    var finalCartIds=""
                    for (i in 0 until adapterBookingCartRecyclerview?.geteCartItemList()?.size!!) {
                        if(adapterBookingCartRecyclerview?.geteCartItemList()?.get(i)?.isselectitem!!){
                            subTotalPrice=subTotalPrice+adapterBookingCartRecyclerview?.geteCartItemList()?.get(i)?.price?.toInt()!!
                            cartIds= adapterBookingCartRecyclerview?.geteCartItemList()?.get(i)?.id!!
                            if(finalCartIds.equals("")){
                                finalCartIds=cartIds
                            }else{
                                finalCartIds=finalCartIds+","+cartIds
                            }
                        }
                    }
                    vatPrice= (subTotalPrice * 20)/100
                    totalPaidPrice=subTotalPrice+vatPrice
//                    fragmentBookingCartBinding?.txtSubtotalPrice?.setText("SAR"+" "+subTotalPrice.toString())
//                    fragmentBookingCartBinding?.txtVatPrice?.setText("SAR"+" "+vatPrice.toString())
//                    fragmentBookingCartBinding?.txtTotalpaidPrice?.setText("SAR"+" "+totalPaidPrice.toString())
                    resultCartItemIds=finalCartIds
                    Log.d("CART ITEMM ID STRING", resultCartItemIds)
                    if(!resultCartItemIds.equals("")){
                        bookingCheckoutApiCall(subTotalPrice.toString(),vatPrice.toString(),totalPaidPrice.toString(),resultCartItemIds)

                    }else{
                        Toast.makeText(activity, "Please select item for checkout", Toast.LENGTH_SHORT).show()
                    }
                   // (activity as HomeActivity).checkFragmentInBackstackAndOpen(FragmentPatientbookPayNow.newInstance())

                }

            }, "Comfirm Payment", "Are you sure for this payment ?")

        })
    }

    // Set up recycler view for service listing if available
    private fun setUpNotificationRecyclerview(cartItemList: ArrayList<ResultItem?>?) {
        assert(fragmentBookingCartBinding!!.recyclerViewRootscareBookingcart!= null)
        val recyclerView =fragmentBookingCartBinding!!.recyclerViewRootscareBookingcart
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        adapterBookingCartRecyclerview = AdapterBookingCartRecyclerview(cartItemList,context!!)
        recyclerView.adapter = adapterBookingCartRecyclerview
        adapterBookingCartRecyclerview?.recyclerViewItemClickWithView= object : OnClickOfCartItem {


            override fun onFirstItemClick(id: Int) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {
                    }

                    override fun onConfirm() {

                        if(isNetworkConnected){
                            baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
                            val cartItemDeleteRequest= CartItemDeleteRequest()
                            cartItemDeleteRequest?.id=id.toString()
                            fragmentBookingCartViewModel?.apideletepatientcart(cartItemDeleteRequest)

                        }else{
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Comfirm Delete", "Are you sure to delete this item ?")
            }

            override fun onSecondItemClick(cartItemList: ArrayList<ResultItem?>?) {
                subTotalPrice=0
                vatPrice=0
                totalPaidPrice=0
                var cartIds=""
                var finalCartIds=""
                for (i in 0 until cartItemList?.size!!) {
                    if(cartItemList?.get(i)?.isselectitem!!){
                        subTotalPrice=subTotalPrice+ cartItemList?.get(i)?.price?.toInt()!!
                        cartIds= cartItemList?.get(i)?.id!!
                        if(finalCartIds.equals("")){
                            finalCartIds=cartIds
                        }else{
                            finalCartIds=finalCartIds+","+cartIds
                        }
                    }
                }
                vatPrice= (subTotalPrice * 20)/100
                totalPaidPrice=subTotalPrice+vatPrice
                fragmentBookingCartBinding?.txtSubtotalPrice?.setText("SAR"+" "+subTotalPrice.toString())
                fragmentBookingCartBinding?.txtVatPrice?.setText("SAR"+" "+vatPrice.toString())
                fragmentBookingCartBinding?.txtTotalpaidPrice?.setText("SAR"+" "+totalPaidPrice.toString())
                resultCartItemIds=finalCartIds
                Log.d("CART ITEMM ID STRING", resultCartItemIds)

            }


        }

    }

    override fun successBookingCartResponse(bookingCartResponse: BookingCartResponse?) {
        baseActivity?.hideLoading()
        if(bookingCartResponse?.code.equals("200")){
            fragmentBookingCartBinding?.btnCartCheckout?.isEnabled=true
            fragmentBookingCartBinding?.btnCartCheckout?.visibility=View.VISIBLE
            fragmentBookingCartBinding?.rlRadiogroup?.visibility=View.VISIBLE
            fragmentBookingCartBinding?.txtStaticText?.visibility=View.VISIBLE
            if(bookingCartResponse?.result!=null && bookingCartResponse?.result.size>0){
                fragmentBookingCartBinding?.recyclerViewRootscareBookingcart?.visibility=View.VISIBLE
                fragmentBookingCartBinding?.tvNoDate?.visibility=View.GONE
                setUpNotificationRecyclerview(bookingCartResponse?.result)
            }else{
                fragmentBookingCartBinding?.recyclerViewRootscareBookingcart?.visibility=View.GONE
                fragmentBookingCartBinding?.tvNoDate?.visibility=View.VISIBLE
                fragmentBookingCartBinding?.tvNoDate?.setText(bookingCartResponse?.message)
            }

            for (i in 0 until bookingCartResponse?.result?.size!!) {
                subTotalPrice=subTotalPrice+ bookingCartResponse?.result?.get(i)?.price?.toInt()!!
            }
            vatPrice= (subTotalPrice * 20)/100
            totalPaidPrice=subTotalPrice+vatPrice
            fragmentBookingCartBinding?.cardViewPricetotal?.visibility=View.VISIBLE
            fragmentBookingCartBinding?.txtSubtotalPrice?.setText("SAR"+" "+subTotalPrice.toString())
            fragmentBookingCartBinding?.txtVatPrice?.setText("SAR"+" "+vatPrice.toString())
            fragmentBookingCartBinding?.txtTotalpaidPrice?.setText("SAR"+" "+totalPaidPrice.toString())


        }else{
            fragmentBookingCartBinding?.recyclerViewRootscareBookingcart?.visibility=View.GONE
            fragmentBookingCartBinding?.tvNoDate?.visibility=View.VISIBLE
            fragmentBookingCartBinding?.cardViewPricetotal?.visibility=View.GONE
            fragmentBookingCartBinding?.btnCartCheckout?.isEnabled=false
            fragmentBookingCartBinding?.btnCartCheckout?.visibility=View.GONE
            fragmentBookingCartBinding?.rlRadiogroup?.visibility=View.GONE
            fragmentBookingCartBinding?.txtStaticText?.visibility=View.GONE
//            button.setEnabled(false);
            fragmentBookingCartBinding?.tvNoDate?.setText(bookingCartResponse?.message)
            Toast.makeText(activity, bookingCartResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun successDoctorCartItemDeleteResponse(doctorCartItemDeleteResponse: DoctorCartItemDeleteResponse?) {
        baseActivity?.hideLoading()
        if(doctorCartItemDeleteResponse?.code.equals("200")){
            Toast.makeText(activity, doctorCartItemDeleteResponse?.message, Toast.LENGTH_SHORT).show()
            getCartItem()
            subTotalPrice=0
            vatPrice=0
            totalPaidPrice=0
        }else{
            Toast.makeText(activity, doctorCartItemDeleteResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun successCheckoutDoctorBookingResponse(checkoutDoctorBookingResponse: CheckoutDoctorBookingResponse?) {
        baseActivity?.hideLoading()
        if(checkoutDoctorBookingResponse?.code.equals("200")){
        //    Toast.makeText(activity, checkoutDoctorBookingResponse?.message, Toast.LENGTH_SHORT).show()
            CommonDialog.showDialog(activity!!, object :
                DialogClickCallback {
                override fun onDismiss() {
                }

                override fun onConfirm() {
                    (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                        FragmentMyAppointment.newInstance())

                }

            }, "Appointment", checkoutDoctorBookingResponse?.message!!)


        }else{
            Toast.makeText(activity, checkoutDoctorBookingResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorBookingCartResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(HomeFragment.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCartItem(){
        if(isNetworkConnected){
            baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
            val bookingCartRequest= BookingCartRequest()
            bookingCartRequest?.userId=fragmentBookingCartViewModel?.appSharedPref?.userId
            fragmentBookingCartViewModel?.apipatientcart(bookingCartRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun bookingCheckoutApiCall(subTotalprice:String,vatprice:String,totalpaidprice:String,cartid:String){
        if(isNetworkConnected){
            baseActivity?.showLoading()
//            patientProfileRequest?.userId="11"
            val checkoutDoctorBookingRequest= CheckoutDoctorBookingRequest()
            checkoutDoctorBookingRequest?.userId=fragmentBookingCartViewModel?.appSharedPref?.userId
            checkoutDoctorBookingRequest?.cartId=cartid
            checkoutDoctorBookingRequest?.subTotalPrice=subTotalprice
            checkoutDoctorBookingRequest?.vatPrice=vatprice
            checkoutDoctorBookingRequest?.totalPrice=totalpaidprice
            checkoutDoctorBookingRequest?.paymentType="Offline"
            fragmentBookingCartViewModel?.apipatientbookappointmentofflinepayment(checkoutDoctorBookingRequest)

        }else{
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }
    }


}