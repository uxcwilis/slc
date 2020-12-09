package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

public class AllMyMenuItem {
    @SerializedName("id")
    private int id_menu;
    @SerializedName("id_resto")
    private int id_resto;
    @SerializedName("nama_menu")
    private String nama_menu;
    @SerializedName("harga")
    private int harga;
    @SerializedName("status")
    private int status;
    @SerializedName("gambar_menu")
    private String gambar_menu;
    @SerializedName("rating")
    private String rating;

    public String getMenu_deskripsi() {
        return menu_deskripsi;
    }

    @SerializedName("menu_deskripsi")
    private String menu_deskripsi;


    public int getId_menu() {
        return id_menu;
    }

    public int getId_resto() {
        return id_resto;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public int getHarga() {
        return harga;
    }

    public int getStatus() {
        return status;
    }

    public String getGambar_menu() {
        return gambar_menu;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "AllMyResto{" +
                "id = '" + id_menu + '\'' +
                ",id_resto = '" + id_resto + '\'' +
                ",nama_menu = '" + nama_menu + '\'' +
                ",harga = '" + harga + '\'' +
                ",gambar_menu = '" + gambar_menu + '\'' +
                ",rating = '" + rating + '\'' +
                ",status = '" + status + '\'' +
                "}";
    }
}
