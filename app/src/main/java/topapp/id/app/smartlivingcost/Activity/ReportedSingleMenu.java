package topapp.id.app.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Adapter.ReportCommentRVAdapter;
import topapp.id.app.smartlivingcost.Data.AllReportedCommentItem;
import topapp.id.app.smartlivingcost.Data.ResponseReportedComment;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

public class ReportedSingleMenu extends AppCompatActivity {
    TextView deskripsi, tv_nodata, tv_rating, tv_harga;
    ImageView imvMenu;
    Switch setstatus;
    int idmenu, suspendmenu, hargamenu;
    String namamenu, gambarmenu, ratingmenu, deskripsimenu;
    Toolbar toolbar;
    BaseApiService mApiService;
    Call<ResponseReportedComment> call;
    SessionManager sessionManager;
    List<AllReportedCommentItem> allMyReportedComment = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_single_menu);

        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        if (b != null) {
            idmenu = b.getInt("idmenu");
            namamenu = b.getString("namamenu");
            gambarmenu = b.getString("gambarmenu");
            suspendmenu = b.getInt("suspendmenu");
            ratingmenu = b.getString("ratingmenu");
            hargamenu = b.getInt("hargamenu");
            deskripsimenu = b.getString("deskripsimenu");
        }

        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(ReportedSingleMenu.this);
        tv_harga = findViewById(R.id.harga_menu);
        tv_rating = findViewById(R.id.rating);
        deskripsi = findViewById(R.id.tvDeskripsi);
        tv_nodata = findViewById(R.id.tv_nodata);
        imvMenu = findViewById(R.id.imv_myMenu);
        recyclerView = findViewById(R.id.rv_review);
        setstatus = findViewById(R.id.switch1);

        toolbar.setTitle(namamenu);

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        tv_harga.setText(String.valueOf(nbFmt.format(hargamenu)));
        Glide.with(ReportedSingleMenu.this).load(gambarmenu).centerCrop().dontAnimate()
                .placeholder(getDrawable(R.drawable.budget)).into(imvMenu);
        tv_rating.setText(ratingmenu);
        if (deskripsimenu.equals("0")) {
            deskripsi.setText("Tidak Ada Deskripsi Menu");
        } else {
            deskripsi.setText(deskripsimenu);

        }

        if (suspendmenu != 0) {
            setstatus.setChecked(true);
            setstatus.setText("Suspended");
        }

        setstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mApiService.suspendMenu("Bearer " + sessionManager.getToken(), idmenu, 1)
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        suspendmenu = 1;
                                        setstatus.setText("Suspended");
                                    } else {
                                        Toast.makeText(ReportedSingleMenu.this, "Masalah Jaringan", Toast.LENGTH_SHORT).show();
                                        suspendmenu = 0;
                                        setstatus.setText("Unsuspend");
                                        setstatus.setChecked(false);
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    Toast.makeText(ReportedSingleMenu.this, "Masalah Jaringan", Toast.LENGTH_SHORT).show();
                                    suspendmenu = 0;
                                    setstatus.setText("Unsuspend");
                                    setstatus.setChecked(false);
                                }
                            });
                } else {
                    mApiService.suspendMenu("Bearer " + sessionManager.getToken(), idmenu, 0)
                            .enqueue(new Callback<ResponseBody>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        suspendmenu = 0;
                                        setstatus.setText("Unsuspend");
                                    } else {
                                        Toast.makeText(ReportedSingleMenu.this, "Masalah Jaringan", Toast.LENGTH_SHORT).show();
                                        suspendmenu = 1;
                                        setstatus.setText("Suspended");
                                        setstatus.setChecked(true);
                                    }
                                }

                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                    Toast.makeText(ReportedSingleMenu.this, "Masalah Jaringan", Toast.LENGTH_SHORT).show();
                                    suspendmenu = 1;
                                    setstatus.setText("Suspended");
                                    setstatus.setChecked(true);
                                }
                            });
                }
            }
        });

        getReview();
    }

    private void getReview() {

        call = mApiService.getReportedCommment("Bearer " + sessionManager.getToken(), idmenu);
        call.enqueue(new Callback<ResponseReportedComment>() {
            @Override
            public void onResponse(@NonNull Call<ResponseReportedComment> call, @NonNull Response<ResponseReportedComment> response) {
                if (response.code() == 200) {
                    allMyReportedComment = response.body().getGetAllReportedComment();

                    layoutManager = new LinearLayoutManager(ReportedSingleMenu.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new ReportCommentRVAdapter(allMyReportedComment);
                    recyclerView.setAdapter(adapter);
                } else if (response.code() == 404) {
                    tv_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else if (response.code() == 401) {
                    Toast.makeText(ReportedSingleMenu.this, "Akun Anda Bermasalah, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReportedSingleMenu.this, "Gagal Mendapatkan Review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseReportedComment> call, @NonNull Throwable t) {
                Toast.makeText(ReportedSingleMenu.this, "Masalah Jaringan", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
