package suska.uin.tif.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import suska.uin.tif.smartlivingcost.Model.AngsuranModel;
import suska.uin.tif.smartlivingcost.R;

public class AngsuranRVItem extends RecyclerView.Adapter<AngsuranRVItem.ViewHolder> {
    private static ClickListener clickListener;
    private List<AngsuranModel> myAngsuran;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public AngsuranRVItem(List<AngsuranModel> data) {
        super();
        this.myAngsuran = data;
        notifyDataSetChanged();
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Nomor, Tanggal, Jumlah;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Jumlah = itemView.findViewById(R.id.jumlah);
            Tanggal = itemView.findViewById(R.id.tanggal);
            Nomor = itemView.findViewById(R.id.nomor_angsuran);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AngsuranRVItem.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.angsuran_rv_item, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final AngsuranModel data = myAngsuran.get(position);
        final String moneyinput = data.getMoneyInput();
        final String dateinput = data.getDateInput();

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;

        try {
            date = originalFormat.parse(dateinput);
            String tanggal = String.valueOf(targetFormat.format(date));
            holder.Tanggal.setText(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.Nomor.setText(String.valueOf(position + 1));
        holder.Jumlah.setText(String.valueOf(nbFmt.format(Integer.parseInt(moneyinput))));

    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myAngsuran.size();
    }
}
