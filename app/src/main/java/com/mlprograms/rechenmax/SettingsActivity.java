package com.mlprograms.rechenmax;

import static com.mlprograms.rechenmax.RechenMaxUI.setLocale;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Locale;
import java.util.Objects;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.json.JSONException;

public class SettingsActivity extends AppCompatActivity {
    private ClipboardManager clipboardManager;
    DataManager dataManager = new DataManager();

    private boolean oldValue;

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
    }

    public void openChangelogInAnotherActivity(View item) {
        //ToastHelper.showToastShort(getString(R.string.actionbar_function_is_not_available), getApplicationContext());

        Intent intent = new Intent(SettingsActivity.this, ChangelogActivity.class);
        startActivity(intent);
    }

    public void openPrivacyPolicyOnGitHubInBrowser(View item) {
        findViewById(R.id.settings_privacy_policy).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gist.github.com/gamingPanther3/b78ad25eb51d05a020f48efa86f660ad"));
            startActivity(browserIntent);
        });
    }

    public void openAppLicenseOnGitHubInBrowser(View item) {
        findViewById(R.id.settings_license).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gamingPanther3/RechenMax/blob/master/LICENSE"));
            startActivity(browserIntent);
        });
    }

    public void openEmailAppAndFoundReportError(View item) {
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
    }
    
    public void openSettingsLanguageConfig(View item) {
        TextView actionbarMenuTextview = findViewById(R.id.settings_language);
        PopupMenu popup = new PopupMenu(this, actionbarMenuTextview);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings_language_config, popup.getMenu());
        popup.show();
    }

    public void changeDecimalPointsTo(MenuItem item) {
        int numberOfDecimalPoints = Integer.parseInt(Objects.requireNonNull(item.getTitle()).toString());
        dataManager.updateValuesInJSONSettingsData("numberOfDecimals", "value", String.valueOf(numberOfDecimalPoints), getApplicationContext());

        ToastHelper.showToastShort(
                getString(R.string.settings_decimal_points_set_to) + " " + numberOfDecimalPoints
                , getApplicationContext()
        );
    }

    public void openSettingsInsertPIConfig(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("refactorPI", getApplicationContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }
        dataManager.updateValuesInJSONSettingsData("refactorPI", "value", String.valueOf(!oldValue), getApplicationContext());

        ToastHelper.showToastShort(
            getString(R.string.settings_insert_pi_set_to) + " " +
                (oldValue ? getString(R.string.settings_insert_pi_insert) : getString(R.string.settings_insert_pi_not_insert))
                , getApplicationContext()
        );
    }

    public void openSettingsRememberNotificationConfig(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("allowRememberNotifications", getApplicationContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }
        dataManager.updateValuesInJSONSettingsData("allowRememberNotifications", "value", String.valueOf(!oldValue), getApplicationContext());

        ToastHelper.showToastShort(
                getString(R.string.settings_remember_notification_set_to) + " " +
                        (oldValue ? getString(R.string.settings_remember_notification_send) : getString(R.string.settings_remember_notification_do_not_send))
                , getApplicationContext()
        );
    }

    public void openSettingsDailyHintsNotificationConfig(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("allowDailyNotifications", getApplicationContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }
        dataManager.updateValuesInJSONSettingsData("allowDailyNotifications", "value", String.valueOf(!oldValue), getApplicationContext());

        ToastHelper.showToastShort(
                getString(R.string.settings_daily_hints_notification_set_to) + " " +
                        (oldValue ? getString(R.string.settings_daily_hints_notification_send) : getString(R.string.settings_daily_hints_notification_do_not_send))
                , getApplicationContext()
        );    }

    public void openSettingsDecimalPointsConfig(View item) {
        TextView actionbarMenuTextview = findViewById(R.id.settings_decimal_points);
        PopupMenu popup = new PopupMenu(this, actionbarMenuTextview);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings_decimals_config, popup.getMenu());
        popup.show();
    }

    public void changeLanguageToGerman(MenuItem item) {
        dataManager.saveToJSONSettings("appLanguage", "de", getApplicationContext());
        setLocale(this, "de");
    }

    public void changeLanguageToEnglish(MenuItem item) {
        ToastHelper.showToastShort("English is not available yet", getApplicationContext());
        // TODO add language
        if(true) {
            return;
        }

        dataManager.saveToJSONSettings("appLanguage", "en", getApplicationContext());
        setLocale(this, "en");
    }

    public void changeLanguageToSpanish(MenuItem item) {
        ToastHelper.showToastShort("Spanish is not available yet", getApplicationContext());
        // TODO add language
        if(true) {
            return;
        }
        dataManager.saveToJSONSettings("appLanguage", "es", getApplicationContext());
        setLocale(this, "es");
    }

    public void changeLanguageToFrench(MenuItem item) {
        ToastHelper.showToastShort("French is not available yet", getApplicationContext());
        // TODO add language
        if(true) {
            return;
        }
        dataManager.saveToJSONSettings("appLanguage", "fr", getApplicationContext());
        setLocale(this, "fr");
    }

    public void openRechenMaxUI(View item) {
        Intent intent = new Intent(SettingsActivity.this, RechenMaxUI.class);
        startActivity(intent);
    }
}
