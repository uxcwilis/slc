package suska.uin.tif.smartlivingcost.Adapter;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import suska.uin.tif.smartlivingcost.Model.KategoriDetailTransModel;
import suska.uin.tif.smartlivingcost.R;

public class CategoryDetailTransRV extends RecyclerView.Adapter<CategoryDetailTransRV.ViewHolder> {
    private static ClickListener clickListener;
    private Context context;
    private List<KategoriDetailTransModel> myKategoriDetail;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public CategoryDetailTransRV(List<KategoriDetailTransModel> mykategoriDetail) {
        super();
        this.myKategoriDetail = mykategoriDetail;
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
            TglTrans = itemView.findViewById(R.id.tv_tanggal_search_transrv);
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
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CategoryDetailTransRV.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaksi_search_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final KategoriDetailTransModel data = myKategoriDetail.get(position);
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String idcat = data.getIDCat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String idtrans = data.getIDTransaksi();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String namacat = data.getNamaCat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String iconcat = data.getIconCat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String kettrans = data.getKetCat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String phototrans = data.getPhotoTrans();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String biaya = data.getBiayaTrans();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String jenis = data.getJenisKat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String tgl = data.getTglTrans();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;

        if (phototrans.equals("") || phototrans.equals("null")) {
            holder.PhotoTrans.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(Uri.fromFile(new File(phototrans))).centerCrop()
                    .fitCenter().dontAnimate()
                    .error(R.drawable.camera)
                    .placeholder(R.drawable.gallery).into(holder.PhotoTrans);
        }

        if (Integer.parseInt(jenis) == 1) {
            holder.iconView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.iconView.setColorFilter(ContextCompat.getColor(context, R.color.RedTheme));
        }

        if (kettrans.equals("") || kettrans.equals("null")) {
            holder.KetTrans.setVisibility(View.GONE);
        } else {
            holder.KetTrans.setText(kettrans);
        }


        try {
            date = originalFormat.parse(tgl);
            String tanggal = String.valueOf(targetFormat.format(date));
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d = null;
            try {
                d = dateFormat.parse(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setTime(d);
            String[] days = new String[]{"", "Ahad", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu"};
            String day = days[calendar.get(Calendar.DAY_OF_WEEK)];
            holder.TglTrans.setText(day + ", " + tanggal);
        } catch (ParseException ex) {
            // Handle Exception.
        }

        holder.iconView.setIcon(Integer.parseInt(iconcat));
        holder.NamaCat.setText(namacat);
        holder.BiayaTrans.setText(nbFmt.format(Integer.parseInt(biaya)));
        holder.biayaaja.setText(biaya);
        holder.IdKat.setText(idcat);
        holder.IdTrans.setText(idtrans);
        holder.nomoricon.setText(iconcat);
        holder.locphoto.setText(phototrans);
        holder.jeniskat.setText(jenis);


    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myKategoriDetail.size();
    }


}
