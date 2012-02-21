package chokoapp.imanani;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class AppPreferenceActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference etp = (EditTextPreference)findPreference("monthly_summary_time_of_day");
        etp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String input = newValue.toString();
                if (input != null && Integer.parseInt(input) > 0 && Integer.parseInt(input) <= 24) {
                    ((EditTextPreference)preference).setSummary((String)newValue + getString(R.string.hours));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
