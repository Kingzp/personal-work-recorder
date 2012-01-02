package chokoapp.imanani;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class DisplayDifference implements TextWatcher {
    private DailySummaryActivity act;

    public DisplayDifference(DailySummaryActivity act) {
        this.act = act;
    }

    @Override
    public void afterTextChanged(Editable e) {
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        TextView differenceTimeView = (TextView)act.findViewById(R.id.differenceTimeView);
        long difference = act.calculateTaskTotal() - act.calculateWorkTotal();
        long diffsec = difference / 1000;
        long sec = diffsec % 60;
        long min = ( diffsec / 60 ) % 60;
        long hour = diffsec / (60 * 60);
        differenceTimeView.setText(String.format("%02d:%02d:%02d", hour, min, sec));
    }
}