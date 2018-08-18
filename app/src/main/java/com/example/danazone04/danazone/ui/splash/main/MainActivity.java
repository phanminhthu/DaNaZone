package com.example.danazone04.danazone.ui.splash.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.common.BaseImageActivity_;
import com.example.danazone04.danazone.dialog.DialogCheckin;
import com.example.danazone04.danazone.dialog.EndDialog;
import com.example.danazone04.danazone.dialog.FinishDialog;
import com.example.danazone04.danazone.dialog.GpsDialog;
import com.example.danazone04.danazone.dialog.ShareDialog;
import com.example.danazone04.danazone.dialog.StartDialog;
import com.example.danazone04.danazone.remote.IGoogleApi;
import com.example.danazone04.danazone.speed.Data;
import com.example.danazone04.danazone.speed.GpsServices;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.metter.MetterActivity_;
import com.example.danazone04.danazone.ui.splash.main.setting.SettingActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity;
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
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyline;
    private IGoogleApi mService;
    private LatLng currentPosition;
    private List<LatLng> polyLineList;

    @ViewById
    RelativeLayout mTvSetting;
    @ViewById
    ImageView mImgStart;
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

    private LocationManager mLocationManager;
    private Bitmap bitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE_END = 101;
    public static final String image = "image";
    public static final String imageName = "name";
    private String distanceValue;
    private String mLats, mLngs, mLate, mLnge;

    private Data.onGpsServiceUpdate onGpsServiceUpdate;
    private SharedPreferences sharedPreferences;
    private static Data data;

    private boolean firstfix;
    private AlertDialog waitingDialog;

    private boolean LocationAvailable;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String date;
    private String time, timeEnd, numTime;
    private String speed, maxSpeed, minSpeed, distance, calo, ms, ms1, ms2, ms3;


    @Override
    protected void afterView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        printKeyHash();
        data = new Data(onGpsServiceUpdate);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mRlPause.setVisibility(View.INVISIBLE);
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

                SpannableString s1 = new SpannableString(String.format("%.0f", averageTemp) + speedUnits);
                s1.setSpan(new RelativeSizeSpan(0.5f), s1.length() - 4, s1.length(), 0);
                ms1 = String.valueOf(s1);
                mTvMinxSpeed.setText(s);

                double calo = averageTemp / 20.0;

                mTvCalo.setText(String.valueOf(calo) + " calo");

                SpannableString s2 = new SpannableString(String.format("%.3f", distanceTemp) + distanceUnits);
                s2.setSpan(new RelativeSizeSpan(0.5f), s2.length() - 2, s2.length(), 0);
                ms2 = String.valueOf(s2);
                mTvDistance.setText(s2);


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

    @Click({R.id.mImgStart, R.id.mImgStop, R.id.mTvSetting, R.id.mRlPause})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mRlPause:
                setUpDate();
                setUpTime();
                if (!data.isRunning()) {
                    mRlPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_ic));

                    data.setRunning(true);
                    mTvTime.setBase(SystemClock.elapsedRealtime() - data.getTime());
                    mTvTime.start();
                    data.setFirstTime(true);
                    startService(new Intent(getBaseContext(), GpsServices.class));
                    // refresh.setVisibility(View.INVISIBLE);
                } else {
                    mRlPause.setImageDrawable(getResources().getDrawable(R.drawable.play_ic));
                    data.setRunning(false);
                    stopService(new Intent(getBaseContext(), GpsServices.class));
                    // refresh.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.mTvSetting:
                SettingActivity_.intent(MainActivity.this).start();
                break;


            case R.id.mImgStart:
                new StartDialog(MainActivity.this, new StartDialog.OnDialogClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCallSerVice() {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            dispatchTakenPictureIntent();
                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            }
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                        }
                        // BaseImageActivity_.intent(MainActivity.this).start();

                    }
                }).show();
                break;

            case R.id.mImgStop:
                setUpTimeEnd();
                data.setRunning(false);
                stopService(new Intent(getBaseContext(), GpsServices.class));
                numTime = mTvTime.getText().toString();
                System.out.println("222222222222222222222222: " + mTvTime.getText().toString());
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
        }
    }

    private void dispatchTakenPictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
        }
    }

    private void takenImageEnd() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE_END);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == MY_CAMERA_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                final String a = encodeTobase64(bitmap);
                if (bitmap != null) {
                    new DialogCheckin(MainActivity.this, bitmap, new DialogCheckin.OnDialogClickListener() {
                        @Override
                        public void onCallSerVice() {
                            SessionManager.getInstance().setKeyImageStart(bitmap);
                            start();
                            mImgStart.setVisibility(View.GONE);
                            mLnStop.setVisibility(View.VISIBLE);
                            waitingDialog = new SpotsDialog(MainActivity.this);
                            waitingDialog.show();
                        }
                    }).show();
                }
            }

            if (requestCode == MY_CAMERA_REQUEST_CODE_END) {
                Bundle bundle = data.getExtras();
                final Bitmap bm = (Bitmap) bundle.get("data");
                String a = encodeTobase64(bm);

                new DialogCheckin(MainActivity.this, bm, new DialogCheckin.OnDialogClickListener() {
                    @Override
                    public void onCallSerVice() {
                        mImgStart.setVisibility(View.VISIBLE);
                        mLnStop.setVisibility(View.GONE);
                        SessionManager.getInstance().setKeySaveImageEnd(bm);
                        resetData();

                        BaseImageActivity_.intent(MainActivity.this)
                                .mStart(bitmap)
                                .mEnd(bm)
                                .mTime(numTime)
                                .mKM(ms2)
                                .mSpeed(ms1)
                                .mMaxSpeed(ms)
                                .mCalo("")
                                .mTimeStart(time)
                                .mTimeEnd(timeEnd)
                                .mDate(date)
                                .mLats(mLats)
                                .mLngs(mLngs)
                                .mLate(mLate)
                                .mLnge(mLnge)
                                .start();
                    }
                }).show();
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
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                /**
//                 * We are good, turn on monitoring
//                 */
//                if (checkPermission()) {
//                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
//                } else {
//                    requestPermission();
//                }
//            } else {
//                /**
//                 * No permissions, block out all activities that require a location to function
//                 */
//                Toast.makeText(this, "Permission Not Granted.", Toast.LENGTH_LONG).show();
//            }
        //  }
    }


    private void start() {

        Toast.makeText(this, "bat dau", Toast.LENGTH_SHORT).show();
        final double latitud = mLocation.getLatitude();
        final double longitud = mLocation.getLongitude();

        mLats = String.valueOf(latitud);
        mLngs = String.valueOf(latitud);

        System.out.println("22222222222222" + mLats + "---- " + mLngs);


        mLatLng = new LatLng(latitud, longitud);
        mCurrent = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_blue))
                .position(new LatLng(latitud, longitud)).title("bat dau"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 14.0f));

    }

    private void end() {
        Toast.makeText(this, "ket thuc", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        final double latitu = location.getLatitude();
        final double longitu = location.getLongitude();

        mLate = String.valueOf(latitu);
        mLnge = String.valueOf(latitu);
        System.out.println("22222222222222333333" + mLate + "---- " + mLnge);
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
            //  accuracy.setText(s);

            if (firstfix) {
                // mTvPause.setText("");
                mRlPause.setVisibility(View.VISIBLE);
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

//    /**
//     * Request permissions from the user
//     */
//    private void requestPermission() {
//
//        /**
//         * Previous denials will warrant a rationale for the user to help convince them.
//         */
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            Toast.makeText(this, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//            LocationAvailable = true;
//            return true;
//        } else {
//            LocationAvailable = false;
//            return false;
//        }
//    }

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


    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
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
                //satellite.setText(String.valueOf(satsUsed) + "/" + String.valueOf(satsInView));
                if (satsUsed == 0) {
                    mRlPause.setImageDrawable(getResources().getDrawable(R.drawable.play_ic));
                    data.setRunning(false);
                    //mTvPause.setText("");
                    stopService(new Intent(getBaseContext(), GpsServices.class));
                    mRlPause.setVisibility(View.INVISIBLE);
                    //mTvPause.setText("GPS");
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
        mTvMaxSpeed.setText("");
        mTvMinxSpeed.setText("");
        mTvDistance.setText("");
        mTvTime.setText("00:00:00");
        data = new Data(onGpsServiceUpdate);
    }

    public static Data getData() {
        return data;
    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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

    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.danazone04.danazone",
                    getPackageManager().GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest ms = null;
                try {
                    ms = MessageDigest.getInstance("SHA");
                    System.out.println("111111111111111: " + Base64.encodeToString(ms.digest(), Base64.DEFAULT));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                ms.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}