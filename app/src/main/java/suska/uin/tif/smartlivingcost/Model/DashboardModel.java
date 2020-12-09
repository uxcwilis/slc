package suska.uin.tif.smartlivingcost.Model;

public class DashboardModel {
    private String idcat;
    private String idtrans;
    private String namacat;
    private String iconcat;
    private String kettrans;
    private String phototrans;
    private String biaya;
    private String jeniskategori;
    private String tgl;
    private String budget;

    public String getBudget() {
        return budget;
    }

    public DashboardModel(String idcat, String idtrans, String namacat, String iconcat, String kettrans, String phototrans, String biaya, String jeniskategori, String tgl, String budget) {
        this.idcat = idcat;
        this.idtrans = idtrans;
        this.namacat = namacat;
        this.iconcat = iconcat;
        this.kettrans = kettrans;
        this.phototrans = phototrans;
        this.biaya = biaya;
        this.jeniskategori = jeniskategori;
        this.tgl = tgl;
        this.budget = budget;
    }

    public String getIdcat() {
        return idcat;
    }

    public void setIdcat(String idcat) {
        this.idcat = idcat;
    }

    public String getIdtrans() {
        return idtrans;
    }

    public void setIdtrans(String idtrans) {
        this.idtrans = idtrans;
    }

    public String getNamacat() {
        return namacat;
    }

    public void setNamacat(String namacat) {
        this.namacat = namacat;
    }

    public String getIconcat() {
        return iconcat;
    }

    public void setIconcat(String iconcat) {
        this.iconcat = iconcat;
    }

    public String getKettrans() {
        return kettrans;
    }

    public void setKettrans(String kettrans) {
        this.kettrans = kettrans;
    }

    public String getPhototrans() {
        return phototrans;
    }

    public void setPhototrans(String phototrans) {
        this.phototrans = phototrans;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getJeniskategori() {
        return jeniskategori;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }


}
