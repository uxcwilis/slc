package suska.uin.tif.smartlivingcost.Model;

public class PlanningTableModel {
     private String IdPlanning, IdKategori, NamaPlanning, TargetMoney, TargetDate, DateInput;

    public PlanningTableModel(String idPlanning, String idKategori, String namaPlanning, String targetMoney, String targetDate, String dateInput) {
        IdPlanning = idPlanning;
        IdKategori = idKategori;
        NamaPlanning = namaPlanning;
        TargetMoney = targetMoney;
        TargetDate = targetDate;
        DateInput = dateInput;
    }

    public String getIdPlanning() {
        return IdPlanning;
    }

    public String getIdKategori() {
        return IdKategori;
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
}
