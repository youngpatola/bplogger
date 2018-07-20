package com.youngpatola.bplogger.settings;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.base.BaseActivity;
import com.youngpatola.bplogger.utils.ThemeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements SettingsContract.View {
    @BindView(R.id.viewSettings) View rootView;
    @BindView(R.id.progressBar) View progressCircle;
    @BindView(R.id.layoutClickableExportSetting) View layoutExport;
    @BindView(R.id.layoutClickableThemeSetting) View layoutTheme;

    private static final int STORAGE_PERMISSION = 222;
    private ViewButtonSettings exportButton = new ViewButtonSettings();
    private ViewButtonSettings themeButton = new ViewButtonSettings();

    SettingsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Theme setup
        ThemeUtil.assignTheme(ThemeUtil.getCurrentTheme(this));
        ThemeUtil.setupTheme(this);

        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        ButterKnife.bind(exportButton, layoutExport);
        ButterKnife.bind(themeButton, layoutTheme);

        setPresenter(new SettingsPresenter(this, this));

        // TODO: Create chart views.
        // TODO: Create adjust-text-size setting
        setupExportButtonSetting();
        setupThemeSetting();
    }

    private void setPresenter(SettingsPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    static class ViewButtonSettings {
//        @BindView(R.id.viewViewDetails) View rootView;
        @BindView(R.id.textViewFieldAnswer) TextView textViewFieldAnswer;
        @BindView(R.id.textViewFieldTitle) TextView textViewFieldTitle;
    }

    public void setupExportButtonSetting() {
        exportButton.textViewFieldAnswer.setText(R.string.label_setting_export);
        exportButton.textViewFieldTitle.setText(R.string.label_setting_export_sub);
//        exportButton.rootView.setClickable(true);
    }

    public void setupThemeSetting() {
        themeButton.textViewFieldAnswer.setText(R.string.label_setting_theme);
        themeButton.textViewFieldTitle.setText(R.string.label_setting_theme_sub);
//        themeButton.rootView.setClickable(true);
    }

    // Theme settings ---
    @OnClick(R.id.layoutClickableThemeSetting)
    public void onClickTheme(View view) {
        int assignedNum = ThemeUtil.getCurrentTheme(this);
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Set theme color");
        String[] items = getResources().getStringArray(R.array.list_pref_themes);
        b.setSingleChoiceItems(items, assignedNum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThemeUtil.assignTheme(i);
            }
        });
        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThemeUtil.toggleTheme(SettingsActivity.this);
                dialogInterface.dismiss();
                recreate();
            }
        });
        b.show();
    }
    // end region ---

    // CSV Export settings ---
    @OnClick(R.id.layoutClickableExportSetting)
    public void onClickExport(View view) {
        isStorageAllowed();
    }

    void fireExportButton() {
        showDialog("Export database?", "Do you want to export database?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showProgressDialog(true);
                mPresenter.exportToCSV();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    @Override
    public void showProgressDialog(boolean toggle) {
        if (toggle) {
            progressCircle.setVisibility(View.VISIBLE);
        } else {
            progressCircle.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProcessDone(int message) {
        showMessageSnackLong(rootView, message);
    }
    // end region ---

    // check storage permission
    void isStorageAllowed() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else {
            fireExportButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fireExportButton();
            } else {
                showMessageSnackLong(rootView, R.string.prompt_message_request_storage);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

}
