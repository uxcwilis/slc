package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

public class AllUserReviewItem {
    @SerializedName("id")
    private int id_review;
    @SerializedName("nama_menu")
    private String nama_menu;
    @SerializedName("id_menu")
    private int id_menu;
    @SerializedName("rating")
    private int rating;
    @SerializedName("comment")
    private String comment;
    @SerializedName("tanggal")
    private String tanggal;

    public int getId_review() {
        return id_review;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public int getId_menu() {
        return id_menu;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getTanggal() {
        return tanggal;
    }
}
