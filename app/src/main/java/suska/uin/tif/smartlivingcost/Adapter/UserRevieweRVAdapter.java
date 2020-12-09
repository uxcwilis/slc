package suska.uin.tif.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import suska.uin.tif.smartlivingcost.Activity.MainActivity;
import suska.uin.tif.smartlivingcost.Data.AllUserReviewItem;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class UserRevieweRVAdapter extends RecyclerView.Adapter<UserRevieweRVAdapter.ViewHolder> {
    private List<AllUserReviewItem> allUserReview;
    private BaseApiService mApiService;
    private SessionManager sessionManager;
    private Context context;


    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public UserRevieweRVAdapter(List<AllUserReviewItem> allUserReview) {
        super();
        this.allUserReview = allUserReview;
        setHasStableIds(true);

    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView NamaMenu, Tanggal, Rating, Comment;


        ViewHolder(View itemView) {
            super(itemView);
            mApiService = UtilsApi.getAPIService();
            sessionManager = new SessionManager(context);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            NamaMenu = itemView.findViewById(R.id.nama_menu);
            Comment = itemView.findViewById(R.id.comment);
            Rating = itemView.findViewById(R.id.rating);
            Tanggal = itemView.findViewById(R.id.tanggal_review);
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.userreview_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Date todayDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String set_tgl_input = formatter.format(todayDate);
        AllUserReviewItem single = allUserReview.get(position);
        final String namamenu = single.getNama_menu();
        final int idmenu = single.getId_menu();
        final int idreview = single.getId_review();
        final String tanggal = single.getTanggal();
        final String comment = single.getComment();
        final int rating = single.getRating();

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

        holder.Comment.setText(comment);
        holder.Rating.setText(String.valueOf(rating));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.review_dialog_box, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                TextView dialogTitlte = mView.findViewById(R.id.dialogTitle);
                ImageView btn_delete = mView.findViewById(R.id.btn_delete);
                final RatingBar ratingBar = mView.findViewById(R.id.ratingBar);
                final EditText commentt = mView.findViewById(R.id.comment);

                dialogTitlte.setText("Perbaharui Review");
                btn_delete.setVisibility(View.VISIBLE);
                ratingBar.setRating(rating);
                commentt.setText(comment);


                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Perbaharui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                mApiService.updateReview("Bearer " + sessionManager.getToken(), idreview, commentt.getText().toString(),
                                        (int) ratingBar.getRating(), set_tgl_input, idmenu)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("Berhasil")
                                                            .setContentText("Memperbaharui Review")
                                                            .show();
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
                        })
                        .setNegativeButton("Batal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mApiService.deleteReview("Bearer " + sessionManager.getToken(), idreview, idmenu)
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        if (response.code() == 200) {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Berhasil")
                                                    .setContentText("Review Dihapus")
                                                    .show();
                                            MainActivity.performReOpenInfo();
                                            alertDialogAndroid.dismiss();
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
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return allUserReview.size();
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