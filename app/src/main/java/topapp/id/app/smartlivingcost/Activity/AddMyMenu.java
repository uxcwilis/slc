package topapp.id.app.smartlivingcost.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
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

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.abhinay.input.CurrencyEditText;
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

public class AddMyMenu extends AppCompatActivity {
    BaseApiService mApiService;
    SweetAlertDialog loading;
    Button btn_simpan;
    EditText ed_namaMenu, ed_menuDeskripsi;
    CurrencyEditText ed_hargaMenu;
    SessionManager sessionManager;

    int Status = 0;
    String mediaPath = "";
    int idresto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_menu);
        setTitle("Tambah Menu");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.RedTheme)));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));

        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(this);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        idresto = b.getInt("idresto");

        initComponent();
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_namaMenu.getText().toString().isEmpty() || ed_namaMenu.getText().toString().equals("")) {
                    new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Nama Menu")
                            .show();
                } else if (ed_hargaMenu.getText().toString().isEmpty() || ed_hargaMenu.getText().toString().equals("")) {
                    new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Harga Menu")
                            .show();
                } else if (mediaPath.isEmpty() || mediaPath.equals("")) {
                    new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan MasuKkan Foto Menu")
                            .show();
                } else if (ed_menuDeskripsi.getText().toString().isEmpty() || ed_menuDeskripsi.getText().toString().equals("")) {
                    new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Deskripsi Menu")
                            .show();
                } else if (Status == 0) {
                    new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Menu Habis")
                            .setContentText("Gunakan Status Habis Pada Menu?")
                            .setCancelText("Tidak")
                            .setConfirmText("Ya")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    initAddMenu();
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
                    initAddMenu();
                }
            }
        });


    }

    private void initComponent() {
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

        statusresto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                        }).show(AddMyMenu.this.getSupportFragmentManager());
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

    private void initAddMenu() {
        File file = new File(mediaPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar_menu", file.getName(), requestBody);
        RequestBody nama_menu = RequestBody.create(MediaType.parse("text/plain"), ed_namaMenu.getText().toString());
        RequestBody deskripsi_menu = RequestBody.create(MediaType.parse("text/plain"), ed_menuDeskripsi.getText().toString());
        int harga_menu = ed_hargaMenu.getCleanIntValue();


        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Menambahkan Menu");
        loading.setCancelable(false);
        loading.show();
        mApiService.addMyMenu("Bearer " + sessionManager.getToken(), fileToUpload, idresto, nama_menu, harga_menu, deskripsi_menu, Status)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            finish();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Isi Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan Teknis")
                                    .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(AddMyMenu.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Masalah Jaringan")
                                .setContentText("Periksa Kembali Koneksi Anda")
                                .show();
                    }
                });
    }
}
