package com.mlprograms.rechenmax;

import static com.mlprograms.rechenmax.RechenMaxUI.setLocale;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

import org.json.JSONException;

public class SettingsActivity extends AppCompatActivity {
    private ClipboardManager clipboardManager;
    DataManager dataManager = new DataManager();

    private static final String PREFS_NAME = "NotificationPermissionPrefs";
    private static final String PERMISSION_GRANTED_KEY = "permission_granted";

    private boolean oldValue;
    public static Context rechenMaxUI;

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

    public static void setMainActivityContext(RechenMaxUI activity) {
        rechenMaxUI = activity;
    }

    public static Context getMainActivityContext() {
        return rechenMaxUI;
    }

    public void openChangelogInAnotherActivity(View item) {
        //ToastHelper.showToastShort(getString(R.string.actionbar_function_is_not_available), getMainActivityContext());

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
            ToastHelper.showToastLong(getString(R.string.about_no_email_client), getMainActivityContext());
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
        dataManager.updateValuesInJSONSettingsData("numberOfDecimals", "value", String.valueOf(numberOfDecimalPoints), getMainActivityContext());

        ToastHelper.showToastShort(
                getString(R.string.settings_decimal_points_set_to) + " " + numberOfDecimalPoints
                , getMainActivityContext()
        );
    }

    public void openSettingsHistoryAnimation(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("historyAnimation", getMainActivityContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }
        dataManager.updateValuesInJSONSettingsData("historyAnimation", "value", String.valueOf(!oldValue), getMainActivityContext());

        ToastHelper.showToastShort(
                getString(R.string.settings_history_animation_message) + " " +
                        (!oldValue ? getString(R.string.settings_play_history_animation) : getString(R.string.settings_do_not_play_history_animation))
                , getMainActivityContext()
        );
    }

    public void openSettingsSpeechRecognition(View view) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("speech_recognition", getMainActivityContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }
        dataManager.updateValuesInJSONSettingsData("speech_recognition", "value", String.valueOf(!oldValue), getMainActivityContext());

        ToastHelper.showToastShort(
                getString(R.string.settings_speech_recognition_message) + " " +
                        (!oldValue ? getString(R.string.settings_speech_recognition_on) : getString(R.string.settings_speech_recognition_off))
                , getMainActivityContext()
        );
    }

    public void openSettingsInsertPIConfig(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("refactorPI", getMainActivityContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }
        dataManager.updateValuesInJSONSettingsData("refactorPI", "value", String.valueOf(!oldValue), getMainActivityContext());

        ToastHelper.showToastShort(
            getString(R.string.settings_insert_pi_set_to) + " " +
                (!oldValue ? getString(R.string.settings_do_insert_pi) : getString(R.string.settings_do_not_insert_pi))
                , getMainActivityContext()
        );
    }

    public void openSettingsRememberNotificationConfig(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("allowRememberNotifications", getMainActivityContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }

        boolean isPermissionGranted = isNotificationPermissionGranted();
        if (!isPermissionGranted) {
            requestNotificationPermission();
        } else {
            dataManager.updateValuesInJSONSettingsData("allowRememberNotifications", "value", String.valueOf(!oldValue), getMainActivityContext());

            ToastHelper.showToastShort(
                    getString(R.string.settings_remember_notification_set_to) + " " +
                            (!oldValue ? getString(R.string.settings_remember_notification_send) : getString(R.string.settings_remember_notification_do_not_send))
                    , getMainActivityContext()
            );
        }
    }

    public void openSettingsDailyHintsNotificationConfig(View item) {
        try {
            oldValue = Boolean.parseBoolean(dataManager.getJSONSettingsData("allowDailyNotifications", getMainActivityContext()).getString("value"));
        } catch (JSONException e) {
            oldValue = false;
        }

        boolean isPermissionGranted = isNotificationPermissionGranted();
        if (!isPermissionGranted) {
            requestNotificationPermission();
        } else {
            dataManager.updateValuesInJSONSettingsData("allowDailyNotifications", "value", String.valueOf(!oldValue), getMainActivityContext());

            ToastHelper.showToastShort(
                    getString(R.string.settings_daily_hints_notification_set_to) + " " +
                            (!oldValue ? getString(R.string.settings_daily_hints_notification_send) : getString(R.string.settings_daily_hints_notification_do_not_send))
                    , getMainActivityContext()
            );
        }
    }

    boolean isNotificationPermissionGranted() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PERMISSION_GRANTED_KEY, false);
    }

    public void requestNotificationPermission() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isPermissionGranted = sharedPreferences.getBoolean(PERMISSION_GRANTED_KEY, false);

        if (!isPermissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
                }
            }
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            savePermissionStatus(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                dataManager.saveToJSONSettings("allowDailyNotifications", true, getApplicationContext());
                dataManager.saveToJSONSettings("allowRememberNotifications", true, getApplicationContext());
            }
        }
    }

    private void savePermissionStatus(boolean isGranted) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PERMISSION_GRANTED_KEY, isGranted);
        editor.apply();
    }

    public void openSettingsDecimalPointsConfig(View item) {
        TextView actionbarMenuTextview = findViewById(R.id.settings_decimal_points);
        PopupMenu popup = new PopupMenu(this, actionbarMenuTextview);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings_decimals_config, popup.getMenu());
        popup.show();
    }

    public void changeLanguageToGerman(MenuItem item) {
        dataManager.saveToJSONSettings("appLanguage", "de", getMainActivityContext());
        setLocale(this, "de");
    }

    public void changeLanguageToEnglish(MenuItem item) {
        ToastHelper.showToastShort("English is not available yet", getMainActivityContext());
        // TODO add language
        if(true) {
            return;
        }

        dataManager.saveToJSONSettings("appLanguage", "en", getMainActivityContext());
        setLocale(this, "en");
    }

    public void changeLanguageToSpanish(MenuItem item) {
        ToastHelper.showToastShort("Spanish is not available yet", getMainActivityContext());
        // TODO add language
        if(true) {
            return;
        }
        dataManager.saveToJSONSettings("appLanguage", "es", getMainActivityContext());
        setLocale(this, "es");
    }

    public void changeLanguageToFrench(MenuItem item) {
        ToastHelper.showToastShort("French is not available yet", getMainActivityContext());
        // TODO add language
        if(true) {
            return;
        }
        dataManager.saveToJSONSettings("appLanguage", "fr", getMainActivityContext());
        setLocale(this, "fr");
    }

    public void openRechenMaxUI(View item) {
        Intent intent = new Intent(SettingsActivity.this, RechenMaxUI.class);
        startActivity(intent);
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
}
