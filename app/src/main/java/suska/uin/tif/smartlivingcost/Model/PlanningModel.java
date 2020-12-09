package suska.uin.tif.smartlivingcost.Model;

public class PlanningModel {
    private String IdPlanning, IdCategory, NamaPlanning, TargetMoney, TargetDate, DateInput, NamaKategori, IdIconKategori;

    public PlanningModel(String idPlanning, String idCategory, String namaPlanning, String targetMoney, String targetDate, String dateInput, String namaKategori, String idIconKategori) {
        IdPlanning = idPlanning;
        IdCategory = idCategory;
        NamaPlanning = namaPlanning;
        TargetMoney = targetMoney;
        TargetDate = targetDate;
        DateInput = dateInput;
        NamaKategori = namaKategori;
        IdIconKategori = idIconKategori;
    }

    public String getIdPlanning() {
        return IdPlanning;
    }

    public String getIdCategory() {
        return IdCategory;
    }

    public String getNamaPlanning() {
        return NamaPlanning;
    }

    public String getTargetMoney() {
        return TargetMoney;
    }

    public String getTargetDate() {
        return TargetDate;
    }

    public String getDateInput() {
        return DateInput;
    }

    public String getNamaKategori() {
        return NamaKategori;
    }

    public String getIdIconKategori() {
        return IdIconKategori;
    }
}
