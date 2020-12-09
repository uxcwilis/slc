package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMyResto {
    @SerializedName("data")
    private List<AllMyRestoItem> getAllMyResto;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllMyRestoItem> getGetAllMyResto() {
        return getAllMyResto;
    }

    @Override
    public String toString() {
        return "ResponseResto{" +
                "data= '" + getAllMyResto + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
