package suska.uin.tif.smartlivingcost.Adapter;

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

import suska.uin.tif.smartlivingcost.Model.KategoriModel;
import suska.uin.tif.smartlivingcost.R;

public class SelectCategoryRVItem extends RecyclerView.Adapter<SelectCategoryRVItem.ViewHolder> {
    private static ClickListener clickListener;
    private List<KategoriModel> myKategori;
    private Context context;
    private int selected_position = -1;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public SelectCategoryRVItem(List<KategoriModel> data) {
        super();
        this.myKategori = data;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SelectCategoryRVItem.clickListener = clickListener;
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
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final IconHelper iconHelper = IconHelper.getInstance(context);
        final KategoriModel data = myKategori.get(position);
        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        final String idcat = data.getIdKat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String namacat = data.getNamaKat();//Mengambil data (Nama) sesuai dengan posisi yang telah ditentukan
        final String idiconcat = data.getIdIconKat();//Mengambil data (NIM) sesuai dengan posisi yang telah ditentukan
        final String jeniskat = data.getJenisKat();//Mengambil data (NIM) sesuai dengan posisi yang telah ditentukan
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
