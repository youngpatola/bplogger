package com.youngpatola.bplogger.logs;

import android.content.Context;

import com.youngpatola.bplogger.data.BPLog;
import com.youngpatola.bplogger.data.sqlite.SqlController;

/**
 * Created by youngpatola on 04/03/2018.
 */

public class LogsPresenter implements LogsContract.Presenter{
    // Bindings
    private LogsContract.View mView;
    private Context context;
    private SqlController controller;

    LogsPresenter(LogsContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
        controller = new SqlController(context);
    }

    @Override
    public void loadLogs() {
        mView.showLogs(controller.getList());
    }

    @Override
    public void addNewLog() {
        mView.showAddLog();
    }

    @Override
    public void deleteLog(BPLog bpLog) {
        if (controller.deleteData(String.valueOf(bpLog.getId()))) {
            loadLogs();
            mView.showDeleteLog();
        }
    }
}
