package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("success")
    @Expose
    public AllSuccessItem AllSuccessItem;

    public AllSuccessItem getAllSuccessItem() {
        return AllSuccessItem;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "success= '" + AllSuccessItem + '\'' +
                "}";
    }
}
