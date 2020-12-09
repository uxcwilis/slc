package topapp.id.app.smartlivingcost.Model;

public class TransaksiChildModel {
    private String IDTransaksi, IDCat, IconCat, NamaCat, KetCat, PhotoTrans, BiayaTrans, JenisKat, TglTrans, RemindAt;

    public TransaksiChildModel(String IDTransaksi, String IDCat, String iconCat, String namaCat, String ketCat, String photoTrans, String biayaTrans, String jenisKat, String tglTrans, String remindAt) {
        this.IDTransaksi = IDTransaksi;
        this.IDCat = IDCat;
        this.IconCat = iconCat;
        this.NamaCat = namaCat;
        this.KetCat = ketCat;
        this.PhotoTrans = photoTrans;
        this.BiayaTrans = biayaTrans;
        this.JenisKat = jenisKat;
        this.TglTrans = tglTrans;
        this.RemindAt = remindAt;
    }

    public String getIDTransaksi() {
        return IDTransaksi;
    }

    public String getIDCat() {
        return IDCat;
    }

    public String getIconCat() {
        return IconCat;
    }

    public String getNamaCat() {
        return NamaCat;
    }

    public String getKetCat() {
        return KetCat;
    }

    public String getPhotoTrans() {
        return PhotoTrans;
    }

    public String getBiayaTrans() {
        return BiayaTrans;
    }

    public String getJenisKat() {
        return JenisKat;
    }

    public String getTglTrans() {
        return TglTrans;
    }

    public String getRemindAt() {
        return RemindAt;
    }
}
