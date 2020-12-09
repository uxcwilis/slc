package suska.uin.tif.smartlivingcost.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import suska.uin.tif.smartlivingcost.Adapter.PromoRVAdapter;
import suska.uin.tif.smartlivingcost.Data.AllPromoItem;
import suska.uin.tif.smartlivingcost.R;

public class Promo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        final Toolbar toolbar = findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        List<AllPromoItem> list = (List<AllPromoItem>) i.getSerializableExtra("data");
        RecyclerView recyclerView = findViewById(R.id.promo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Promo.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new PromoRVAdapter(list);
        recyclerView.setAdapter(adapter);

    }
}
