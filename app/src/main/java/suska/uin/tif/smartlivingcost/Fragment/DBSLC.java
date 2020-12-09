package suska.uin.tif.smartlivingcost.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBSLC extends SQLiteOpenHelper {
    //InnerClass, untuk mengatur artibut seperti Nama Tabel, nama-nama kolom dan Query
    public static abstract class MyColumns implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabelKat = "Kategori";
        public static final String IdKat = "IdKat";
        public static final String NamaKat = "NamaKat";
        public static final String IdIconKat = "IdIconKat";
        public static final String JenisKat = "JenisKat";
        public static final String BudgetKat = "BudgetKat";
    }

    public static abstract class Transaksi implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabelTrans = "Transaksi";
        public static final String IdTrans = "IdTrans";
        public static final String IdKat = "IdKat";
        public static final String TglTrans = "TglTrans";
        public static final String JumlahTrans = "JumlahTrans";
        public static final String KetTrans = "KetTrans";
        public static final String GbrTrans = "GbrTrans";
        public static final String TglInputTrans = "TglInputTrans";
        public static final String JamInputTrans = "JamInputTrans";
        public static final String Remind_At = "Remind_at";
    }

    public static abstract class Review implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabelReview = "Review";
        public static final String IdReview = "IdReview";
        public static final String IdMenu = "IdMenu";
        public static final String Reviewed = "Reviewed";
        public static final String Reported = "Reported";
        public static final String NamaMenu = "NamaMenu";
        public static final String NamaResto = "NamaResto";
        public static final String Tgl = "Tgl";

    }

    public static abstract class Planning implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabelPlanning = "Planning";
        public static final String IdPlanning = "IdPlanning";
        public static final String IdKategori = "IdKategori";
        public static final String NamaPlanning = "NamaPlanning";
        public static final String TargetMoney = "TargetMoney";
        public static final String TargetDate = "TargetDate";
        public static final String DateInput = "DateInput";
    }

    public static abstract class Angsuran implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabelAngsuran = "Angsuran";
        public static final String IdAngsuran = "IdAngsuran";
        public static final String IdPlanning = "IdPlanning";
        public static final String MoneyInput = "MoneyInput";
        public static final String DateInput = "DateInput";
    }

    public static abstract class Puasa implements BaseColumns {
        //Menentukan Nama Table dan Kolom
        public static final String NamaTabelPuasa = "Puasa";
        public static final String IdPuasa = "IdPuasa";
        public static final String Jenis = "JenisPuasa";
        public static final String Tanggal = "TanggalPuasa";
    }

    private static final String NamaDatabse = "dbslc.db";
    private static final int VersiDatabase = 24;

    private SQLiteDatabase dataBase = null;
    private Cursor cursor;

    //Query yang digunakan untuk membuat Tabel
    private static final String SQL_CREATE_ENTRIES1 = "CREATE TABLE " + MyColumns.NamaTabelKat +
            "(" + MyColumns.IdKat + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MyColumns.NamaKat + " TEXT NOT NULL, " + MyColumns.IdIconKat +
            " TEXT NOT NULL, " + MyColumns.JenisKat + " TEXT NOT NULL," + MyColumns.BudgetKat + " INTEGER NOT NULL)";

    private static final String SQL_CREATE_ENTRIES2 = "CREATE TABLE " + Transaksi.NamaTabelTrans +
            "(" + Transaksi.IdTrans + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Transaksi.IdKat + " INTEGER NOT NULL, " + Transaksi.TglTrans +
            " TEXT NOT NULL, " + Transaksi.JumlahTrans + " INTEGER NOT NULL, " + Transaksi.KetTrans + " TEXT NOT NULL, "
            + Transaksi.GbrTrans + " TEXT NOT NULL, " + Transaksi.TglInputTrans + " TEXT NOT NULL, " + Transaksi.JamInputTrans + " TEXT NOT NULL, " + Transaksi.Remind_At + " TEXT NOT NULL DEFAULT '' , " +
            "FOREIGN KEY (" + Transaksi.IdKat + ") REFERENCES " + MyColumns.NamaTabelKat + " (" + MyColumns.IdKat + "))";

    private static final String SQL_CREATE_ENTRIES3 = "CREATE TABLE " + Review.NamaTabelReview +
            "(" + Review.IdReview + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Review.IdMenu + " INTEGER NOT NULL, " +
            Review.Reviewed + " INTEGER NOT NULL, " + Review.Reported + " INTEGER NOT NULL, " + Review.NamaMenu + " TEXT NOT NULL," +
            Review.NamaResto + " TEXT NOT NULL, " + Review.Tgl + " TEXT NOT NULL)";

    private static final String SQL_CREATE_ENTRIES4 = "CREATE TABLE " + Planning.NamaTabelPlanning +
            "(" + Planning.IdPlanning + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Planning.IdKategori + " INTEGER NOT NULL, " + Planning.NamaPlanning +
            " TEXT NOT NULL, " + Planning.TargetMoney + " INTEGER NOT NULL, " + Planning.TargetDate + " TEXT NOT NULL, " + Planning.DateInput + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + Planning.IdKategori + ") REFERENCES " + MyColumns.NamaTabelKat + " (" + MyColumns.IdKat + "))";

    private static final String SQL_CREATE_ENTRIES5 = "CREATE TABLE " + Angsuran.NamaTabelAngsuran +
            "(" + Angsuran.IdAngsuran + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Angsuran.IdPlanning + " INTEGER NOT NULL, " +
            Angsuran.MoneyInput + " INTEGER NOT NULL, " + Angsuran.DateInput + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + Angsuran.IdPlanning + ") REFERENCES " + Planning.NamaTabelPlanning + " (" + Planning.IdPlanning + "))";

    private static final String SQL_INSERT = "INSERT INTO KATEGORI (NamaKat, IdIconKat, JenisKat, BudgetKat) values" +
            "('Makan Minum', '454', '0', 0)," +
            "('Pulsa','200','0',0)," +
            "('Bensin', '398','0',0)," +
            "('Uang Saku','259','1',0)," +
            "('Gaji','262','1',0)";

    //JENISPuasa =
    // 1= Puasa Senin Kamis
    // 2= Puasa Ayyamul Bidh
    // 3= Puasa Ramadhan
    // 4= Puasa Arafah
    // 5= Puasa asyura dan tasu'a

    private static final String SQL_CREATE_ENTRIES6 = "CREATE TABLE " + Puasa.NamaTabelPuasa +
            "(" + Puasa.IdPuasa + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Puasa.Jenis + " INTEGER NOT NULL, " + Puasa.Tanggal +
            " TEXT NOT NULL) ";
    private static final String SQL_INSERT2 = "INSERT INTO PUASA (JenisPuasa, TanggalPuasa) values"+
            "(1,'2019-11-04')," +
            "(1,'2019-11-07')," +
            "(2,'2019-11-10')," +
            "(2,'2019-11-11')," +
            "(2,'2019-11-12')," +
            "(1,'2019-11-14')," +
            "(1,'2019-11-18')," +
            "(1,'2019-11-21')," +
            "(1,'2019-11-25')," +
            "(1,'2019-11-28')," +
            "(1,'2019-12-02')," +
            "(1,'2019-12-05')," +
            "(1,'2019-12-09')," +
            "(2,'2019-12-10')," +
            "(2,'2019-12-11')," +
            "(2,'2019-12-12')," +
            "(1,'2019-12-16')," +
            "(1,'2019-12-19')," +
            "(1,'2019-12-23')," +
            "(1,'2019-12-26')," +
            "(1,'2019-12-30')," +
            "(1,'2020-1-02')," +
            "(1,'2020-1-06')," +
            "(2,'2020-1-08')," +
            "(2,'2020-1-09')," +
            "(2,'2020-1-10')," +
            "(1,'2020-1-13')," +
            "(1,'2020-1-16')," +
            "(1,'2020-1-20')," +
            "(1,'2020-1-23')," +
            "(1,'2020-1-27')," +
            "(1,'2020-1-30')";

//    private static final String SQL_INSERT2 = "INSERT INTO PLANNING (IdKategori, NamaPlanning, TargetMoney, TargetDate, DateInput) values" +
//            "(1, 'Uang jalan', 2000000, '2019-12-20', '2019-04-30')";
//    private static final String SQL_INSERT3 = "INSERT INTO Angsuran (IdPlanning, MoneyInput, DateInput) values" +
//            "(1, 222222, '2019-12-20')";

    //Query yang digunakan untuk mengupgrade Tabel
    private static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " + MyColumns.NamaTabelKat;
    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + Transaksi.NamaTabelTrans;
    private static final String SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS " + Review.NamaTabelReview;
    private static final String SQL_DELETE_ENTRIES4 = "DROP TABLE IF EXISTS " + Planning.NamaTabelPlanning;
    private static final String SQL_DELETE_ENTRIES5 = "DROP TABLE IF EXISTS " + Angsuran.NamaTabelAngsuran;
    private static final String SQL_DELETE_ENTRIES6 = "DROP TABLE IF EXISTS " + Puasa.NamaTabelPuasa;

    public DBSLC(Context context) {
        super(context, NamaDatabse, null, VersiDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES1);
        db.execSQL(SQL_CREATE_ENTRIES2);
        db.execSQL(SQL_CREATE_ENTRIES3);
        db.execSQL(SQL_CREATE_ENTRIES4);
        db.execSQL(SQL_CREATE_ENTRIES5);
        db.execSQL(SQL_CREATE_ENTRIES6);
        db.execSQL(SQL_INSERT);
        db.execSQL(SQL_INSERT2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES1);
        db.execSQL(SQL_DELETE_ENTRIES2);
        db.execSQL(SQL_DELETE_ENTRIES3);
        db.execSQL(SQL_DELETE_ENTRIES4);
        db.execSQL(SQL_DELETE_ENTRIES5);
        db.execSQL(SQL_DELETE_ENTRIES6);
        onCreate(db);
    }

    public int countRecord(int jenis) {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) from " + MyColumns.NamaTabelKat + " WHERE " + MyColumns.JenisKat + " = " + jenis, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int countTrans() {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) from " + Transaksi.NamaTabelTrans, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int countTransOut() {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) from Transaksi INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '0'", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }



    public int countTotalCollectedPlanning(String idplanning) {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("SELECT Sum(MoneyInput) FROM " + Angsuran.NamaTabelAngsuran + " WHERE IdPlanning = '" + idplanning + "'", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int countMonth(String idplanning) {
        int count = 0;
        dataBase = getReadableDatabase();
//        cursor = dataBase.rawQuery("SELECT (strftime('%m',TargetDate) - strftime('%m', DateInput))+1 FROM Planning WHERE IdPlanning = '" + idplanning + "'", null);
        cursor = dataBase.rawQuery("SELECT (julianday(TargetDate) - julianday(DateInput))/30 FROM Planning WHERE IdPlanning = '" + idplanning + "'", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int angsuranperbulan(String idplanning) {
        int bulan = 0;
        dataBase = getReadableDatabase();
//        cursor = dataBase.rawQuery("SELECT (strftime('%m',TargetDate) - strftime('%m', DateInput))+1  From Planning WHERE IdPlanning = '" + idplanning + "'", null);
        cursor = dataBase.rawQuery("SELECT (julianday(TargetDate) - julianday(DateInput))/30  From Planning WHERE IdPlanning = '" + idplanning + "'", null);
        cursor.moveToFirst();
        bulan = cursor.getInt(0);
        dataBase.close();
        int total_target = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("SELECT TargetMoney From Planning WHERE IdPlanning = '" + idplanning + "'", null);
        cursor.moveToFirst();
        total_target = cursor.getInt(0);
        dataBase.close();


        return total_target / bulan;
    }

    public void recreate() {
        dataBase = getReadableDatabase();
        String sql = "Delete FROM Transaksi";
        String sql2 = "Delete FROM Kategori";
        String sql4 = "Delete FROM Review";
        String sql5 = "Delete FROM Planning";
        String sql6 = "Delete FROM Angsuran";
        String sql3 = "INSERT INTO KATEGORI (NamaKat, IdIconKat, JenisKat, BudgetKat) values" +
                "('Makan Minum', '454', '0', 0)," +
                "('Pulsa','200','0',0)," +
                "('Bensin', '398','0',0)," +
                "('Uang Saku','259','1',0)," +
                "('Gaji','262','1',0)";
        dataBase.execSQL(sql);
        dataBase.execSQL(sql2);
        dataBase.execSQL(sql4);
        dataBase.execSQL(sql5);
        dataBase.execSQL(sql6);
        dataBase.execSQL(sql3);
        dataBase.close();
    }

    public int gettotal(String jenis, String tglawal, String tglakhir) {
        int count = 0;
        dataBase = getReadableDatabase();
        String sql1 = "SELECT SUM(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '" + jenis + "'";
        String sql2 = "SELECT SUM(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '" + jenis + "' AND Transaksi.TglTrans BETWEEN '" + tglawal + "' AND '" + tglakhir + "'";

        if (tglawal.equals("0") || tglawal.equals("") || tglakhir.equals("0") || tglakhir.equals("")) {
            cursor = dataBase.rawQuery(sql1, null);
        } else {
            cursor = dataBase.rawQuery(sql2, null);
        }
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int gettotal_category(String id) {
        int count = 0;
        dataBase = getReadableDatabase();
        String sql1 = "SELECT SUM(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.IdKat = '" + id + "' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now'))";
        cursor = dataBase.rawQuery(sql1, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int gettotal_category_currentmonth(String jenis) {
        int count = 0;
        dataBase = getReadableDatabase();
        String sql1 = "SELECT SUM(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '" + jenis + "' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now'))";
        cursor = dataBase.rawQuery(sql1, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }


    public int countRecordTransaksi(int id) {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) from " + Transaksi.NamaTabelTrans + " WHERE " + Transaksi.IdKat + " = " + id, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int countRecordTransaksiThisMonth(int id) {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) from " + Transaksi.NamaTabelTrans + " WHERE " + Transaksi.IdKat + " = " + id + " AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now'))", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int getIdCatUpdate(int id) {
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Transaksi.IdKat from " + Transaksi.NamaTabelTrans + " WHERE " + Transaksi.IdTrans + " = " + id, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        return count;
    }

    public int rataPengeluaranBulanini() {
        int total = 0;
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Sum(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '0' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now'))", null);
        cursor.moveToFirst();
        total = cursor.getInt(0);
        dataBase.close();
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '0' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now'))", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();
        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public int rataPengeluaranBulanKemarin() {
        int total = 0;
        int count = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Sum(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '0' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now','start of month','-1 months'))", null);
        cursor.moveToFirst();
        total = cursor.getInt(0);
        dataBase.close();
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Count(*) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE Kategori.JenisKat = '0' AND strftime('%Y',Transaksi.TglTrans) = strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) = strftime('%m',date('now','start of month','-1 months'))", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();

        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    public int sisaBulanKemarin() {
        int total = 0;
        dataBase = getReadableDatabase();
        cursor = dataBase.rawQuery("select Sum(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON Transaksi.IdKat = Kategori.IdKat WHERE  strftime('%Y',Transaksi.TglTrans) <= strftime('%Y',date('now')) AND  strftime('%m',Transaksi.TglTrans) <= strftime('%m',date('now','start of month','-1 month'))", null);
        cursor.moveToFirst();
        total = cursor.getInt(0);
        dataBase.close();

        return total;
    }

    public int isKategoriexist(String id) {
        int count = 0;
        dataBase = getReadableDatabase();
        String sql1 = "SELECT COUNT(Kategori.IdKat) FROM " + MyColumns.NamaTabelKat + " WHERE Kategori.IdKat = '" + id + "'";

        cursor = dataBase.rawQuery(sql1, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dataBase.close();

        return count;
    }


}