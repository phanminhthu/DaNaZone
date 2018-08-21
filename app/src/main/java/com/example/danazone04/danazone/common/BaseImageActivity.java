package com.example.danazone04.danazone.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.example.danazone04.danazone.remote.IGoogleApi;
import com.example.danazone04.danazone.ui.splash.TakeImage_;
import com.example.danazone04.danazone.utils.ConnectionUtil;
import com.google.android.gms.common.ConnectionResult;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressLint("Registered")
@EActivity(R.layout.activity_base_iage)
public class BaseImageActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @Extra
    String mStart;
    @Extra
    String mEnd;
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
    @Extra
    String mLats;
    @Extra
    String mLngs;
    @Extra
    String mLate;
    @Extra
    String mLnge;

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
    private JSONObject jsonObject;

    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyline;
    private IGoogleApi mService;
    private LatLng currentPosition;
    private List<LatLng> polyLineList;
    private Uri mUriStart, mUriEnd;
    private Bitmap bmStart, bmEnd;


    @Override
    protected void afterView() {
        getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(BaseImageActivity.this);
        buidGoogleApiClient();
        createLocationRequest();
        displayLocation();

        if (ConnectionUtil.isConnected(this)) {
            mService = GGApi.getGoogleAPI();
            getDirection();
        }

        key = key + 1;
        if (!SessionManager.getInstance().getKeySaveCoin().equals("")) {
            SessionManager.getInstance().updateCoin(String.valueOf(Integer.valueOf(SessionManager.getInstance().getKeySaveCoin()) + 1));
        } else {
            SessionManager.getInstance().setKeySaveCoin(String.valueOf(key));
        }
        mUriStart = Uri.parse(mStart);
        mUriEnd = Uri.parse(mEnd);

        try {
            bmStart = MediaStore.Images.Media.getBitmap(getContentResolver(), mUriStart);
            bmEnd = MediaStore.Images.Media.getBitmap(getContentResolver(), mUriEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImgStart.setImageBitmap(bmStart);
        mImgEnd.setImageBitmap(bmEnd);
        mTvTime.setText(mTime);
        if (mSpeed == null) {
            mTvSpeed.setText("0 KM/H");

        }else{
            mTvSpeed.setText(mSpeed);
        }

        if (mKM == null) {
            mTvKM.setText("0.0 KM");
        } else {
            mTvKM.setText(mKM);
        }

        mTvTimeStart.setText(mTimeStart + "h");
        mTvTimeEnd.setText(mTimeEnd + "h");
        mTvData.setText(mDate);
        mTvMaxSpeed.setText(mMaxSpeed);
        mTvCalo.setText(mCalo);

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

        File imagePath = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/" + filename + ".jpg");

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            boolean_save = true;

            mTvSubmit.setText("Check image");

            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{imagePath.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
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
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_blue))
                    );

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        // getInformationService(mLats, mLngs, mLate, mLnge);
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

    private void start() {
        if (mLats != null && mLngs != null) {
            mLatLng = new LatLng(Double.valueOf(mLats), Double.valueOf(mLngs));
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    .position(new LatLng(Double.valueOf(mLats), Double.valueOf(mLngs))));
        }
    }
}
