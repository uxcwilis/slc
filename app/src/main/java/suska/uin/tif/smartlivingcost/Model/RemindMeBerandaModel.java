package suska.uin.tif.smartlivingcost.Model;

public class RemindMeBerandaModel {
    private String IdTrans_remindme, IdKat_remindme, TglTrans_remindme, JumlahTrans_remindme,
            KetTrans_remindme, GbrTrans_remindme, TglInputTrans_remindme, JamInputTrans_remindme,
            Remind_At_remindme, NamaKat_remindme, IdIconKat_remindme, JenisKat_remindme;

    public RemindMeBerandaModel(String idTrans_remindme, String idKat_remindme, String tglTrans_remindme, String jumlahTrans_remindme, String ketTrans_remindme, String gbrTrans_remindme, String tglInputTrans_remindme, String jamInputTrans_remindme, String remind_At_remindme, String namaKat_remindme, String idIconKat_remindme, String jenisKat_remindme) {
        IdTrans_remindme = idTrans_remindme;
        IdKat_remindme = idKat_remindme;
        TglTrans_remindme = tglTrans_remindme;
        JumlahTrans_remindme = jumlahTrans_remindme;
        KetTrans_remindme = ketTrans_remindme;
        GbrTrans_remindme = gbrTrans_remindme;
        TglInputTrans_remindme = tglInputTrans_remindme;
        JamInputTrans_remindme = jamInputTrans_remindme;
        Remind_At_remindme = remind_At_remindme;
        NamaKat_remindme = namaKat_remindme;
        IdIconKat_remindme = idIconKat_remindme;
        JenisKat_remindme = jenisKat_remindme;
    }

    public String getIdTrans_remindme() {
        return IdTrans_remindme;
    }

    public String getIdKat_remindme() {
        return IdKat_remindme;
    }

    public String getTglTrans_remindme() {
        return TglTrans_remindme;
    }

    public String getJumlahTrans_remindme() {
        return JumlahTrans_remindme;
    }

    public String getKetTrans_remindme() {
        return KetTrans_remindme;
    }

    public String getGbrTrans_remindme() {
        return GbrTrans_remindme;
    }

    public String getTglInputTrans_remindme() {
        return TglInputTrans_remindme;
    }

    public String getJamInputTrans_remindme() {
        return JamInputTrans_remindme;
    }

    public String getRemind_At_remindme() {
        return Remind_At_remindme;
    }

    public String getNamaKat_remindme() {
        return NamaKat_remindme;
    }

    public String getIdIconKat_remindme() {
        return IdIconKat_remindme;
    }

    public String getJenisKat_remindme() {
        return JenisKat_remindme;
    }
}
