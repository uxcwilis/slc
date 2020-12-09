package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AllPromoItem implements Serializable {
    @SerializedName("id_menu")
    private String id_menu;
    @SerializedName("id_resto")
    private String id_resto;
    @SerializedName("lat_resto")
    private BigDecimal lat_resto;
    @SerializedName("lng_resto")
    private BigDecimal lng_resto;
    @SerializedName("nama_menu")
    private String nama_menu;
    @SerializedName("nama_resto")
    private String nama_resto;
    @SerializedName("distance")
    private Double distance;
    @SerializedName("harga_menu")
    private int harga_menu;
    @SerializedName("gambar_menu")
    private String gambar_menu;
    @SerializedName("gambar_resto")
    private String gambar_resto;
    @SerializedName("status")
    private int status;
    @SerializedName("rating")
    private String rating;
    @SerializedName("menu_deskripsi")
    private String menu_deskripsi;
    @SerializedName("alamat")
    private String alamat;

    @SerializedName("id_promo")
    private int id_promo;
    @SerializedName("gambar_promo")
    private String gambar_promo;
    @SerializedName("harga_promo")
    private int harga_promo;
    @SerializedName("keterangan_promo")
    private String keterangan_promo;
    @SerializedName("mulai_promo")
    private Date mulai_promo;
    @SerializedName("batas_promo")
    private Date batas_promo;

    public String getAlamat() {
        return alamat;
    }

    public String getMenu_deskripsi() {
        return menu_deskripsi;
    }

    public String getGambar_menu() {
        return gambar_menu;
    }

    public String getRating() {
        return rating;
    }

    public int getStatus() {
        return status;
    }

    public BigDecimal getLng_resto() {
        return lng_resto;
    }

    public String getId_menu() {
        return id_menu;
    }

    public String getId_resto() {
        return id_resto;
    }

    public BigDecimal getLat_resto() {
        return lat_resto;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public Double getDistance() {
        return distance;
    }

    public int getHarga_menu() {
        return harga_menu;
    }

    public String getNama_resto() {
        return nama_resto;
    }

    public String getGambar_resto() {
        return gambar_resto;
    }


    public int getId_promo() {
        return id_promo;
    }

    public String getGambar_promo() {
        return gambar_promo;
    }

    public int getHarga_promo() {
        return harga_promo;
    }

    public String getKeterangan_promo() {
        return keterangan_promo;
    }

    public Date getMulai_promo() {
        return mulai_promo;
    }

    public Date getBatas_promo() {
        return batas_promo;
    }

}
