package com.youngpatola.bplogger.addlog;

import android.content.Context;

import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.data.BPLog;
import com.youngpatola.bplogger.data.sqlite.SqlController;

import static java.lang.String.valueOf;

/**
 * Created by youngpatola on 06/03/2018.
 */

public class AddLogPresenter implements AddLogContract.Presenter {
    private static final String TAG = AddLogPresenter.class.getSimpleName();
    private AddLogContract.View mView;
    private SqlController sqlController;

    AddLogPresenter(AddLogContract.View mView, Context context) {
        this.mView = mView;
        sqlController = new SqlController(context);
    }

    @Override
    public void saveLog(String systolic, String diastolic, String heartRate, long datetime, String note, String feel, String position, String arm) {
        BPLog bpLog = new BPLog(systolic, diastolic, heartRate, datetime, note, feel, position, arm);
        bpLog.setIndicator(dummyIndicator());
        if (!sqlController.saveData(bpLog)) {
            mView.showLogList();
        } else {
            mView.showError(R.string.prompt_message_error_saving);
        }
    }

    @Override
    public void saveChangesOnLog(int logKey, String systolic, String diastolic, String heartRate, long datetime, String note, String feel, String position, String arm) {
        BPLog bpLog = new BPLog(logKey, systolic, diastolic, heartRate, datetime, note, feel, position, arm);
        bpLog.setIndicator(dummyIndicator());
        int i = sqlController.updateData(bpLog);
        if (i == 1) mView.showLogList();
        else mView.showError(R.string.prompt_message_error_updating);
    }

    @Override
    public BPLog getLog(int id) {
        return sqlController.getData(valueOf(id));
    }

    private String dummyIndicator() {
        return "Normal";
    }
}
