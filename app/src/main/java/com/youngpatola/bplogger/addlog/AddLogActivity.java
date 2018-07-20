package com.youngpatola.bplogger.addlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.base.BaseActivity;
import com.youngpatola.bplogger.data.BPLog;
import com.youngpatola.bplogger.utils.ThemeUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
    I learned that DATE/TIME sucks your soul for 2 days.
    And that Joda Time is a 1 based array while java.util.Date or that
    DatepickerDialog is 0 based.
 */

public class AddLogActivity extends BaseActivity implements AddLogContract.View {
    private static final String TAG = AddLogActivity.class.getSimpleName();
    @BindView(R.id.viewAddLogLayout) View parentLayout;
    @BindView(R.id.npSys) EditText numberPickerSystolic;
    @BindView(R.id.npDia) EditText numberPickerDiastolic;
    @BindView(R.id.npRate) EditText numberPickerRate;
    @BindView(R.id.dpDate) EditText editTextDate;
    @BindView(R.id.etTime) EditText editTextTime;
    @BindView(R.id.etFeeling) EditText editTextFeeling;
    @BindView(R.id.etPosition) EditText editTextPosition;
    @BindView(R.id.etArm) EditText editTextArm;
    @BindView(R.id.etNotes) EditText editTextNotes;

    AddLogPresenter mPresenter;
    int year, day, month, hour, min;
    String[] feelArray, posArray, armArray;

    // TODO: Change UI, it is ugly. So is for LogDetailActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Theme setup
        ThemeUtil.assignTheme(ThemeUtil.getCurrentTheme(this));
        ThemeUtil.setupTheme(this);

        setContentView(R.layout.alt_add_log);
        ButterKnife.bind(this);
        setPresenter(new AddLogPresenter(this, this));

        // Add launchMode:"singleTop" on the parent activity to fire its onResume.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            getSupportActionBar().setTitle(R.string.actionbar_title_edit);
            setupEditLog(mPresenter.getLog(bundle.getInt("logKey")));
        } else {
            getSupportActionBar().setTitle(R.string.actionbar_title_add);
            setupAddLog();
        }
    }

    void setPresenter(AddLogPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        feelArray = getResources().getStringArray(R.array.list_feelings);
        posArray = getResources().getStringArray(R.array.list_position);
        armArray = getResources().getStringArray(R.array.list_arm);
    }

    @Override
    public void showLogList() {
        Log.d(TAG, "showLogList: ");
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showError(int message) {
        showMessageSnackLong(parentLayout, message);
    }


    @OnClick(R.id.dpDate)
    public void onClickDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year,  month-1, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.etTime)
    public void onClickTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, min, false);
        timePickerDialog.show();
    }

    @OnClick({R.id.npSys, R.id.npDia, R.id.npRate, R.id.etFeeling, R.id.etPosition, R.id.etArm})
    public void onClickValues(View view) {
        Log.d(TAG, "onClickValues: " + view.getId());
        switch (view.getId()) {
            case R.id.npSys : initiateNumberPicker(numberPickerSystolic, R.string.label_sys); break;
            case R.id.npDia : initiateNumberPicker(numberPickerDiastolic, R.string.label_dia); break;
            case R.id.npRate : initiateNumberPicker(numberPickerRate, R.string.label_rate); break;
            case R.id.etFeeling : initiateDialogSpinner(editTextFeeling, feelArray, R.string.label_feeling); break;
            case R.id.etPosition : initiateDialogSpinner(editTextPosition, posArray, R.string.label_position); break;
            case R.id.etArm : initiateDialogSpinner(editTextArm, armArray, R.string.label_arm); break;
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            DateTime setDate = new DateTime(year, month+1, day, 0, 0);
            editTextDate.setText(setDate.toString("E, MMM d"));
            AddLogActivity.this.year = year;
            AddLogActivity.this.month = month+1;
            AddLogActivity.this.day = day;
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int min) {
            String ampm = (hour > 12) ? " PM" : " AM";
            // if setting is 12 hour
            int hour12 = (hour > 12) ? hour-12 : hour;
            String time = hour12 + ":" + min + ampm;

            editTextTime.setText(time);
            AddLogActivity.this.hour = hour;
            AddLogActivity.this.min = min;
        }
    };

    void setupAddLog() {
        DateTime now = new DateTime();
        // if setting is 12 hour
        editTextDate.setText(now.toString("E, MMM d"));
        editTextTime.setText(now.toString("h:mm a").toUpperCase());
        setDateTimeVars(now.getYear(), now.getMonthOfYear(),
                now.getDayOfMonth(), now.getHourOfDay(), now.getMinuteOfHour());
    }

    void setupEditLog(BPLog bpLog) {
        Log.d(TAG, "setupEditLog: id(" + bpLog.getId() + ")");
        if (bpLog!=null) {
            DateTime epochToDate = new DateTime(bpLog.getDatetime() * 1000);
            numberPickerSystolic.setText(bpLog.getSystolic());
            numberPickerDiastolic.setText(bpLog.getDiastolic());
            numberPickerRate.setText(bpLog.getHeartRate());

            editTextDate.setText(epochToDate.toString("E, MMM d"));
            editTextTime.setText(epochToDate.toString("h:mm a").toUpperCase());

            setDateTimeVars(epochToDate.getYear(), epochToDate.getMonthOfYear(),
                    epochToDate.getDayOfMonth(), epochToDate.getHourOfDay(), epochToDate.getMinuteOfHour());

            editTextFeeling.setText(bpLog.getFeel());
            editTextPosition.setText(bpLog.getPosition());
            editTextArm.setText(bpLog.getArm());
            editTextNotes.setText(bpLog.getNote());
        }
    }

    // Utilities
    long getDateInEpoch() {
        String toBeParsed = month + "/" + day +"/" + year + " " + hour + ":" + min;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/d/yyyy HH:mm");
        DateTime joda = formatter.parseDateTime(toBeParsed).withZone(DateTimeZone.UTC);
        return joda.getMillis() / 1000;
    }

    void setDateTimeVars(int year, int month, int day, int hour, int min) {
        this.year = year;
        this.day = day;
        this.month = month;
        this.hour = hour;
        this.min = min;
    }

    // Custom dialog views
    void initiateNumberPicker(final EditText editText, int title) {
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(200);
        picker.setValue(Integer.parseInt(editText.getText().toString()));
        picker.setScaleX(1.5f);
        picker.setScaleY(1.5f);

        final FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        new AlertDialog.Builder(this)
                .setView(frameLayout)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String value = String.valueOf(picker.getValue());
                        editText.setText(value);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    void initiateDialogSpinner(final EditText ed, final String[] items, int title) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);
        b.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selected = items[i];
                ed.setText(selected);
            }
        });
        b.show();
    }

    private void buttonSave() {
        String sys = String.valueOf(numberPickerSystolic.getText());
        String dia = String.valueOf(numberPickerDiastolic.getText());
        String hRate = String.valueOf(numberPickerRate.getText());
        long dateTime = getDateInEpoch();
        String feel = editTextFeeling.getText().toString();
        String pos = editTextPosition.getText().toString();
        String arm = editTextArm.getText().toString();
        String note = editTextNotes.getText().toString();
        Log.d(TAG, "onClickSave: " + sys + ", " + dia + ", "
                + hRate + ", " + dateTime + ", " + feel + ", " + pos + ", " + arm + ", " + note );

        if (getIntent().getExtras()!=null) {
            int key = getIntent().getExtras().getInt("logKey");
            mPresenter.saveChangesOnLog(key, sys, dia, hRate, dateTime, note, feel, pos, arm);
        } else {
            mPresenter.saveLog(sys, dia, hRate, dateTime, note, feel, pos, arm);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(true);
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save :
                buttonSave();
                return true;

            default : return super.onOptionsItemSelected(item);
        }
    }
}
