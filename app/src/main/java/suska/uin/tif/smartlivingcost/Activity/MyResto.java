package suska.uin.tif.smartlivingcost.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suska.uin.tif.smartlivingcost.Adapter.MyRestoRVAdapter;
import suska.uin.tif.smartlivingcost.Data.AllMyRestoItem;
import suska.uin.tif.smartlivingcost.Data.ResponseMyResto;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

public class MyResto extends AppCompatActivity {
    Context mContext;
    BaseApiService mApiService;
    SweetAlertDialog loading;
    SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Call<ResponseMyResto> call;
    LinearLayout tvnodata;
    private List<AllMyRestoItem> myAllRestoItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resto);
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

        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(this);


        recyclerView = findViewById(R.id.rv_myresto);
        tvnodata = findViewById(R.id.tv_nodata);


    }

    private void initData() {
        loading = new SweetAlertDialog(MyResto.this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mencari Restoran Anda");
        loading.setCancelable(false);
        loading.show();
        call = mApiService.myResto("Bearer " + sessionManager.getToken());
        call.enqueue(new Callback<ResponseMyResto>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMyResto> call, @NonNull Response<ResponseMyResto> response) {
                loading.dismissWithAnimation();
                if (response.code() == 200) {
                    myAllRestoItem = response.body().getGetAllMyResto();
                    layoutManager = new LinearLayoutManager(MyResto.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new MyRestoRVAdapter(myAllRestoItem);
                    recyclerView.setAdapter(adapter);
                    ((MyRestoRVAdapter) adapter).setOnItemClickListener(new MyRestoRVAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            final AllMyRestoItem data = myAllRestoItem.get(position);
                            Intent i = new Intent(MyResto.this, FrontResto.class);
                            i.putExtra("statususer", 1);
                            i.putExtra("namaresto", data.getNama_resto());
                            i.putExtra("alamatresto", data.getAlamat());
                            i.putExtra("statusresto", data.getStatus());
                            i.putExtra("idresto", data.getId_resto());
                            i.putExtra("gbrresto", data.getGambar_resto());
                            i.putExtra("latresto", data.getLat().doubleValue());
                            i.putExtra("lngresto", data.getLng().doubleValue());
                            startActivity(i);
                        }
                    });
                } else if (response.code() == 401) {
                    new SweetAlertDialog(MyResto.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Akun Bermasalah")
                            .setContentText("Silahkan Login Kembali")
                            .show();
                } else if (response.code() == 404) {
                    recyclerView.setVisibility(View.GONE);
                    tvnodata.setVisibility(View.VISIBLE);
                    new SweetAlertDialog(MyResto.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Restoran Anda Belum Tersedia")
                            .show();
                } else {
                    new SweetAlertDialog(MyResto.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Kesalahan Teknis")
                            .setContentText("Silahkan Muat Ulang Halaman")
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMyResto> call, @NonNull Throwable t) {
                loading.dismissWithAnimation();
                new SweetAlertDialog(MyResto.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Jaringan Bermasalah")
                        .setContentText("Periksa Kembali Jarigan Internet Anda")
                        .show();
            }
        });
    }

    void resetRV() {
        myAllRestoItem.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_myresto, menu);
        Drawable yourdrawable = menu.getItem(0).getIcon();
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.add_myresto) {
            startActivity(new Intent(this, AddMyResto.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetRV();
        initData();
    }
}
