package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AllMyPromoItem {
    @SerializedName("id")
    private int id;
    @SerializedName("id_menu")
    private int id_menu;
    @SerializedName("harga")
    private int harga_promo;
    @SerializedName("mulai")
    private Date tanggal_mulai;
    @SerializedName("batas")
    private Date tanggal_batas;
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("keterangan")
    private String keterangan;

    public int getId() {
        return id;
    }

    public int getId_menu() {
        return id_menu;
    }

    public int getHarga_promo() {
        return harga_promo;
    }

    public Date getTanggal_mulai() {
        return tanggal_mulai;
    }

    public Date getTanggal_batas() {
        return tanggal_batas;
    }

    public String getGambar() {
        return gambar;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
