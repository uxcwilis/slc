package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

public class AllSuspendMenuItem {
    @SerializedName("id_menu")
    private int id_menu;
    @SerializedName("nama_menu")
    private String nama_menu;
    @SerializedName("harga")
    private int harga;
    @SerializedName("suspend")
    private int suspend;
    @SerializedName("gambar_menu")
    private String gambar_menu;
    @SerializedName("rating")
    private String rating;
    @SerializedName("menu_deskripsi")
    private String menu_deskripsi;

    public String getMenu_deskripsi() {
        return menu_deskripsi;
    }

    public int getId_menu() {
        return id_menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public int getHarga() {
        return harga;
    }

    public int getSuspend() {
        return suspend;
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
                ",nama_menu = '" + nama_menu + '\'' +
                ",harga = '" + harga + '\'' +
                ",gambar_menu = '" + gambar_menu + '\'' +
                ",rating = '" + rating + '\'' +
                ",suspend = '" + suspend + '\'' +
                "}";
    }
}
