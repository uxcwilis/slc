package suska.uin.tif.smartlivingcost.Fragment.TabInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suska.uin.tif.smartlivingcost.Adapter.UserRevieweRVAdapter;
import suska.uin.tif.smartlivingcost.Data.AllUserReviewItem;
import suska.uin.tif.smartlivingcost.Data.ResponseUserReview;
import suska.uin.tif.smartlivingcost.R;
import suska.uin.tif.smartlivingcost.Rest.BaseApiService;
import suska.uin.tif.smartlivingcost.Rest.UtilsApi;
import suska.uin.tif.smartlivingcost.Util.SessionManager;


public class ReviewFragment extends Fragment {
    private SessionManager sessionManager;
    private BaseApiService mApiService;
    private RecyclerView recyclerView;
    private TextView tv_nodata;
    private List<AllUserReviewItem> allUserReview = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        mApiService = UtilsApi.getAPIService();
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));


        recyclerView = rootView.findViewById(R.id.rv_reviewed);
        tv_nodata = rootView.findViewById(R.id.tv_nodata);
        resetRV();
        getData();

        return rootView;

    }

    private void getData() {
        //    ArrayList IdReviewList, IdMenuList, NamaMenuList, TanggalList, RatingList, CommentList;
        Call<ResponseUserReview> call = mApiService.getUserReview("Bearer " + sessionManager.getToken(), Integer.parseInt(sessionManager.getId()));
        call.enqueue(new Callback<ResponseUserReview>() {
            @Override
            public void onResponse(@NonNull Call<ResponseUserReview> call, @NonNull Response<ResponseUserReview> response) {
                if (response.code() == 200) {
                    allUserReview = response.body().getGetAllUserReview();
                    createRV();
                } else if (response.code() == 401) {
                    Toast.makeText(getActivity(), "Akun Anda Bermasalah, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    tv_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "Gagal Memuat Review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseUserReview> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Masalah Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createRV() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        UserRevieweRVAdapter adapter = new UserRevieweRVAdapter(allUserReview);
        recyclerView.setAdapter(adapter);
    }

    private void resetRV() {
       allUserReview.clear();
    }

}
