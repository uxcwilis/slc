package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import topapp.id.app.smartlivingcost.Data.AllMyMenuItem;
import topapp.id.app.smartlivingcost.R;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class MyMenuRVAdapter extends RecyclerView.Adapter<MyMenuRVAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<AllMyMenuItem> myMenuItems;

    private Context context;

    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public MyMenuRVAdapter(List<AllMyMenuItem> data) {
        super();
        this.myMenuItems = data;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView NamaMenu, HargaMenu, ratingMenu, deskripsiMenu, Status;
        ImageView ImvMenu;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            NamaMenu = itemView.findViewById(R.id.nama_menu);
            HargaMenu = itemView.findViewById(R.id.harga_menu);
            ImvMenu = itemView.findViewById(R.id.imv_resto);
            ratingMenu = itemView.findViewById(R.id.rating);
            deskripsiMenu = itemView.findViewById(R.id.menu_deskripsi);
            Status = itemView.findViewById(R.id.tv_statusResto);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MyMenuRVAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymenu_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final AllMyMenuItem data = myMenuItems.get(position);
        final String NamaMenu = data.getNama_menu();
        final String Deskripsi = data.getMenu_deskripsi();
        final int Harga = data.getHarga();
        final String rating = data.getRating();
        final String GambarMenu = data.getGambar_menu();
        final int Status = data.getStatus();

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);


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

        holder.HargaMenu.setText(String.valueOf(nbFmt.format(Harga)));
        holder.NamaMenu.setText(NamaMenu);
        if (!Deskripsi.equals("0")) {
            holder.deskripsiMenu.setText(Deskripsi);
        }
        holder.ratingMenu.setText(rating);
        if (Status != 1) {
            holder.Status.setText("Habis");
            holder.Status.setBackgroundResource(R.drawable.rounded_corner_red);
        }

    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myMenuItems.size();
    }

}