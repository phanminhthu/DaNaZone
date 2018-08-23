package com.example.danazone04.danazone.ui.splash.main.bmi;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

@EFragment(R.layout.fragment_bmi)
public class BmiFragment extends BaseContainerFragment {
    @ViewById
    TextView mTvBMI;
    @ViewById
    TextView mTvValue;
    @ViewById
    TextView mTvSubmit;
    @ViewById
    EditText mEdtWeight;
    @ViewById
    EditText mEdtHeight;
    private double mHeight, mWeight, mBMI;
    private DecimalFormat df;

    @Override
    protected void afterViews() {

    }

    @Click(R.id.mTvSubmit)
    void onClick(View v) {
        try {
            mHeight = Double.parseDouble(mEdtHeight.getText().toString());
            mWeight = Double.parseDouble(mEdtWeight.getText().toString());
            df = new DecimalFormat("0.00");
            mBMI = mWeight / Math.pow(mHeight, 2) * 10000;

            mTvBMI.setText(df.format(mBMI) + "");
            if (mBMI < 18)
                mTvValue.setText("Bạn hơi gầy");
            else if (18 <= mBMI && mBMI < 25)
                mTvValue.setText("Bạn bình thường");
            else if (25 <= mBMI && mBMI < 30)
                mTvValue.setText("Bạn béo phì độ 1");
            else if (30 <= mBMI && mBMI < 35)
                mTvValue.setText("Bạn béo phì độ 2");
            else if (35 <= mBMI)
                mTvValue.setText("Bạn béo phì độ 3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
