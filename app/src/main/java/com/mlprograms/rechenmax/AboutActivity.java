package com.mlprograms.rechenmax;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

    /**
     * onPause method is called when the activity is paused.
     * It starts the background service.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startBackgroundService();
        }
    }

    /**
     * onResume method is called when the activity is resumed.
     * It stops the background service.
     */
    @Override
    protected void onResume() {
        super.onResume();
        stopBackgroundService();
    }

    /**
     * This method stops the background service.
     * It creates an intent to stop the BackgroundService and calls stopService() with that intent.
     * This method is typically called when the activity is being destroyed or when it's no longer necessary to run the background service.
     */
    private void stopBackgroundService() {
        try {
            Intent serviceIntent = new Intent(this, BackgroundService.class);
            stopService(serviceIntent);
        } catch (Exception e) {
            Log.e("stopBackgroundService", e.toString());
        }
    }

    /**
     * This method starts a background service if the necessary permission is granted.
     * It checks if the app has the required permission to post notifications.
     * If the permission is granted, it starts the BackgroundService.
     * This method is typically called when the window loses focus.
     */
    private void startBackgroundService() {
        stopBackgroundService();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(this, BackgroundService.class));
            }
        } catch (Exception e) {
            Log.e("startBackgoundService", e.toString());
        }
    }

    private void openRechenMaxUI() {
        Intent intent = new Intent(AboutActivity.this, RechenMaxUI.class);
        startActivity(intent);
    }
}
