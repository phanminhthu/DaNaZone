package com.example.danazone04.danazone.ui.splash.main.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.BaseContainerFragment;
import com.example.danazone04.danazone.BaseFragment;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.ui.splash.main.bmi.BmiFragment;
import com.example.danazone04.danazone.ui.splash.main.bmi.BmiFragment_;
import com.example.danazone04.danazone.ui.splash.main.coin.CoinFragment;
import com.example.danazone04.danazone.ui.splash.main.coin.CoinFragment_;
import com.example.danazone04.danazone.ui.splash.main.contact.FragmentContact;
import com.example.danazone04.danazone.ui.splash.main.contact.FragmentContact_;
import com.example.danazone04.danazone.ui.splash.main.history.HistoryFragment;
import com.example.danazone04.danazone.ui.splash.main.history.HistoryFragment_;
import com.example.danazone04.danazone.ui.splash.main.metter.MetterActivity_;
import com.example.danazone04.danazone.ui.splash.main.start.HomeFragment;
import com.example.danazone04.danazone.ui.splash.main.start.HomeFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main_menu)
public class MainMenuActivity extends BaseActivity {
    @ViewById
    DrawerLayout mDrawer;
    @ViewById
    NavigationView mNV;

    private ActionBarDrawerToggle mToggle;
    private Toast mToastExit;

    @Override
    protected void afterView() {
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.colose);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        replaceFragment(HomeFragment_.builder().build());
        mToastExit = Toast.makeText(MainMenuActivity.this, getResources().getString(R.string.text_back_exit), Toast.LENGTH_SHORT);
        setupDrawerContent(mNV);
    }

    /**
     * Open new fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransactionAccount = getSupportFragmentManager().beginTransaction();
        fragmentTransactionAccount.replace(R.id.mFrameContainer, fragment);
        fragmentTransactionAccount.commit();
    }

    /**
     * check current fragment id
     *
     * @return
     */
    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.mFrameContainer);
    }

    @Override
    public void onBackPressed() {
        BaseContainerFragment fragment = (BaseContainerFragment) getCurrentFragment();

        if (!fragment.popFragment()) {
            boolean isExit = mToastExit.getView().isShown();
            if (!isExit) {
                mToastExit.show();
            } else {
                super.onBackPressed();
                System.exit(0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Selected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mHome:
                if (!((BaseContainerFragment) getCurrentFragment() instanceof HomeFragment))
                    replaceFragment(HomeFragment_.builder().build());
                break;

            case R.id.mSetting:
                MetterActivity_.intent(this).start();
                break;

            case R.id.mCoin:
                if (!((BaseContainerFragment) getCurrentFragment() instanceof CoinFragment))
                    replaceFragment(CoinFragment_.builder().build());
                break;

            case R.id.mContact:
                if (!((BaseContainerFragment) getCurrentFragment() instanceof FragmentContact))
                    replaceFragment(FragmentContact_.builder().build());
                break;

            case R.id.mBMI:
                if (!((BaseContainerFragment) getCurrentFragment() instanceof BmiFragment))
                    replaceFragment(BmiFragment_.builder().build());
                break;

            case R.id.mHistory:
                if (!((BaseContainerFragment) getCurrentFragment() instanceof HistoryFragment))
                    replaceFragment(HistoryFragment_.builder().build());
                break;

            case R.id.mExit:
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
                break;
        }
        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawer.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Selected(item);
                return false;
            }
        });
    }

}
