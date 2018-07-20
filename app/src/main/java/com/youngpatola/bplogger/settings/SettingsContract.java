package com.youngpatola.bplogger.settings;

public interface SettingsContract {
    interface View {
        void showProgressDialog(boolean toggle);
        void showProcessDone(int message);
    }
    interface Presenter {
        void exportToCSV();
    }
}
