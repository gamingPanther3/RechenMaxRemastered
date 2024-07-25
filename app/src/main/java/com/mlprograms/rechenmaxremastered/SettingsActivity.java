package com.mlprograms.rechenmaxremastered;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    private void openRechenMaxUI() {
        Intent intent = new Intent(SettingsActivity.this, RechenMaxUI.class);
        startActivity(intent);
    }
}
