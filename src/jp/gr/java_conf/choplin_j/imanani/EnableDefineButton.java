package jp.gr.java_conf.choplin_j.imanani;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

public class EnableDefineButton implements TextWatcher {
    private Button defineButton;

    public EnableDefineButton(Button button) {
        defineButton = button;
    }

    @Override
    public void afterTextChanged(Editable s) {
        defineButton.setEnabled(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
    }
}