package suska.uin.tif.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.abhinay.input.CurrencyEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

public class UpdateMyMenu extends AppCompatActivity {
    SessionManager sessionManager;

    BaseApiService mApiService;
    SweetAlertDialog loading;
    Button btn_simpan;
    EditText ed_namaMenu, ed_menuDeskripsi;
    CurrencyEditText ed_hargaMenu;

    int Status = 0;
    String mediaPath = "";
    String namamenu, deskripsimenu, gambarmenu;
    int hargamenu, statusmenu, idmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_menu);
        sessionManager = new SessionManager(this);
        mApiService = UtilsApi.getAPIService();

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.RedTheme)));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));

        initComponent();
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_namaMenu.getText().toString().isEmpty() || ed_namaMenu.getText().toString().equals("")) {
                    new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Nama Menu")
                            .show();
                } else if (Objects.requireNonNull(ed_hargaMenu.getText()).toString().isEmpty() || ed_hargaMenu.getText().toString().equals("")) {
                    new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Harga Menu")
                            .show();
                } else if (ed_menuDeskripsi.getText().toString().isEmpty() || ed_menuDeskripsi.getText().toString().equals("")) {
                    new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Deskripsi Menu")
                            .show();
                } else if (Status == 0) {
                    new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Menu Habis")
                            .setContentText("Gunakan Status Habis Pada Menu?")
                            .setCancelText("Tidak")
                            .setConfirmText("Ya")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    updateMyMenu();
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
                    updateMyMenu();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    void initComponent() {
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null) {
            idmenu = b.getInt("idmenu");
            namamenu = b.getString("namamenu");
            deskripsimenu = b.getString("deskripsimenu");
            hargamenu = b.getInt("hargamenu");
            gambarmenu = b.getString("gambarmenu");
            statusmenu = b.getInt("statusmenu");
        }

        ed_namaMenu = findViewById(R.id.dialogNamaMenu);
        ed_hargaMenu = findViewById(R.id.edHargaMenu);
        ed_menuDeskripsi = findViewById(R.id.edMenuDeskripsi);
        btn_simpan = findViewById(R.id.btn_simpan);

        ed_hargaMenu.setSeparator(".");
        ed_hargaMenu.setDecimals(false);

        final Switch statusresto = findViewById(R.id.switch1);
        final ImageView imvPhoto = findViewById(R.id.imv_photo);
        final Button btn_select_photo = findViewById(R.id.btn_select_photo);
        final TextView btncancel_photo = findViewById(R.id.btncancel_photo);

        Status = statusmenu;

        statusresto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    statusresto.setText("Tersedia");
                    Status = 1;
                } else {
                    statusresto.setText("Habis");
                    Status = 0;
                }
            }
        });

        if (statusmenu != 0) {
            statusresto.setChecked(true);
            statusresto.setText("Tersedia");
        }
        ed_namaMenu.setText(namamenu);
        ed_hargaMenu.setText(String.valueOf(hargamenu));
        ed_menuDeskripsi.setText(deskripsimenu);
        Glide.with(UpdateMyMenu.this).load(gambarmenu).dontAnimate().fitCenter().into(imvPhoto);

        btn_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imvPhoto.setImageBitmap(r.getBitmap());
                                imvPhoto.setVisibility(View.VISIBLE);
                                btncancel_photo.setVisibility(View.VISIBLE);
                                btn_select_photo.setVisibility(View.GONE);
                                mediaPath = String.valueOf(r.getPath());
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                            }
                        }).show(UpdateMyMenu.this.getSupportFragmentManager());
            }

        });
        btncancel_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvPhoto.setVisibility(View.GONE);
                btncancel_photo.setVisibility(View.GONE);
                btn_select_photo.setVisibility(View.VISIBLE);
            }
        });
    }

    void updateMyMenu() {
        if (!mediaPath.equals("")) {
            File file = new File(mediaPath);
            final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar_menu", file.getName(), requestBody);
            RequestBody nama_menu = RequestBody.create(MediaType.parse("text/plain"), ed_namaMenu.getText().toString());
            RequestBody deskripsi_menu = RequestBody.create(MediaType.parse("text/plain"), ed_menuDeskripsi.getText().toString());
            int harga_menu = ed_hargaMenu.getCleanIntValue();


            loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            loading.setTitleText("Memperbaharui Menu");
            loading.setCancelable(false);
            loading.show();
            mApiService.updateMyMenu("Bearer " + sessionManager.getToken(), fileToUpload, idmenu, nama_menu, harga_menu, deskripsi_menu, Status)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            loading.dismissWithAnimation();
                            if (response.code() == 200) {
                                startActivity(new Intent(UpdateMyMenu.this, MyResto.class));
                                finish();
                            } else if (response.code() == 401) {
                                new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Akun Bermasalah")
                                        .setContentText("Silahkan Isi Login Kembali")
                                        .show();
                            } else {
                                new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Kesalahan Teknis")
                                        .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            loading.dismissWithAnimation();
                            new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Masalah Jaringan")
                                    .setContentText("Periksa Kembali Koneksi Anda")
                                    .show();
                        }
                    });
        } else {
            RequestBody nama_menu = RequestBody.create(MediaType.parse("text/plain"), ed_namaMenu.getText().toString());
            RequestBody deskripsi_menu = RequestBody.create(MediaType.parse("text/plain"), ed_menuDeskripsi.getText().toString());
            int harga_menu = ed_hargaMenu.getCleanIntValue();


            loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            loading.setTitleText("Memperbaharui Menu");
            loading.setCancelable(false);
            loading.show();
            mApiService.updateMyMenuNoPic("Bearer " + sessionManager.getToken(), idmenu, nama_menu, harga_menu, deskripsi_menu, Status)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            loading.dismiss();
                            if (response.code() == 200) {
                                startActivity(new Intent(UpdateMyMenu.this, MyResto.class));
                                finish();
                            } else if (response.code() == 401) {
                                new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Akun Bermasalah")
                                        .setContentText("Silahkan Isi Login Kembali")
                                        .show();
                            } else {
                                new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Kesalahan Teknis")
                                        .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            loading.dismiss();
                            new SweetAlertDialog(UpdateMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Masalah Jaringan")
                                    .setContentText("Periksa Kembali Koneksi Anda")
                                    .show();
                        }
                    });
        }

    }
}
