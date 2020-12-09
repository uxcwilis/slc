package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import topapp.id.app.smartlivingcost.Activity.MainActivity;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.ReviewModel;
import topapp.id.app.smartlivingcost.R;
import topapp.id.app.smartlivingcost.Rest.BaseApiService;
import topapp.id.app.smartlivingcost.Rest.UtilsApi;
import topapp.id.app.smartlivingcost.Util.SessionManager;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class NotReviewedRVAdapter extends RecyclerView.Adapter<NotReviewedRVAdapter.ViewHolder> {
    List<ReviewModel> data;
    private DBSLC MyDatabase;
    private SessionManager sessionManager;
    private BaseApiService mApiService;
    private Context context;


    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public NotReviewedRVAdapter(List<ReviewModel> data) {
        super();
        this.data = data;
        setHasStableIds(true);

    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView NamaMenu, NamaResto, BtnReview, BtnReport, Tanggal;


        ViewHolder(View itemView) {
            super(itemView);
            mApiService = UtilsApi.getAPIService();
            MyDatabase = new DBSLC(context);
            sessionManager = new SessionManager(context);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            NamaMenu = itemView.findViewById(R.id.nama_menu);
            NamaResto = itemView.findViewById(R.id.nama_resto);
            BtnReview = itemView.findViewById(R.id.btn_review);
            BtnReport = itemView.findViewById(R.id.btn_report);
            Tanggal = itemView.findViewById(R.id.tanggal_review);
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.notreviewed_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String set_tgl_input = formatter.format(todayDate);
        ReviewModel single = data.get(position);

        final String namamenu = single.getNamaMenu();
        final String namaresto = single.getNamaResto();
        final String idmenu = single.getIdMenu();
        final String idreview = single.getIdReview();
        final String reported = single.getReported();
        final String reviewed = single.getReviewed();
        final String tanggal = single.getTgl();

        holder.NamaResto.setText(namaresto);
        holder.NamaMenu.setText(namamenu);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;

        try {
            date = originalFormat.parse(tanggal);
            String jadi = String.valueOf(targetFormat.format(date));
            holder.Tanggal.setText(jadi);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (reviewed.equals("1")) {
            holder.BtnReview.setVisibility(View.GONE);
        }

        if (reported.equals("1")) {
            holder.BtnReport.setVisibility(View.GONE);
        }

        holder.BtnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.review_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                final RatingBar ratingBar = mView.findViewById(R.id.ratingBar);
                final EditText comment = mView.findViewById(R.id.comment);

                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                if (comment.getText().toString().equals("") || comment.getText().toString().isEmpty()) {
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf")
                                            .setContentText("Silahkan Tambah Review Anda")
                                            .show();
                                } else {
                                    mApiService.createReview("Bearer " + sessionManager.getToken(), Integer.parseInt(idmenu), Integer.parseInt(sessionManager.getId()),
                                            comment.getText().toString(), (int) ratingBar.getRating(), set_tgl_input)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                    if (response.code() == 200) {
                                                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Berhasil")
                                                                .setContentText("Review Terkirim")
                                                                .show();
                                                        SQLiteDatabase database = MyDatabase.getWritableDatabase();

                                                        //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
                                                        ContentValues values = new ContentValues();
                                                        values.put(DBSLC.Review.Reviewed, 1);

                                                        //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
                                                        String selection = DBSLC.Review.IdReview + " LIKE ?";
                                                        String[] selectionArgs = {idreview};
                                                        database.update(DBSLC.Review.NamaTabelReview, values, selection, selectionArgs);
                                                        MainActivity.performReOpenInfo();
                                                    } else if (response.code() == 401) {
                                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Akun Bermasalah")
                                                                .setContentText("Silahkan Isi Login Kembali")
                                                                .show();
                                                    } else {
                                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Kesalahan Teknis")
                                                                .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                                                .show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Masalah Jaringan")
                                                            .setContentText("Periksa Kembali Koneksi Anda")
                                                            .show();
                                                }
                                            });
                                }

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
            }
        });

        holder.BtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.user_report, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                final EditText report = mView.findViewById(R.id.report);

                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                if (report.getText().toString().equals("") || report.getText().toString().isEmpty()) {
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Maaf")
                                            .setContentText("Silahkan Masukkan Keluhan Anda")
                                            .show();
                                } else {
                                    mApiService.sendReport("Bearer " + sessionManager.getToken(), Integer.parseInt(idmenu), report.getText().toString())
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                    if (response.code() == 200) {
                                                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Berhasil")
                                                                .setContentText("Laporan Terkirim")
                                                                .show();
                                                        SQLiteDatabase database = MyDatabase.getWritableDatabase();

                                                        //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
                                                        ContentValues values = new ContentValues();
                                                        values.put(DBSLC.Review.Reported, 1);

                                                        //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
                                                        String selection = DBSLC.Review.IdReview + " LIKE ?";
                                                        String[] selectionArgs = {idreview};
                                                        database.update(DBSLC.Review.NamaTabelReview, values, selection, selectionArgs);
                                                        MainActivity.performReOpenInfo();
                                                    } else if (response.code() == 401) {
                                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Akun Bermasalah")
                                                                .setContentText("Silahkan Isi Login Kembali")
                                                                .show();
                                                    } else {
                                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                                .setTitleText("Kesalahan Teknis")
                                                                .setContentText("Silahkan Tekan Tombol Simpan Kembali")
                                                                .show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                            .setTitleText("Masalah Jaringan")
                                                            .setContentText("Periksa Kembali Koneksi Anda")
                                                            .show();
                                                }
                                            });
                                }

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
            }
        });

    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}