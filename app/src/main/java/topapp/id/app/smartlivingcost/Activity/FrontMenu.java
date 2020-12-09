package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Adapter.MyReviewRVAdapter;
import topapp.id.app.smartlivingcost.Data.AllMyReviewItem;
import topapp.id.app.smartlivingcost.Data.ResponseMyReview;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class FrontMenu extends AppCompatActivity {

    TextView deskripsi, tv_rating, tv_harga, edit_promo;
    LinearLayout tv_nodata;
    ImageView editMyMenu, deleteMyMenu, imvMenu;
    String namamenu, deskripsimenu, gambarmenu, ratingmenu;
    int hargamenu, statusmenu, idmenu, statususer;
    Toolbar toolbar;
    BaseApiService mApiService;
    Call<ResponseMyReview> call;
    SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<AllMyReviewItem> myAllReviewItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_menu);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        initComponent();
        getReview();

    }

    private void getReview() {
        call = mApiService.getMyReview("Bearer " + sessionManager.getToken(), idmenu);
        call.enqueue(new Callback<ResponseMyReview>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMyReview> call, @NonNull Response<ResponseMyReview> response) {
                if (response.code() == 200) {
                    myAllReviewItem = response.body().getGetAllMyReview();
                    layoutManager = new LinearLayoutManager(FrontMenu.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new MyReviewRVAdapter(myAllReviewItem);
                    recyclerView.setAdapter(adapter);
                } else if (response.code() == 404) {
                    tv_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Menu Anda Belum Memiliki Review")
                            .show();
                } else if (response.code() == 401) {
                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Akun Bermasalah")
                            .setContentText("Silahkan Login Kembali")
                            .show();
                } else {
                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Kesalahan Teknis")
                            .setContentText("Silahkan Muat Ulang Halaman")
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMyReview> call, @NonNull Throwable t) {
                new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Jaringan Bermasalah")
                        .setContentText("Periksa Kembali Jarigan Internet Anda")
                        .show();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void initComponent() {
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(FrontMenu.this);
        tv_harga = findViewById(R.id.harga_menu);
        tv_rating = findViewById(R.id.rating);
        deskripsi = findViewById(R.id.tvDeskripsi);
        tv_nodata = findViewById(R.id.tv_nodata);
        imvMenu = findViewById(R.id.imv_myMenu);
        editMyMenu = findViewById(R.id.editMymenu);
        deleteMyMenu = findViewById(R.id.deleteMyMenu);
        recyclerView = findViewById(R.id.rv_review);
        edit_promo = findViewById(R.id.edit_promo);
        final Switch setstatus = findViewById(R.id.switch1);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null) {
            idmenu = b.getInt("idmenu");
            namamenu = b.getString("namamenu");
            deskripsimenu = b.getString("deskripsimenu");
            hargamenu = b.getInt("hargamenu");
            ratingmenu = b.getString("ratingmenu");
            gambarmenu = b.getString("gambarmenu");
            statusmenu = b.getInt("statusmenu");
            statususer = b.getInt("statususer");
        }

        toolbar.setTitle(namamenu);


        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        tv_harga.setText(String.valueOf(nbFmt.format(hargamenu)));
        Glide.with(FrontMenu.this).load(gambarmenu).centerCrop().dontAnimate()
                .placeholder(getDrawable(R.drawable.budget)).into(imvMenu);
        tv_rating.setText(ratingmenu);
        if (deskripsimenu.equals("0")) {
            deskripsi.setText("Tidak Ada Deskripsi Menu");
        } else {
            deskripsi.setText(deskripsimenu);

        }

        if (statusmenu != 0) {
            setstatus.setChecked(true);
            setstatus.setText("Tersedia");
        }

        setstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mApiService.setStatusMenu("Bearer " + sessionManager.getToken(), idmenu, 1)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        statusmenu = 1;
                                        setstatus.setText("Tersedia");
                                    } else {
                                        new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Jaringan Bermasalah")
                                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                                .show();
                                        statusmenu = 0;
                                        setstatus.setText("Habis");
                                        setstatus.setChecked(false);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Jaringan Bermasalah")
                                            .setContentText("Periksa Kembali Jarigan Internet Anda")
                                            .show();
                                    statusmenu = 0;
                                    setstatus.setText("Habis");
                                    setstatus.setChecked(false);
                                }
                            });
                } else {
                    mApiService.setStatusMenu("Bearer " + sessionManager.getToken(), idmenu, 0)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        statusmenu = 0;
                                        setstatus.setText("Habis");
                                    } else {
                                        new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Jaringan Bermasalah")
                                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                                .show();
                                        statusmenu = 1;
                                        setstatus.setText("Tersedia");
                                        setstatus.setChecked(true);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Jaringan Bermasalah")
                                            .setContentText("Periksa Kembali Jarigan Internet Anda")
                                            .show();
                                    statusmenu = 1;
                                    setstatus.setText("Tersedia");
                                    setstatus.setChecked(true);
                                }
                            });
                }
            }
        });

        editMyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontMenu.this, UpdateMyMenu.class);
                i.putExtra("idmenu", idmenu);
                i.putExtra("namamenu", namamenu);
                i.putExtra("deskripsimenu", deskripsimenu);
                i.putExtra("hargamenu", hargamenu);
                i.putExtra("gambarmenu", gambarmenu);
                i.putExtra("statusmenu", statusmenu);
                startActivity(i);


            }
        });

        edit_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FrontMenu.this, EditPromo.class);
                i.putExtra("idmenu",idmenu);
                startActivity(i);
            }
        });

        deleteMyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Hapus Restoran")
                        .setContentText("Apakah anda yakin?")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                mApiService.deleteMymenu("Bearer " + sessionManager.getToken(), idmenu)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    finish();
                                                } else if (response.code() == 401) {
                                                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Akun Bermasalah")
                                                            .setContentText("Silahkan Login Kembali")
                                                            .show();
                                                } else {
                                                    new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Kesalahan Teknis")
                                                            .setContentText("Silahkan Muat Ulang Halaman")
                                                            .show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                                new SweetAlertDialog(FrontMenu.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Jaringan Bermasalah")
                                                        .setContentText("Periksa Kembali Jarigan Internet Anda")
                                                        .show();
                                            }
                                        });

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
        });

        if (statususer != 1) {
            editMyMenu.setVisibility(View.GONE);
            deleteMyMenu.setVisibility(View.GONE);
            edit_promo.setVisibility(View.GONE);
            setstatus.setVisibility(View.GONE);
        }
    }

}
