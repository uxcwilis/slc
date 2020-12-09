package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import topapp.id.app.smartlivingcost.Data.AllMyPromoItem;
import topapp.id.app.smartlivingcost.Data.ResponseMyPromo;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class EditPromo extends AppCompatActivity {
    BaseApiService mApiService;
    SweetAlertDialog loading;
    int idmenu, idpromo = 0, harga_promo;
    CurrencyEditText ed_hargaPromo;
    SessionManager sessionManager;
    EditText et_tanggal_mulai, et_tanggal_batas, ket;
    TextView delete, btncancel_photo;
    private Calendar newCalendar;
    List<AllMyPromoItem> data = new ArrayList<>();
    Button btn_select_photo, btn_simpan;
    ImageView imvPhoto;
    TextInputLayout tilMulaiPromo, tilBatasPromo;


    String mediaPath = "";
    String tanggal_mulai, tanggal_batas, tanggal_mulai_r, tanggal_batas_r, gambar_promo, keterangan;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_promo);
        final Toolbar toolbar = findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_tanggal_mulai = findViewById(R.id.edmulaiPromo);
        et_tanggal_batas = findViewById(R.id.edbatasPromo);
        tilBatasPromo = findViewById(R.id.batas_promo);
        tilMulaiPromo = findViewById(R.id.mulai_promo);
        ket = findViewById(R.id.ket);
        ed_hargaPromo = findViewById(R.id.edHargaPromo);
        delete = findViewById(R.id.delete);
        imvPhoto = findViewById(R.id.imv_photo);
        btn_select_photo = findViewById(R.id.btn_select_photo);
        btncancel_photo = findViewById(R.id.btncancel_photo);
        btn_simpan = findViewById(R.id.btn_simpan);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        newCalendar = Calendar.getInstance();
        mApiService = UtilsApi.getAPIService();

        Bundle b = getIntent().getExtras();
        idmenu = b.getInt("idmenu");

        ed_hargaPromo.setSeparator(".");
        ed_hargaPromo.setDecimals(false);

        sessionManager = new SessionManager(getApplicationContext());
        getPromo();
        setData();
        if (idpromo == 0){
            imvPhoto.setVisibility(View.GONE);
            btncancel_photo.setVisibility(View.GONE);
            btn_select_photo.setVisibility(View.VISIBLE);
        }



        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ket.getText().toString().isEmpty() || ket.getText().toString().equals("")) {
                    new SweetAlertDialog(EditPromo.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Keterangan Promo")
                            .show();
                } else if (Objects.requireNonNull(ed_hargaPromo.getText()).toString().isEmpty() || ed_hargaPromo.getText().toString().equals("")) {
                    new SweetAlertDialog(EditPromo.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Harga Promo")
                            .show();
                } else if (et_tanggal_mulai.getText().toString().isEmpty() || et_tanggal_mulai.getText().toString().equals("")) {
                    new SweetAlertDialog(EditPromo.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Tanggal Mulai Promo")
                            .show();
                } else if (et_tanggal_batas.getText().toString().isEmpty() || et_tanggal_batas.getText().toString().equals("")) {
                    new SweetAlertDialog(EditPromo.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Silahkan Isi Tanggal Batas Promo")
                            .show();
                } else {
                    if (idpromo != 0) {
                        if (!mediaPath.equals("")) {
                            updatePromo();
                        } else {
                            updatePromoNoPic();
                        }
                    } else {
                        if (!mediaPath.equals("")) {
                            createPromo();
                        } else {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Maaf")
                                    .setContentText("Silahkan Isi Masukkan Gambar Promo")
                                    .show();
                        }
                    }

                }
            }
        });


    }

    void getPromo() {

        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mendapatkan Promo Menu Ini");
        loading.setCancelable(false);
        loading.show();

        Call<ResponseMyPromo> call = mApiService.singlePromo("Bearer " + sessionManager.getToken(), idmenu);
        call.enqueue(new Callback<ResponseMyPromo>() {
            @Override
            public void onResponse(Call<ResponseMyPromo> call, Response<ResponseMyPromo> response) {
                if (response.code() == 200) {
                    loading.dismissWithAnimation();
                    data = response.body().getGetAllMyPromo();
                    if (!data.isEmpty()) {
                        idpromo = data.get(0).getId();
                        harga_promo = data.get(0).getHarga_promo();
                        ed_hargaPromo.setText(String.valueOf(harga_promo));
                        gambar_promo = "http://slc.mengkako.masuk.id/fotopromo/"+data.get(0).getGambar();
                        Glide.with(EditPromo.this).load(gambar_promo).dontAnimate().fitCenter().into(imvPhoto);
                        keterangan = data.get(0).getKeterangan();
                        ket.setText(keterangan);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date_mulai = data.get(0).getTanggal_mulai();
                        Date date_batas = data.get(0).getTanggal_batas();
                        tanggal_mulai_r = String.valueOf(defaultFormat.format(date_mulai));
                        tanggal_batas_r = String.valueOf(defaultFormat.format(date_batas));
                        tanggal_mulai = String.valueOf(targetFormat.format(date_mulai));
                        tanggal_batas = String.valueOf(targetFormat.format(date_batas));
                        et_tanggal_batas.setText(tanggal_batas);
                        et_tanggal_mulai.setText(tanggal_mulai);
                        delete.setVisibility(View.VISIBLE);
//                        setData();

                    }
                } else if (response.code() == 404) {
                    loading.dismissWithAnimation();
//                    setData();


                } else {
                    loading.dismissWithAnimation();
                    new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal")
                            .setContentText("Terjadi Kesalahan Teknis, Muat Ulang Halaman")
                            .show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMyPromo> call, Throwable t) {
                loading.dismissWithAnimation();
                new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Jaringan Bermasalah")
                        .setContentText("Harap Periksa Jaringan Internet Anda")
                        .show();
            }
        });


    }

    void setData() {
        et_tanggal_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditPromo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_tanggal_mulai.setText(dateFormatter.format(newDate.getTime()));
                        tanggal_mulai = dateFormatter.format(newDate.getTime());
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

                        Date date;
                        try {
                            date = originalFormat.parse(tanggal_mulai);
                            tanggal_mulai_r = String.valueOf(targetFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        et_tanggal_batas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditPromo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_tanggal_batas.setText(dateFormatter.format(newDate.getTime()));
                        tanggal_batas = dateFormatter.format(newDate.getTime());
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

                        Date date;
                        try {
                            date = originalFormat.parse(tanggal_batas);
                            tanggal_batas_r = String.valueOf(targetFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

       et_tanggal_batas.setInputType(InputType.TYPE_NULL);
       et_tanggal_mulai.setInputType(InputType.TYPE_NULL);
       et_tanggal_mulai.setShowSoftInputOnFocus(false);
       et_tanggal_batas.setShowSoftInputOnFocus(false);

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
                        }).show(EditPromo.this.getSupportFragmentManager());
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

    private void createPromo() {
        File file = new File(mediaPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar", file.getName(), requestBody);
        RequestBody keterangan = RequestBody.create(MediaType.parse("text/plain"), ket.getText().toString());
        RequestBody tanggal_mulai = RequestBody.create(MediaType.parse("text/plain"), tanggal_mulai_r);
        RequestBody tanggal_batas = RequestBody.create(MediaType.parse("text/plain"), tanggal_batas_r);
        int harga_promo = ed_hargaPromo.getCleanIntValue();
        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Menambahkan Promo");
        loading.setCancelable(false);
        loading.show();
        mApiService.createMyPromo("Bearer " + sessionManager.getToken(), fileToUpload, idmenu, keterangan, harga_promo, tanggal_mulai, tanggal_batas)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Promo Menu Ditambahkan")
                                    .show();
                            finish();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Isi Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan Teknis")
                                    .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Masalah Jaringan")
                                .setContentText("Periksa Kembali Koneksi Anda")
                                .show();
                    }
                });
    }

    private void updatePromo() {
        File file = new File(mediaPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar", file.getName(), requestBody);
        RequestBody keterangan = RequestBody.create(MediaType.parse("text/plain"), ket.getText().toString());
        RequestBody tanggal_mulai = RequestBody.create(MediaType.parse("text/plain"), tanggal_mulai_r);
        RequestBody tanggal_batas = RequestBody.create(MediaType.parse("text/plain"), tanggal_batas_r);
        int harga_promo = ed_hargaPromo.getCleanIntValue();

        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Memperbaharui Promo");
        loading.setCancelable(false);
        loading.show();
        mApiService.updateMyPromo("Bearer " + sessionManager.getToken(), fileToUpload, idpromo, keterangan, harga_promo, tanggal_mulai, tanggal_batas)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Promo Menu Diperbaharui")
                                    .show();
                            finish();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Isi Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan Teknis")
                                    .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Masalah Jaringan")
                                .setContentText("Periksa Kembali Koneksi Anda")
                                .show();
                    }
                });
    }

    private void updatePromoNoPic() {
        File file = new File(mediaPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("gambar", file.getName(), requestBody);
        RequestBody keterangan = RequestBody.create(MediaType.parse("text/plain"), ket.getText().toString());
        RequestBody tanggal_mulai = RequestBody.create(MediaType.parse("text/plain"), tanggal_mulai_r);
        RequestBody tanggal_batas = RequestBody.create(MediaType.parse("text/plain"), tanggal_batas_r);
        int harga_promo = ed_hargaPromo.getCleanIntValue();

        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Memperbaharui Promo");
        loading.setCancelable(false);
        loading.show();
        mApiService.updateMyPromoNoPic("Bearer " + sessionManager.getToken(), idpromo, keterangan, harga_promo, tanggal_mulai, tanggal_batas)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Promo Menu Diperbaharui")
                                    .show();
                            finish();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Isi Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan Teknis")
                                    .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Masalah Jaringan")
                                .setContentText("Periksa Kembali Koneksi Anda")
                                .show();
                    }
                });
    }

    public void Delete(View view) {
        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Menghapus Promo");
        loading.setCancelable(false);
        loading.show();
        mApiService.deletePromo("Bearer " + sessionManager.getToken(), idpromo)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Promo Dihapus")
                                    .show();
                            finish();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Isi Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kesalahan Teknis")
                                    .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(EditPromo.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Masalah Jaringan")
                                .setContentText("Periksa Kembali Koneksi Anda")
                                .show();
                    }
                });
    }
}
