package com.youngpatola.bplogger.addlog;

import com.youngpatola.bplogger.data.BPLog;

/**
 * Created by youngpatola on 06/03/2018.
 */

public interface AddLogContract {
    interface View {
        void showLogList();
        void showError(int message);
    }

    interface Presenter {
        void saveLog(String systolic, String diastolic, String heartRate, long datetime,
                     String note, String feel, String position, String arm);
        BPLog getLog(int id);
        void saveChangesOnLog(int logKey, String systolic, String diastolic, String heartRate, long datetime,
                              String note, String feel, String position, String arm);
    }
}
