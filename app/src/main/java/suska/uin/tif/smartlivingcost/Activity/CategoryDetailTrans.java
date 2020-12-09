package suska.uin.tif.smartlivingcost.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import suska.uin.tif.smartlivingcost.Adapter.CategoryDetailTransRV;
import suska.uin.tif.smartlivingcost.Fragment.DBSLC;
import suska.uin.tif.smartlivingcost.Model.KategoriDetailTransModel;
import suska.uin.tif.smartlivingcost.R;

public class CategoryDetailTrans extends AppCompatActivity {
    private DBSLC MyDatabase;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CategoryDetailTransRV CDTRV;
    private List<KategoriDetailTransModel> myKategoriDetail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail_trans);
        MyDatabase = new DBSLC(getBaseContext());

        Calendar calendar = Calendar.getInstance();
        final Date date = calendar.getTime();
        String stringMonth = (String) android.text.format.DateFormat.format("MMMM", date);
        String year = (String) android.text.format.DateFormat.format("yyyy", date);


        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String getidcat = b.getString("idcat");
        String getnamacat = b.getString("namacat");

        setTitle(getnamacat + " " + stringMonth + " " + year);

        getData(getidcat);
        recyclerView = findViewById(R.id.rv_detail_category_trans);
        CDTRV = new CategoryDetailTransRV(myKategoriDetail);
        layoutManager = new LinearLayoutManager(CategoryDetailTrans.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(CDTRV);

        CDTRV.setOnItemClickListener(new CategoryDetailTransRV.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(CategoryDetailTrans.this, UpdateDeleteTrans.class);
                final KategoriDetailTransModel data = myKategoriDetail.get(position);
                intent.putExtra("idcat", data.getIDCat());
                intent.putExtra("idtrans", data.getIDTransaksi());
                intent.putExtra("namacat", data.getNamaCat());
                intent.putExtra("iconcat", data.getIconCat());
                intent.putExtra("kettrans", data.getKetCat());
                intent.putExtra("phototrans", data.getPhotoTrans());
                intent.putExtra("biaya", data.getBiayaTrans());
                intent.putExtra("tgl", data.getTglTrans());
                intent.putExtra("jenis", data.getJenisKat());
                startActivity(intent);
                finish();
            }
        });


    }

    @SuppressLint("Recycle")
    public void getData(String id) {
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT Transaksi.IdTrans, Transaksi.IdKat, Kategori.IdIconKat, Kategori.NamaKat, Transaksi.KetTrans, Transaksi.GbrTrans, Transaksi.JumlahTrans, " +
                "Kategori.JenisKat, Transaksi.TglTrans FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat " +
                "WHERE Transaksi.IdKat = '" + id + "' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  " +
                "strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now')) ORDER BY Transaksi.TglTrans DESC", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            KategoriDetailTransModel kategoriDetailTransModel = new KategoriDetailTransModel(
                    cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getString(8)
            );
            myKategoriDetail.add(kategoriDetailTransModel);
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("frgToLoad", "Beranda");
        // Now start your activity
        startActivity(i);
        CategoryDetailTrans.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();

    }
}
