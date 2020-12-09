package topapp.id.app.smartlivingcost.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Activity.CategoryDetailTrans;
import topapp.id.app.smartlivingcost.Adapter.CategoryRVDashboard;
import topapp.id.app.smartlivingcost.Adapter.LineCharMarker;
import topapp.id.app.smartlivingcost.Adapter.MyValueFormatter;
import topapp.id.app.smartlivingcost.Adapter.RemindMeAngsuranRVAdapter;
import topapp.id.app.smartlivingcost.Adapter.RemindMeRVAdapter;
import topapp.id.app.smartlivingcost.Model.PlanningTableModel;
import topapp.id.app.smartlivingcost.Model.RemindMeBerandaModel;
import topapp.id.app.smartlivingcost.Model.TransaksiBerandaModel;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

import static androidx.core.content.ContextCompat.getColor;


public class Beranda extends Fragment {
    private DBSLC MyDatabase;
    private List<TransaksiBerandaModel> transaksiBerandaModels = new ArrayList<>();
    private List<PlanningTableModel> myPlanning = new ArrayList<>();
    private List<RemindMeBerandaModel> remindMe = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView, recyclerView2, recyclerView_remindme, recyclerView_angsuran;
    private static TabLayout tabLayout;
    private RecyclerView.LayoutManager layoutManager, layoutManager2, layoutManager_remindme, layoutManager_angsuran;
    private ImageView imv_profil_text;
    private PieChart chart, chart1;
    private TextDrawable builder;
    private ColorGenerator generator;
    private LineChart lineChart;
    private TextView tv_total_peng_bulan, tv_total_pem_bulan, tv_name_dashboard, tv_nodata;
    private CategoryRVDashboard adapter;
    private SessionManager sessionManager;
    private EditText dialogName, dialogPassword, dialogEmail;
    private BaseApiService mApiService;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_beranda, container, false);
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        Toolbar toolbar = rootView.findViewById(R.id.toolbar1);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        setHasOptionsMenu(true);
        Context mContext = getActivity();
        mApiService = UtilsApi.getAPIService();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String stringMonth = (String) android.text.format.DateFormat.format("MMMM", date);
        String year = (String) android.text.format.DateFormat.format("yyyy", date);

        getActivity().setTitle(stringMonth + " " + year);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        MyDatabase = new DBSLC(getActivity().getBaseContext());
        sessionManager = new SessionManager(getActivity());
        tabLayout = rootView.findViewById(R.id.piechart_tab);
        tv_name_dashboard = rootView.findViewById(R.id.tv_name_dashboard);
        tv_name_dashboard.setText(sessionManager.getName());

        RelativeLayout rlnodata = rootView.findViewById(R.id.rl_nodata);
        RelativeLayout rlcontent = rootView.findViewById(R.id.rl_content);
        tv_nodata = rootView.findViewById(R.id.tv_nodata);

        if (MyDatabase.countTrans() != 0) {

            getData("0");
            recyclerView = rootView.findViewById(R.id.rv_pc0_kat);
            recyclerView2 = rootView.findViewById(R.id.rv_pc1_kat);
            layoutManager = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            layoutManager2 = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            createRV0();


            imv_profil_text = rootView.findViewById(R.id.imv_profil_text);
            generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color1 = generator.getRandomColor();
            builder = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(15)
                    .endConfig()
                    .buildRound(printFirst(sessionManager.getName()), color1);
            imv_profil_text.setImageDrawable(builder);
            TextView sisa_bulanlalu = rootView.findViewById(R.id.tv_total_bulan_lalu);
            TextView tv_rata_pengeluaran = rootView.findViewById(R.id.tv_rata_pengeluaran);
            TextView tv_bulanlalu = rootView.findViewById(R.id.tv_bulankemarin);
            TextView tv_perbandingan = rootView.findViewById(R.id.tv_perbandingan);
            tv_total_pem_bulan = rootView.findViewById(R.id.tv_total_pemasukan_bulanan);
            tv_total_peng_bulan = rootView.findViewById(R.id.tv_total_pengeluaran_bulanan);

            Locale localeID = new Locale("in", "ID");
            NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(localeID);
            df.setGroupingUsed(true);
            df.setGroupingSize(3);

            int bulanlalu = MyDatabase.rataPengeluaranBulanKemarin();
            int bulanini = MyDatabase.rataPengeluaranBulanini();
            double perubahan = 0;
            TextView tv_ket_perbandingan = rootView.findViewById(R.id.tv_ket_perbandingan);
            if (MyDatabase.countTransOut() != 0) {
                if (bulanlalu != 0 && bulanini != 0) {
                    double perubahan1 = (-1 * bulanini) - (-1 * bulanlalu);
                    double perubahan2 = perubahan1/ (-1*bulanlalu);
                    perubahan = perubahan2 * 100;
                    Log.d("isi_perubahan", String.valueOf(df.format(perubahan)));
                    if (bulanini < bulanlalu) {
                        if (perubahan < 0) {
                            tv_perbandingan.setText(df.format(-1 * (int) perubahan) + "%");
                            tv_ket_perbandingan.setText("Lebih Boros");
                        } else {
                            tv_perbandingan.setText(df.format((int) perubahan) + "%");
                            tv_ket_perbandingan.setText("Lebih Boros");
                        }
                    } else if (bulanini > bulanlalu) {
                        if (perubahan < 0) {
                            tv_perbandingan.setText(df.format(-1 * (int) perubahan) + "%");
                            tv_perbandingan.setTextColor(getColor(getActivity(), R.color.colorPrimary));
                            tv_perbandingan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
                            setTextViewDrawableColor(tv_perbandingan);
                            tv_ket_perbandingan.setText("Lebih Hemat");
                        } else {
                            tv_perbandingan.setText(df.format((int) perubahan) + "%");
                            tv_perbandingan.setTextColor(getColor(getActivity(), R.color.colorPrimary));
                            tv_perbandingan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
                            setTextViewDrawableColor(tv_perbandingan);
                            tv_ket_perbandingan.setText("Lebih Hemat");
                        }
                    } else {
                        tv_perbandingan.setText("0%");
                        tv_ket_perbandingan.setText("Setara");
                    }
                } else {
                    if (bulanini < bulanlalu) {
                        if (bulanini < 0) {
                            tv_perbandingan.setText(df.format(-1 * bulanini) + "%");
                            tv_ket_perbandingan.setText("Lebih Boros");
                        } else {
                            tv_perbandingan.setText(df.format(bulanini) + "%");
                            tv_ket_perbandingan.setText("Lebih Boros");
                        }
                    } else if (bulanini > bulanlalu) {
                        if (bulanlalu < 0) {
                            tv_perbandingan.setText(df.format(-1 * bulanlalu) + "%");
                            tv_perbandingan.setTextColor(getColor(getActivity(), R.color.colorPrimary));
                            tv_perbandingan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
                            setTextViewDrawableColor(tv_perbandingan);
                            tv_ket_perbandingan.setText("Lebih Hemat");
                        } else {
                            tv_perbandingan.setText(df.format(bulanlalu) + "%");
                            tv_perbandingan.setTextColor(getColor(getActivity(), R.color.colorPrimary));
                            tv_perbandingan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
                            setTextViewDrawableColor(tv_perbandingan);
                            tv_ket_perbandingan.setText("Lebih Hemat");
                        }
                    } else {
                        tv_perbandingan.setText("0%");
                        tv_ket_perbandingan.setText("Setara");
                    }
                }
            } else {
                tv_perbandingan.setText("0%");
                tv_ket_perbandingan.setText("Setara");
            }

            tv_rata_pengeluaran.setText(String.valueOf(nbFmt.format(bulanini)));
            tv_total_pem_bulan.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal_category_currentmonth("1"))));
            tv_total_peng_bulan.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal_category_currentmonth("0"))));
            TextView tv_pengeluaran_max = rootView.findViewById(R.id.tv_pengeluaran_max);
            Calendar calendar1 = Calendar.getInstance();
            int lastDay = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
            int currentDay = calendar1.get(Calendar.DAY_OF_MONTH);
            int daysLeft = (lastDay - currentDay) + 1;

            int sisa_total = (MyDatabase.gettotal("1", "0", "0") + MyDatabase.gettotal("0", "0", "0"));
            if (sisa_total < 100000) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Keuangan Menipis")
                        .setContentText("Sisa Keuangan Anda Rp" + sisa_total)
                        .setConfirmText("Terimakasih")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }

            int sisa = (MyDatabase.gettotal("1", "0", "0") + MyDatabase.gettotal("0", "0", "0")) / daysLeft;
            double bulat_turun = sisa / 1000;
            bulat_turun = Math.floor(bulat_turun);
            bulat_turun *= 1000;
            int rounded = (int) bulat_turun;
            if (sisa < 0) {
                tv_pengeluaran_max.setText("-" + nbFmt.format(rounded));
            } else {
                tv_pengeluaran_max.setText(String.valueOf(nbFmt.format(rounded)));
            }
            if (MyDatabase.sisaBulanKemarin() < 0) {
                sisa_bulanlalu.setText(String.valueOf(nbFmt.format(MyDatabase.sisaBulanKemarin())));
                sisa_bulanlalu.setTextColor(ContextCompat.getColor(getActivity(), R.color.RedTheme));
            } else {
                sisa_bulanlalu.setText(String.valueOf(nbFmt.format(MyDatabase.sisaBulanKemarin())));
            }
            tv_bulanlalu.setText("Bulan Lalu " + nbFmt.format(bulanlalu));

            chart = rootView.findViewById(R.id.pieChart0);
            chart1 = rootView.findViewById(R.id.pieChart1);
            lineChart = rootView.findViewById(R.id.lineChart);

            setLineChart();

            creatTablayout();
            createPieChar0();

            if (MyDatabase.countTrans() == 0) {
                rlnodata.setVisibility(View.VISIBLE);
                rlcontent.setVisibility(View.GONE);
            } else {
                rlnodata.setVisibility(View.GONE);
                rlcontent.setVisibility(View.VISIBLE);
            }

//        -----------------------------CARD VIEW REKOMENDASI-----------------------------------------------------
            Date todayDate = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal_hari_ini = formatter.format(todayDate);
            recyclerView_remindme = rootView.findViewById(R.id.rl_remindme);
            layoutManager_remindme = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            getRemindMe(tanggal_hari_ini);

            recyclerView_angsuran = rootView.findViewById(R.id.rl_angsuran_bulan_ini);
            layoutManager_angsuran = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            getAngsuran(tanggal_hari_ini);

            if (myPlanning.isEmpty() && remindMe.isEmpty()) {
                CardView cv_smart = rootView.findViewById(R.id.cv_smart);
                cv_smart.setVisibility(View.GONE);
            }
        } else {
            rlnodata.setVisibility(View.VISIBLE);
            rlcontent.setVisibility(View.GONE);
        }


        return rootView;
    }

    private void setTextViewDrawableColor(TextView textView) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private void createRV1() {
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        CategoryRVDashboard adapter2 = new CategoryRVDashboard(transaksiBerandaModels);
        adapter2.notifyDataSetChanged();
        adapter2.setOnItemClickListener(new CategoryRVDashboard.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(), CategoryDetailTrans.class);
                intent.putExtra("idcat", transaksiBerandaModels.get(position).getIDCat());
                intent.putExtra("namacat", transaksiBerandaModels.get(position).getNamaCat());
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
            }
        });
        recyclerView2.setAdapter(adapter);
    }

    private void createRV0() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CategoryRVDashboard(transaksiBerandaModels);
        adapter.setOnItemClickListener(new CategoryRVDashboard.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getActivity(), CategoryDetailTrans.class);
                intent.putExtra("idcat", transaksiBerandaModels.get(position).getIDCat());
                intent.putExtra("namacat", transaksiBerandaModels.get(position).getNamaCat());
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void creatTablayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"));
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"));
        if (transaksiBerandaModels.isEmpty()) {
            tv_nodata.setVisibility(View.VISIBLE);
            chart1.setVisibility(View.GONE);
            chart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            tv_total_pem_bulan.setVisibility(View.GONE);
            tv_total_peng_bulan.setVisibility(View.GONE);

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    cleardata();
                    getData("0");
                    if (transaksiBerandaModels.isEmpty()) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        chart1.setVisibility(View.GONE);
                        chart.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        tv_total_pem_bulan.setVisibility(View.GONE);
                        tv_total_peng_bulan.setVisibility(View.GONE);
                    } else {
                        createPieChar0();
                        chart1.setVisibility(View.GONE);
                        chart.setVisibility(View.VISIBLE);
                        createRV0();
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView2.setVisibility(View.GONE);
                        tv_total_pem_bulan.setVisibility(View.GONE);
                        tv_total_peng_bulan.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.GONE);
                    }

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    cleardata();
                    getData("1");
                    if (transaksiBerandaModels.isEmpty()) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        chart1.setVisibility(View.GONE);
                        chart.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.GONE);
                        tv_total_pem_bulan.setVisibility(View.GONE);
                        tv_total_peng_bulan.setVisibility(View.GONE);
                    } else {
                        chart.setVisibility(View.GONE);
                        chart1.setVisibility(View.VISIBLE);
                        createPieChar1();
                        createRV1();
                        recyclerView2.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        tv_total_pem_bulan.setVisibility(View.VISIBLE);
                        tv_total_peng_bulan.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });
    }

    private void createPieChar0() {

        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setHoleRadius(45f);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setEntryLabelColor(Color.parseColor("#737373"));
        chart.setData(generatePieData());
        chart.animateXY(2000, 2000, Easing.EaseOutBounce);
        chart.invalidate();
    }

    private void createPieChar1() {

        chart1.getDescription().setEnabled(false);
        chart1.setUsePercentValues(true);
        chart1.setHoleRadius(45f);
        chart1.getLegend().setEnabled(false);
        chart1.setTouchEnabled(false);
        chart1.setEntryLabelColor(Color.parseColor("#737373"));
        chart1.setData(generatePieData());
        chart1.animateXY(2000, 2000, Easing.EaseOutBounce);
        chart.invalidate();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        MenuItem edit_profil = menu.findItem(R.id.edit_profil);
        MenuItem log_out = menu.findItem(R.id.logout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_profil:
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                alertDialogBuilderUserInput.setView(mView);

                dialogName = mView.findViewById(R.id.dialogName);
                dialogPassword = mView.findViewById(R.id.dialogPassword);
                dialogEmail = mView.findViewById(R.id.dialogEmail);

                dialogName.setText(sessionManager.getName());
                dialogPassword.setText(sessionManager.getPassword());
                dialogEmail.setText(sessionManager.getEmail());
                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                UpdateProfile();
                            }
                        })

                        .setNegativeButton("Batal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
                return true;
            case R.id.logout:
                logout();
                return true;
            case R.id.backup:
                backup();
                return true;
        }
        return false;
    }

    private void backup() {
        @SuppressLint("SdCardPath") File file = new File("/data/data/suska.uin.tif.smartlivingcost/databases/dbslc.db");
        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("sqlite_backup", file.getName(), requestBody);
        final SweetAlertDialog loading = new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Mencadangkan Data Diri Anda");
        loading.setCancelable(false);
        loading.show();
        mApiService.backup("Bearer " + sessionManager.getToken(), fileToUpload)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.code() == 200) {
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Data Anda Telah Tersimpan")
                                    .show();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Login Kembali")
                                    .show();
                        } else if (response.code() == 404) {
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Tidak Ditemukan")
                                    .setContentText("Silahkan Periksa Kembali Data Anda")
                                    .show();
                        } else {
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal")
                                    .setContentText("Terjadi Kesalahan Teknis, Silahkan Cadangkan Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                .show();
                    }
                });

    }

    private void logout() {

        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Keluar?")
                .setContentText("Apakah anda yakin untuk keluar dari akun anda?\n\n Pastikan Data Anda Telah dicadangkan")
                .setCancelText("Tidak")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Objects.requireNonNull(getActivity()).getBaseContext().deleteDatabase("dbslc.db");
                        sessionManager.logout();
                        getActivity().finish();
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

    private void UpdateProfile() {
        final SweetAlertDialog loading = new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText("Memperbaharui Data Diri");
        loading.setCancelable(false);
        loading.show();
        mApiService.updateProfil("Bearer " + sessionManager.getToken(), dialogEmail.getText().toString(), dialogName.getText().toString(), dialogPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        loading.dismissWithAnimation();
                        if (response.isSuccessful()) {
                            sessionManager.updateDetail(dialogName.getText().toString(),
                                    dialogEmail.getText().toString(),
                                    dialogPassword.getText().toString());
                            int color1 = generator.getRandomColor();
                            builder = TextDrawable.builder()
                                    .beginConfig()
                                    .withBorder(15)
                                    .endConfig()
                                    .buildRound(printFirst(sessionManager.getName()), color1);
                            tv_name_dashboard.setText(sessionManager.getName());
                            imv_profil_text.setImageDrawable(builder);
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Berhasil")
                                    .setContentText("Data Anda Telah Diperbaharui")
                                    .show();
                        } else if (response.code() == 401) {
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Akun Bermasalah")
                                    .setContentText("Silahkan Login Kembali")
                                    .show();
                        } else {
                            new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal")
                                    .setContentText("Terjadi Kesalahan Teknis, Silahkan Perbaharui Kembali")
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        loading.dismissWithAnimation();
                        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setContentText("Periksa Kembali Jarigan Internet Anda")
                                .show();
                    }
                });
    }

    private PieData generatePieData() {

        int count = transaksiBerandaModels.size();
        int color;
        int[] warna = new int[count];

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float total = (float) MyDatabase.gettotal_category(transaksiBerandaModels.get(i).getIDCat());
            if (total < 0) {
                total = total * -1;
            }
            entries1.add(new PieEntry(total, transaksiBerandaModels.get(i).getNamaCat()));
            color = generator.getRandomColor();
            warna[i] = color;
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Pengeluaran");

        ds1.setColors(warna);
        ds1.setValueFormatter(new MyValueFormatter());
        ds1.setValueTextColor(Color.parseColor("#737373"));
        ds1.setValueTextSize(12f);
        ds1.setSliceSpace(3f);
        ds1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ds1.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        ds1.setSelectionShift(70f);
        ds1.setUsingSliceColorAsValueLineColor(true);
        ds1.setValueLinePart1Length(0.8f);
        ds1.setValueLinePart2Length(0.04f);

        return new PieData(ds1);
    }

    @SuppressLint("Recycle")
    public void getData(String jeniskat) {
        transaksiBerandaModels.clear();
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT Transaksi.IdTrans, Transaksi.IdKat, Kategori.IdIconKat, Kategori.NamaKat, " +
                "Transaksi.KetTrans, Transaksi.GbrTrans, Transaksi.JumlahTrans, Kategori.JenisKat, " +
                "Transaksi.TglTrans, Kategori.BudgetKat FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON " +
                "Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = " + jeniskat + " AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now')) GROUP BY Transaksi.IdKat ORDER BY SUM(Transaksi.JumlahTrans) ASC", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            TransaksiBerandaModel transaksiBerandaModel = new TransaksiBerandaModel(
                    cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9));
            transaksiBerandaModels.add(transaksiBerandaModel);

        }


    }

    @SuppressLint("Recycle")
    private void getRemindMe(String tanggal_harini) {
        remindMe.clear();
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT Transaksi.*, Kategori.IdIconKat, Kategori.NamaKat, Kategori.JenisKat FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON " +
                "Transaksi.IdKat = Kategori.IdKat WHERE Transaksi.Remind_at = '" + tanggal_harini + "'", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);

            RemindMeBerandaModel remindMeBerandaModel = new RemindMeBerandaModel(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                    cursor.getString(11));
            remindMe.add(remindMeBerandaModel);
        }

        if (remindMe.isEmpty()) {
            recyclerView_remindme.setVisibility(View.GONE);
        }
        recyclerView_remindme.setLayoutManager(layoutManager_remindme);
        recyclerView_remindme.setHasFixedSize(true);
        RemindMeRVAdapter adapter_remindme = new RemindMeRVAdapter(remindMe);
        adapter_remindme.notifyDataSetChanged();
        adapter_remindme.setOnItemClickListener(new RemindMeRVAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Bundle b = new Bundle();
                b.putString("idtrans", remindMe.get(position).getIdTrans_remindme());
                b.putString("idkat", remindMe.get(position).getIdKat_remindme());
                b.putString("tgltrans", remindMe.get(position).getTglTrans_remindme());
                b.putString("jumlahtrans", remindMe.get(position).getJumlahTrans_remindme());
                b.putString("kettrans", remindMe.get(position).getKetTrans_remindme());
                b.putString("gbrtrans", remindMe.get(position).getGbrTrans_remindme());
                b.putString("tglinput", remindMe.get(position).getTglInputTrans_remindme());
                b.putString("jaminput", remindMe.get(position).getJamInputTrans_remindme());
                b.putString("remindat", remindMe.get(position).getRemind_At_remindme());
                b.putString("namakat", remindMe.get(position).getNamaKat_remindme());
                b.putString("idiconkat", remindMe.get(position).getIdIconKat_remindme());
                b.putString("jeniskat", remindMe.get(position).getJenisKat_remindme());

                Input fragB = new Input();
                fragB.setArguments(b);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, fragB);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        recyclerView_remindme.setAdapter(adapter_remindme);

    }

    @SuppressLint("Recycle")
    private void getAngsuran(String tanggal_harini) {
        myPlanning.clear();
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.Planning.NamaTabelPlanning + " WHERE Planning.TargetDate >= '" + tanggal_harini + "'", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);
            PlanningTableModel planningTableModel = new PlanningTableModel(cursor.getString(0), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            myPlanning.add(planningTableModel);
        }

        if (myPlanning.isEmpty()) {
            recyclerView_angsuran.setVisibility(View.GONE);
        }
        recyclerView_angsuran.setLayoutManager(layoutManager_angsuran);
        recyclerView_angsuran.setHasFixedSize(true);
        RemindMeAngsuranRVAdapter adapter_angsuran = new RemindMeAngsuranRVAdapter(myPlanning);
        adapter_angsuran.notifyDataSetChanged();
        recyclerView_angsuran.setAdapter(adapter_angsuran);

    }

    private void cleardata() {
        transaksiBerandaModels.clear();
    }

    @SuppressLint("Recycle")
    private void setLineChart() {
        lineChart.getLegend().setEnabled(false);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setKeepPositionOnRotation(true);
        String tgl;
        ArrayList lineEntries = new ArrayList<Entry>();
        ArrayList xVal = new ArrayList<>();
        ArrayList yVal = new ArrayList<>();
        int countt = MyDatabase.sisaBulanKemarin();
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();

        Cursor cursor;
        String sql1 = "SELECT Transaksi.TglTrans, Sum(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON " +
                "Transaksi.IdKat = Kategori.IdKat WHERE Transaksi.TglTrans Between date('now','start of month') AND date('now','start of month','+1 month','-1 day') GROUP BY Transaksi.TglTrans ";
        cursor = ReadData.rawQuery(sql1, null);

        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal

        if (cursor.getCount() <= 0) {
            yVal.add((float) countt);
            xVal.add((float) 1);
            lineEntries.add(new Entry(1, (float) countt));
            lineEntries.add(new Entry(31, (float) countt));
        } else {
            //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
            for (int count = 0; count < cursor.getCount(); count++) {
                cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
                tgl = cursor.getString(0);
                lineEntries.add(new Entry(1, (float) countt));
                countt = countt + Integer.parseInt(cursor.getString(1));
                xVal.add((float) Integer.parseInt(tgl.substring(tgl.length() - 2)));
                yVal.add((float) countt);

            }

            for (int count = 0; count < cursor.getCount(); count++) {
                lineEntries.add(new Entry((float) xVal.get(count), (float) yVal.get(count)));
                if (count == cursor.getCount() - 1 && count < 32) {
                    lineEntries.add(new Entry(31, (float) yVal.get(count)));
                }
            }
        }


        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Oil Price");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary));
        lineDataSet.setCircleColor(getColor(getActivity(), R.color.white));
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setCircleHoleColor(getColor(getActivity(), R.color.RedTheme));
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);

        lineChart.getDescription().setEnabled(false);
        lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setMarker(new LineCharMarker(context, R.layout.line_char_marker));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateX(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.setExtraOffsets(20f, 0, 40f, 0);
        lineChart.fitScreen();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private static String printFirst(String s) {
        String first = "";
        Pattern p = Pattern.compile("\\b[a-zA-Z]");
        Matcher m = p.matcher(s);

        while (m.find()) {
            first = first + m.group();
        }
        return first;

    }


}
