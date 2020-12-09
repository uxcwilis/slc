package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import topapp.id.app.smartlivingcost.Data.AllMyReviewItem;
import topapp.id.app.smartlivingcost.R;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class MyReviewRVAdapter extends RecyclerView.Adapter<MyReviewRVAdapter.ViewHolder> {
    private List<AllMyReviewItem> myReviewItems;

    //Membuat Konstruktor pada Class RecyclerViewAdapter
    public MyReviewRVAdapter(List<AllMyReviewItem> myreviewItems) {
        super();
        this.myReviewItems = myreviewItems;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView NamaUser, ratingMenu, commentMenu, tanggal;
        ImageView ImvAva;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            tanggal = itemView.findViewById(R.id.tanggal_review);
            commentMenu = itemView.findViewById(R.id.comment);
            NamaUser = itemView.findViewById(R.id.nama_user);
            ratingMenu = itemView.findViewById(R.id.rating);
            ImvAva = itemView.findViewById(R.id.imv_avatar);
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.myreview_rv, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final AllMyReviewItem data = myReviewItems.get(position);
        String NamaUser = data.getNama_user();
        String comment = data.getComment();
        int rating = data.getRating();
        String tanggal = data.getTanggal();

        TextDrawable builder;
        ColorGenerator generator;

        generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(15)
                .endConfig()
                .buildRound(printFirst(NamaUser), color1);
        holder.ImvAva.setImageDrawable(builder);
        holder.commentMenu.setText(comment);
        holder.NamaUser.setText(NamaUser);
        holder.ratingMenu.setText(String.valueOf(rating));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;

        try {
            date = originalFormat.parse(tanggal);
            String jadi = String.valueOf(targetFormat.format(date));
            holder.tanggal.setText(jadi);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myReviewItems.size();
    }

    private static String printFirst(String s) {
        String first = "";
        Pattern p = Pattern.compile("\\b[a-zA-Z]");
        Matcher m = p.matcher(s);

        while (m.find()) {
            first = first + m.group();
        }
        return first;

    }

}