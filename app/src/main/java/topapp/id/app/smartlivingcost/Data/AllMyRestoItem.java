package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class AllMyRestoItem {
    @SerializedName("id")
    private int id_resto;
    @SerializedName("id_inputer")
    private int id_inputer;
    @SerializedName("nama")
    private String nama_resto;
    @SerializedName("lat")
    private BigDecimal lat;
    @SerializedName("lng")
    private BigDecimal lng;
    @SerializedName("gambar_resto")
    private String gambar_resto;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("status")
    private int status;


    public int getId_resto() {
        return id_resto;
    }

    public int getId_inputer() {
        return id_inputer;
    }

    public String getNama_resto() {
        return nama_resto;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public String getGambar_resto() {
        return gambar_resto;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "AllMyResto{" +
                "id = '" + id_resto + '\'' +
                ",id_inputer = '" + id_inputer + '\'' +
                ",nama = '" + nama_resto + '\'' +
                ",lat = '" + lat + '\'' +
                ",lng = '" + lng + '\'' +
                ",gambar_resto = '" + gambar_resto + '\'' +
                ",alamat = '" + alamat + '\'' +
                ",status = '" + status + '\'' +
                "}";
    }
}
