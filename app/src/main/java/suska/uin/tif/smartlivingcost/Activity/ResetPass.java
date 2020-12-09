package suska.uin.tif.smartlivingcost.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;

public class ResetPass extends AppCompatActivity {
    LinearLayout ln_req_reset, ln_reset;
    EditText reqResetEmail, ResetEmail, ResetKode, ResetPass;
    Button btnReqReset, btnReset;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        mApiService = UtilsApi.getAPIService();

        ln_req_reset = findViewById(R.id.input_req_reset);
        ln_reset = findViewById(R.id.input_reset);
        reqResetEmail = findViewById(R.id.emaill);
        ResetEmail = findViewById(R.id.email);
        ResetKode = findViewById(R.id.token);
        ResetPass = findViewById(R.id.password);
        btnReqReset = findViewById(R.id.btn_req_reset);
        btnReset = findViewById(R.id.btn_reset);

        TextView login1 = findViewById(R.id.go_login1);
        TextView login = findViewById(R.id.go_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReqReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reqResetEmail.length() < 1) {
                    new SweetAlertDialog(ResetPass.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Email")
                            .setContentText("Mohon Masukkan Alamat Email Anda")
                            .show();
                } else {
                    ReqReset();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ResetEmail.length() < 1) {
                    new SweetAlertDialog(ResetPass.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Email")
                            .setContentText("Mohon Masukkan Alamat Email Anda")
                            .show();
                } else if (ResetKode.length() <= 5 || ResetKode.length() > 6) {
                    new SweetAlertDialog(ResetPass.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Kode")
                            .setContentText("Periksa Kembali Kode Anda")
                            .show();
                } else if (ResetPass.length() <= 5) {
                    new SweetAlertDialog(ResetPass.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Password")
                            .setContentText("Panjang Password Minimal Adalah 6 Karakter")
                            .show();
                } else {
                    Reset();
                }
            }
        });

    }

    private void Reset() {
        final SweetAlertDialog loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Menyetel Ulang Password Anda");
        loading.setCancelable(false);
        loading.show();
        mApiService.ResetPass(ResetEmail.getText().toString(), ResetPass.getText().toString(), ResetKode.getText().toString(), ResetPass.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            new SweetAlertDialog(ResetPass.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Password Diperharui")
                                    .setContentText("Password Anda Berhasil diperharui, Silahkan Login")
                                    .show();
                            finish();
                        } else if (response.code() == 404) {
                            new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Email")
                                    .setContentText("Email Anda Tidak Cocok")
                                    .show();
                        } else if (response.code() == 409) {
                            new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Kode")
                                    .setContentText("Kode Anda Tidak Cocok")
                                    .show();
                        } else {
                            new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal Memperbaharui")
                                    .setContentText("Terjadi Kesalahan Teknis, Silahkan Lakukan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismiss();
                        new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                .show();
                    }
                });
    }

    private void ReqReset() {
        final SweetAlertDialog loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Meminta Kode Rahasia");
        loading.setCancelable(false);
        loading.show();
        mApiService.reqResetPass(reqResetEmail.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            ln_req_reset.setVisibility(View.GONE);
                            ln_reset.setVisibility(View.VISIBLE);
                        } else if (response.code() == 404) {
                            new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Email")
                                    .setContentText("Email Anda Tidak Cocok")
                                    .show();
                        } else {
                            new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal Meminta Kode")
                                    .setContentText("Terjadi Kesalahan Teknis, Silahkan Lakukan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(ResetPass.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                .show();
                    }
                });
    }
}
