package suska.uin.tif.smartlivingcost.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import suska.uin.tif.smartlivingcost.Data.ResponseUserReview;
import suska.uin.tif.smartlivingcost.Fragment.DBSLC;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

public class NavigateMe extends AppCompatActivity implements LocationListener {
    private DBSLC MyDatabase;
    MapView map = null;
    IMapController mapController;
    SessionManager sessionManager;
    MyLocationNewOverlay mLocationOverlay;
    CompassOverlay mCompassOverlay;
    TextView tv_nama_resto, tv_nama_jalan_start, tv_jarak;
    String nama_jalan_start, total_jarak, getNamaResto, getNamaMenu, getIdMenu;
    private LocationManager lm;
    GeoPoint endPoint, curLoc;
    int getHargaMenu;
    BaseApiService mApiService;
    Call<ResponseUserReview> call;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_navigate_me);
        final Toolbar toolbar = findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        MyDatabase = new DBSLC(getBaseContext());


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sessionManager = new SessionManager(NavigateMe.this);
        mApiService = UtilsApi.getAPIService();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        Bundle b = new Bundle();
        b = getIntent().getExtras();
        double getLat = b.getDouble("lat");
        double getLng = b.getDouble("lng");
        getNamaResto = b.getString("nama_resto");
        getHargaMenu = b.getInt("harga_menu");
        getNamaMenu = b.getString("nama_menu");
        getIdMenu = b.getString("id_menu");


        tv_nama_resto = findViewById(R.id.nama_resto);
        tv_nama_jalan_start = findViewById(R.id.nama_jalan_start);
        tv_jarak = findViewById(R.id.jarak);
        tv_nama_resto.setText(getNamaResto);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(Double.parseDouble(sessionManager.getLat()), Double.parseDouble(sessionManager.getLng()));
        mapController.animateTo(startPoint);
        map.setBuiltInZoomControls(false);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Start point");
        map.getOverlays().add(startMarker);

        RoadManager roadManager = new GraphHopperRoadManager("251f1cab-fc55-48e8-837d-6314f0a1d1c0", true);
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(startPoint);
        endPoint = new GeoPoint(getLat, getLng);
        waypoints.add(endPoint);


        Marker endMarker = new Marker(map);
        endMarker.setPosition(endPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
        endMarker.setTitle("End Point");
        map.getOverlays().add(endMarker);

        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);
        DecimalFormat df2 = new DecimalFormat("####0.00");
        if (road.mLength < 1) {
            total_jarak = df2.format(road.mLength * 1000) + " m";
        } else {
            total_jarak = df2.format(road.mLength) + " km";
        }


        Drawable icon = getResources().getDrawable(R.drawable.ic_node_arrow);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.ic_node_arrow);
        for (int i = 0; i < road.mNodes.size(); i++) {
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(map);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setSnippet(node.mInstructions);
            if (i == 0) {
                nama_jalan_start = node.mInstructions;
            }
            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
            nodeMarker.setImage(icon);
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setTitle("Step " + i);
            map.getOverlays().add(nodeMarker);
        }

        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.setDrawAccuracyEnabled(true);
        map.getOverlays().add(mLocationOverlay);
        this.mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);

        tv_nama_jalan_start.setText(nama_jalan_start.substring(nama_jalan_start.lastIndexOf("onto ") + 5));
        tv_jarak.setText(total_jarak);


    }

    private void buildAlertMessageNoGps() {
        new SweetAlertDialog(NavigateMe.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Memerlukan GPS")
                .setContentText("Hidupkan Sekarang?")
                .setConfirmText("Ya")
                .setCancelText("Lainkali")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(NavigateMe.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Koneksi GPS")
                                .setContentText("Aplikasi Memerlukan GPS")
                                .show();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rekom, menu);
        MenuItem recenter = menu.findItem(R.id.recenter);
        recenter.setTitle("Lokasi Saya");
        MenuItem setting = menu.findItem(R.id.setting_rekom);
        setting.setVisible(false);
        MenuItem but = menu.findItem(R.id.buy_rekom);
        but.setVisible(true);
        Drawable yourdrawable = menu.getItem(2).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.recenter:
                mapController.animateTo(mLocationOverlay.getMyLocation());
                return true;
            case R.id.buy_rekom:
                alertBuy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        map.onResume();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);
        } catch (Exception ignored) {
        }

        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        curLoc = new GeoPoint(location.getLatitude(), location.getLongitude());
        if (curLoc.distanceToAsDouble(endPoint) < 20) {
            alertBuy();
        }
    }

    private void alertBuy() {
        lm.removeUpdates(this);
        new SweetAlertDialog(NavigateMe.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sampai ke Restoran")
                .setContentText("Masukkan Dalam Catatan Transaksi?")
                .setConfirmText("Ya")
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        saveData();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })
                .show();
    }

    private void saveData() {
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String set_tgl_input = formatter.format(todayDate);
        @SuppressLint("SimpleDateFormat") String set_jam_input = new SimpleDateFormat("HH:mm:ss").format(todayDate);
        SQLiteDatabase create = MyDatabase.getWritableDatabase();

        //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
        ContentValues values = new ContentValues();
        values.put(DBSLC.Transaksi.IdKat, Integer.parseInt(sessionManager.getId_kat_recom()));
        values.put(DBSLC.Transaksi.TglTrans, set_tgl_input);
        values.put(DBSLC.Transaksi.JumlahTrans, -1 * getHargaMenu);
        values.put(DBSLC.Transaksi.KetTrans, "Makan " + getNamaMenu + " di " + getNamaResto);
        values.put(DBSLC.Transaksi.GbrTrans, "");
        values.put(DBSLC.Transaksi.TglInputTrans, set_tgl_input);
        values.put(DBSLC.Transaksi.JamInputTrans, set_jam_input);


        //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
        create.insert(DBSLC.Transaksi.NamaTabelTrans, null, values);

        ContentValues values1 = new ContentValues();
        values1.put(DBSLC.Review.IdMenu, Integer.parseInt(getIdMenu));
        values1.put(DBSLC.Review.Reviewed, 0);
        values1.put(DBSLC.Review.Reported, 0);
        values1.put(DBSLC.Review.NamaMenu, getNamaMenu);
        values1.put(DBSLC.Review.NamaResto, getNamaResto);
        values1.put(DBSLC.Review.Tgl, set_tgl_input);

        create.insert(DBSLC.Review.NamaTabelReview, null, values1);

        new SweetAlertDialog(NavigateMe.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Berhasil")
                .setContentText("Transaksi Tersimpan")
                .show();
        Intent i = new Intent(NavigateMe.this, MainActivity.class);
        i.putExtra("frgToLoad", "Transaksi");
        startActivity(i);
        NavigateMe.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();

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
}
