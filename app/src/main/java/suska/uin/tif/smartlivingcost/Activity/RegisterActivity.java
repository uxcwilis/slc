package suska.uin.tif.smartlivingcost.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailView, mPasswordView, nama;
    SessionManager sessionManager;
    Context mContext;
    BaseApiService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#BF006684"));
        }
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        nama = findViewById(R.id.nama);
        Button btn_reg = findViewById(R.id.btn_reg);
        sessionManager = new SessionManager(getApplicationContext());
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailView.getText().toString().trim();
                if (email.length() < 6 || !email.matches(emailPattern)) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Email")
                            .setContentText("Mohon Masukkan Alamat Email Yang Valid")
                            .show();
                } else if (mPasswordView.length() < 6) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Password")
                            .setContentText("Panjang Password Minimal adalah 6 Karakter")
                            .show();
                } else if (nama.length() < 2) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Nama")
                            .setContentText("Panjang Nama Minimal Adalah 2 Karakter")
                            .show();
                } else {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Daftar Akun?")
                            .setContentText("Nama : " + nama.getText().toString() +
                                    "\nEmail : " + mEmailView.getText().toString() +
                                    "\nPassword : " + mPasswordView.getText().toString() + "?")
                            .setCancelText("Tidak")
                            .setConfirmText("Tentu Saja")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    requestRegister();
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

                }
            }
        });

        TextView go_log = findViewById(R.id.go_login);
        go_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void requestRegister() {
        final SweetAlertDialog loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mendaftarkan Akun Anda");
        loading.setCancelable(false);
        loading.show();


        mApiService.register(nama.getText().toString(),
                mEmailView.getText().toString(),
                mPasswordView.getText().toString(),
                mPasswordView.getText().toString()).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismissWithAnimation();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("success")) {
                                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Registrasi Berhasil")
                                            .setContentText("Silahkan Login")
                                            .show();
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                    finish();
                                } else {
                                    loading.dismissWithAnimation();
                                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Registrasi Gagal")
                                            .setContentText("Silahkan Coba lagi")
                                            .show();
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                .show();
                    }
                }
        );
    }

    public void onBackPressed() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Keluar?")
                .setContentText("Apakah anda yakin untuk keluar dari aplikasi?")
                .setCancelText("Tidak")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        RegisterActivity.this.finish();
                        Intent d = new Intent(Intent.ACTION_MAIN);
                        d.addCategory(Intent.CATEGORY_HOME);
                        d.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(d);
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

    }
}
