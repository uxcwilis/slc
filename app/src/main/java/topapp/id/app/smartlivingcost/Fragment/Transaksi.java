package topapp.id.app.smartlivingcost.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import topapp.id.app.smartlivingcost.Activity.UpdateDeleteTrans;
import topapp.id.app.smartlivingcost.Adapter.TransaksiSearchRV;
import topapp.id.app.smartlivingcost.Model.TransaksiChildModel;
import topapp.id.app.smartlivingcost.Model.TransaksiParentModel;
import topapp.id.app.smartlivingcost.R;

public class Transaksi extends Fragment {
    private DBSLC MyDatabase;
    private TransaksiParentRV TPRV;
    private RecyclerView recyclerView, recyclerViewSearch;
    private TextView tv_total_pengeluaran, tv_total_pemasukan, tv_total_sisa;
    private MenuItem reset, setrange;
    private Calendar newCalendar;
    private SimpleDateFormat dateFormatter;
    private int sisa;
    private NumberFormat nbFmt;
    private String settglawal, settglakhir;
    private ScrollView sv_trans;
    private TransaksiSearchRV adapter;
    private RelativeLayout rlnodata;
    private List<TransaksiParentModel> transaksiParentModels = new ArrayList<>();
    private List<TransaksiChildModel> search = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaksi, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Sepanjang Waktu");
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        setHasOptionsMenu(true);

        MyDatabase = new DBSLC(getActivity().getBaseContext());

        sv_trans = rootView.findViewById(R.id.sv_trans);
        recyclerViewSearch = rootView.findViewById(R.id.rv_search_trans);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerViewSearch.setLayoutManager(layoutManager2);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        adapter = new TransaksiSearchRV(search);
        adapter.setOnItemClickListener(new TransaksiSearchRV.ClickListener() {
            @Override
            public void onItemClick(int position, String idcat, String idtrans, String namacat, String iconcat, String kettrans, String phototrans, String biaya, String tgl, String jenis, String remindat, View v) {
                Intent intent = new Intent(getActivity(), UpdateDeleteTrans.class);
                intent.putExtra("idcat", idcat);
                intent.putExtra("idtrans", idtrans);
                intent.putExtra("namacat", namacat);
                intent.putExtra("iconcat", iconcat);
                intent.putExtra("kettrans", kettrans);
                intent.putExtra("phototrans", phototrans);
                intent.putExtra("biaya", biaya);
                intent.putExtra("tgl", tgl);
                intent.putExtra("jenis", jenis);
                intent.putExtra("tanggal_remindat", remindat);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
            }
        });
        recyclerViewSearch.setAdapter(adapter);


        TPRV = new TransaksiParentRV(transaksiParentModels);
        recyclerView = rootView.findViewById(R.id.rv_parent_trans);
        rlnodata = rootView.findViewById(R.id.rl_nodata);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(TPRV);
        getData("0", "0");
        getDataSearch();
        tv_total_pemasukan = rootView.findViewById(R.id.tv_total_pemasukan);
        tv_total_pengeluaran = rootView.findViewById(R.id.tv_total_pengeluaran);
        tv_total_sisa = rootView.findViewById(R.id.tv_total_sisa);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Locale localeID = new Locale("in", "ID");
        nbFmt = NumberFormat.getCurrencyInstance(localeID);
        newCalendar = Calendar.getInstance();
        tv_total_pemasukan.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal("1", "0", "0"))));
        tv_total_pengeluaran.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal("0", "0", "0"))));

        sisa = MyDatabase.gettotal("1", "0", "0") + MyDatabase.gettotal("0", "0", "0");
        if (sisa < 0) {
            tv_total_sisa.setText("-" + nbFmt.format(sisa));
        } else {
            tv_total_sisa.setText(String.valueOf(nbFmt.format(sisa)));
        }

        return rootView;
    }

    @SuppressLint("Recycle")
    public void getData(String tglawal, String tglakhir) {
        transaksiParentModels.clear();
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor;
        String sql1 = "SELECT Transaksi.TglTrans, Sum(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON " +
                "Transaksi.IdKat = Kategori.IdKat GROUP BY Transaksi.TglTrans ORDER BY Transaksi.TglTrans DESC";
        String sql2 = "SELECT Transaksi.TglTrans, Sum(Transaksi.JumlahTrans) FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON " +
                "Transaksi.IdKat = Kategori.IdKat WHERE Transaksi.TglTrans BETWEEN '" + tglawal + "' AND '" + tglakhir + "' GROUP BY Transaksi.TglTrans  " +
                "ORDER BY Transaksi.TglTrans DESC";
        if (tglawal.equals("0") || tglawal.equals("") || tglakhir.equals("0") || tglakhir.equals("")) {
            cursor = ReadData.rawQuery(sql1, null);
        } else {
            cursor = ReadData.rawQuery(sql2, null);
        }
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            TransaksiParentModel transaksiParentModel = new TransaksiParentModel(cursor.getString(0),
                    cursor.getString(1));
            transaksiParentModels.add(transaksiParentModel);
        }
    }

    @SuppressLint("Recycle")
    private void getDataSearch() {
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT Transaksi.IdTrans, Transaksi.IdKat, Kategori.IdIconKat, Kategori.NamaKat, " +
                "Transaksi.KetTrans, Transaksi.GbrTrans, Transaksi.JumlahTrans, Kategori.JenisKat, " +
                "Transaksi.TglTrans, Transaksi.Remind_at FROM " + DBSLC.Transaksi.NamaTabelTrans + " INNER JOIN Kategori ON " +
                "Transaksi.IdKat = Kategori.IdKat  ORDER BY Transaksi.TglTrans DESC", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            TransaksiChildModel transaksiChildModel = new TransaksiChildModel(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8),
                    cursor.getString(9));
            search.add(transaksiChildModel);

        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_transaction, menu);
        reset = menu.findItem(R.id.reset);
        setrange = menu.findItem(R.id.view);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                nextText = nextText.toLowerCase();
                ArrayList<TransaksiChildModel> dataFilter = new ArrayList<>();
                for (TransaksiChildModel data : search) {
                    String nama = data.getNamaCat().toLowerCase();
                    String ket = data.getKetCat().toLowerCase();
                    String harga = data.getBiayaTrans().toLowerCase();
                    if (nama.contains(nextText) || ket.contains(nextText) || harga.contains(nextText)) {
                        dataFilter.add(data);
                        rlnodata.setVisibility(View.GONE);
                    }
                    if (dataFilter.isEmpty()) {
                        rlnodata.setVisibility(View.VISIBLE);
                    }
                }
                adapter.setFilter(dataFilter);
                return true;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                sv_trans.setVisibility(View.VISIBLE);
                recyclerViewSearch.setVisibility(View.GONE);
                setrange.setVisible(true);
                rlnodata.setVisibility(View.GONE);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        searchView.onActionViewExpanded(); //new Added line
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Kategori, Keterangan, Biaya");

        if (!searchView.isFocused()) {
            searchView.clearFocus();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                sv_trans.setVisibility(View.GONE);
                recyclerViewSearch.setVisibility(View.VISIBLE);
                setrange.setVisible(false);
                return true;
            case R.id.view:
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
                @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.set_range_date_dialogbox, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogCustom);
                alertDialogBuilderUserInput.setView(mView);

                final EditText tglawal = mView.findViewById(R.id.et_tanggal);
                final EditText tglakhir = mView.findViewById(R.id.et_tanggal2);

                tglawal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                tglawal.setText(String.valueOf(dateFormatter.format(newDate.getTime())));
                                settglawal = String.valueOf(dateFormatter.format(newDate.getTime()));
                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                tglakhir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                tglakhir.setText(String.valueOf(dateFormatter.format(newDate.getTime())));
                                settglakhir = String.valueOf(dateFormatter.format(newDate.getTime()));
                            }

                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                alertDialogBuilderUserInput
                        .setCancelable(true)
                        .setPositiveButton("Tampilkan",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        if (tglawal.getText().toString().isEmpty() || tglawal.getText().toString().equals("")) {
                                            Toast.makeText(getActivity(), "Tanggal Awal Belum Di Atur", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (tglakhir.getText().toString().isEmpty() || tglakhir.getText().toString().equals("")) {
                                                Toast.makeText(getActivity(), "Tanggal Akhir Belum Di Atur", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                                    @SuppressLint("SetTextI18n")
                                                    @Override
                                                    public void run() {
                                                        transaksiParentModels.clear();
                                                        getData(tglawal.getText().toString(), tglakhir.getText().toString());
                                                        TPRV = new TransaksiParentRV(transaksiParentModels);
                                                        TPRV.notifyDataSetChanged();
                                                        recyclerView.setAdapter(TPRV);
                                                        reset.setVisible(true);
                                                        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(tglawal.getText().toString()
                                                                + " Hingga " + tglakhir.getText().toString());
                                                        tv_total_pemasukan.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal("1",
                                                                tglawal.getText().toString(), tglakhir.getText().toString()))));
                                                        tv_total_pengeluaran.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal("0",
                                                                tglawal.getText().toString(), tglakhir.getText().toString()))));
                                                        sisa = MyDatabase.gettotal("1", tglawal.getText().toString(), tglakhir.getText().toString()) +
                                                                MyDatabase.gettotal("0", tglawal.getText().toString(), tglakhir.getText().toString());
                                                        if (sisa < 0) {
                                                            tv_total_sisa.setText("-" + nbFmt.format(sisa));
                                                        } else {
                                                            tv_total_sisa.setText(String.valueOf(nbFmt.format(sisa)));
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                })

                        .setNegativeButton("Batal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

                return true;
            case R.id.reset:
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        transaksiParentModels.clear();
                        getData("0", "0");
                        TPRV = new TransaksiParentRV(transaksiParentModels);
                        TPRV.notifyDataSetChanged();
                        recyclerView.setAdapter(TPRV);
                        reset.setVisible(false);
                        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle("Sepanjang Waktu");
                        tv_total_pemasukan.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal("1", "0", "0"))));
                        tv_total_pengeluaran.setText(String.valueOf(nbFmt.format(MyDatabase.gettotal("0", "0", "0"))));

                        sisa = MyDatabase.gettotal("1", "0", "0") + MyDatabase.gettotal("0", "0", "0");
                        if (sisa < 0) {
                            tv_total_sisa.setText("-" + nbFmt.format(sisa));
                        } else {
                            tv_total_sisa.setText(String.valueOf(nbFmt.format(sisa)));
                        }
                    }
                });
                return true;
        }
        return false;
    }


}
