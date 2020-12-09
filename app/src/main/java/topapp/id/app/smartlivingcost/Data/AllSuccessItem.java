package topapp.id.app.smartlivingcost.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllSuccessItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("sqlite_backup")
    @Expose
    private String sqlite_backup;
    @SerializedName("isAdmin")
    @Expose
    private String isAdmin;

    public String getIsAdmin() {
        return isAdmin;
    }

    public String getSqlite_backup() {
        return sqlite_backup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
