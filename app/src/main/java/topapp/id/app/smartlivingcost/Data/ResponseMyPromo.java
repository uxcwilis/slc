package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMyPromo {
    @SerializedName("success")
    private List<AllMyPromoItem> getAllMyPromo;


    public List<AllMyPromoItem> getGetAllMyPromo() {
        return getAllMyPromo;
    }

}
