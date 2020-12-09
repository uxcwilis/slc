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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import suska.uin.tif.smartlivingcost.Fragment.DBSLC;
import suska.uin.tif.smartlivingcost.Model.TransaksiBerandaModel;
import suska.uin.tif.smartlivingcost.R;

public class CategoryRVDashboard extends RecyclerView.Adapter<CategoryRVDashboard.ViewHolder> {
    private static ClickListener clickListener;
    private List<TransaksiBerandaModel> arrayList;
    private Context context;
    private String jeniskat;
    private DBSLC MyDatabase;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public CategoryRVDashboard(List<TransaksiBerandaModel> arrayList) {
        super();
        this.arrayList = arrayList;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView NamaCat, TraxCat, IDCat, IdIcon, TotalTrans, SisaBudget, TotalBudget, tvSisa;
        private ImageView IdIconCat;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            MyDatabase = new DBSLC(context);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            IDCat = itemView.findViewById(R.id.IdCat);
            IdIcon = itemView.findViewById(R.id.IdIconCataja);
            NamaCat = itemView.findViewById(R.id.name_cat);
            TraxCat = itemView.findViewById(R.id.trax_cat);
            IdIconCat = itemView.findViewById(R.id.imv_icon_cat);
            TotalTrans = itemView.findViewById(R.id.overflow_tv);
            SisaBudget = itemView.findViewById(R.id.sisa_budget);
            TotalBudget = itemView.findViewById(R.id.total_budget);
            tvSisa = itemView.findViewById(R.id.tv_sisa);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);

        }


    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CategoryRVDashboard.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_dashboard, parent, false);
        context = parent.getContext();

        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final IconHelper iconHelper = IconHelper.getInstance(context);
        TransaksiBerandaModel data = arrayList.get(position);
        final String idcat = data.getIDCat();
        final String namacat = data.getNamaCat();
        final String idiconcat = data.getIconCat();
        final String totalbudget = data.getBudget();
        jeniskat = data.getJenisKat();


        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);

        if (jeniskat.equals("1") || totalbudget.equals("0")) {
            holder.TotalBudget.setVisibility(View.GONE);
            holder.SisaBudget.setVisibility(View.GONE);
            holder.tvSisa.setVisibility(View.GONE);
        } else {
            holder.TotalBudget.setText(" dari " + nbFmt.format(Integer.parseInt(totalbudget)));
            int sisa = Integer.parseInt(totalbudget) + MyDatabase.gettotal_category(idcat);
            holder.SisaBudget.setText(String.valueOf(nbFmt.format(sisa)));
            if (sisa < 0) {
                holder.SisaBudget.setTextColor(ContextCompat.getColor(context, R.color.RedTheme));
            }
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
                    holder.IdIconCat.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                } else {
                    holder.IdIconCat.setColorFilter(ContextCompat.getColor(context, R.color.RedTheme));
                }
            }
        });

        holder.TraxCat.setText(MyDatabase.countRecordTransaksiThisMonth(Integer.parseInt(idcat)) + " Transaksi");
        holder.TotalTrans.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal_category(idcat))));
        if (MyDatabase.gettotal_category(idcat) > 0) {
            holder.TotalTrans.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return arrayList.size();
    }
}
