package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePromo {
    @SerializedName("data")
    private List<AllPromoItem> getAllPromo;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllPromoItem> getGetAllPromo() {
        return getAllPromo;
    }


}
