package com.youngpatola.bplogger.logs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.addlog.AddLogActivity;
import com.youngpatola.bplogger.base.BaseActivity;
import com.youngpatola.bplogger.data.BPLog;
import com.youngpatola.bplogger.logdetail.LogDetailActivity;
import com.youngpatola.bplogger.logs.logadapter.LogItemListener;
import com.youngpatola.bplogger.logs.logadapter.LogListAdapter;
import com.youngpatola.bplogger.settings.SettingsActivity;
import com.youngpatola.bplogger.utils.ThemeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogsActivity extends BaseActivity implements LogsContract.View{
    private static final String TAG = LogsActivity.class.getSimpleName();
    private static final int REQUEST_ADD_LOG = 1;
    private static final int SETTINGS = 10;
    private static final int REQUEST_EDIT_LOG = 101;

    // Bindings
    @BindView(R.id.rvLogList) RecyclerView rvLogList;
    @BindView(R.id.viewLogsActivity) View viewLogsActivity;
    @BindView(R.id.bottomAppBar) BottomAppBar appBar;
    @OnClick(R.id.btn_addLog)
    public void onAddButtonClicked() {
        mPresenter.addNewLog();
    }

    private LogsPresenter mPresenter;

    LogListAdapter mAdapter;
    LogItemListener mItemListener = new LogItemListener() {
        @Override
        public void onLogItemClick(BPLog bpLog) {
            Intent intent = new Intent(getApplicationContext(), LogDetailActivity.class);
            intent.putExtra("logKey", bpLog.getId());
            startActivity(intent);
        }

        @Override
        public void onLogItemLongClick(final BPLog bpLog) {
            AlertDialog.Builder b = new AlertDialog.Builder(LogsActivity.this);
            b.setTitle(R.string.prompt_title_options)
                .setMessage(R.string.prompt_message_options)
                .setPositiveButton(R.string.prompt_choice_edit, (dialogInterface, i) -> {
                    Intent intent = new Intent(LogsActivity.this, AddLogActivity.class);
                    intent.putExtra("logKey", bpLog.getId());
                    startActivityForResult(intent, REQUEST_EDIT_LOG);
                })
                .setNegativeButton(R.string.prompt_choice_delete, (dialogInterface, i) -> mPresenter.deleteLog(bpLog))
                .setNeutralButton(android.R.string.cancel, null)
                .setCancelable(true)
                .show();
        }
    };

    void setPresenter(LogsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme setup
        ThemeUtil.assignTheme(ThemeUtil.getCurrentTheme(this));
        ThemeUtil.setupTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alt_activity_entry_list);
        ButterKnife.bind(this);
        setupAppBar();

        setPresenter(new LogsPresenter(this, this));

        mAdapter = new LogListAdapter(new ArrayList<>(0), mItemListener);
        rvLogList.setAdapter(mAdapter);
        rvLogList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvLogList.setLayoutManager(layoutManager);


        // Bluestacks emulator detector
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck();
        } else {
            Log.d(TAG, "Files: Well");
            isEmulator();
        }
    }

    private void setupAppBar() {
        setSupportActionBar(appBar);
    }

    @Override
    protected void onResume() {
        mPresenter.loadLogs();
        super.onResume();
    }

    @Override
    public void showLogs(List<BPLog> bpLogList) {
        mAdapter.replaceList(bpLogList);
    }

    @Override
    public void showAddLog() {
        Intent intent = new Intent(getApplicationContext(), AddLogActivity.class);
        startActivityForResult(intent, REQUEST_ADD_LOG);
    }

    @Override
    public void showDeleteLog() {
        showMessageSnackShort(viewLogsActivity, R.string.prompt_message_logdelete);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == REQUEST_ADD_LOG)
                showMessageSnackShort(viewLogsActivity, R.string.prompt_message_logadded);
            if (requestCode == REQUEST_EDIT_LOG)
                showMessageSnackShort(viewLogsActivity, R.string.prompt_message_logupdated);
        } else if (resultCode == RESULT_CANCELED) {
            recreate();
        }
    }

    // Menu start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings : {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS);
                return true;
            }
            case R.id.action_about : showDialog(getString(R.string.title_about),
                    getString(R.string.prompt_message_about)); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        isEmulator();
    }

    private void isEmulator() {
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains("windows")) {
                Toast.makeText(this, "Windows file exists", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Files: windows file exists but is " + files[i].exists());
            }
            if (i==files.length) {
                Toast.makeText(this, "No windows file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            isEmulator();
        }
    }

    // end region
}
