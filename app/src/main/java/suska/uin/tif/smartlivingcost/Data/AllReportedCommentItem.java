package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

public class AllReportedCommentItem {
    @SerializedName("id")
    private int id_report;
    @SerializedName("name")
    private String nama_user;
    @SerializedName("id_user")
    private int id_user;
    @SerializedName("id_menu")
    private int id_menu;
    @SerializedName("comment")
    private String comment;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("deleted")
    private int deleted;

    public String getNama_user() {
        return nama_user;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_menu() {
        return id_menu;
    }

    public String getComment() {
        return comment;
    }

    public String getTanggal() {
        return tanggal;
    }

    public int getDeleted() {
        return deleted;
    }

    public int getId_report() {
        return id_report;
    }
}
