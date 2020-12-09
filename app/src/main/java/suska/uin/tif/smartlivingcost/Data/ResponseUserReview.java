package suska.uin.tif.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUserReview {
    @SerializedName("data")
    private List<AllUserReviewItem> getAllUserReview;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllUserReviewItem> getGetAllUserReview() {
        return getAllUserReview;
    }

    @Override
    public String toString() {
        return "ResponseResto{" +
                "data= '" + getAllUserReview + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
