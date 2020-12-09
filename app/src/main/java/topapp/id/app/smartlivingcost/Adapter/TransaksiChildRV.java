package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maltaisn.icondialog.IconView;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import topapp.id.app.smartlivingcost.Model.TransaksiChildModel;
import topapp.id.app.smartlivingcost.R;

public class TransaksiChildRV extends RecyclerView.Adapter<TransaksiChildRV.ViewHolder> {
    private static ClickListener clickListener;
    List<TransaksiChildModel> data;
    private Context context;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public TransaksiChildRV(List<TransaksiChildModel> data) {
        super();
        this.data = data;
        setHasStableIds(true);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView NamaCat, KetTrans, BiayaTrans, TglTrans, IdTrans, IdKat, locphoto, nomoricon, jeniskat, biayaaja;
        private ImageView PhotoTrans;
        private IconView iconView;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            NamaCat = itemView.findViewById(R.id.namakat);
            KetTrans = itemView.findViewById(R.id.ket);
            BiayaTrans = itemView.findViewById(R.id.harga);
            TglTrans = itemView.findViewById(R.id.tgl_trans);
            IdTrans = itemView.findViewById(R.id.id_trans);
            IdKat = itemView.findViewById(R.id.id_kat);
            locphoto = itemView.findViewById(R.id.locphoto);
            nomoricon = itemView.findViewById(R.id.nomoricon);
            jeniskat = itemView.findViewById(R.id.jeniskategori);
            biayaaja = itemView.findViewById(R.id.biayaajaa);
            PhotoTrans = itemView.findViewById(R.id.photo);
            iconView = itemView.findViewById(R.id.iconrv);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), IdKat.getText().toString(), IdTrans.getText().toString(), NamaCat.getText().toString(), nomoricon.getText().toString(),
                    KetTrans.getText().toString(), locphoto.getText().toString(), biayaaja.getText().toString(), TglTrans.getText().toString(), jeniskat.getText().toString(),
                    data.get(getAdapterPosition()).getRemindAt(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TransaksiChildRV.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, String idcat, String idtrans, String namacat, String iconcat, String kettrans, String phototrans, String biaya, String tgl, String jenis, String remindat, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaksi_childrv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final TransaksiChildModel single = data.get(position);
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String idcat = single.getIDCat();
        final String idtrans = single.getIDTransaksi();
        final String namacat = single.getNamaCat();
        final String iconcat = single.getIconCat();
        final String kettrans = single.getKetCat();
        final String phototrans = single.getPhotoTrans();
        final String biaya = single.getBiayaTrans();
        final String jenis = single.getJenisKat();
        final String tgl = single.getTglTrans();
        holder.biayaaja.setText(biaya);
        holder.TglTrans.setText(tgl);
        holder.IdKat.setText(idcat);
        holder.IdTrans.setText(idtrans);
        holder.nomoricon.setText(iconcat);
        holder.locphoto.setText(phototrans);
        holder.jeniskat.setText(jenis);

        if (phototrans.equals("") || phototrans.equals("null")) {
            holder.PhotoTrans.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(Uri.fromFile(new File(phototrans))).centerCrop().fitCenter().dontAnimate().error(R.drawable.camera).placeholder(R.drawable.gallery).into(holder.PhotoTrans);
        }

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);

        if (Integer.parseInt(jenis) == 1) {
            holder.iconView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.iconView.setColorFilter(ContextCompat.getColor(context, R.color.RedTheme));
        }
        holder.iconView.setIcon(Integer.parseInt(iconcat));


        holder.BiayaTrans.setText(nbFmt.format(Integer.parseInt(biaya)));
        if (kettrans.equals("") || kettrans.equals("null")) {
            holder.KetTrans.setVisibility(View.GONE);
        } else {
            holder.KetTrans.setText(kettrans);
        }

        holder.NamaCat.setText(namacat);


        holder.PhotoTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView popupImv = new ImageView(context);
                Glide.with(context).load(Uri.fromFile(new File(phototrans)))
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
