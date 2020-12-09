package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.maltaisn.icondialog.IconHelper;

import java.util.List;

import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.KategoriModel;
import topapp.id.app.smartlivingcost.R;

public class SelectCategoryUpdateRVItem extends RecyclerView.Adapter<SelectCategoryUpdateRVItem.ViewHolder> {
    private static ClickListener clickListener;
    private List<KategoriModel> myKategori;
    private Context context;
    private String jeniskat;
    private DBSLC MyDatabase;
    private int selected_position = -1;
    private int idTrans;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public SelectCategoryUpdateRVItem(List<KategoriModel> myKategori, int idTrans) {
        super();
        this.myKategori = myKategori;
        this.idTrans = idTrans;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SelectCategoryUpdateRVItem.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_rv_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final IconHelper iconHelper = IconHelper.getInstance(context);
        final KategoriModel data = myKategori.get(position);
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String idcat = data.getIdKat();
        final String namacat = data.getNamaKat();
        final String idiconcat = data.getIdIconKat();
        jeniskat = data.getJenisKat();
        if (Integer.parseInt(idcat) == MyDatabase.getIdCatUpdate(idTrans)) {
            selected_position = position;
        }
        holder.IDCat.setText(idcat);
        holder.NamaCat.setText(namacat);
        holder.IdIcon.setText(idiconcat);
        iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
            @Override
            public void onDataLoaded() {
                // This happens on UI thread, and is guaranteed to be called.
                holder.IdIconCat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(idiconcat)).getDrawable(context));
                if (Integer.parseInt(jeniskat) == 1) {
                    holder.IdIconCat.setColorFilter(selected_position == position ? ContextCompat.getColor(context, R.color.colorAccent) : ContextCompat.getColor(context, R.color.colorPrimary));
                } else {
                    holder.IdIconCat.setColorFilter(selected_position == position ? ContextCompat.getColor(context, R.color.colorAccent) : ContextCompat.getColor(context, R.color.RedTheme));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myKategori.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView NamaCat, IDCat, IdIcon;
        private ImageView IdIconCat;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            MyDatabase = new DBSLC(context);
            IDCat = itemView.findViewById(R.id.IdCat);
            IdIcon = itemView.findViewById(R.id.IdIconCataja);
            NamaCat = itemView.findViewById(R.id.tv_selcat);
            IdIconCat = itemView.findViewById(R.id.imv_icon_cat);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

        }
    }
}
