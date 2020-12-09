package topapp.id.app.smartlivingcost.Model;

public class ReviewModel {
     String IdReview;
     String IdMenu;
     String Reviewed;
     String Reported;
     String NamaMenu;
     String NamaResto;
     String Tgl;

    public ReviewModel(String idReview, String idMenu, String reviewed, String reported, String namaMenu, String namaResto, String tgl) {
        IdReview = idReview;
        IdMenu = idMenu;
        Reviewed = reviewed;
        Reported = reported;
        NamaMenu = namaMenu;
        NamaResto = namaResto;
        Tgl = tgl;
    }

    public String getIdReview() {
        return IdReview;
    }

    public String getIdMenu() {
        return IdMenu;
    }

    public String getReviewed() {
        return Reviewed;
    }

    public String getReported() {
        return Reported;
    }

    public String getNamaMenu() {
        return NamaMenu;
    }

    public String getNamaResto() {
        return NamaResto;
    }

    public String getTgl() {
        return Tgl;
    }
}
