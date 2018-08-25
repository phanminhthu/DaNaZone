package com.example.danazone04.danazone.ui.splash.main.overall;

import android.widget.TextView;

import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.bean.Run;
import com.example.danazone04.danazone.sqlite.DBManager;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@EFragment(R.layout.fragment_overall)
public class OverallFragment extends BaseContainerFragment {
    @ViewById
    TextView mTvTime;
    @ViewById
    TextView mTvDistance;
    @ViewById
    TextView mTvCalo;

   // private DBManager dbManager;


    @Override
    protected void afterViews() {
//        dbManager = new DBManager(getContext());
//        int time = dbManager.getSumTime();
//        double distance = dbManager.getSumDistance();
//        double calo = dbManager.getSumCalo();
//
//
//        mTvTime.setText("" + getCountDownStringInMinutes(time));
//        mTvDistance.setText("" + dbManager.getSumTime());
//        mTvCalo.setText(calo + " Calo");
    }

//    private String getCountDownStringInMinutes(int timeInSeconds) {
//        return getTwoDecimalsValue(timeInSeconds / 3600) + ":" + getTwoDecimalsValue(timeInSeconds / 60) + ":" + getTwoDecimalsValue(timeInSeconds % 60);
//    }
//
//
//    private static String getTwoDecimalsValue(int value) {
//        if (value >= 0 && value <= 9) {
//            return "0" + value;
//        } else {
//            return value + "";
//        }
//    }
}
