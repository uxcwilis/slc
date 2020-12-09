package suska.uin.tif.smartlivingcost.Adapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter {

    private DecimalFormat mFormat;
    private PieChart pieChart;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0 '%'"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        String a = String.valueOf(mFormat.format(value) + " %");
        return a; // e.g. append a dollar-sign
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return mFormat.format(value);
        }
    }
}