package com.example.danazone04.danazone.ui.splash.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
        LocationListener, GoogleMap.OnInfoWindowClickListener {
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
    LinearLayout mLnStart;
    @ViewById
    LinearLayout mLnEnd;
    @ViewById
    RelativeLayout mTvSetting;
    @ViewById
    ImageView mImgStart;
    @ViewById
    ImageView mImgEnd;
    @ViewById
    TextView mImgText;

    Bitmap bitmap;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_CAMERA_REQUEST_CODE_END = 101;
    public static final String image = "image";
    public static final String imageName = "name";
    private String distanceValue;
    private String mLats, mLngs,  mLate, mLnge;

    long lStartTime, lPauseTime, lSystemTime = 0L;
    Handler handler = new Handler();
    boolean isRun;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            lSystemTime = SystemClock.uptimeMillis() - lStartTime;
            long lUpdateTime = lPauseTime + lSystemTime;
            long secs = (long)(lUpdateTime/1000);
            long mins= secs/60;
            secs = secs %60;
            mImgText.setText(""+mins+":" + String.format("%02d",secs));
            handler.postDelayed(this,0);
        }
    };

    @Override
    protected void afterView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Animation vibrateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate);
        mImgStart.startAnimation(vibrateAnimation);
        mImgEnd.startAnimation(vibrateAnimation);
        mService = GGApi.getGoogleAPI();
        setUpLocation();
        jsonObject = new JSONObject();
    }

    @Click({R.id.mLnStart, R.id.mLnEnd, R.id.mTvSetting})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvSetting:
                SettingActivity_.intent(MainActivity.this).start();
                break;

            case R.id.mLnStart:
                new StartDialog(MainActivity.this, new StartDialog.OnDialogClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCallSerVice() {
                        if(isRun)
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


                    }
                }).show();
                break;

            case R.id.mLnEnd:
                new EndDialog(MainActivity.this, new EndDialog.OnDialogClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCallSerVice() {
                        if(!isRun)
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


//                        mCurrent = mMap.addMarker(new MarkerOptions().position(mLatLng)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15.0f));
                        end();
                        getDirection();


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

    private void takenImageEnd(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) !=null){
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
                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.URL_UP_AVATAR, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.i("Myresponse",""+response);
                                    Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                    start();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("Mysmart",""+error);
                                    Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> param = new HashMap<>();


                                   // Log.i("Mynewsam",""+a);
                                    param.put("image",a);
                                    param.put("image1",a);
                                    return param;
                                }
                            };

                            requestQueue.add(stringRequest);
                        }
                    }).show();
                }
            }

            if(requestCode == MY_CAMERA_REQUEST_CODE_END){
                Bundle bundle = data.getExtras();
                final Bitmap bm = (Bitmap) bundle.get("data");

                String a = encodeTobase64(bm);

                new DialogCheckin(MainActivity.this,bm, new DialogCheckin.OnDialogClickListener() {
                    @Override
                    public void onCallSerVice() {
                        SessionManager.getInstance().setKeySaveImageEnd(bm);
                        BaseImageActivity_.intent(MainActivity.this)
                                .mStart(bitmap
                                ).mEnd(bm)
                                .mKM(distanceValue)
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
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);

        } else {
            if (checkPlayServices()) {
                buidGoogleApiClient();
                createLocationRequest();
                //  startLocationUpdates();
                displayLocation();
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buidGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
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


    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           // mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLocation != null) {
            final double latitude = mLocation.getLatitude();
            final double longitude = mLocation.getLongitude();
            System.out.println("111111111111111111111" + latitude + "----- " +longitude);
            mLatLng = new LatLng(latitude, longitude);

            //add marker
            if (mCurrent != null)
                mCurrent.remove();

            mCurrent = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    .position(new LatLng(latitude, longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));

        }
    }

    private void start(){
        mMap.clear();
        final double latitud = mLocation.getLatitude();
        final double longitud = mLocation.getLongitude();

        mLats = String.valueOf(latitud);
        mLngs = String.valueOf(latitud);

        mLatLng = new LatLng(latitud, longitud);
        mCurrent = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .position(new LatLng(latitud, longitud)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 14.0f));

    }

    private void end(){
        mMap.clear();
        final double latitu = mLocation.getLatitude();
        final double longitu = mLocation.getLongitude();

        mLate = String.valueOf(latitu);
        mLnge = String.valueOf(latitu);

        mLatLng = new LatLng(latitu, longitu);
        mCurrent = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .position(new LatLng(latitu, longitu)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitu, longitu), 14.0f));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
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
    public void onInfoWindowClick(Marker marker) {

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
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    private void getDirection() {
       // currentPosition = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        String requestApi = null;
        try {
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + mLats + "," + mLngs + "&"
                    + "destination=" + mLate + "," + mLnge + "&" +
                    "key+" + getResources().getString(R.string.google_direction_api);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mService.getPath(requestApi).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject route = jsonArray.getJSONObject(i);
                        JSONObject poly = route.getJSONObject("overview_polyline");
                        String polyline = poly.getString("points");
                        polyLineList = decodePoly(polyline);
                    }
                    // bound
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : polyLineList)
                        builder.include(latLng);
                    LatLngBounds bounds = builder.build();
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                    mMap.animateCamera(mCameraUpdate);

                    polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.GRAY);
                    polylineOptions.width(5);
                    polylineOptions.startCap(new SquareCap());
                    polylineOptions.endCap(new SquareCap());
                    polylineOptions.jointType(JointType.ROUND);
                    polylineOptions.addAll(polyLineList);
                    greyPolyline = mMap.addPolyline(polylineOptions);

                    blackPolylineOptions = new PolylineOptions();
                    blackPolylineOptions.color(Color.BLACK);
                    blackPolylineOptions.width(5);
                    blackPolylineOptions.startCap(new SquareCap());
                    blackPolylineOptions.endCap(new SquareCap());
                    blackPolylineOptions.jointType(JointType.ROUND);
                    blackPolylineOptions.addAll(polyLineList);
                    blackPolyline = mMap.addPolyline(blackPolylineOptions);

                    mLatLng = polyLineList.get(polyLineList.size() - 1);
                    mMap.addMarker(new MarkerOptions()
                            .position(polyLineList.get(polyLineList.size() - 1))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    );

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
       getInformationService(mLats,mLngs , mLate, mLnge );
    }

    /**
     *
     * @param mLats
     * @param mLngs
     * @param mLate
     * @param mLnge
     */
    private void getInformationService(String mLats, String mLngs, String mLate, String mLnge) {
        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + mLats + "," + mLngs + "&"
                    + "destination=" + mLate + "," + mLnge + "&" +
                    "key+" + getResources().getString(R.string.google_direction_api);
            mService.getPath(requestUrl).enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray router = jsonObject.getJSONArray("routes");

                        JSONObject object = router.getJSONObject(0);
                        JSONArray legs = object.getJSONArray("legs");

                        JSONObject legsObject = legs.getJSONObject(0);
                        JSONObject distance = legsObject.getJSONObject("distance");
                        String distance_text = distance.getString("text");

                        Double distance_value = Double.parseDouble(distance_text.replaceAll("[^0-9\\\\.]+", ""));
                        distanceValue = String.valueOf(distance_value);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
