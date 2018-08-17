package com.example.danazone04.danazone.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.ui.splash.TakeImage_;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@SuppressLint("Registered")
@EActivity(R.layout.activity_base_iage)
public class BaseImageActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @Extra
    Bitmap mStart;
    @Extra
    Bitmap mEnd;
    @ViewById
    ImageView mImgStart;
    @ViewById
    ImageView mImgEnd;
    @ViewById
    RelativeLayout mRlBase;
    @ViewById
    TextView mTvSubmit;
    @ViewById
    TextView mTvTime;
    @ViewById
    TextView mTvSpeed;
    @ViewById
    TextView mTvKM;
    @ViewById
    TextView mTvHide;
    @ViewById
    ImageView mImgCancelDialog;
    @ViewById
    TextView mTvMaxSpeed;
    @ViewById
    TextView mTvCalo;


    @Extra
    String mTime;
    @Extra
    String mKM;
    @Extra
    String mSpeed;
    @Extra
    String mMaxSpeed;
    @Extra
    String mCalo;
    @Extra
    String mStartPoint;
    @Extra
    String mEndPoint;
    @Extra
    String mTimeStart;
    @Extra
    String mTimeEnd;
    @Extra
    String mDate;

    @ViewById
    TextView mTvTimeStart;
    @ViewById
    TextView mTvTimeEnd;
    @ViewById
    TextView mTvData;


    private String filename;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    private int key = 0;


    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrent;
    private LatLng mLatLng;
    private Location mLocation = null;
    private GoogleMap mMap;
    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private LocationRequest mLocationRequest;


    @Override
    protected void afterView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(BaseImageActivity.this);
        buidGoogleApiClient();
        createLocationRequest();
        displayLocation();
        key = key + 1;
        if (!SessionManager.getInstance().getKeySaveCoin().equals("")) {
            SessionManager.getInstance().updateCoin(String.valueOf(Integer.valueOf(SessionManager.getInstance().getKeySaveCoin()) + 1));
        } else {
            SessionManager.getInstance().setKeySaveCoin(String.valueOf(key));
        }
        if (mStart != null && mEnd != null) {
            mImgStart.setImageBitmap(mStart);
            mImgEnd.setImageBitmap(mEnd);
            mTvTime.setText(mTime);
            if (mSpeed == null) {
                mTvSpeed.setText("0 KM/H");
                mTvSpeed.setText(mSpeed);
            }

            if (mKM == null) {
                mTvKM.setText("0.0 KM");
            } else {
                mTvKM.setText(mKM);
            }

            mTvTimeStart.setText(mTimeStart +"h");
            mTvTimeEnd.setText(mTimeEnd + "h");
            mTvData.setText(mDate);
            mTvMaxSpeed.setText(mMaxSpeed);
            mTvCalo.setText(mCalo);
        }
        filename = String.valueOf(Random());
        fn_permission();
    }


    @Click({R.id.mTvSubmit, R.id.mTvHide, R.id.mImgCancelDialog})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvSubmit:
                if (boolean_save) {
                    TakeImage_.intent(this).fileName(filename).start();

                } else {
                    if (boolean_permission) {
                        Bitmap bitmap1 = loadBitmapFromView(mRlBase, mRlBase.getWidth(), mRlBase.getHeight());
                        saveBitmap(bitmap1);
                    } else {
                    }
                }
                break;
            case R.id.mTvHide:
                mRlBase.setVisibility(View.VISIBLE);
                mTvHide.setVisibility(View.GONE);
                break;
            case R.id.mImgCancelDialog:
                mRlBase.setVisibility(View.GONE);
                mTvHide.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void saveBitmap(Bitmap bitmap) {

        File imagePath = new File("/sdcard/" + filename + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(getApplicationContext(), imagePath.getAbsolutePath() + "", Toast.LENGTH_SHORT).show();
            boolean_save = true;

            mTvSubmit.setText("Check image");

            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BaseImageActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BaseImageActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BaseImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(BaseImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                boolean_permission = true;
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    public int Random() {
        //tong tu 10 den 19
        Random rand = new Random();
        int num = rand.nextInt(10000000);
        return num;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        displayLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLocation != null) {
            final double latitude = mLocation.getLatitude();
            final double longitude = mLocation.getLongitude();

            mLatLng = new LatLng(latitude, longitude);
            if (mCurrent != null)
                mCurrent.remove();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));

        }
    }

    private void buidGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
}
