package com.youngpatola.bplogger.logs;

import com.youngpatola.bplogger.data.BPLog;

import java.util.List;

/**
 * Created by youngpatola on 04/03/2018.
 */

public interface LogsContract {
    interface View {
        void showLogs(List<BPLog> bpLogList);
        void showAddLog();
        void showDeleteLog();
    }
    interface Presenter{
        void loadLogs();
        void addNewLog();
        void deleteLog(BPLog bpLog);
    }
}
