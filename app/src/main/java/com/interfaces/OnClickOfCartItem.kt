package com.interfaces

import com.rootscare.data.model.api.response.doctorallapiresponse.doctorbooking.bookingcartresponse.ResultItem

interface OnClickOfCartItem {
    fun onFirstItemClick(id: Int)
    fun onSecondItemClick(cartItemList: ArrayList<ResultItem?>?)
}