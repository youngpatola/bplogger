package com.youngpatola.bplogger.settings;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.data.sqlite.SqlController;

import org.joda.time.DateTime;

import java.io.File;

public class SettingsPresenter implements SettingsContract.Presenter{
    private static final String TAG = "SettingsPresenter";

    private SettingsContract.View mView;
    private SqlController controller;

    public SettingsPresenter(SettingsContract.View mView, Context context) {
        this.mView = mView;
        controller = new SqlController(context);
    }

    @Override
    public void exportToCSV() {
        File location = new File(Environment.getExternalStorageDirectory() + "/BPLogs");
        if (!location.exists()) location.mkdir();
        String fileName = location.getAbsolutePath() + "/BP Log Report_" + DateTime.now().toString("MMddyyyHH:mm") + ".csv";
        Log.d(TAG, "exportToCSV: fileName(" + fileName + ")");
        controller.getFileForCSV(fileName, new SqlController.ExportToCSVCallback() {
            @Override
            public void csvCreatedSuccess(final boolean isNotEmpty) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mView.showProgressDialog(false);
                        int msg = isNotEmpty ? R.string.prompt_message_export_success : R.string.prompt_message_export_empty;
                        mView.showProcessDone(msg);
                    }
                }, 1000);
            }

            @Override
            public void csvCreatedFailure(Exception e) {
                Log.d(TAG, "csvCreatedFailed: " + e.getMessage());
                mView.showProgressDialog(false);
                mView.showProcessDone(R.string.prompt_message_export_error);
            }
        });
    }
}
