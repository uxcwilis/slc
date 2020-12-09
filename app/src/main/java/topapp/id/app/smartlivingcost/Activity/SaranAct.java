package topapp.id.app.smartlivingcost.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import topapp.id.app.smartlivingcost.R;

public class SaranAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran_act);
        final Toolbar toolbar = findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi
        toolbar.setNavigationIcon(R.drawable.ic_back); // Set the icon
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.buttonHighlight));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SaranAct.this, MainActivity.class);
                i.putExtra("frgToLoad", "");
                startActivity(i);
                finish();
            }
        });
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int harian = b.getInt("harian");
        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);


        TextView tv_harian = findViewById(R.id.tv_harian);
        tv_harian.setText(String.valueOf(nbFmt.format(-1 * harian)));

        TextView btn_puasa = findViewById(R.id.btn_puasa);
        TextView btn_input = findViewById(R.id.btn_input);

        btn_puasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SaranAct.this, Puasa.class);
                i.putExtra("harian", harian);
                startActivity(i);
                finish();
            }
        });

        btn_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SaranAct.this, MainActivity.class);
                i.putExtra("frgToLoad", "Input");
                startActivity(i);
                finish();
            }
        });

    }
}
