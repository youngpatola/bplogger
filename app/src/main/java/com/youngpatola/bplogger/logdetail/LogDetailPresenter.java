package com.youngpatola.bplogger.logdetail;

import android.content.Context;

import com.youngpatola.bplogger.data.BPLog;
import com.youngpatola.bplogger.data.sqlite.SqlController;

import static java.lang.String.valueOf;

/**
 * Created by youngpatola on 10/03/2018.
 */

public class LogDetailPresenter implements LogDetailContract.Presenter{
    LogDetailContract.View mView;
    private SqlController sqlController;
    private BPLog bpLog;

    public LogDetailPresenter(LogDetailContract.View view, Context context) {
        this.mView = view;
        sqlController = new SqlController(context);
    }

    @Override
    public void getLog(int id) {
        BPLog bpLog = sqlController.getData(valueOf(id));
        this.bpLog = bpLog;
        if (bpLog!=null) {
            mView.showDetails(bpLog);
            setOtherDetails();
        }
    }

    private void setOtherDetails() {
        String feelVal = (bpLog.getFeel()!=null && !bpLog.getFeel().isEmpty()) ? bpLog.getFeel() : "No feeling specified";
        String posVal = (bpLog.getPosition()!=null && !bpLog.getPosition().isEmpty()) ? bpLog.getPosition() : "No position specified";
        String armVal = (bpLog.getArm()!=null && !bpLog.getArm().isEmpty()) ? bpLog.getArm() : "No arm specified";
        mView.showFeelingDetail(feelVal);
        mView.showPositionDetail(posVal);
        mView.showArmDetail(armVal);
    }
}
