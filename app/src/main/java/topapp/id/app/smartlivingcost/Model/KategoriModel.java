package topapp.id.app.smartlivingcost.Model;

public class KategoriModel {
    String IdKat;
    String NamaKat;
    String IdIconKat;
    String JenisKat;
    String BudgetKat;

    public KategoriModel(String idKat, String namaKat, String idIconKat, String jenisKat, String budgetKat) {
        IdKat = idKat;
        NamaKat = namaKat;
        IdIconKat = idIconKat;
        JenisKat = jenisKat;
        BudgetKat = budgetKat;
    }

    public String getIdKat() {
        return IdKat;
    }

    public void setIdKat(String idKat) {
        IdKat = idKat;
    }

    public String getNamaKat() {
        return NamaKat;
    }

    public void setNamaKat(String namaKat) {
        NamaKat = namaKat;
    }

    public String getIdIconKat() {
        return IdIconKat;
    }

    public void setIdIconKat(String idIconKat) {
        IdIconKat = idIconKat;
    }

    public String getJenisKat() {
        return JenisKat;
    }

    public void setJenisKat(String jenisKat) {
        JenisKat = jenisKat;
    }

    public String getBudgetKat() {
        return BudgetKat;
    }

    public void setBudgetKat(String budgetKat) {
        BudgetKat = budgetKat;
    }
}
