package com.youngpatola.bplogger.logs.logadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youngpatola.bplogger.R;
import com.youngpatola.bplogger.data.BPLog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by youngpatola on 05/03/2018.
 */

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogListViewHolder> {
    private static final String TAG = "LogListAdapter";
    private List<BPLog> bpLogList;
    private LogItemListener logItemClickListener;

    public LogListAdapter(List<BPLog> bpLogList, LogItemListener logItemClickListener) {
        this.bpLogList = bpLogList;
        this.logItemClickListener = logItemClickListener;
    }

    @Override
    public LogListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View logView = layoutInflater.inflate(R.layout.alt_item_log2, parent, false);
        return new LogListViewHolder(logView, logItemClickListener);
    }

    @Override
    public void onBindViewHolder(LogListViewHolder holder, int position) {
        BPLog bpLog = bpLogList.get(position);
        if (bpLog.getSystolic()!=null) holder.tvSystolic.setText(bpLog.getSystolic());
        if (bpLog.getDiastolic()!=null) holder.tvDiastolic.setText(bpLog.getDiastolic());
        if (bpLog.getHeartRate()!=null) holder.tvHeartRate.setText(bpLog.getHeartRate());
        if (bpLog.getDatetime()!=0) {
            String epochToDTUTC = new DateTime(bpLog.getDatetime()*1000, DateTimeZone.UTC).toString();
            DateTime dateTime = new DateTime(epochToDTUTC);
            holder.tvDateTime.setText(dateTime.toString("MMMM d, yyyy hh:mm a"));
            // TODO: [low-prio] Create a time util?
        }
    }

    @Override
    public int getItemCount() {
        return bpLogList.size();
    }

    public BPLog getItem(int position) {
        return bpLogList.get(position);
    }

    public void replaceList(List<BPLog> list) {
        if (list!=null) bpLogList = list;
        notifyDataSetChanged();
    }

    public class LogListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewSystolic) TextView tvSystolic;
        @BindView(R.id.textViewDiastolic) TextView tvDiastolic;
        @BindView(R.id.textViewHeartRate) TextView tvHeartRate;
        @BindView(R.id.textViewDateTime) TextView tvDateTime;
//        @BindView(R.id.textViewMonth) TextView tvMonth;
//        @BindView(R.id.textViewDay) TextView tvDay;
//        @BindView(R.id.viewDate) View dateIcon;
        @BindView(R.id.constraintLayoutItem) View v;
        LogItemListener vhLogItemListener;

        public LogListViewHolder(View itemView, LogItemListener logItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            v = itemView;
            vhLogItemListener = logItemListener;
        }

        @OnClick
        public void onClickItem(View view) {
            int pos = getAdapterPosition();
            BPLog bpLogSelected = getItem(pos);
            vhLogItemListener.onLogItemClick(bpLogSelected);
        }

        @OnLongClick
        public boolean onLongClickItem(View view) {
            int pos = getAdapterPosition();
            BPLog bpLogSelected = getItem(pos);
            vhLogItemListener.onLogItemLongClick(bpLogSelected);
            return true;
        }
    }

}
