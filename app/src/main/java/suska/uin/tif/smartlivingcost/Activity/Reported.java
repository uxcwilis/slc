package suska.uin.tif.smartlivingcost.Activity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suska.uin.tif.smartlivingcost.Adapter.SuspendMenuRVAdapter;
import suska.uin.tif.smartlivingcost.Data.AllSuspendMenuItem;
import suska.uin.tif.smartlivingcost.Data.ResponseSuspendMenu;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

public class Reported extends AppCompatActivity {
    BaseApiService mApiService;
    Call<ResponseSuspendMenu> call;
    SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List<AllSuspendMenuItem> allSuspendMenu = new ArrayList<>();
    TextView tv_nodata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported);

        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(Reported.this);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.RedTheme)));

        tv_nodata = findViewById(R.id.tv_nodata);
        recyclerView = findViewById(R.id.rv_menu_reported);
    }

    private void getData() {


        final ProgressDialog loading = ProgressDialog.show(Reported.this, null, "Mengambil Data Dari Server", true, false);
        call = mApiService.getReport("Bearer " + sessionManager.getToken());
        call.enqueue(new Callback<ResponseSuspendMenu>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSuspendMenu> call, @NonNull Response<ResponseSuspendMenu> response) {
                loading.dismiss();
                if (response.code() == 200) {
                    allSuspendMenu = response.body().getAllSuspendMenu();
                    layoutManager = new LinearLayoutManager(Reported.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new SuspendMenuRVAdapter(allSuspendMenu);
                    recyclerView.setAdapter(adapter);
                } else if (response.code() == 401) {
                    Toast.makeText(Reported.this, "Akun Anda Bermasalah, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    tv_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Reported.this, "Gagal Mengambil Menu Bermasalah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSuspendMenu> call, @NonNull Throwable t) {
                loading.dismiss();
                Toast.makeText(Reported.this, "Masalah Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
