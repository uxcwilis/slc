package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMyMenu {
    @SerializedName("data")
    private List<AllMyMenuItem> getAllMyMenu;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllMyMenuItem> getGetAllMyMenu() {
        return getAllMyMenu;
    }

    @Override
    public String toString() {
        return "ResponseResto{" +
                "data= '" + getAllMyMenu + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
