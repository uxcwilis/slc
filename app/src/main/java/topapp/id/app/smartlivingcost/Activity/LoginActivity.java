package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
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

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Data.ResponseLogin;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class LoginActivity extends AppCompatActivity {
    DBSLC MyDatabase;
    private EditText mPasswordView, mEmailView;
    SessionManager sessionManager;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#BF006684"));
        }

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        Button btn_log = findViewById(R.id.btn_login);
        sessionManager = new SessionManager(getApplicationContext());
        MyDatabase = new DBSLC(getBaseContext());
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        PRDownloader.initialize(getApplicationContext());

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailView.length() < 5) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Email")
                            .setContentText("Mohon Masukkan Alamat Email Anda")
                            .show();
                } else if (mPasswordView.length() < 6) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Password")
                            .setContentText("Panjang Password Minimal adalah 6 Karakter")
                            .show();
                } else {
                    requestLogin();
                }
            }
        });

        TextView go_reg = findViewById(R.id.go_reg);
        go_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        TextView go_resetpass = findViewById(R.id.go_resetpass);
        go_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPass.class));
            }
        });
    }

    private void requestLogin() {
        final SweetAlertDialog loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mencoba Masuk Dengan Akun Anda");
        loading.setCancelable(false);
        loading.show();

        mApiService.login(mEmailView.getText().toString(), mPasswordView.getText().toString())
                .enqueue(new Callback<ResponseLogin>() {
                    @SuppressLint("SdCardPath")
                    @Override
                    public void onResponse(@NonNull Call<ResponseLogin> call, @NonNull Response<ResponseLogin> response) {
                        loading.dismissWithAnimation();
                        if (response.isSuccessful()) {
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Login Berhasil")
                                    .setContentText("Anda Akan Menuju Halaman Beranda")
                                    .show();
                            ResponseLogin result = response.body();
                            sessionManager.createSession(result.getAllSuccessItem().getName(), result.getAllSuccessItem().getEmail(),
                                    result.getAllSuccessItem().getId(), result.getAllSuccessItem().getToken(), mPasswordView.getText().toString(), result.getAllSuccessItem().getIsAdmin());
                            sessionManager.setReq_harga("15000");
                            sessionManager.setReq_jarak("2000");
                            sessionManager.setReq_row("3");
                            if (!result.getAllSuccessItem().getSqlite_backup().equals("0")) {
                                getBaseContext().deleteDatabase("dbslc.db");
                                PRDownloader.download(result.getAllSuccessItem().getSqlite_backup(), "/data/data/suska.uin.tif.smartlivingcost/databases/", "dbslc.db")
                                        .build()
                                        .start(new OnDownloadListener() {
                                            @Override
                                            public void onDownloadComplete() {
                                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                i.putExtra("frgToLoad", "");
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                LoginActivity.this.finish();
                                            }

                                            @Override
                                            public void onError(Error error) {

                                            }

                                        });
                            } else {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("frgToLoad", "");
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        } else {
                            loading.dismissWithAnimation();
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Login Gagal")
                                    .setContentText("Email atau Password Anda Salah")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseLogin> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                .show();
                    }
                });
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
                        LoginActivity.this.finish();
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

