package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseRecom {
    @SerializedName("data")
    private List<AllRecomItem> getAllRecom;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllRecomItem> getGetAllRecom() {
        return getAllRecom;
    }

    @Override
    public String toString() {
        return "ResponseRescom{" +
                "data= '" + getAllRecom + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
