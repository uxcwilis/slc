package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMyReview {
    @SerializedName("data")
    private List<AllMyReviewItem> getAllMyReview;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllMyReviewItem> getGetAllMyReview() {
        return getAllMyReview;
    }

    @Override
    public String toString() {
        return "ResponseResto{" +
                "data= '" + getAllMyReview + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
