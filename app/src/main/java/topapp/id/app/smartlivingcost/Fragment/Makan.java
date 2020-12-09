package topapp.id.app.smartlivingcost.Fragment;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Activity.Promo;
import topapp.id.app.smartlivingcost.Activity.RecomSetting;
import topapp.id.app.smartlivingcost.Activity.SaranAct;
import topapp.id.app.smartlivingcost.Adapter.RecomRVAdapter;
import topapp.id.app.smartlivingcost.Data.AllPromoItem;
import topapp.id.app.smartlivingcost.Data.AllRecomItem;
import topapp.id.app.smartlivingcost.Data.ResponsePromo;
import topapp.id.app.smartlivingcost.Data.ResponseRecom;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;


public class Makan extends Fragment {
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private BaseApiService mApiService;
    private SweetAlertDialog loading;
    private Marker marker;
    private double lng, lat;
    private int sisa;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private IMapController mapController;
    private SessionManager sessionManager;
    private LinearLayout tv_nodata;
    private LocationManager locationManager;
    private List<AllRecomItem> allRecomItems = new ArrayList<>();
    private List<AllPromoItem> allPromoItems = new ArrayList<>();
    private SlidingUpPanelLayout mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_makan, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi

        setHasOptionsMenu(true);
        mApiService = UtilsApi.getAPIService();
        Context ctx = getActivity().getApplicationContext();
        mLayout = rootView.findViewById(R.id.sliding_layout);

        sessionManager = new SessionManager(getActivity());
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.buttonHighlight));

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        DBSLC myDatabase = new DBSLC(getActivity().getBaseContext());


        recyclerView = rootView.findViewById(R.id.rv_resto);
        tv_nodata = rootView.findViewById(R.id.rl_nodata);
        map = rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        Calendar calendar1 = Calendar.getInstance();
        int lastDay = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = calendar1.get(Calendar.DAY_OF_MONTH);
        int daysLeft = (lastDay - currentDay) + 1;

        sisa = (myDatabase.gettotal("1", "0", "0") + myDatabase.gettotal("0", "0", "0")) / daysLeft;
        loading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mencari Menu yang Sesuai");
        loading.setCancelable(false);
        loading.show();

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.setDrawAccuracyEnabled(true);
        this.mLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    public void run() {
                        mapController.animateTo(mLocationOverlay.getMyLocation());
                        mLocationOverlay.isFollowLocationEnabled();
                        mapController.setZoom(18);
                        lat = mLocationOverlay.getLastFix().getLatitude();
                        lng = mLocationOverlay.getLastFix().getLongitude();
                        sessionManager.setLat(lat);
                        sessionManager.setLng(lng);
                        if (sisa < 8000) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Uang Tidak Cukup")
                                    .setContentText("Lihat Tips?")
                                    .setConfirmText("Baiklah")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            Intent i = new Intent(getActivity(), SaranAct.class);
//                                            i.putExtra("frgToLoad", "Input");
                                            i.putExtra("harian", sisa);
                                            getActivity().startActivity(i);
                                            getActivity().finish();
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            initData();
                        }

                    }
                });
            }
        });
        map.getOverlays().add(mLocationOverlay);
        CompassOverlay mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);
        map.setBuiltInZoomControls(false);
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        //play around with these values to get the location on screen in the right place for your application
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d("onPanelSlide, offset ", String.valueOf(slideOffset));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.d("onPanelStateChanged ", String.valueOf(newState));
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        return rootView;
    }


    private void initData() {
        Call<ResponseRecom> call = mApiService.recom("Bearer " + sessionManager.getToken(), lat, lng, sessionManager.getReq_jarak(), sessionManager.getReq_nama(), sessionManager.getReq_row(), String.valueOf(sisa));
        call.enqueue(new Callback<ResponseRecom>() {
            @Override
            public void onResponse(@NonNull Call<ResponseRecom> call, @NonNull Response<ResponseRecom> response) {
                if (response.isSuccessful()) {
                    loading.dismissWithAnimation();
                    allRecomItems = response.body().getGetAllRecom();
                    Log.d("hasil", String.valueOf(response.code()));
                    for (int i = 0; i < allRecomItems.size(); i++) {
                        marker = new Marker(map);
                        marker.setPosition(new GeoPoint(allRecomItems.get(i).getLat_resto().doubleValue(), allRecomItems.get(i).getLng_resto().doubleValue()));
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        marker.setIcon(getResources().getDrawable(R.drawable.ic_resto));
                        marker.setTitle(String.valueOf(allRecomItems.get(i).getNama_resto()));
                        marker.setId("coba");
                        map.getOverlays().add(marker);


                    }
                    map.invalidate();
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new RecomRVAdapter(allRecomItems);
                    ((RecomRVAdapter) adapter).setOnItemClickListener(new RecomRVAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int pos, View v) {
                            mapController.animateTo(new GeoPoint(allRecomItems.get(pos).getLat_resto().doubleValue(), allRecomItems.get(pos).getLng_resto().doubleValue()));
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            getPromo();
                        }
                    }, 2000);

                } else if (response.code() == 404) {
                    loading.dismissWithAnimation();
                    recyclerView.setVisibility(View.GONE);
                    tv_nodata.setVisibility(View.VISIBLE);
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Menu Berdasarkan Kriteria Anda Tidak Tersedia")
                            .show();
                } else {
                    loading.dismissWithAnimation();
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal")
                            .setContentText("Terjadi Kesalahan Teknis, Muat Ulang Halaman")
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseRecom> call, @NonNull Throwable t) {
                loading.dismissWithAnimation();
                new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Jaringan Bermasalah")
                        .setContentText("Periksa Kembali Jarigan Internet Anda")
                        .show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_rekom, menu);
        MenuItem my_loc = menu.findItem(R.id.recenter);
        MenuItem setting = menu.findItem(R.id.setting_rekom);

    }

    private void buildAlertMessageNoGps() {
        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.WARNING_TYPE)
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
                        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Koneksi GPS")
                                .setContentText("Aplikasi Memerlukan GPS")
                                .show();
                    }
                })
                .show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.recenter:
                loading = new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.PROGRESS_TYPE);
                loading.setTitleText("Mencari Menu yang Sesuai");
                loading.setCancelable(false);
                loading.show();
                clearRV();
                mapController.animateTo(mLocationOverlay.getMyLocation());
                lat = mLocationOverlay.getLastFix().getLatitude();
                lng = mLocationOverlay.getLastFix().getLongitude();
                sessionManager.setLat(lat);
                sessionManager.setLng(lng);
                initData();
                return true;
            case R.id.setting_rekom:
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), RecomSetting.class));
                getActivity().finish();
                return true;
        }
        return false;
    }

    private void clearRV() {
        allRecomItems.clear();
    }

    void getPromo() {
        Call<ResponsePromo> call2 = mApiService.getAllPromo("Bearer " + sessionManager.getToken(), lat, lng, sessionManager.getReq_jarak(), sessionManager.getReq_nama(), sessionManager.getReq_row(), String.valueOf(sisa));
        call2.enqueue(new Callback<ResponsePromo>() {
            @Override
            public void onResponse(@NonNull Call<ResponsePromo> call, @NonNull Response<ResponsePromo> response) {
                if (response.code()==200) {
                    allPromoItems = response.body().getGetAllPromo();
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Mendapatkan Promo")
                            .setContentText("Tampilkan Promo Yang Sesuai?")
                            .setConfirmText("Ya")
                            .setCancelText("Lainkali")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent = new Intent(getActivity(), Promo.class);
                                    intent.putExtra("data", (Serializable) allPromoItems);
                                    getActivity().startActivity(intent);
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
            }

            @Override
            public void onFailure(@NonNull Call<ResponsePromo> call, @NonNull Throwable t) {
            }
        });
    }
}
