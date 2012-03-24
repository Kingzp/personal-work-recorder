package jp.gr.java_conf.choplin_j.imanani;

import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.TextView;

import android.preference.Preference;

public class AboutPreference extends Preference {
    private TextView aboutView;
    private TextView versionView;

    public AboutPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AboutPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AboutPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindView(View view) {
        aboutView = (TextView)view.findViewById(R.id.aboutView);
        versionView = (TextView)view.findViewById(R.id.versionView);

        String versionString;
        try {
            PackageManager packageManager = getContext().getPackageManager();
            PackageInfo packageInfo;
            packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 
                                                        PackageManager.GET_META_DATA);
            versionString = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            versionString = "0.0";
        }

        aboutView.setText(getContext().getString(R.string.app_name));
        versionView.setText(getContext().getString(R.string.version, versionString));
    }
}