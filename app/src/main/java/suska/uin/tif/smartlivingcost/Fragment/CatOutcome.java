package suska.uin.tif.smartlivingcost.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconHelper;
import com.maltaisn.icondialog.IconView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.abhinay.input.CurrencyEditText;
import suska.uin.tif.smartlivingcost.Activity.Category;
import suska.uin.tif.smartlivingcost.Adapter.CategoryRVItem;
import suska.uin.tif.smartlivingcost.Model.KategoriModel;
import suska.uin.tif.smartlivingcost.R;

public class CatOutcome extends Fragment implements IconDialog.Callback {
    private DBSLC MyDatabase;
    public static RecyclerView recyclerView;
    public static TextView tv_nodata;
    public FloatingActionButton fabCatIncome;
    private Context context;
    private EditText dialogName;
    private CurrencyEditText Budget;
    private IconDialog iconDialog;

    private IconView IconCat;
    private TextView tvidicon2, dialogTitle, tvid;
    private int IconID;
    private String setNamaCat, setIconKat, setBudget, rcNamaKat, rcIconKat, rcIdKat, rcJenisKat;
    private int rcBudgetKat;
    private List<KategoriModel> myKategori = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cat_outcome, container, false);
        context = getActivity().getApplicationContext();

        MyDatabase = new DBSLC(getActivity().getBaseContext());
        recyclerView = rootView.findViewById(R.id.recycler);
        tv_nodata = rootView.findViewById(R.id.tv_nodata_outcomecat);

        fabCatIncome = rootView.findViewById(R.id.fab_addOutcomeCat);
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical


        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.line)));
        recyclerView.addItemDecoration(itemDecoration);
        iconDialog = new IconDialog();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fabCatIncome.isShown())
                    fabCatIncome.show();
                else if (dy > 0 && fabCatIncome.isShown())
                    fabCatIncome.hide();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        fabCatIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOutcomeCat();

            }
        });
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void updateOutcomeCat() {
        if (Integer.parseInt(rcJenisKat) == 0) {
            //Statement disini akan dijalankan saat FAB di Klik
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.outcomecat_dialogbox, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogCustomRed);
            alertDialogBuilderUserInput.setView(mView);

            final TextInputLayout errorInputLayout = mView.findViewById(R.id.layout_Name);
            dialogTitle = mView.findViewById(R.id.dialogTitle);
            dialogName = mView.findViewById(R.id.dialogName);
            tvidicon2 = mView.findViewById(R.id.idicon_outcomecatdb);
            IconCat = mView.findViewById(R.id.icv_dialog_add_outcom_cat);
            tvid = mView.findViewById(R.id.id_outcomecatdb);
            Budget = mView.findViewById(R.id.budget_cat);
            Budget.setSeparator(".");
            Budget.setDecimals(false);


            dialogTitle.setText("Perbaharui Kategori Pengeluaran");
            dialogName.setText(rcNamaKat);
            tvidicon2.setText(rcIconKat);
            tvid.setText(String.valueOf(rcIdKat));
            Budget.setText(String.valueOf(rcBudgetKat));

            final IconHelper iconHelper = IconHelper.getInstance(context);
            iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                @Override
                public void onDataLoaded() {
                    // This happens on UI thread, and is guaranteed to be called.
                    IconCat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(context));
                }
            });

            IconCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iconDialog.setSelectedIcons(IconID);
                    iconDialog.setTargetFragment(CatOutcome.this, 0);
                    iconDialog.show(getFragmentManager(), "icon_dialog");
                }
            });
            alertDialogBuilderUserInput
                    .setCancelable(true)
                    .setPositiveButton("Simpan",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    updateData();
                                    reload();
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


            dialogName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() < 1) {
                        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        errorInputLayout.setError("Silahkan ketik nama kategori");
                    } else {
                        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        errorInputLayout.setError(null);
                    }

                }
            });
        } else {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.outcomecat_dialogbox, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogCustom);
            alertDialogBuilderUserInput.setView(mView);

            final TextInputLayout errorInputLayout = mView.findViewById(R.id.layout_Name);
            dialogTitle = mView.findViewById(R.id.dialogTitle);
            dialogName = mView.findViewById(R.id.dialogName);
            tvidicon2 = mView.findViewById(R.id.idicon_outcomecatdb);
            IconCat = mView.findViewById(R.id.icv_dialog_add_outcom_cat);
            tvid = mView.findViewById(R.id.id_outcomecatdb);
            Budget = mView.findViewById(R.id.budget_cat);
            Budget.setSeparator(".");
            Budget.setDecimals(false);
            TextView ket_budget_tv = mView.findViewById(R.id.ket_budget_tv);
            Budget.setVisibility(View.GONE);
            ket_budget_tv.setVisibility(View.GONE);


            dialogTitle.setText("Perbaharui Kategori Pemasukan");
            dialogName.setText(rcNamaKat);
            tvidicon2.setText(rcIconKat);
            tvid.setText(String.valueOf(rcIdKat));


            final IconHelper iconHelper = IconHelper.getInstance(context);
            iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                @Override
                public void onDataLoaded() {
                    // This happens on UI thread, and is guaranteed to be called.
                    IconCat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(context));
                }
            });
            IconCat.setColorFilter(ContextCompat.getColor(context, R.color.RedTheme));
            IconCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iconDialog.setSelectedIcons(IconID);
                    iconDialog.setTargetFragment(CatOutcome.this, 0);
                    iconDialog.show(getFragmentManager(), "icon_dialog");

                }
            });
            alertDialogBuilderUserInput
                    .setCancelable(true)
                    .setPositiveButton("Simpan",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    updateData();
                                    reload();
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


            dialogName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() < 1) {
                        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        errorInputLayout.setError("Silahkan ketik nama kategori");
                    } else {
                        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        errorInputLayout.setError(null);
                    }

                }
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void addOutcomeCat() {
        //Statement disini akan dijalankan saat FAB di Klik
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams") final View mView = layoutInflaterAndroid.inflate(R.layout.outcomecat_dialogbox, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogCustomRed);
        alertDialogBuilderUserInput.setView(mView);

        final TextInputLayout errorInputLayout = mView.findViewById(R.id.layout_Name);
        dialogName = mView.findViewById(R.id.dialogName);
        dialogTitle = mView.findViewById(R.id.dialogTitle);
        tvidicon2 = mView.findViewById(R.id.idicon_outcomecatdb);
        IconCat = mView.findViewById(R.id.icv_dialog_add_outcom_cat);
        Budget = mView.findViewById(R.id.budget_cat);
        Budget.setSeparator(".");
        Budget.setDecimals(false);

        dialogTitle.setText("Tambah Kategori Pengeluaran");

        IconCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconDialog.setSelectedIcons(Integer.parseInt(tvidicon2.getText().toString()));
                iconDialog.setTargetFragment(CatOutcome.this, 0);
                iconDialog.show(getFragmentManager(), "icon_dialog");
            }
        });
        alertDialogBuilderUserInput
                .setCancelable(true)
                .setPositiveButton("Simpan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                setData();
                                saveData();
                                reload();
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
        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


        dialogName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    errorInputLayout.setError("Silahkan ketik nama kategori");
                } else {
                    alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    errorInputLayout.setError(null);
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        myKategori.clear();
        rcJenisKat = "0";
        getData();


        if (!myKategori.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }

    private void reload() {
        Intent i = new Intent(getActivity(), Category.class);
        if (Integer.parseInt(rcJenisKat) == 0) {
            i.putExtra("index", 0);
        } else {
            i.putExtra("index", 1);
        }
        startActivity(i);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void updateData() {
        setNamaCat = dialogName.getText().toString();
        setIconKat = tvidicon2.getText().toString();
        String setIdKat = tvid.getText().toString();
        setBudget = String.valueOf(Budget.getCleanIntValue());


        SQLiteDatabase database = MyDatabase.getReadableDatabase();

        //Memasukan Data baru pada 3 kolom (NIM, Nama dan Jurusan)
        ContentValues values = new ContentValues();
        values.put(DBSLC.MyColumns.IdKat, setIdKat);
        values.put(DBSLC.MyColumns.NamaKat, setNamaCat);
        values.put(DBSLC.MyColumns.IdIconKat, setIconKat);
        values.put(DBSLC.MyColumns.JenisKat, rcJenisKat);
        values.put(DBSLC.MyColumns.BudgetKat, Integer.parseInt(setBudget));
        //Untuk Menentukan Data/Item yang ingin diubah, berdasarkan NIM
        String selection = DBSLC.MyColumns.IdKat + " LIKE ?";
        String[] selectionArgs = {setIdKat};
        database.update(DBSLC.MyColumns.NamaTabelKat, values, selection, selectionArgs);
        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Berhasil")
                .setContentText("Sukses Memperbaharui Kategori")
                .show();
    }

    private void setData() {
        setNamaCat = dialogName.getText().toString();
        setIconKat = tvidicon2.getText().toString();
        if (Budget.getText().toString().isEmpty() || Budget.getText().toString().equals("")) {
            setBudget = "0";
        } else {
            setBudget = String.valueOf(Budget.getCleanIntValue());
        }

    }

    private void saveData() {
        //Mendapatkan Repository dengan Mode Menulis
        SQLiteDatabase create = MyDatabase.getWritableDatabase();

        //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
        ContentValues values = new ContentValues();
        values.put(DBSLC.MyColumns.NamaKat, setNamaCat);
        values.put(DBSLC.MyColumns.IdIconKat, setIconKat);
        values.put(DBSLC.MyColumns.JenisKat, "0");
        values.put(DBSLC.MyColumns.BudgetKat, Integer.parseInt(setBudget));
        //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
        create.insert(DBSLC.MyColumns.NamaTabelKat, null, values);
    }


    @Override
    public void onIconDialogIconsSelected(@NonNull Icon[] icons) {
        int[] selectedIconIds = new int[icons.length];
        for (int i = 0; i < icons.length; i++) {
            selectedIconIds[i] = icons[i].getId();
            IconCat.setIcon(selectedIconIds[0]);
            if (Integer.parseInt(rcJenisKat) == 0) {
                IconCat.setColorFilter(ContextCompat.getColor(context, R.color.RedTheme), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                IconCat.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            IconID = selectedIconIds[i];
        }
        tvidicon2.setText(String.valueOf(IconID));
    }

    //Berisi Statement-Statement Untuk Mengambi Data dari Database
    @SuppressLint("Recycle")
    protected void getData() {
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();

        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.MyColumns.NamaTabelKat + " WHERE " + DBSLC.MyColumns.JenisKat + "= '0'", null);

        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal

        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {

            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            KategoriModel kategoriModel = new KategoriModel(cursor.getString(0), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4));
            myKategori.add(kategoriModel);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        CategoryRVItem CRVI = new CategoryRVItem(myKategori);
        CRVI.notifyDataSetChanged();
        recyclerView.setAdapter(CRVI);
        CRVI.setOnItemClickListener(new CategoryRVItem.ClickListener() {
            @Override
            public void onItemClick(int position, String IdKat, String NamaKat, String IdIconKat, String JenisKat, String BudgetKat, View v) {
                rcIconKat = IdIconKat;
                rcIdKat = IdKat;
                rcNamaKat = NamaKat;
                rcJenisKat = JenisKat;
                rcBudgetKat = Integer.parseInt(BudgetKat);
                IconID = Integer.parseInt(rcIconKat);
                updateOutcomeCat();
            }
        });
    }


}


