package topapp.id.app.smartlivingcost.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.maltaisn.calcdialog.CalcDialog;
import com.maltaisn.icondialog.IconHelper;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import topapp.id.app.smartlivingcost.Activity.Category;
import topapp.id.app.smartlivingcost.Activity.MainActivity;
import topapp.id.app.smartlivingcost.Activity.Planning;
import topapp.id.app.smartlivingcost.Adapter.SelectCategoryRVItem;
import topapp.id.app.smartlivingcost.Model.KategoriModel;
import topapp.id.app.smartlivingcost.R;

import static android.content.ContentValues.TAG;


public class Input extends Fragment implements CalcDialog.CalcDialogCallback {
    private DBSLC MyDatabase;
    private String rcNamaKat, rcIconKat, rcIdKat, rcJenisKat, photo_uri, tanggal, ket, tanggal_remindat = "";
    private static TabLayout tabLayout;
    @Nullable
    private BigDecimal value = null;
    @NonNull
    private NumberFormat nbFmt;
    public TextView namaCat, tv_photo;

    private EditText et_biaya, et_tanggal, et_ket, et_remindat;
    private ImageView iconcat, iconbiaya, iconket, icontanggal, iconphoto, imvphoto, icon_remindat;
    private Toolbar toolbar;
    private SimpleDateFormat dateFormatter;
    private RecyclerView recyclerView2, recyclerView;
    private SelectCategoryRVItem SCRVI;
    private RelativeLayout rlcat;
    private Button btn_addTrans;
    private TextView btncancel_photo;
    private Calendar newCalendar;
    private int biaya, tabselect_cat;

    private List<KategoriModel> myKategori = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_input, container, false);
        //Menerapkan TabLayout dan ViewPager pada Activity
        toolbar = rootView.findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Input Data Transaksi Baru");
        setHasOptionsMenu(true);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));


        MyDatabase = new DBSLC(getActivity().getBaseContext());
        getData("0");
        SCRVI = new SelectCategoryRVItem(myKategori);
        recyclerView = rootView.findViewById(R.id.select_category);
        recyclerView2 = rootView.findViewById(R.id.select_category2);
        rlcat = rootView.findViewById(R.id.rlcat);
        final IconHelper iconHelper = IconHelper.getInstance(getContext());
        rcJenisKat = "";
        btn_addTrans = rootView.findViewById(R.id.add_trans);
        et_tanggal = rootView.findViewById(R.id.et_tanggal);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        iconbiaya = rootView.findViewById(R.id.icon_biaya);
        iconket = rootView.findViewById(R.id.icon_ket);
        icontanggal = rootView.findViewById(R.id.icon_tanggal);
        iconphoto = rootView.findViewById(R.id.icon_photo);
        imvphoto = rootView.findViewById(R.id.imv_photo);
        btncancel_photo = rootView.findViewById(R.id.btncancel_photo);
        tv_photo = rootView.findViewById(R.id.tv_photo);
        icon_remindat = rootView.findViewById(R.id.icon_remindat);
        et_remindat = rootView.findViewById(R.id.et_remindat);
        photo_uri = "";
        et_ket = rootView.findViewById(R.id.et_ket);


        newCalendar = Calendar.getInstance();

        tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        createrv();
        createrv2();

        SCRVI.setOnItemClickListener(new SelectCategoryRVItem.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                Log.d(TAG, "onItemClick position: " + rcIdKat + rcIconKat + rcNamaKat + rcJenisKat);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rcIconKat = myKategori.get(position).getIdIconKat();
                        rcIdKat = myKategori.get(position).getIdKat();
                        rcNamaKat = myKategori.get(position).getNamaKat();
                        rcJenisKat = myKategori.get(position).getJenisKat();
                        namaCat.setText(rcNamaKat);
                        rlcat.setVisibility(View.GONE);

                        iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                            @Override
                            public void onDataLoaded() {
                                // This happens on UI thread, and is guaranteed to be called.
                                iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(rcIconKat)).getDrawable(Objects.requireNonNull(getContext())));
                                if (Integer.parseInt(rcJenisKat) == 1) {
                                    iconcat.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Input Data Pemasukan Baru");
                                    btn_addTrans.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_primary));
                                    iconbiaya.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    iconket.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    iconphoto.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    icontanggal.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    icon_remindat.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));


                                } else {
                                    iconcat.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                                    Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Input Data Pengeluaran Baru");
                                    toolbar.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                                    btn_addTrans.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_red));
                                    iconbiaya.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                                    iconket.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                                    iconphoto.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                                    icontanggal.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                                    icon_remindat.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));


                                }
                            }
                        });
                    }
                });

            }
        });


        et_biaya = rootView.findViewById(R.id.et_biaya);
        namaCat = rootView.findViewById(R.id.tv_catname);
        final CalcDialog calcDialog = new CalcDialog();
        Locale localeID = new Locale("in", "ID");
        nbFmt = NumberFormat.getCurrencyInstance(localeID);

        iconcat = rootView.findViewById(R.id.imv_iconcat);

        et_biaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcJenisKat.isEmpty() || rcJenisKat.equals("") || rcJenisKat.equals("null") || rcJenisKat == null) {
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Menambah Data Gagal")
                            .setContentText("Silahkan Pilih Kategori")
                            .show();
                } else {
                    calcDialog.getSettings().setInitialValue(value)
                            .setNumberFormat(nbFmt).setExpressionEditable(true).setExpressionShown(true);
                    calcDialog.show(getChildFragmentManager(), "calc_dialog");
                }
            }
        });

        et_remindat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_remindat.setText(dateFormatter.format(newDate.getTime()));
                        tanggal_remindat = dateFormatter.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        et_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et_tanggal.setText(dateFormatter.format(newDate.getTime()));
                        tanggal = dateFormatter.format(newDate.getTime());
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        iconcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewrlcat();
            }
        });

        namaCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewrlcat();
            }
        });

        tabLayout = rootView.findViewById(R.id.tabs_select_category);
        final RelativeLayout appBar = rootView.findViewById(R.id.appbarv);
        ImageView catset = rootView.findViewById(R.id.btnsetcat);

        catset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Category.class);
                i.putExtra("index", tabselect_cat);
                startActivity(i);
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"));
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    cleardatacategory();
                    getData("0");
                    createrv();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.GONE);
                    appBar.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.selectcat_bg2));
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                    tabselect_cat = 0;
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    cleardatacategory();
                    getData("1");
                    createrv2();
                    recyclerView.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    appBar.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.selectcat_bg1));
                    tabselect_cat = 1;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });


        btn_addTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcJenisKat.isEmpty() || rcJenisKat.equals("") || rcJenisKat.equals("null") || rcJenisKat == null) {
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Menambah Data Gagal")
                            .setContentText("Silahkan Pilih Kategori")
                            .show();
                } else {
                    if (et_biaya.length() < 1) {
                        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Menambah Data Gagal")
                                .setContentText("Silahkan Isi Jumlah Transaksi")
                                .show();
                    } else {
                        saveData();
                    }
                }
            }
        });

        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                imvphoto.setImageBitmap(r.getBitmap());
                                imvphoto.setVisibility(View.VISIBLE);
                                btncancel_photo.setVisibility(View.VISIBLE);
                                photo_uri = String.valueOf(r.getPath());
//                                Toast.makeText(getActivity(), r.getPath(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                            }
                        }).show(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
            }
        });

        btncancel_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Batalkan Gambar")
                        .setContentText("Apakah Anda Yakin untuk membatalkan gambar terpilih?")
                        .setCancelText("Tidak")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                imvphoto.setVisibility(View.GONE);
                                btncancel_photo.setVisibility(View.GONE);
                                photo_uri = "";
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });


        //      Skenarion Menggunakan Remider
        Bundle b = this.getArguments();
        if (b != null) {
            String getIdtrans = b.getString("idtrans");
            String getIdkat = b.getString("idkat");
            String gettgltrans = b.getString("tgltrans");
            String getjumlahtrans = b.getString("jumlahtrans");
            String getkettrans = b.getString("kettrans");
            String getgbrtrans = b.getString("gbrtrans");
            String gettglinput = b.getString("tglinput");
            String getjaminput = b.getString("jaminput");
            String getremindat = b.getString("remindat");
            String getnamakat = b.getString("namakat");
            String getidiconkat = b.getString("idiconkat");
            String getjeniskat = b.getString("jeniskat");

            rcJenisKat = getjeniskat;
            if (!getkettrans.equals("")) {
                et_ket.setText(getkettrans);
            }
            namaCat.setText(getnamakat);
            iconcat.setImageDrawable(iconHelper.getIcon(Integer.parseInt(getidiconkat)).getDrawable(getActivity()));
            rcIdKat = getIdkat;
            if (!getgbrtrans.equals("") || !getgbrtrans.isEmpty()) {
                photo_uri = getgbrtrans;
                Glide.with(getActivity()).load(photo_uri).fitCenter().centerCrop().dontAnimate().error(R.drawable.gallery)
                        .into(imvphoto);
                imvphoto.setVisibility(View.VISIBLE);
                btncancel_photo.setVisibility(View.VISIBLE);
            }

            if (Integer.parseInt(getjeniskat) == 1) {
                iconcat.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Input Data Pemasukan Baru");
                btn_addTrans.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                iconbiaya.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                iconket.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                iconphoto.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                icontanggal.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                icon_remindat.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            } else {
                iconcat.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Input Data Pengeluaran Baru");
                toolbar.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                btn_addTrans.setBackgroundColor(getResources().getColor(R.color.RedTheme));
                iconbiaya.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                iconket.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                iconphoto.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                icontanggal.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));
                icon_remindat.setColorFilter(ContextCompat.getColor(getContext(), R.color.RedTheme));

            }
        }

        return rootView;
    }

    private void viewrlcat() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rlcat.getVisibility() == View.VISIBLE) {
                    rlcat.setVisibility(View.GONE);
                } else {
                    selectCategory();
                }
            }
        });
    }

    private void saveData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date, date_remindat;
        String set_remindat = "";
        try {
            date = originalFormat.parse(tanggal);
            if (!tanggal_remindat.equals("") || !tanggal_remindat.isEmpty()) {
                date_remindat = originalFormat.parse(tanggal_remindat);
                set_remindat = String.valueOf(targetFormat.format(date_remindat));
            }
            String set_tgl_transaksi = String.valueOf(targetFormat.format(date));

            String set_idKat = String.valueOf(rcIdKat);

            String set_ket_transaksi = et_ket.getText().toString();
            String set_photo_uri = photo_uri;


//            String set_tgl_input = String.valueOf(tanggal);
            Date todayDate = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String set_tgl_input = formatter.format(todayDate);
            @SuppressLint("SimpleDateFormat") String set_jam_input = new SimpleDateFormat("HH:mm:ss").format(newCalendar.getTime());


            //Mendapatkan Repository dengan Mode Menulis
            SQLiteDatabase create = MyDatabase.getWritableDatabase();

            //Membuat Map Baru, Yang Berisi Nama Kolom dan Data Yang Ingin Dimasukan
            ContentValues values = new ContentValues();
            values.put(DBSLC.Transaksi.IdKat, Integer.parseInt(set_idKat));
            values.put(DBSLC.Transaksi.TglTrans, set_tgl_transaksi);
            values.put(DBSLC.Transaksi.JumlahTrans, biaya);
            values.put(DBSLC.Transaksi.KetTrans, String.valueOf(set_ket_transaksi));
            values.put(DBSLC.Transaksi.GbrTrans, set_photo_uri);
            values.put(DBSLC.Transaksi.TglInputTrans, set_tgl_input);
            values.put(DBSLC.Transaksi.JamInputTrans, set_jam_input);
            if (!tanggal_remindat.equals("") || !tanggal_remindat.isEmpty()) {
                values.put(DBSLC.Transaksi.Remind_At, set_remindat);
            }


            //Menambahkan Baris Baru, Berupa Data Yang Sudah Diinputkan pada Kolom didalam Database
            create.insert(DBSLC.Transaksi.NamaTabelTrans, null, values);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Berhasil")
                .setContentText("Sukses Menyimpan Transaksi")
                .show();
        et_ket.setText("");
        et_biaya.setText("");
        et_tanggal.setText("");
        et_remindat.setText("");
        MainActivity.performTransaksiClick();

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_more_input, menu);
        MenuItem more = menu.findItem(R.id.more);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.more) {
            Intent i = new Intent(getActivity(), Planning.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        this.value = value;
        et_biaya.setText(String.valueOf(nbFmt.format(value)));
        if (Integer.parseInt(rcJenisKat) == 0) {
            biaya = -1 * value.intValue();
        } else {
            biaya = value.intValue();
        }

    }


    private void selectCategory() {
        rlcat.setVisibility(View.VISIBLE);
        if (rcJenisKat.equals("1")) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            TabLayout.Tab tab2 = tabLayout.getTabAt(1);
            tab2.select();
        } else {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            TabLayout.Tab tab2 = tabLayout.getTabAt(0);
            tab2.select();
        }


    }


    //Berisi Statement-Statement Untuk Mengambi Data dari Database
    @SuppressLint("Recycle")
    protected void getData(String id) {
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.MyColumns.NamaTabelKat + " WHERE " + DBSLC.MyColumns.JenisKat + "=" + id, null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            KategoriModel kategoriModel = new KategoriModel(cursor.getString(0), cursor.getString(1)
                    , cursor.getString(2), cursor.getString(3), cursor.getString(4));
            myKategori.add(kategoriModel);
        }
    }

    private void cleardatacategory() {
        myKategori.clear();
    }

    private void createrv() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(SCRVI);
    }

    private void createrv2() {
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(getActivity(), 4);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(SCRVI);

    }

}


