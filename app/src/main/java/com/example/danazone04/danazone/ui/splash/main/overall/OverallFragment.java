package com.example.danazone04.danazone.ui.splash.main.overall;

import android.widget.TextView;

import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.bean.Run;
import com.example.danazone04.danazone.sqlite.DBManager;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_overall)
public class OverallFragment extends BaseContainerFragment {
    @ViewById
    TextView mTvTime;
    @ViewById
    TextView mTvDistance;
    @ViewById
    TextView mTvCalo;

    private DBManager dbManager;


    @Override
    protected void afterViews() {
        dbManager = new DBManager(getContext());
        mTvTime.setText("" + dbManager.getId());
        mTvDistance.setText("" + dbManager.getSumTime());
        mTvCalo.setText("" + dbManager.getSumDistance());
    }
}
