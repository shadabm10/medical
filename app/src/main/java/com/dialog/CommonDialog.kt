package com.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interfaces.DropDownDialogCallBack
import com.interfaces.DropdownRowItemItemClickOnConfirm
import com.interfaces.OnDropDownListItemClickListener
import com.interfaces.OnRowItemDropdownItemClick
import com.rootscare.R
import com.rootscare.adapter.AdapterCommonDropdown
import com.rootscare.adapter.AdapterRowItemListDropdown
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.model.RowItem

@SuppressLint("StaticFieldLeak")
object CommonDialog {

    private val TAG = "CommonDialog"

    fun showDialog(activity: Context, dialogClickCallback: DialogClickCallback, dialog_title: String,
                   dialog_message: String) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(dialog_title)
        alertDialogBuilder.setMessage(dialog_message)
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(R.string.yes) { dialogInterface, i ->
            dialogClickCallback.onConfirm()
            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, which ->
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }


    // Add appointment to calendar after successful appointment creation
//    fun showDialogForAskingAddress(context: Context, title: String,dialogClickCallback: DialogClickCallback) {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.ask_for_address_dialog_layout)
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
//
//        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
//        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }
//
//        cancel.setOnClickListener {
//            dialogClickCallback.onDismiss()
//            dialog.dismiss()
//        }
//        confirm.setOnClickListener {
//            dialogClickCallback.onConfirm()
//            dialog.dismiss()
//        }
//        dialog.show()
//    }


    fun showDialogForAddPatient(context: Context, title: String,dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_add_patient)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true);
        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }
    fun showDialogForDropDownList(context: Context,title: String,dropdownList: ArrayList<String?>?,dialogClickCallback: DropDownDialogCallBack) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_dropdown)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true);
        val recyclerView_dropdown_list = dialog.findViewById<RecyclerView>(R.id.recyclerView_dropdown_list)
        val tv_title=dialog.findViewById<TextView>(R.id.txt_header_title)
        tv_title?.setText(title)
        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        recyclerView_dropdown_list.layoutManager = gridLayoutManager
        recyclerView_dropdown_list.setHasFixedSize(true)
        val dropdownListAdapter = AdapterCommonDropdown(dropdownList!!,context!!)
        recyclerView_dropdown_list.adapter = dropdownListAdapter
        dropdownListAdapter?.recyclerViewItemClick= object : OnDropDownListItemClickListener {
            override fun onConfirm(text: String) {
                dialogClickCallback.onConfirm(text)
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    fun showDialogForRowItemDropDownList(context: Context,title: String,dropdownList: ArrayList<RowItem?>?,dialogClickCallback: DropdownRowItemItemClickOnConfirm) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_dropdown)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true);
        val recyclerView_dropdown_list = dialog.findViewById<RecyclerView>(R.id.recyclerView_dropdown_list)
        val tv_title=dialog.findViewById<TextView>(R.id.txt_header_title)
        tv_title?.setText(title)

        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        recyclerView_dropdown_list.layoutManager = gridLayoutManager
        recyclerView_dropdown_list.setHasFixedSize(true)
        val dropdownListAdapter = AdapterRowItemListDropdown(dropdownList!!,context!!)
        recyclerView_dropdown_list.adapter = dropdownListAdapter
        dropdownListAdapter?.recyclerViewItemClick= object : OnRowItemDropdownItemClick {
            override fun onConfirm(title_name: String,title_id:String) {
                dialogClickCallback.onConfirm(title_name,title_id)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
        // Show Image uploading progress
    var dialog: Dialog? = null

}
