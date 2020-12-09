package topapp.id.app.smartlivingcost.Model;

public class PuasaModel {
    int id;
    int jenis;
    String tanggal;

    public PuasaModel(int id, int jenis, String tanggal) {
        this.id = id;
        this.jenis = jenis;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public int getJenis() {
        return jenis;
    }

    public String getTanggal() {
        return tanggal;
    }
}
