package suska.uin.tif.smartlivingcost.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maltaisn.icondialog.IconHelper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import suska.uin.tif.smartlivingcost.Activity.MainActivity;
import suska.uin.tif.smartlivingcost.Activity.UpdateDeleteTrans;
import suska.uin.tif.smartlivingcost.Adapter.CustomLinearLayoutManager;
import suska.uin.tif.smartlivingcost.Adapter.TransaksiChildRV;
import suska.uin.tif.smartlivingcost.Model.TransaksiChildModel;
import suska.uin.tif.smartlivingcost.Model.TransaksiParentModel;
import suska.uin.tif.smartlivingcost.R;

public class TransaksiParentRV extends RecyclerView.Adapter<TransaksiParentRV.ViewHolder> {
    private Context context;
    private DBSLC MyDatabase;
    private List<TransaksiParentModel> data;
    private List<TransaksiChildModel> dataChild = new ArrayList<>();

//    ArrayList IDTransaksiList, IDCatList, IconCatList, NamaCatList, KetCatList, PhotoTransList, BiayaTransList, JenisKatList, TglTransList, RemindAtList;
    private RecyclerView.LayoutManager layoutManager;
    TransaksiChildRV TCRV;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public TransaksiParentRV(List<TransaksiParentModel> data) {
        super();
        this.data = data;
        setHasStableIds(true);
        notifyDataSetChanged();
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Tanggal, Total;
        RecyclerView rvchild;


        ViewHolder(View itemView) {
            super(itemView);
            MyDatabase = new DBSLC(context);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            Tanggal = itemView.findViewById(R.id.tv_tanggal_parentrvtrans);
            Total = itemView.findViewById(R.id.tv_total_parentrvtrans);
            rvchild = itemView.findViewById(R.id.rv_child_trans);
            TCRV = new TransaksiChildRV(dataChild);
            TCRV.notifyDataSetChanged();
            layoutManager = new CustomLinearLayoutManager(context);
            rvchild.setLayoutManager(layoutManager);
            rvchild.setHasFixedSize(true);

        }


    }

    @SuppressLint("Recycle")
    public void getData(String id) {
        dataChild.clear();
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT Transaksi.IdTrans, Transaksi.IdKat, Kategori.IdIconKat, Kategori.NamaKat, Transaksi.KetTrans, Transaksi.GbrTrans, Transaksi.JumlahTrans, Kategori.JenisKat, Transaksi.TglTrans, Transaksi.Remind_at FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Transaksi.TglTrans Like '%" + id + "%'", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            TransaksiChildModel transaksiChildModel = new TransaksiChildModel(cursor.getString(0),
                    cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),
                    cursor.getString(7),cursor.getString(8),
                    cursor.getString(9));
            dataChild.add(transaksiChildModel);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaksi_parentrv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final IconHelper iconHelper = IconHelper.getInstance(context);
        TransaksiParentModel single = data.get(position);

        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String tanggal_terbalik = single.getTanggal();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String total = single.getTotal();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan

        getData(tanggal_terbalik);

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        holder.Total.setText(nbFmt.format(Integer.parseInt(total)));
        if (Integer.parseInt(total) <= 0) {
            holder.Total.setTextColor(ContextCompat.getColor(context, R.color.buttonHighlight));
        } else {
            holder.Total.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }


        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = originalFormat.parse(tanggal_terbalik);
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
            holder.Tanggal.setText(day + ", " + tanggal);
        } catch (ParseException ex) {
            // Handle Exception.
        }


        TCRV.setOnItemClickListener(new TransaksiChildRV.ClickListener() {
            @Override
            public void onItemClick(int position, String idcat, String idtrans, String namacat, String iconcat, String kettrans, String phototrans, String biaya, String tgl, String jenis, String remindat, View v) {
                Intent intent = new Intent(context, UpdateDeleteTrans.class);
                intent.putExtra("idcat", idcat);
                intent.putExtra("idtrans", idtrans);
                intent.putExtra("namacat", namacat);
                intent.putExtra("iconcat", iconcat);
                intent.putExtra("kettrans", kettrans);
                intent.putExtra("phototrans", phototrans);
                intent.putExtra("biaya", biaya);
                intent.putExtra("tgl", tgl);
                intent.putExtra("jenis", jenis);
                intent.putExtra("tanggal_remindat", remindat);
                context.startActivity(intent);
                ((MainActivity) context).finish();
            }
        });
        holder.rvchild.setAdapter(TCRV);
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
