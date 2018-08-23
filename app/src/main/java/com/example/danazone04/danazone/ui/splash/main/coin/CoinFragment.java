package com.example.danazone04.danazone.ui.splash.main.coin;

import android.widget.TextView;

import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.dialog.ShareDialog;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

@EFragment(R.layout.fragment_coin)
public class CoinFragment extends BaseContainerFragment {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    @ViewById
    TextView mTvCoin;
    @ViewById
    TextView mTvCode;

    @Override
    protected void afterViews() {
        String coin = SessionManager.getInstance().getKeySaveCoin();
        mTvCoin.setText(coin);
        mTvCode.setText(SessionManager.getInstance().getKeySaveCode());
        if (!mTvCoin.getText().toString().equals("0")) {
            if (mTvCoin.getText().toString().equals("2")) {
                new ShareDialog(getContext(), new ShareDialog.OnDialogClickListener() {
                    @Override
                    public void onCallSerVice() {
                        SessionManager.getInstance().updateCoin("0");
                        String a = randomString(5);
                        if (SessionManager.getInstance().getKeySaveCode().equals("")) {
                            SessionManager.getInstance().setKeySaveCode(a);
                        }
                        SessionManager.getInstance().updateCode(a);
                        mTvCode.setText(SessionManager.getInstance().getKeySaveCode());
                        mTvCoin.setText("0");
                    }
                }).show();
            }
        }
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i <= len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
