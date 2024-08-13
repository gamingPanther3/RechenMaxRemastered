package com.mlprograms.rechenmax;

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

public class AboutActivity extends AppCompatActivity {
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.AboutUI), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.settings_back_button).setOnClickListener(v -> {
            openRechenMaxUI();
        });

        findViewById(R.id.settings_language).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/DeniseTheil"));
            startActivity(browserIntent);
        });

        findViewById(R.id.about_rate).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mlprograms.rechenmax"));
            startActivity(browserIntent);
        });

        findViewById(R.id.about_github).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gamingPanther3"));
            startActivity(browserIntent);
        });

        findViewById(R.id.about_email_address).setOnClickListener(v -> {
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

        findViewById(R.id.about_license).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gamingPanther3/RechenMax/blob/master/LICENSE"));
            startActivity(browserIntent);
        });

        findViewById(R.id.about_privacy_policy).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gist.github.com/gamingPanther3/b78ad25eb51d05a020f48efa86f660ad"));
            startActivity(browserIntent);
        });

        findViewById(R.id.about_app_version).setOnClickListener(v -> {
            // TODO: display easter egg if 12 times pressed
        });
    }

    private void openRechenMaxUI() {
        Intent intent = new Intent(AboutActivity.this, RechenMaxUI.class);
        startActivity(intent);
    }
}
