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

        ListPreference lp = (ListPreference)findPreference("monthly_summary_time_display_list");

        lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String summary = "";
                String[] entries = getResources().getStringArray(R.array.monthly_summary_preference_time_display_entries);
                String[] values = getResources().getStringArray(R.array.monthly_summary_preference_time_display_values);
                for (int i = 0; i < values.length; ++i) {
                    if (values[i].equals(newValue)) {
                        summary = entries[i];
                        break;
                    }
                }
                ((ListPreference)preference).setSummary(summary);
                return true;
            }
        });

        EditTextPreference etp = (EditTextPreference)findPreference("monthly_summary_time_of_day");
        etp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String input = newValue.toString();
                if (input != null && Integer.parseInt(input) > 0 && Integer.parseInt(input) <= 24) {
                    ((EditTextPreference)preference).setSummary((String)newValue + getString(R.string.hour));
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
