package com.example.danazone04.danazone.ui.splash.main.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseAdapter;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.bean.Run;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    public interface OnHistoryClickListener {
        void onClickItem(int position);
    }
    private List<Run> mList;
    private OnHistoryClickListener mListener;

    protected HistoryAdapter(@NonNull Context context, List<Run> list, OnHistoryClickListener listener) {
        super(context);
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_history_run, parent, false);
        return new ViewHolderItem(view);
    }

    /**
     * onBindHolder Item
     *
     * @param holder
     * @param position
     */
    private void onBindViewHolderItem(ViewHolderItem holder, int position) {
        Run mRun = mList.get(position);
        holder.mTvDistance.setText(mRun.getDistance());
        holder.mTvDate.setText(mRun.getDate());
        holder.mTvStartTime.setText(mRun.getTimeStart());
        holder.mTvSpeed.setText(mRun.getSpeed());
        holder.mTvCalo.setText(mRun.getCalo());
        holder.mTvTimeRun.setText(mRun.getTime());


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindViewHolderItem((ViewHolderItem) holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * ViewHolderItem
     */
    private class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView mTvDate;
        private TextView mTvStartTime;
        private TextView mTvDistance;
        private TextView mTvSpeed;
        private TextView mTvCalo;
        private TextView mTvTimeRun;
        private ImageView mImgDelete;

        public ViewHolderItem(View view) {
            super(view);
            mTvDate = (TextView) view.findViewById(R.id.mTvDate);
            mTvStartTime = (TextView) view.findViewById(R.id.mTvStartTime);
            mTvDistance = (TextView) view.findViewById(R.id.mTvDistance);
            mTvSpeed = (TextView) view.findViewById(R.id.mTvSpeed);
            mTvCalo = (TextView) view.findViewById(R.id.mTvCalo);
            mTvTimeRun = (TextView) view.findViewById(R.id.mTvTimeRun);
            mImgDelete = (ImageView) view.findViewById(R.id.mImgDelete);
            mImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClickItem(getLayoutPosition());
                    }
                }
            });
        }
    }
}
