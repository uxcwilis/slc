package suska.uin.tif.smartlivingcost.Fragment.TabInfo;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import suska.uin.tif.smartlivingcost.Adapter.NotReviewedRVAdapter;
import suska.uin.tif.smartlivingcost.Fragment.DBSLC;
import suska.uin.tif.smartlivingcost.Model.ReviewModel;
import suska.uin.tif.smartlivingcost.R;

public class MyMenuFragment extends Fragment {
    private DBSLC MyDatabase;
    private RecyclerView recyclerView;
    List<ReviewModel> myReview = new ArrayList<>();
    private TextView tvnodata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mymenu, container, false);

        MyDatabase = new DBSLC(getActivity().getBaseContext());


        recyclerView = rootView.findViewById(R.id.rv_mymenu);
        tvnodata = rootView.findViewById(R.id.tv_nodata);


        return rootView;
    }

    private void createRV() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        NotReviewedRVAdapter adapter = new NotReviewedRVAdapter(myReview);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("Recycle")
    public void getData() {
        //Mengambil Repository dengan Mode Membaca
        SQLiteDatabase ReadData = MyDatabase.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM " + DBSLC.Review.NamaTabelReview + " ORDER BY IdReview DESC", null);
        cursor.moveToFirst();//Memulai Cursor pada Posisi Awal
        //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);//Berpindah Posisi dari no index 0 hingga no index terakhir
            ReviewModel reviewModel = new ReviewModel(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6));
            myReview.add(reviewModel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetRV();
        getData();
        if (myReview.isEmpty()) {
            tvnodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        createRV();
    }

    void resetRV() {
        myReview.clear();
    }
}