package com.youngpatola.bplogger.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.android.material.snackbar.Snackbar;
import com.youngpatola.bplogger.utils.ThemeUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
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
            .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss()).show();
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
