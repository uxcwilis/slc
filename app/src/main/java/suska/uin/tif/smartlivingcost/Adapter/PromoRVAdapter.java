package suska.uin.tif.smartlivingcost.Adapter;

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
import suska.uin.tif.smartlivingcost.Activity.FrontMenu;
import suska.uin.tif.smartlivingcost.Activity.FrontResto;
import suska.uin.tif.smartlivingcost.Activity.NavigateMe;
import suska.uin.tif.smartlivingcost.Data.AllPromoItem;
import suska.uin.tif.smartlivingcost.Data.AllRecomItem;
import suska.uin.tif.smartlivingcost.R;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class PromoRVAdapter extends RecyclerView.Adapter<PromoRVAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<AllPromoItem> data;
    private Context context;


    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public PromoRVAdapter(List<AllPromoItem> data) {
        super();
        this.data = data;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView IdResto, IdMenu, KetPromo, HargaPromo, NamaResto, NamaMenu, Lat, Lng, HargaMenu, Distance, Arahkan, Rating, deskripsiList;
        ImageView ImvMenu, ImvPromo;

        ViewHolder(View itemView) {
            super(itemView);
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
            ImvPromo = itemView.findViewById(R.id.imv_promo);
            HargaPromo = itemView.findViewById(R.id.harga_promo);
            KetPromo = itemView.findViewById(R.id.ket_promo);
            Rating = itemView.findViewById(R.id.rating);
            deskripsiList = itemView.findViewById(R.id.menu_deskripsi);
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PromoRVAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final AllPromoItem single = data.get(position);
        final String IdResto = single.getId_resto();
        final String IdMenu = single.getId_menu();
        final String NamaResto = single.getNama_resto();
        final String NamaMenu = single.getNama_menu() +" mendapatkan Promo (" +single.getKeterangan_promo()+ ")";
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

        if (!single.getGambar_promo().equals("0")) {
            holder.ImvPromo.setColorFilter(Color.parseColor("#00000000"));
            Glide.with(context).load(single.getGambar_promo()).centerCrop().fitCenter().dontAnimate()
                    .error(R.drawable.camera).placeholder(R.drawable.gallery).into(holder.ImvPromo);
            holder.ImvPromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView popupImv = new ImageView(context);
                    Glide.with(context).load(single.getGambar_promo())
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
        holder.KetPromo.setText(single.getKeterangan_promo());
        holder.HargaPromo.setText(String.valueOf(nbFmt.format(single.getHarga_promo())));
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
                                i.putExtra("harga_menu", single.getHarga_promo());
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