package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Adapter.MyMenuRVAdapter;
import topapp.id.app.smartlivingcost.Data.AllMyMenuItem;
import topapp.id.app.smartlivingcost.Data.ResponseMyMenu;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class FrontResto extends AppCompatActivity {
    int statusresto, idresto, statususer;
    Toolbar toolbar;
    BaseApiService mApiService;
    Call<ResponseMyMenu> call;
    SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<AllMyMenuItem> myAllMenuItem = new ArrayList<>();
    LinearLayout tv_nodata;
    ImageView editMyResto, deleteMyResto;
    String namaresto, alamatresto, gbrresto;
    double latresto, lngresto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_resto);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        initComponent();


    }

    private void getMyMenu() {
        final SweetAlertDialog loading = new SweetAlertDialog(FrontResto.this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mengumpulkan Menu Anda");
        loading.setCancelable(false);
        loading.show();
        call = mApiService.myMenu("Bearer " + sessionManager.getToken(), idresto);
        call.enqueue(new Callback<ResponseMyMenu>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMyMenu> call, @NonNull Response<ResponseMyMenu> response) {
                loading.dismissWithAnimation();
                if (response.code() == 200) {
                    myAllMenuItem = response.body().getGetAllMyMenu();
                    layoutManager = new LinearLayoutManager(FrontResto.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new MyMenuRVAdapter(myAllMenuItem);
                    recyclerView.setAdapter(adapter);

                    ((MyMenuRVAdapter) adapter).setOnItemClickListener(new MyMenuRVAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Intent i = new Intent(FrontResto.this, FrontMenu.class);
                            final AllMyMenuItem data = myAllMenuItem.get(position);
                            i.putExtra("idmenu", data.getId_menu());
                            i.putExtra("namamenu", data.getNama_menu());
                            i.putExtra("deskripsimenu", data.getMenu_deskripsi());
                            i.putExtra("hargamenu", data.getHarga());
                            i.putExtra("ratingmenu", data.getRating());
                            i.putExtra("gambarmenu", data.getGambar_menu());
                            i.putExtra("statusmenu", data.getStatus());
                            i.putExtra("statususer", statususer);
                            startActivity(i);
                        }
                    });

                } else if (response.code() == 401) {
                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Akun Bermasalah")
                            .setContentText("Silahkan Login Kembali")
                            .show();
                } else if (response.code() == 404) {
                    tv_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Anda Belum Memiliki Menu")
                            .show();
                } else {
                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Kesalahan Teknis")
                            .setContentText("Silahkan Muat Ulang Halaman")
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMyMenu> call, @NonNull Throwable t) {
                loading.dismissWithAnimation();
                new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Jaringan Bermasalah")
                        .setContentText("Periksa Kembali Jarigan Internet Anda")
                        .show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initComponent() {

        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(FrontResto.this);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null) {
            statususer = b.getInt("statususer");
            namaresto = b.getString("namaresto");
            alamatresto = b.getString("alamatresto");
            statusresto = b.getInt("statusresto");
            idresto = b.getInt("idresto");
            gbrresto = b.getString("gbrresto");
            latresto = b.getDouble("latresto");
            lngresto = b.getDouble("lngresto");
        }

        tv_nodata = findViewById(R.id.tv_nodata);
        recyclerView = findViewById(R.id.rv_menu);
        editMyResto = findViewById(R.id.editMyresto);
        deleteMyResto = findViewById(R.id.deleteMyResto);

        toolbar.setTitle(namaresto);

        TextView tv_alamat = findViewById(R.id.tvAlamat);
        tv_alamat.setText(alamatresto);

        ImageView imv_resto = findViewById(R.id.imv_resto);
        if (!gbrresto.equals("0")) {
            Glide.with(FrontResto.this).load(gbrresto).centerCrop().dontAnimate()
                    .placeholder(getDrawable(R.drawable.budget)).into(imv_resto);
        }

        final Switch switch1 = findViewById(R.id.switch1);
        if (statusresto != 0) {
            switch1.setChecked(true);
            switch1.setText("Buka");
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mApiService.setStatusResto("Bearer " + sessionManager.getToken(), idresto, 1)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        statusresto = 1;
                                        switch1.setText("Buka");
                                    } else {
                                        new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Jaringan Bermasalah")
                                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                                .show();
                                        switch1.setChecked(false);
                                        switch1.setText("Tutup");
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Jaringan Bermasalah")
                                            .setContentText("Periksa Kembali Jarigan Internet Anda")
                                            .show();
                                    switch1.setChecked(false);
                                    switch1.setText("Tutup");
                                }
                            });
                } else {
                    mApiService.setStatusResto("Bearer " + sessionManager.getToken(), idresto, 0)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        statusresto = 0;
                                        switch1.setText("Tutup");
                                    } else {
                                        new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Jaringan Bermasalah")
                                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                                .show();
                                        switch1.setChecked(true);
                                        switch1.setText("Buka");
                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Jaringan Bermasalah")
                                            .setContentText("Periksa Kembali Jarigan Internet Anda")
                                            .show();
                                    switch1.setChecked(true);
                                    switch1.setText("Buka");
                                }
                            });
                }
            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontResto.this, AddMyMenu.class);
                i.putExtra("idresto", idresto);
                startActivity(i);
            }
        });

        editMyResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontResto.this, UpdateMyResto.class);
                i.putExtra("namaresto", namaresto);
                i.putExtra("alamatresto", alamatresto);
                i.putExtra("statusresto", statusresto);
                i.putExtra("idresto", idresto);
                i.putExtra("gbrresto", gbrresto);
                i.putExtra("latresto", latresto);
                i.putExtra("lngresto", lngresto);
                startActivity(i);
            }
        });

        deleteMyResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(FrontResto.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Hapus Restoran")
                        .setContentText("Apakah anda yakin?")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                mApiService.deleteMyresto("Bearer " + sessionManager.getToken(), idresto)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    finish();
                                                } else if (response.code() == 401) {
                                                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Akun Bermasalah")
                                                            .setContentText("Silahkan Login Kembali")
                                                            .show();
                                                } else {
                                                    new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Kesalahan Teknis")
                                                            .setContentText("Silahkan Muat Ulang Halaman")
                                                            .show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                                new SweetAlertDialog(FrontResto.this, SweetAlertDialog.ERROR_TYPE)
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
            fab.hide();
            editMyResto.setVisibility(View.GONE);
            deleteMyResto.setVisibility(View.GONE);
            switch1.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        resetRV();
        getMyMenu();
    }

    void resetRV() {
        myAllMenuItem.clear();
    }
}
