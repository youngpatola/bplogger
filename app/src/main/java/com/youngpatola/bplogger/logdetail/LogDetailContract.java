package com.youngpatola.bplogger.logdetail;

import com.youngpatola.bplogger.data.BPLog;

/**
 * Created by youngpatola on 10/03/2018.
 */

public interface LogDetailContract {
    interface View {
        void showDetails(BPLog bpLog);
        void showFeelingDetail(String label);
        void showPositionDetail(String label);
        void showArmDetail(String label);
    }

    interface Presenter {
        void getLog(int id);
    }
}
