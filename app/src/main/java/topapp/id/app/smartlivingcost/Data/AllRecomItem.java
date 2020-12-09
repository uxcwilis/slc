package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class AllRecomItem {
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

    @Override
    public String toString() {
        return "AllRecomItem{" +
                "id_menu = '" + id_menu + '\'' +
                ",id_resto = '" + id_resto + '\'' +
                ",nama_resto = '" + nama_resto + '\'' +
                ",nama_menu = '" + nama_menu + '\'' +
                ",lat_resto = '" + lat_resto + '\'' +
                ",lng_resto = '" + lng_resto + '\'' +
                ",distance = '" + distance + '\'' +
                ",harga_menu = '" + harga_menu + '\'' +
                ",gambar_menu = '" + gambar_menu + '\'' +
                ",status = '" + status + '\'' +
                ",rating = '" + rating + '\'' +
                "}";
    }
}
