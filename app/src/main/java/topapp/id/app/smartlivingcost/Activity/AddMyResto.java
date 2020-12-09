package topapp.id.app.smartlivingcost.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class AddMyResto extends AppCompatActivity {

    MapView map = null;
    IMapController mapController;
    SessionManager sessionManager;
    CompassOverlay mCompassOverlay;
    double myrestoLat = 0, myrestoLng = 0;
    Context ctx;
    String mediaPath = "";
    EditText namaResto, alamatResto;
    Context mContext;
    BaseApiService mApiService;
    SweetAlertDialog loading;
    Button btn_simpan;

    String Status = "0";
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_add_my_resto);
        setTitle("Tambah Restoran");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.RedTheme)));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        sessionManager = new SessionManager(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        initComponent();
        initMap();
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaResto.getText().toString().isEmpty() || namaResto.getText().toString().equals("")) {
                    new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Nama Restoran")
                            .show();
                } else if (alamatResto.getText().toString().isEmpty() || alamatResto.getText().toString().equals("")) {
                    new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Alamat Restoran")
                            .show();
                } else if (mediaPath.isEmpty() || mediaPath.equals("")) {
                    new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Masukkan Foto Restoran")
                            .show();
                } else if (myrestoLat == 0 || myrestoLng == 0) {
                    new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Pilih Lokasi Restoran")
                            .show();
                } else if (Status.equals("0")) {
                    new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Restoran Tutup")
                            .setContentText("Gunakan Status Tutup Pada Restoran?")
                            .setCancelText("Tidak")
                            .setConfirmText("Ya")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    initAddResto();
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                } else {
                    initAddResto();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    private void buildAlertMessageNoGps() {
        new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.WARNING_TYPE)
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
                        new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Koneksi GPS")
                                .setContentText("Aplikasi Memerlukan GPS")
                                .show();
                    }
                })
                .show();
    }

    private void initAddResto() {
        File file = new File(mediaPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar_resto", file.getName(), requestBody);
        RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), namaResto.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(myrestoLat));
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(myrestoLng));
        RequestBody alamat = RequestBody.create(MediaType.parse("text/plain"), alamatResto.getText().toString());
        int id_inputer = Integer.parseInt(sessionManager.getId());
        int myrestostatus = Integer.parseInt(Status);


        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Menambahkan Restoran");
        loading.setCancelable(false);
        loading.show();
        mApiService.addMyResto("Bearer " + sessionManager.getToken(), fileToUpload, id_inputer, nama, lat, lng, alamat, myrestostatus)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            startActivity(new Intent(AddMyResto.this, MyResto.class));
                            finish();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Isi Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan Teknis")
                                    .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(AddMyResto.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Masalah Jaringan")
                                .setContentText("Periksa Kembali Koneksi Anda")
                                .show();
                    }
                });
    }

    void initComponent() {
        namaResto = findViewById(R.id.dialogNamaResto);
        alamatResto = findViewById(R.id.dialogAlamatResto);
        btn_simpan = findViewById(R.id.btn_simpan);

        final Switch statusresto = findViewById(R.id.switch1);

        statusresto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    statusresto.setText("Buka");
                    Status = "1";
                } else {
                    statusresto.setText("Tutup");
                    Status = "0";
                }
            }
        });

        final ImageView imvphoto = findViewById(R.id.imv_photo);
        final TextView btncancel_photo = findViewById(R.id.btncancel_photo);
        final Button btn_select_photo = findViewById(R.id.btn_select_photo);
        btn_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imvphoto.setImageBitmap(r.getBitmap());
                                imvphoto.setVisibility(View.VISIBLE);
                                btncancel_photo.setVisibility(View.VISIBLE);
                                btn_select_photo.setVisibility(View.GONE);
                                mediaPath = String.valueOf(r.getPath());
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                            }
                        }).show(AddMyResto.this.getSupportFragmentManager());
            }

        });
        btncancel_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvphoto.setVisibility(View.GONE);
                btncancel_photo.setVisibility(View.GONE);
                btn_select_photo.setVisibility(View.VISIBLE);
            }
        });
    }


    void initMap() {
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(18);
        GeoPoint startPoint = new GeoPoint(Double.parseDouble(sessionManager.getLat()), Double.parseDouble(sessionManager.getLng()));
        mapController.animateTo(startPoint);
        map.setBuiltInZoomControls(false);
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);
        this.mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);

        final MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                //build the marker
                if (map.getOverlays().size() > 3) {
                    map.getOverlays().remove(map.getOverlays().size() - 1);
                    map.invalidate();
                }

                GeoPoint clickPoint = new GeoPoint(p.getLatitude(), p.getLongitude());
                myrestoLat = p.getLatitude();
                myrestoLng = p.getLongitude();
                Marker startMarker = new Marker(map);
                startMarker.setPosition(clickPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                startMarker.setTitle("Lokasi Restoran Anda");
                startMarker.showInfoWindow();
                map.getOverlays().add(startMarker);
                mapController.animateTo(clickPoint);
                map.invalidate();
                return false;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));
    }

    public void onBackPressed() {
        startActivity(new Intent(AddMyResto.this, MyResto.class));
        finish();

    }

}
