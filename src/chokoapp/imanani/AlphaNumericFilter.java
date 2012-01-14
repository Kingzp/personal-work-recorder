package chokoapp.imanani;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

public class AlphaNumericFilter implements InputFilter {
    private Context context;

    public AlphaNumericFilter(Context context) {
        this.context = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        if ( source.toString().matches("^[a-zA-Z0-9]*$") ) {
            return null;
        } else {
            Toast.makeText(context, context.getString(R.string.should_be_alpha_numeric),
                           Toast.LENGTH_SHORT).show();
            return  "";
        }
    }
}