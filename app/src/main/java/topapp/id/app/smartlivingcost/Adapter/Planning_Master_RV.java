package topapp.id.app.smartlivingcost.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import topapp.id.app.smartlivingcost.Activity.Angsuran;
import topapp.id.app.smartlivingcost.Fragment.DBSLC;
import topapp.id.app.smartlivingcost.Model.PlanningModel;
import topapp.id.app.smartlivingcost.R;

public class Planning_Master_RV extends RecyclerView.Adapter<Planning_Master_RV.ViewHolder> {
    private List<PlanningModel> myPlanning;
    private Context context;
    private DBSLC MyDatabase;


    //Membuat Konstruktor pada Class RecyclerViewAdapter ,
    public Planning_Master_RV(List<PlanningModel> myPlanning) {
        super();
        this.myPlanning = myPlanning;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ImvIcon;
        ImageButton ImvCheck;
        TextView NamaPlannign, TargetMoney, TargetDate, TvCollected, MoneyPerBulan, TargetMonthCount, DateInput;

        ViewHolder(View itemView) {
            super(itemView);
            MyDatabase = new DBSLC(context);
            NamaPlannign = itemView.findViewById(R.id.name_planning);
            TargetMoney = itemView.findViewById(R.id.target_money);
            TargetDate = itemView.findViewById(R.id.target_date);
            TvCollected = itemView.findViewById(R.id.tv_collected);
            MoneyPerBulan = itemView.findViewById(R.id.tv_moneyperbulan);
            TargetMonthCount = itemView.findViewById(R.id.target_month_count);
            ImvIcon = itemView.findViewById(R.id.imv_icon);
            ImvCheck = itemView.findViewById(R.id.overflow);
            DateInput = itemView.findViewById(R.id.DateInput);
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.planning_master_rv_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final PlanningModel data = myPlanning.get(position);
        final String idplanning = data.getIdPlanning();
        final String idcategory = data.getIdCategory();
        final String namaplanning = data.getNamaPlanning();
        final String targetmoney = data.getTargetMoney();
        final String targetdate = data.getTargetDate();
        final String dateinput = data.getDateInput();
        final String namakategori = data.getNamaKategori();
        final String idiconkategori = data.getIdIconKategori();

        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date, date2;

        try {
            date = originalFormat.parse(targetdate);
            date2 = originalFormat.parse(dateinput);
            String tanggal = String.valueOf(targetFormat.format(date));
            String tanggal2 = String.valueOf(targetFormat.format(date2));
            holder.TargetDate.setText(tanggal);
            holder.DateInput.setText(tanggal2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.NamaPlannign.setText(namaplanning);
        holder.TargetMoney.setText(String.valueOf(nbFmt.format(Integer.parseInt(targetmoney))));
        holder.TargetMonthCount.setText(String.valueOf(MyDatabase.countMonth(idplanning)));
        holder.TvCollected.setText(String.valueOf(nbFmt.format(MyDatabase.countTotalCollectedPlanning(idplanning))));
        holder.MoneyPerBulan.setText(String.valueOf(nbFmt.format(MyDatabase.angsuranperbulan(idplanning))));
        if (MyDatabase.countTotalCollectedPlanning(idplanning) >= Integer.parseInt(targetmoney)) {
            holder.ImvCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ImvCheck.setVisibility(View.GONE);
        }

        TextDrawable builder;
        ColorGenerator generator;

        generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(15)
                .endConfig()
                .buildRound(printFirst(namaplanning), color1);
        holder.ImvIcon.setImageDrawable(builder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Angsuran.class);
                i.putExtra("idplanning", idplanning);
                i.putExtra("namaplanning", namaplanning);
                i.putExtra("idkategori", idcategory);
                i.putExtra("targetmoney", targetmoney);
                i.putExtra("targetdate", targetdate);
                i.putExtra("namakategori", namakategori);
                i.putExtra("idiconkategori", idiconkategori);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return myPlanning.size();
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
