package suska.uin.tif.smartlivingcost.Model;

public class AngsuranModel {
    String IdAngsuran, IdPlanning, MoneyInput, DateInput;

    public AngsuranModel(String idAngsuran, String idPlanning, String moneyInput, String dateInput) {
        IdAngsuran = idAngsuran;
        IdPlanning = idPlanning;
        MoneyInput = moneyInput;
        DateInput = dateInput;
    }

    public String getIdAngsuran() {
        return IdAngsuran;
    }

    public void setIdAngsuran(String idAngsuran) {
        IdAngsuran = idAngsuran;
    }

    public String getIdPlanning() {
        return IdPlanning;
    }

    public void setIdPlanning(String idPlanning) {
        IdPlanning = idPlanning;
    }

    public String getMoneyInput() {
        return MoneyInput;
    }

    public void setMoneyInput(String moneyInput) {
        MoneyInput = moneyInput;
    }

    public String getDateInput() {
        return DateInput;
    }

    public void setDateInput(String dateInput) {
        DateInput = dateInput;
    }
}
