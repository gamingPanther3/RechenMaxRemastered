package com.mlprograms.rechenmaxremastered;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class SettingsActivity extends AppCompatActivity {
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsUI), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.settings_back_button).setOnClickListener(v -> {
            openRechenMaxUI();
        });

        // TODO: language

        findViewById(R.id.settings_report_error).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data;
                data = Uri.parse("mailto:ml.programs.service@gmail.com?subject=");
                intent.setData(data);
                startActivity(intent);

            } catch (android.content.ActivityNotFoundException e) {
                ToastHelper.showToastLong(getString(R.string.about_no_email_client), getApplicationContext());
                ClipData clipData = ClipData.newPlainText("", getString(R.string.about_email_address));
                clipboardManager.setPrimaryClip(clipData);
            }
        });

        // TODO: insert pi

        // TODO: decimals

        // TODO: reminder

        // TODO: daily hints

        // TODO: release notes

        findViewById(R.id.settings_license).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gamingPanther3/RechenMax/blob/master/LICENSE"));
            startActivity(browserIntent);
        });

        findViewById(R.id.settings_privacy_policy).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gist.github.com/gamingPanther3/b78ad25eb51d05a020f48efa86f660ad"));
            startActivity(browserIntent);
        });

        findViewById(R.id.settings_app_version).setOnClickListener(v -> {
            // TODO: display easter egg if 12 times pressed
        });
    }



    // TODO improve this
    public void openSettingsLanguageConfig(MenuItem item) {
        TextView actionbarMenuTextview = findViewById(R.id.settings_language);
        PopupMenu popup = new PopupMenu(this, actionbarMenuTextview);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings_language_config, popup.getMenu());
        popup.show();


    }

    private void openRechenMaxUI() {
        Intent intent = new Intent(SettingsActivity.this, RechenMaxUI.class);
        startActivity(intent);
    }

    public void changeLanguageToGerman(MenuItem item) {
        setLocale("de");
    }

    public void changeLanguageToEnglish(MenuItem item) {
        setLocale("en");
    }

    public void changeLanguageToSpanish(MenuItem item) {
        setLocale("es");
    }

    public void changeLanguageToFrench(MenuItem item) {
        setLocale("fr");
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SettingsActivity.class);
        finish();
        startActivity(refresh);
    }
}
