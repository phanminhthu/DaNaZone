package com.example.danazone04.danazone.ui.splash.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.dialog.DialogCheckin;
import com.example.danazone04.danazone.dialog.EndDialog;
import com.example.danazone04.danazone.dialog.FinishDialog;
import com.example.danazone04.danazone.dialog.GpsDialog;
import com.example.danazone04.danazone.speed.Data;
import com.example.danazone04.danazone.speed.GpsServices;
import com.example.danazone04.danazone.ui.splash.main.base.BaseImageActivity_;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GpsStatus.Listener {
    private GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_CODE = 7000;
    private static final int PLAY_SERVICE_RES_REQUEST = 7001;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrent;
    private LatLng mLatLng;
    private Location mLocation = null;

    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private JSONObject jsonObject;


    @ViewById
    RelativeLayout mTvSetting;
    @ViewById
    TextView mImgText;

    @ViewById
    TextView mTvDistance;
    @ViewById
    TextView mTvSpeed;
    @ViewById
    Chronometer mTvTime;
    @ViewById
    TextView mTvCalo;
    @ViewById
    ImageView mRlPause;
    @ViewById
    TextView mTvMaxSpeed;
    @ViewById
    TextView mTvMinxSpeed;
    @ViewById
    ImageView mImgStop;
    @ViewById
    LinearLayout mLnStop;
    @ViewById
    ImageView mImgPlay;
    @ViewById
    ImageView mImgStops;
    @ViewById
    ImageView mImgLock;
    @ViewById
    ImageView mImgUnlocked;
    @ViewById
    ImageView mImgLocked;
    @Extra
    String mBitmapStart;
    @Extra
    String mLat;
    @Extra
    String mLng;
    @Extra
    String mSL;

    private LocationManager mLocationManager;
    private Bitmap bitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE_END = 101;
    public static final String image = "image";
    private String mLate, mLnge;

    private Data.onGpsServiceUpdate onGpsServiceUpdate;
    private SharedPreferences sharedPreferences;
    private static Data data;

    private boolean firstfix;
    private ContentValues values;
    private Uri imageUri;
    private String sl;


    private String date;
    private String time, timeEnd, numTime;
    private String ms, ms1, ms2, ms3;
    private double calos;
    private AlertDialog waitingDialog;
    boolean mLocked;

    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        data = new Data(onGpsServiceUpdate);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        waitingDialog = new SpotsDialog(MainActivity.this);
        //waitingDialog.show();
        updateService();
        buidGoogleApiClient();
        createLocationRequest();
        displayLocation();
    }

    private void updateService() {
        onGpsServiceUpdate = new Data.onGpsServiceUpdate() {
            @Override
            public void update() {
                double maxSpeedTemp = data.getMaxSpeed();
                double distanceTemp = data.getDistance();
                double averageTemp;
                if (sharedPreferences.getBoolean("auto_average", false)) {
                    averageTemp = data.getAverageSpeedMotion();
                } else {
                    averageTemp = data.getAverageSpeed();
                }

                String speedUnits;
                String distanceUnits;
                if (sharedPreferences.getBoolean("miles_per_hour", false)) {
                    maxSpeedTemp *= 0.62137119;
                    distanceTemp = distanceTemp / 1000.0 * 0.62137119;
                    averageTemp *= 0.62137119;
                    speedUnits = "mi/h";
                    distanceUnits = "mi";
                } else {
                    speedUnits = "km/h";
                    if (distanceTemp <= 1000.0) {
                        distanceUnits = "m";
                    } else {
                        distanceTemp /= 1000.0;
                        distanceUnits = "km";
                    }
                }
                SpannableString s = new SpannableString(String.format("%.0f", maxSpeedTemp) + speedUnits);
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);

                ms = String.valueOf(s);

                mTvMaxSpeed.setText(s);

                s = new SpannableString(String.format("%.0f", averageTemp) + speedUnits);
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);
                ms1 = String.valueOf(s);
                mTvMinxSpeed.setText(s);

                DecimalFormat dcf = new DecimalFormat("#.###");
                double a = averageTemp / 20.0;
//                if (a != 0.00) {
//                    calos = Math.ceil(a * 1000 / 1000);
//                } else {
//                    mTvCalo.setText("0.00");
//                }

                mTvCalo.setText(String.valueOf(dcf.format(a)));

                s = new SpannableString(String.format("%.3f", distanceTemp) + distanceUnits);
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 2, s.length(), 0);
                ms2 = String.valueOf(s);
                mTvDistance.setText(s);


            }
        };

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        if (checkPermission()) {
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
//        } else {
//            requestPermission();
//        }
        mTvTime.setText("00:00:00");
        mTvTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean isPair = true;

            @Override
            public void onChronometerTick(Chronometer chrono) {
                long time;
                if (data.isRunning()) {
                    time = SystemClock.elapsedRealtime() - chrono.getBase();
                    data.setTime(time);
                } else {
                    time = data.getTime();
                }

                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chrono.setText(hh + ":" + mm + ":" + ss);

                if (data.isRunning()) {
                    chrono.setText(hh + ":" + mm + ":" + ss);
                } else {
                    if (isPair) {
                        isPair = false;
                        chrono.setText(hh + ":" + mm + ":" + ss);
                    } else {
                        isPair = true;
                        chrono.setText("");
                    }
                }

            }
        });

    }

    @Click({R.id.mImgStops, R.id.mTvSetting, R.id.mImgPlay, R.id.mImgLock})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mImgPlay:
                setUpDate();
                setUpTime();
                if (!data.isRunning()) {
                    mImgPlay.setImageDrawable(getResources().getDrawable(R.drawable.new_pause));
                    data.setRunning(true);
                    mTvTime.setBase(SystemClock.elapsedRealtime() - data.getTime());
                    mTvTime.start();
                    data.setFirstTime(true);
                    startService(new Intent(getBaseContext(), GpsServices.class));

                } else {
                    mImgPlay.setImageDrawable(getResources().getDrawable(R.drawable.new_play));
                    data.setRunning(false);
                    stopService(new Intent(getBaseContext(), GpsServices.class));
                }
                break;

            case R.id.mTvSetting:
                String title = "Iridescent";

                Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                        MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
                intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, title);
                intent.putExtra(SearchManager.QUERY, title);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

                break;

            case R.id.mImgStops:
                setUpTimeEnd();
                data.setRunning(false);
                stopService(new Intent(getBaseContext(), GpsServices.class));
                numTime = mTvTime.getText().toString();

                new EndDialog(MainActivity.this, new EndDialog.OnDialogClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCallSerVice() {

                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takenImageEnd();
                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            }
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                        }

                        mCurrent = mMap.addMarker(new MarkerOptions().position(mLatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15.0f));
                        end();

                    }
                }).show();
                break;

            case R.id.mImgLock:

                System.out.println("6666666666666666666666: " + mLocked);
                if (!mLocked) {
                    mImgLocked.setVisibility(View.VISIBLE);
                    mImgUnlocked.setVisibility(View.GONE);
                    mImgPlay.setClickable(false);
                    mImgPlay.setEnabled(false);
                    mImgStops.setClickable(false);
                    mImgStops.setEnabled(false);
                    mLocked = true;
                } else {
                    mImgLocked.setVisibility(View.GONE);
                    mImgUnlocked.setVisibility(View.VISIBLE);
                    mImgPlay.setClickable(true);
                    mImgPlay.setEnabled(true);
                    mImgStops.setClickable(true);
                    mImgStops.setEnabled(true);
                    mLocked = false;
                }

                break;
        }
    }

    private void dispatchTakenPictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
        }
    }

    private void takenImageEnd() {

        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        sl = getRealPathFromURI(imageUri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, MY_CAMERA_REQUEST_CODE_END);

    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE_END:
                if (requestCode == MY_CAMERA_REQUEST_CODE_END)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            new DialogCheckin(MainActivity.this, bitmap,sl, new DialogCheckin.OnDialogClickListener() {
                                @Override
                                public void onCallSerVice() {
                                    resetData();
                                    BaseImageActivity_.intent(MainActivity.this)
                                            .mStart(mBitmapStart)
                                            .mEnd(String.valueOf(imageUri))
                                            .mTime(numTime)
                                            .mKM(ms2)
                                            .mSpeed(ms1)
                                            .mMaxSpeed(ms)
                                            .mCalo(String.valueOf(calos) + " calo")
                                            .mTimeStart(time)
                                            .mTimeEnd(timeEnd)
                                            .mDate(date)
                                            .mLats(mLat)
                                            .mLngs(mLng)
                                            .mLate(mLate)
                                            .mLnge(mLnge)
                                            .mUrlStart(mSL)
                                            .mUrlEnd(sl)
                                            .start();
                                    finish();
                                }
                            }).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakenPictureIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                dispatchTakenPictureIntent();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void end() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        final double latitu = location.getLatitude();
        final double longitu = location.getLongitude();

        mLate = String.valueOf(latitu);
        mLnge = String.valueOf(latitu);

        mLatLng = new LatLng(latitu, longitu);
        mCurrent = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .position(new LatLng(latitu, longitu))
                .title("ket thuc")
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitu, longitu), 14.0f));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        //startLocationUpdates();
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

        if (location.hasAccuracy()) {
            SpannableString s = new SpannableString(String.format("%.0f", location.getAccuracy()) + "m");
            s.setSpan(new RelativeSizeSpan(0.75f), s.length() - 1, s.length(), 0);

            if (firstfix) {
                //mRlPause.setVisibility(View.VISIBLE);
                if (!data.isRunning()) {

                }
                firstfix = false;
            }
        } else {
            firstfix = true;
        }

        if (location.hasSpeed()) {
            waitingDialog.dismiss();
            String speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6) + "km/h";

            if (sharedPreferences.getBoolean("miles_per_hour", false)) { // Convert to MPH
                speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6 * 0.62137119) + "mi/h";
            }
            SpannableString s = new SpannableString(speed);
            s.setSpan(new RelativeSizeSpan(0.25f), s.length() - 4, s.length(), 0);
            ms3 = String.valueOf(s);
            mTvSpeed.setText(s);

        }
        mLocation = location;
        displayLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);

        }

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

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICE_RES_REQUEST).show();
            else {
                //Toast.makeText(this, getResources().getString(R.string.text_app_name), Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
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


    @Override
    protected void onResume() {
        super.onResume();
        displayLocation();

        firstfix = true;
        if (!data.isRunning()) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("data", "");
            data = gson.fromJson(json, Data.class);
        }
        if (data == null) {
            data = new Data(onGpsServiceUpdate);
        } else {
            data.setOnGpsServiceUpdate(onGpsServiceUpdate);
        }

        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        } else {
            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDisabledDialog();
        }

        mLocationManager.addGpsStatusListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("data", json);
        prefsEditor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetData();
        stopService(new Intent(getBaseContext(), GpsServices.class));
    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                int satsInView = 0;
                int satsUsed = 0;
                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                for (GpsSatellite sat : sats) {
                    satsInView++;
                    if (sat.usedInFix()) {
                        satsUsed++;
                    }
                }

                if (satsUsed == 0) {
                    mImgPlay.setImageDrawable(getResources().getDrawable(R.drawable.new_play));
                    data.setRunning(false);

                    stopService(new Intent(getBaseContext(), GpsServices.class));
                    firstfix = true;
                }
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showGpsDisabledDialog();
                }
                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
        }
    }

    public void showGpsDisabledDialog() {
        new GpsDialog(MainActivity.this, new GpsDialog.OnDialogClickListener() {
            @Override
            public void onCallSerVice() {
                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        }).show();
    }

    public void resetData() {
        mTvTime.stop();
        mTvMaxSpeed.setText("0");
        mTvMinxSpeed.setText("0");
        mTvDistance.setText("0");
        mTvTime.setText("00:00");
        mTvSpeed.setText("0");
        mTvCalo.setText("0");
        data = new Data(onGpsServiceUpdate);
    }

    public static Data getData() {
        return data;
    }


    /**
     * setup date
     * dd/MM/yyyy
     */
    private void setUpDate() {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
    }

    private void setUpTime() {
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        time = simpleDateFormat.format(new Date());
    }

    private void setUpTimeEnd() {
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        timeEnd = simpleDateFormat.format(new Date());
    }
}