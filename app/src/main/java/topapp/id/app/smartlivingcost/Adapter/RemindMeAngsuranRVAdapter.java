package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import topapp.id.app.smartlivingcost.Activity.Planning;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.PlanningTableModel;
import topapp.id.app.smartlivingcost.R;

public class RemindMeAngsuranRVAdapter extends RecyclerView.Adapter<RemindMeAngsuranRVAdapter.ViewHolder> {
    private Context context;
    List<PlanningTableModel> planningTableModel;
    private DBSLC MyDatabase;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public RemindMeAngsuranRVAdapter(List<PlanningTableModel> planningTableModel) {
        super();
        this.planningTableModel = planningTableModel;
        setHasStableIds(true);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView TujuanAngsuran, AngsuranBulanan, Terkumpul;

        ViewHolder(View itemView) {
            super(itemView);
            MyDatabase = new DBSLC(context);
            TujuanAngsuran = itemView.findViewById(R.id.tujuan_angsuran);
            AngsuranBulanan = itemView.findViewById(R.id.angsuran_bulanan);
            Terkumpul = itemView.findViewById(R.id.angsuran_terkumpul);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.remindangsuran_rv, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String idplanning = planningTableModel.get(position).getIdPlanning();
        String tujuan_planning = planningTableModel.get(position).getNamaPlanning();

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);

        holder.TujuanAngsuran.setText(tujuan_planning);
        holder.AngsuranBulanan.setText(String.valueOf(nbFmt.format(MyDatabase.angsuranperbulan(idplanning))));
        holder.Terkumpul.setText(String.valueOf(nbFmt.format(MyDatabase.countTotalCollectedPlanning(idplanning))));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Planning.class));
            }
        });


    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return planningTableModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
