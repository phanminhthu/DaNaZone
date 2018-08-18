package com.example.danazone04.danazone.ui.splash.main.setting.contact;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;

import org.androidannotations.annotations.EActivity;

@SuppressLint("Registered")
@EActivity(R.layout.activity_contact)
public class ContactActivity extends BaseActivity {
    @Override
    protected void afterView() {
        Toast.makeText(this, "Đang phát triển", Toast.LENGTH_SHORT).show();
    }
}
