package com.example.petmate.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import com.example.petmate.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Config {

    private var dialog: Dialog?=null;

    fun showDialog(context: Context){
        dialog = MaterialAlertDialogBuilder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create();

        dialog!!.show();

    }

    fun hideDialog(){
        dialog!!.dismiss();
    }
}
