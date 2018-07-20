package com.youngpatola.bplogger.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by youngpatola on 05/03/2018.
 */

public class BaseActivity extends AppCompatActivity{

    public void showMessageSnackShort(View view, int message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void showMessageSnackLong(View view, int message){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public void showDialog(String title, String message) {
        if (title == null) title = "";
        if (message == null) message = "";

        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
    }

    public void showDialog(String title, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        if (title == null) title = "";
        if (message == null) message = "";

        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes", yesListener)
            .setNegativeButton("No", noListener)
            .show();
    }


}
