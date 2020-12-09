package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSuspendMenu {
    @SerializedName("data")
    private List<AllSuspendMenuItem> getAllSuspendMenu;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllSuspendMenuItem> getAllSuspendMenu() {
        return getAllSuspendMenu;
    }

    @Override
    public String toString() {
        return "ResponseResto{" +
                "data= '" + getAllSuspendMenu + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
