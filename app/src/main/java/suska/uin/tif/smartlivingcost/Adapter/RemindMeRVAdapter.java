package suska.uin.tif.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import suska.uin.tif.smartlivingcost.Model.RemindMeBerandaModel;
import suska.uin.tif.smartlivingcost.R;

public class RemindMeRVAdapter extends RecyclerView.Adapter<RemindMeRVAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<RemindMeBerandaModel> remindMe;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public RemindMeRVAdapter(List<RemindMeBerandaModel> remindMe) {
        super();
        this.remindMe = remindMe;
        setHasStableIds(true);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView NamaTrans, NamaKat;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            NamaTrans = itemView.findViewById(R.id.nama_trans);
            NamaKat = itemView.findViewById(R.id.nama_kat);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RemindMeRVAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.remindme_rv, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String kettrans = remindMe.get(position).getKetTrans_remindme();
        String namakat = remindMe.get(position).getNamaKat_remindme();
        if (kettrans.isEmpty() || kettrans.equals("")) {
            holder.NamaTrans.setVisibility(View.GONE);
        } else {
            holder.NamaTrans.setText(kettrans);
        }
        holder.NamaKat.setText(namakat);


    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return remindMe.size();
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
