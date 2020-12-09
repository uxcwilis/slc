package topapp.id.app.smartlivingcost.Adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.NumberFormat;
import java.util.Locale;

import topapp.id.app.smartlivingcost.R;

public class LineCharMarker extends MarkerView {

    private TextView tvContent;

    public LineCharMarker(Context context, int layoutResource) {
        super(context, layoutResource);

        // find your layout components
        tvContent = (TextView) findViewById(R.id.tv_markerview);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat nbFmt = NumberFormat.getCurrencyInstance(localeID);

        tvContent.setText("" + nbFmt.format(e.getY()));
        if (e.getY() < 0) {
            tvContent.setTextColor(ContextCompat.getColor(getContext(), R.color.RedTheme));
        } else {
            tvContent.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}