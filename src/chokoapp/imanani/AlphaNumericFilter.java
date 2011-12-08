package chokoapp.imanani;

import android.text.InputFilter;
import android.text.Spanned;

public class AlphaNumericFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        return source.toString().matches("^[a-zA-Z0-9]+$") ? source : "";
    }
}