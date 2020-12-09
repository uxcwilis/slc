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

import java.math.BigDecimal;
import java.util.List;

import topapp.id.app.smartlivingcost.Data.AllMyRestoItem;
import topapp.id.app.smartlivingcost.R;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class MyRestoRVAdapter extends RecyclerView.Adapter<MyRestoRVAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<AllMyRestoItem> myRestoItems;

    private Context context;

    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public MyRestoRVAdapter(List<AllMyRestoItem> myrestoItems) {
        super();
        this.myRestoItems = myrestoItems;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView IdResto, NamaResto, Alamat, IdInputer, Lat, Lng, Status;
        ImageView ImvResto;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            IdResto = itemView.findViewById(R.id.id_resto);
            IdInputer = itemView.findViewById(R.id.id_inputer);
            NamaResto = itemView.findViewById(R.id.nama_resto);
            Alamat = itemView.findViewById(R.id.alamat);
            Lat = itemView.findViewById(R.id.lat_resto);
            Lng = itemView.findViewById(R.id.lng_resto);
            ImvResto = itemView.findViewById(R.id.imv_resto);
            Status = itemView.findViewById(R.id.tv_statusResto);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MyRestoRVAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.myresto_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final AllMyRestoItem data = myRestoItems.get(position);
        final int IdResto = data.getId_resto();
        final int IdInputer = data.getId_inputer();
        final String NamaResto = data.getNama_resto();
        final String Alamat = data.getAlamat();
        final BigDecimal Lat = data.getLat();
        final BigDecimal Lng = data.getLng();
        final String GambarResto = data.getGambar_resto();
        final int Status = data.getStatus();


        if (!GambarResto.equals("0")) {
            holder.ImvResto.setColorFilter(Color.parseColor("#00000000"));
            Glide.with(context).load(GambarResto).centerCrop().fitCenter().dontAnimate()
                    .error(R.drawable.camera).placeholder(R.drawable.gallery).into(holder.ImvResto);
            holder.ImvResto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView popupImv = new ImageView(context);
                    Glide.with(context).load(GambarResto)
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

        holder.NamaResto.setText(NamaResto);
        holder.Alamat.setText(Alamat);
        holder.IdResto.setText(String.valueOf(IdResto));
        holder.IdInputer.setText(String.valueOf(IdInputer));
        holder.Lat.setText(String.valueOf(Lat));
        holder.Lng.setText(String.valueOf(Lng));

        if (Status != 1) {
            holder.Status.setText("Tutup");
            holder.Status.setBackgroundResource(R.drawable.rounded_corner_red);
        }

    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myRestoItems.size();
    }

}