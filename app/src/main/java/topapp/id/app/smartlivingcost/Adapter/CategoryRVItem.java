package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.maltaisn.icondialog.IconHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import topapp.id.app.smartlivingcost.Fragment.CatIncome;
import topapp.id.app.smartlivingcost.Fragment.CatOutcome;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.KategoriModel;
import topapp.id.app.smartlivingcost.R;

public class CategoryRVItem extends RecyclerView.Adapter<CategoryRVItem.ViewHolder> {
    private static ClickListener clickListener;
    private List<KategoriModel> myKategori;

    private Context context;
    private String jeniskat;
    private DBSLC MyDatabase;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public CategoryRVItem(List<KategoriModel> myKategori) {
        super();
        this.myKategori = myKategori;

    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView NamaCat, TraxCat, IDCat, IdIcon, BudgetCat;
        private ImageView IdIconCat;
        private ImageButton DeleteCat;


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
            DeleteCat = itemView.findViewById(R.id.overflow);
            BudgetCat = itemView.findViewById(R.id.budget_cat_cat);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),
                    myKategori.get(getAdapterPosition()).getIdKat(),
                    myKategori.get(getAdapterPosition()).getNamaKat(),
                    myKategori.get(getAdapterPosition()).getIdIconKat(),
                    myKategori.get(getAdapterPosition()).getJenisKat(),
                    myKategori.get(getAdapterPosition()).getBudgetKat()
                    ,v);

        }


    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CategoryRVItem.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, String IdKat, String NamaKat,
                         String IdIconKat, String JenisKat,String BudgetKat, View v);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final IconHelper iconHelper = IconHelper.getInstance(context);
        KategoriModel data = myKategori.get(position);
        final String idcat = data.getIdKat();
        final String namacat = data.getNamaKat();
        final String idiconcat = data.getIdIconKat();
        String budgetcat = data.getBudgetKat();
        jeniskat = data.getJenisKat();

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);

        if (jeniskat.equals("1")) {
            holder.BudgetCat.setVisibility(View.GONE);
        } else {
            holder.BudgetCat.setVisibility(View.VISIBLE);
            holder.BudgetCat.setText("Budget " + nbFmt.format(Integer.parseInt(budgetcat)));
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

        holder.TraxCat.setText(MyDatabase.countRecordTransaksi(Integer.parseInt(idcat)) + " Transaksi");

        holder.DeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new android.app.AlertDialog.Builder(context, R.style.AlertDialogCustom)
                        .setMessage("Apakah Anda Yakin untuk Menghapus Kategori " + namacat + " beserta " + holder.TraxCat.getText().toString() + " yang telah dilakukan?")
                        .setCancelable(true)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Menghapus Data Dari Database
                                DBSLC getDatabase = new DBSLC(view.getContext());
                                SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
                                //Menentukan di mana bagian kueri yang akan dipilih
                                String selection = DBSLC.MyColumns.IdKat + " LIKE ?";
                                String selection2 = DBSLC.Transaksi.IdKat + " LIKE ?";
                                //Menentukan Nama Dari Data Yang Ingin Dihapus
                                String[] selectionArgs = {holder.IDCat.getText().toString()};
                                DeleteData.delete(DBSLC.Transaksi.NamaTabelTrans, selection2, selectionArgs);
                                DeleteData.delete(DBSLC.MyColumns.NamaTabelKat, selection, selectionArgs);

                                //Menghapus Data pada List dari Posisi Tertentu
                                myKategori.remove(position);
                                notifyItemRemoved(position);

                                Toast.makeText(view.getContext(), "Data Dihapus", Toast.LENGTH_SHORT).show();

                                if (MyDatabase.countRecord(1) > 0) {
                                    CatIncome.recyclerView2.setVisibility(View.VISIBLE);
                                    CatIncome.tv_nodata.setVisibility(View.GONE);
                                } else {
                                    CatIncome.recyclerView2.setVisibility(View.GONE);
                                    CatIncome.tv_nodata.setVisibility(View.VISIBLE);
                                }
                                if (MyDatabase.countRecord(0) > 0) {
                                    CatOutcome.recyclerView.setVisibility(View.VISIBLE);
                                    CatOutcome.tv_nodata.setVisibility(View.GONE);

                                } else {
                                    CatOutcome.recyclerView.setVisibility(View.GONE);
                                    CatOutcome.tv_nodata.setVisibility(View.VISIBLE);

                                }

                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();

            }
        });
    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myKategori.size();
    }
}
