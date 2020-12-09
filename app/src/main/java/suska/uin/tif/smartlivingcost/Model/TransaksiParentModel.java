package suska.uin.tif.smartlivingcost.Model;

public class TransaksiParentModel {
    private String tanggal, total;

    public TransaksiParentModel(String tanggal, String total) {
        this.tanggal = tanggal;
        this.total = total;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTotal() {
        return total;
    }
}
