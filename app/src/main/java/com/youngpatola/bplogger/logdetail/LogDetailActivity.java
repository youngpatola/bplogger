package com.youngpatola.bplogger.logdetail;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.addlog.AddLogActivity;
import com.youngpatola.bplogger.base.BaseActivity;
import com.youngpatola.bplogger.data.BPLog;
import com.youngpatola.bplogger.utils.ThemeUtil;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogDetailActivity extends BaseActivity implements LogDetailContract.View {
    public static final String TAG = "LogDetailActivity";
    @BindView(R.id.viewLogDetailLayout) View parentLayout;
    @BindView(R.id.include_itemlog) View layoutResult;
//    @BindView(R.id.includeIndicator) View layoutIndicator;
    @BindView(R.id.includeFeelings) View layoutFeelings;
    @BindView(R.id.includePosition) View layoutPosition;
    @BindView(R.id.includeArm) View layoutArm;
    @BindView(R.id.textViewNotes) TextView textViewNotes;

    @BindView(R.id.textViewSystolic) TextView textViewSystolic;
    @BindView(R.id.textViewDiastolic) TextView textViewDiastolic;
    @BindView(R.id.textViewHeartRate) TextView textViewRate;
    @BindView(R.id.textViewDateTime) TextView textViewDateTime;

    private ViewViewDetails itemFeelings = new ViewViewDetails();
    private ViewViewDetails itemPosition = new ViewViewDetails();
    private ViewViewDetails itemArm = new ViewViewDetails();

    private LogDetailPresenter mPresenter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Theme setup
        ThemeUtil.assignTheme(ThemeUtil.getCurrentTheme(this));
        ThemeUtil.setupTheme(this);

        setContentView(R.layout.activity_log_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bindings
        ButterKnife.bind(this);
        ButterKnife.bind(itemFeelings, layoutFeelings);
        ButterKnife.bind(itemPosition, layoutPosition);
        ButterKnife.bind(itemArm, layoutArm);

        setPresenter(new LogDetailPresenter(this, this));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            int logKey = bundle.getInt("logKey");
            mPresenter.getLog(logKey);
        }
        else Log.d(TAG, "onCreate: No bundle!");
    }

    static class ViewViewDetails {
        @BindView(R.id.textViewFieldAnswer) TextView textViewFieldAnswer;
        @BindView(R.id.textViewFieldTitle) TextView textViewFieldTitle;
        @BindView(R.id.imageView) ImageView imageView;
    }

    void setPresenter(LogDetailPresenter presenter) {   mPresenter = presenter;}

    @Override
    public void showDetails(BPLog bpLog) {
        id = bpLog.getId();
        textViewSystolic.setText(bpLog.getSystolic());
        textViewDiastolic.setText(bpLog.getDiastolic());
        textViewRate.setText(bpLog.getHeartRate());

        DateTime epochToDate = new DateTime(bpLog.getDatetime() * 1000);
        textViewDateTime.setText(epochToDate.toString("E, MMM d h:mm a"));

        textViewNotes.setText(bpLog.getNote());
    }

    @Override
    public void showFeelingDetail(String label) {
        itemFeelings.textViewFieldAnswer.setText(label);
        itemFeelings.textViewFieldTitle.setText(R.string.label_feeling);
        // TODO: ADJUST ICONS
    }

    @Override
    public void showPositionDetail(String label) {
        itemPosition.textViewFieldAnswer.setText(label);
        itemPosition.textViewFieldTitle.setText(R.string.label_position);
    }

    @Override
    public void showArmDetail(String label) {
        itemArm.textViewFieldAnswer.setText(label);
        itemArm.textViewFieldTitle.setText(R.string.label_arm);
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
        menu.findItem(R.id.action_edit).setVisible(true);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit :
                Intent intent = new Intent(this, AddLogActivity.class);
                intent.putExtra("logKey", id);
                startActivity(intent);
                return true;

            default : return super.onOptionsItemSelected(item);
        }
    }
    // end region
}
