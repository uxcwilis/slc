package suska.uin.tif.smartlivingcost.Model;

public class TransaksiBerandaModel {
    private String IDTransaksi, IDCat, IconCat, NamaCat, KetCat, PhotoTrans, BiayaTrans, JenisKat, TglTrans, Budget;

    public TransaksiBerandaModel(String IDTransaksi, String IDCat, String iconCat, String namaCat, String ketCat, String photoTrans, String biayaTrans, String jenisKat, String tglTrans, String budget) {
        this.IDTransaksi = IDTransaksi;
        this.IDCat = IDCat;
        IconCat = iconCat;
        NamaCat = namaCat;
        KetCat = ketCat;
        PhotoTrans = photoTrans;
        BiayaTrans = biayaTrans;
        JenisKat = jenisKat;
        TglTrans = tglTrans;
        Budget = budget;
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

    public String getBudget() {
        return Budget;
    }
}
