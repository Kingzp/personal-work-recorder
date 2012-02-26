package chokoapp.imanani;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

public class AppPreferenceActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference etp = (EditTextPreference)findPreference("monthly_summary_time_of_day");
        String monthlySummaryTimeOfDay = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("monthly_summary_time_of_day","");
        if (monthlySummaryTimeOfDay != "") {
            etp.setSummary(monthlySummaryTimeOfDay + getString(R.string.hours));
        }
        etp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                int inputValue;
                try {
                    inputValue = Integer.parseInt((String)newValue);
                }
                catch (NumberFormatException e) {
                    showToast(R.string.monthly_ummary_preference_time_of_day_input_msg);
                    return false;
                }
                if (inputValue > 0 && inputValue <= 24) {
                    ((EditTextPreference)preference).setSummary((String)newValue + getString(R.string.hours));
                    return true;
                } else {
                    showToast(R.string.monthly_ummary_preference_time_of_day_input_msg);
                    return false;
                }
            }
        });
    }

    private void showToast(int resId) {
        Toast toast = Toast.makeText(AppPreferenceActivity.this, resId,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
