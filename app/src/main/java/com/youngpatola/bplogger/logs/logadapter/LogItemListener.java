package com.youngpatola.bplogger.logs.logadapter;

import com.youngpatola.bplogger.data.BPLog;

/**
 * Created by youngpatola on 05/03/2018.
 */

public interface LogItemListener {
    void onLogItemClick(BPLog bpLog);
    void onLogItemLongClick(BPLog bpLog);
}
