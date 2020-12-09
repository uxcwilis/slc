package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseReportedComment {
    @SerializedName("data")
    private List<AllReportedCommentItem> getAllReportedComment;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AllReportedCommentItem> getGetAllReportedComment() {
        return getAllReportedComment;
    }

    @Override
    public String toString() {
        return "ResponseResto{" +
                "data= '" + getAllReportedComment + '\'' +
                ", status = '" + status + '\'' +
                "}";
    }
}
