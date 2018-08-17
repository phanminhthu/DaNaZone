package com.example.danazone04.danazone.ui.splash.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.danazone04.danazone.BaseActivity;
import com.example.danazone04.danazone.R;
import com.example.danazone04.danazone.SessionManager;
import com.example.danazone04.danazone.common.BaseImageActivity_;
import com.example.danazone04.danazone.common.Common;
import com.example.danazone04.danazone.common.GGApi;
import com.example.danazone04.danazone.dialog.DialogCheckin;
import com.example.danazone04.danazone.dialog.EndDialog;
import com.example.danazone04.danazone.dialog.FinishDialog;
import com.example.danazone04.danazone.dialog.ShareDialog;
import com.example.danazone04.danazone.dialog.StartDialog;
import com.example.danazone04.danazone.remote.IGoogleApi;
import com.example.danazone04.danazone.ui.splash.login.LoginActivity_;
import com.example.danazone04.danazone.ui.splash.main.setting.SettingActivity_;
import com.example.danazone04.danazone.ui.splash.register.RegisterActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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
import com.google.android.gms.maps.model.SquareCap;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.internal.Utils;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Runnable, android.location.LocationListener {
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

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 30000;
    protected LocationManager locationManager;
    static double n = 0;
    private Long s1, r1;
    double plat, plon, clat, clon, dis;
    MyCount counter;
    Thread t1;
    boolean bool = true;
    Location location;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;


    @ViewById
    LinearLayout mLnStart;
    @ViewById
    RelativeLayout mLnEnd;
    @ViewById
    RelativeLayout mTvSetting;
    @ViewById
    ImageView mImgStart;
    @ViewById
    ImageView mImgEnd;
    @ViewById
    TextView mImgText;
    @ViewById
    RelativeLayout mRlmView;
    @ViewById
    TextView mTvDistance;
    @ViewById
    TextView mTvSpeed;
    @ViewById
    TextView mTvTime;
    @ViewById
    TextView mTvCalo;
    @ViewById
    TextView mTvPause;
    @ViewById
    TextView mTvResum;

    Bitmap bitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE_END = 101;
    public static final String image = "image";
    public static final String imageName = "name";
    private String distanceValue;
    private String mLats, mLngs, mLate, mLnge;

    long lStartTime, lPauseTime, lSystemTime = 0L;
    Handler handler = new Handler();
    boolean isRun;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            lSystemTime = SystemClock.uptimeMillis() - lStartTime;
            long lUpdateTime = lPauseTime + lSystemTime;
            long secs = (long) (lUpdateTime / 1000);
            long mins = secs / 60;
            secs = secs % 60;
            mTvTime.setText("" + mins + ":" + String.format("%02d", secs));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void afterView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Animation vibrateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
        mImgStart.startAnimation(vibrateAnimation);
        mImgEnd.startAnimation(vibrateAnimation);

        LocationAvailable = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }

        //setUpLocation();
        showCurrentLocation();
        buidGoogleApiClient();
        createLocationRequest();
        displayLocation();

    }

    protected void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            clat = location.getLatitude();
            clon = location.getLongitude();
            Toast.makeText(MainActivity.this, message,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "null location",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Click({R.id.mLnStart, R.id.mLnEnd, R.id.mTvSetting, R.id.mTvPause, R.id.mTvResum})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvPause:
                counter.cancel();
                bool = false;

                if(!isRun)
                    return;
                isRun = false;
                lPauseTime += lSystemTime;
                handler.removeCallbacks(runnable);

                break;
            case R.id.mTvResum:
                counter = new MyCount(s1, 1000);
                counter.start();
                bool = true;

                if(isRun)
                    return;
                isRun = true;
                lStartTime = lPauseTime;
                handler.postDelayed(runnable, 0);

                break;
            case R.id.mTvSetting:
                SettingActivity_.intent(MainActivity.this).start();
                break;

            case R.id.mLnStart:
                new StartDialog(MainActivity.this, new StartDialog.OnDialogClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCallSerVice() {
                        if (isRun)
                            return;
                        isRun = true;
                        lStartTime = SystemClock.uptimeMillis();
                        handler.postDelayed(runnable, 0);

                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            dispatchTakenPictureIntent();
                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                Toast.makeText(getApplicationContext(), "Permission NeededSSSSSS.", Toast.LENGTH_LONG).show();
                            }
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                        }
                        // BaseImageActivity_.intent(MainActivity.this).start();

                    }
                }).show();
                break;

            case R.id.mLnEnd:
//                if (!isRun)
//                    return;
//                isRun = false;
//                lPauseTime = 0;
//                handler.removeCallbacks(runnable);


                new EndDialog(MainActivity.this, new EndDialog.OnDialogClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCallSerVice() {
                        if (!isRun)
                            return;
                        isRun = false;
                        lPauseTime = 0;
                        handler.removeCallbacks(runnable);

                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takenImageEnd();
                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                Toast.makeText(getApplicationContext(), "Permission NeededSSSSSS.", Toast.LENGTH_LONG).show();
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
                            // start();
                            t1 = new Thread();
                            t1.start();
                            counter = new MyCount(30000, 1000);
                            counter.start();

//                            double time = n * 30 + r1;
//                            Toast.makeText(MainActivity.this, "distance in metres:" + String.valueOf(dis) + "Velocity in m/sec :" + String.valueOf(dis / time) + "Time :" + String.valueOf(time), Toast.LENGTH_LONG).show();
//
//                            mTvCalo.setText("11111");
//                            mTvDistance.setText(String.valueOf(dis));
//                            mTvSpeed.setText(String.valueOf(dis / time));

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
                        SessionManager.getInstance().setKeySaveImageEnd(bm);
                        BaseImageActivity_.intent(MainActivity.this)
                                .mStart(bitmap)
                                .mEnd(bm)
                                .mTime(mTvTime.getText().toString())
                                .mKM(mTvDistance.getText().toString())
                                .mSpeed(mImgText.getText().toString()).start();
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

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /**
                 * We are good, turn on monitoring
                 */
                if (checkPermission()) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
                } else {
                    requestPermission();
                }
            } else {
                /**
                 * No permissions, block out all activities that require a location to function
                 */
                Toast.makeText(this, "Permission Not Granted.", Toast.LENGTH_LONG).show();
            }
        }
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
        mLocation = location;
        displayLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (checkPermission()) {
            locationManager.removeUpdates(this);
        } else {
            requestPermission();
        }
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

    @Override
    public void run() {
        while (bool) {
            clat = location.getLatitude();
            clon = location.getLongitude();
            if (clat != plat || clon != plon) {
                dis += getDistance(plat, plon, clat, clon);
                plat = clat;
                plon = clon;

            }

        }
    }

    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            counter = new MyCount(30000, 1000);
            counter.start();
            n = n + 1;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            s1 = millisUntilFinished;
            r1 = (30000 - s1) / 1000;
            //e1.setText(String.valueOf(r1));
            double time = n * 30 + r1;

            mTvCalo.setText("11111");
            mTvDistance.setText(String.valueOf(dis));
            mTvSpeed.setText(String.valueOf(dis / time));


        }
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lon1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lon2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) +
                (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        double dist = ang * 6371;
        return dist;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            LocationAvailable = true;
            return true;
        } else {
            LocationAvailable = false;
            return false;
        }
    }

    /**
     * Request permissions from the user
     */
    private void requestPermission() {

        /**
         * Previous denials will warrant a rationale for the user to help convince them.
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
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