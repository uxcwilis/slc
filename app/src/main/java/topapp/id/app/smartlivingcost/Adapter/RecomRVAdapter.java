package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import topapp.id.app.smartlivingcost.Activity.FrontMenu;
import topapp.id.app.smartlivingcost.Activity.FrontResto;
import topapp.id.app.smartlivingcost.Activity.NavigateMe;
import topapp.id.app.smartlivingcost.Data.AllRecomItem;
import topapp.id.app.smartlivingcost.R;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecomRVAdapter extends RecyclerView.Adapter<RecomRVAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<AllRecomItem> data;
    private Context context;


    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public RecomRVAdapter(List<AllRecomItem> data) {
        super();
        this.data = data;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView IdResto, IdMenu, NamaResto, NamaMenu, Lat, Lng, HargaMenu, Distance, Arahkan, Rating, deskripsiList;
        ImageView ImvMenu;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            IdResto = itemView.findViewById(R.id.id_resto);
            IdMenu = itemView.findViewById(R.id.id_menu);
            NamaResto = itemView.findViewById(R.id.nama_resto);
            NamaMenu = itemView.findViewById(R.id.nama_menu);
            Lat = itemView.findViewById(R.id.lat_resto);
            Lng = itemView.findViewById(R.id.lng_resto);
            HargaMenu = itemView.findViewById(R.id.harga_menu);
            Distance = itemView.findViewById(R.id.distance_resto);
            Arahkan = itemView.findViewById(R.id.tv_arahkan);
            ImvMenu = itemView.findViewById(R.id.imv_resto);
            Rating = itemView.findViewById(R.id.rating);
            deskripsiList = itemView.findViewById(R.id.menu_deskripsi);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }

        @Override
        public boolean onLongClick(View view) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            final AllRecomItem single = data.get(getAdapterPosition());
            @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.longclick_recom_menu, null);
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
            alertDialogBuilderUserInput.setView(mView);

            Button btn_resto = mView.findViewById(R.id.btn_detail_resto);
            Button btn_review = mView.findViewById(R.id.btn_detail_menu);

            alertDialogBuilderUserInput.setCancelable(true);

            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();

            btn_resto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, FrontResto.class);
                    i.putExtra("statususer",0);
                    i.putExtra("namaresto",single.getNama_resto());
                    i.putExtra("alamatresto",single.getAlamat());
                    i.putExtra("statusresto",1);
                    i.putExtra("idresto",Integer.parseInt(single.getId_resto()));
                    i.putExtra("gbrresto",single.getGambar_resto());
                    i.putExtra("latresto",single.getLat_resto().doubleValue());
                    i.putExtra("lngresto",single.getLng_resto().doubleValue());
                    context.startActivity(i);
                    alertDialogAndroid.dismiss();
                }
            });

            btn_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, FrontMenu.class);
                    i.putExtra("idmenu", Integer.parseInt(single.getId_menu()));
                    i.putExtra("namamenu", single.getNama_menu());
                    i.putExtra("deskripsimenu", single.getMenu_deskripsi());
                    i.putExtra("hargamenu", single.getHarga_menu());
                    i.putExtra("ratingmenu", single.getRating());
                    i.putExtra("gambarmenu", single.getGambar_menu());
                    i.putExtra("statusmenu", 1);
                    i.putExtra("statususer", 0);
                    context.startActivity(i);
                    alertDialogAndroid.dismiss();
                }
            });

            return true;
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecomRVAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.recom_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final AllRecomItem single = data.get(position);
        final String IdResto = single.getId_resto();
        final String IdMenu = single.getId_menu();
        final String NamaResto = single.getNama_resto();
        final String NamaMenu = single.getNama_menu();
        final BigDecimal Lat = single.getLat_resto();
        final BigDecimal Lng = single.getLng_resto();
        final Double distance = single.getDistance();
        final int Harga = single.getHarga_menu();
        final String GambarMenu = single.getGambar_menu();
        final String RatingMenu = single.getRating();
        final String deskripsi = single.getMenu_deskripsi();
        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        DecimalFormat df2 = new DecimalFormat("####0.00");

        double jarak;
        String djarak;
        if (distance > 1000) {
            jarak = distance / 1000;
            djarak = df2.format(jarak) + " Km";
        } else {
            jarak = distance;
            djarak = df2.format(jarak) + " m";
        }

        if (!GambarMenu.equals("0")) {
            holder.ImvMenu.setColorFilter(Color.parseColor("#00000000"));
            Glide.with(context).load(GambarMenu).centerCrop().fitCenter().dontAnimate()
                    .error(R.drawable.camera).placeholder(R.drawable.gallery).into(holder.ImvMenu);
            holder.ImvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView popupImv = new ImageView(context);
                    Glide.with(context).load(GambarMenu)
                            .dontAnimate()
                            .error(R.drawable.camera)
                            .override(900, 900)
                            .placeholder(R.drawable.gallery)
                            .into(popupImv);
                    new android.app.AlertDialog.Builder(context)
                            .setView(popupImv)
                            .setCancelable(true)
                            .show();
                }
            });
        }

        if (!deskripsi.equals("0")) {
            holder.deskripsiList.setText(deskripsi);
        }
        holder.Rating.setText(RatingMenu);
        holder.NamaResto.setText(NamaResto);
        holder.NamaMenu.setText(NamaMenu);
        holder.IdResto.setText(IdResto);
        holder.IdMenu.setText(IdMenu);
        holder.Distance.setText(djarak);
        holder.HargaMenu.setText(String.valueOf(nbFmt.format(Harga)));
        holder.Lat.setText(String.valueOf(Lat));
        holder.Lng.setText(String.valueOf(Lng));
        holder.Arahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Menuju Restoran?")
                        .setContentText("Aplikasi akan Menunjukkan Navigasi")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent i = new Intent(v.getContext(), NavigateMe.class);
                                i.putExtra("lat", Lat.doubleValue());
                                i.putExtra("lng", Lng.doubleValue());
                                i.putExtra("nama_resto", NamaResto);
                                i.putExtra("nama_menu", NamaMenu);
                                i.putExtra("harga_menu", Harga);
                                i.putExtra("id_menu", IdMenu);
                                v.getContext().startActivity(i);
                                sDialog.dismissWithAnimation();
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
    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return data.size();
    }

}