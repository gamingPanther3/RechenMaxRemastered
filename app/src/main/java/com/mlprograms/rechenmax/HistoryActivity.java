/*
 * Copyright (c) 2024 by Max Lemberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mlprograms.rechenmax;

import static com.mlprograms.rechenmax.ToastHelper.showToastShort;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryActivity extends AppCompatActivity {

    DataManager dataManager;
    public static RechenMaxUI rechenMaxUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_ui);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.historyUI), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dataManager = new DataManager(rechenMaxUI);

        //System.out.println(dataManager.getAllDataFromHistory(rechenMaxUI.getApplicationContext()));

        createTextViews();

        // scroll to bottom
        try {
            if(Boolean.parseBoolean(dataManager.getJSONSettingsData("historyAnimation", getMainActivityContext()).getString("value"))) {
                NestedScrollView nestedScrollView = findViewById(R.id.historyUI);
                ScrollUtils.smoothScrollToBottom(nestedScrollView, 1500, 1000);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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

    public void createTextViews() {
        try {
            int count = 0;
            JSONObject tempMaxValue = dataManager.getHistoryData("historyTextViewNumber", rechenMaxUI.getApplicationContext());
            int maxValue = tempMaxValue == null ? 0 : Integer.parseInt(tempMaxValue.getString("value"));

            String tempDate = "";
            String date;
            String calculation;
            String result;

            LinearLayout history_layout = findViewById(R.id.history_layout);
            while (count <= maxValue) {
                JSONObject historyData = dataManager.getHistoryData(String.valueOf(count), rechenMaxUI.getApplicationContext());

                if (historyData != null) {
                    date = dataManager.getHistoryData(String.valueOf(count), rechenMaxUI.getApplicationContext()).getString("date");
                    calculation = dataManager.getHistoryData(String.valueOf(count), rechenMaxUI.getApplicationContext()).getString("calculation");
                    result = dataManager.getHistoryData(String.valueOf(count), rechenMaxUI.getApplicationContext()).getString("result");

                    if(!date.isEmpty() && !calculation.isEmpty() && !result.isEmpty()) {
                        if (!tempDate.equals(date)) {
                            tempDate = date;

                            history_layout.addView(createDateTextView(this, date), history_layout.getChildCount());
                        }

                        LinearLayout linearLayout = createCustomLayout(this, calculation, result);
                        linearLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                        history_layout.addView(linearLayout, history_layout.getChildCount());
                    }
                }
                count++;
            }
            history_layout.addView(createSpaceTextView(this), history_layout.getChildCount());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public TextView createSpaceTextView(Context context) {
        TextView dateTextView = new TextView(context);
        dateTextView.setId(View.generateViewId());
        dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        dateTextView.setText(" ");
        dateTextView.setTextColor(context.getResources().getColor(R.color.colorButtonClipboard));
        dateTextView.setGravity(Gravity.CENTER);
        dateTextView.setTypeface(ResourcesCompat.getFont(context, R.font.work_sans_bold));

        // Setzen der Margins
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dateTextView.getLayoutParams();
        params.setMargins(0, 0, 0, 100);
        dateTextView.setLayoutParams(params);

        return dateTextView;
    }

    public TextView createDateTextView(Context context, String dateText) {
        // TextView für das Datum
        TextView dateTextView = new TextView(context);
        dateTextView.setId(View.generateViewId());
        dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        dateTextView.setText(dateText);
        dateTextView.setTextColor(context.getResources().getColor(R.color.colorButtonClipboard));
        dateTextView.setGravity(Gravity.CENTER);
        dateTextView.setTypeface(ResourcesCompat.getFont(context, R.font.work_sans_bold));

        // Setzen der Margins
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dateTextView.getLayoutParams();
        params.setMargins(dpToPx(40, context), dpToPx(10, context), dpToPx(40, context), 0);
        dateTextView.setLayoutParams(params);

        return dateTextView;
    }

    public LinearLayout createCustomLayout(Context context, String calculationText, String resultText) {
        // Haupt-LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Erstes HorizontalScrollView mit TextView für die Berechnung
        HorizontalScrollView scrollView1 = new HorizontalScrollView(context);
        LinearLayout.LayoutParams scrollView1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView1Params.setMargins(dpToPx(20, context), 0, dpToPx(20, context), 0);
        scrollView1.setLayoutParams(scrollView1Params);
        scrollView1.setHorizontalScrollBarEnabled(false);

        TextView calculationTextView = new TextView(context);
        calculationTextView.setId(View.generateViewId());
        calculationTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Set layout_gravity to end|center_vertical for calculationTextView
        calculationTextView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        calculationTextView.setTextColor(context.getResources().getColor(R.color.colorButtonVeryHigh));
        calculationTextView.setText(calculationText);
        calculationTextView.setPadding(0, dpToPx(12, context), 0, dpToPx(6, context));
        calculationTextView.setTextSize(30);

        calculationTextView.setOnClickListener(v -> {
            copyToClipboard(calculationText);
        });

        scrollView1.addView(calculationTextView);
        linearLayout.addView(scrollView1);

        // Zweites HorizontalScrollView mit TextView für das Ergebnis
        HorizontalScrollView scrollView2 = new HorizontalScrollView(context);
        LinearLayout.LayoutParams scrollView2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView2Params.setMargins(dpToPx(20, context), 0, dpToPx(20, context), 0);
        scrollView2.setLayoutParams(scrollView2Params);
        scrollView2.setHorizontalScrollBarEnabled(false);

        TextView resultTextView = new TextView(context);
        resultTextView.setId(View.generateViewId());
        resultTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Set layout_gravity to end|center_vertical for resultTextView
        resultTextView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        resultTextView.setTextColor(context.getResources().getColor(R.color.colorButtonVeryHigh));
        resultTextView.setText(resultText);
        resultTextView.setPadding(0, 0, 0, dpToPx(12, context));
        resultTextView.setTextSize(30);
        resultTextView.setAlpha(0.6f);

        resultTextView.setOnClickListener(v -> {
            copyToClipboard(resultText);
        });

        scrollView2.addView(resultTextView);
        linearLayout.addView(scrollView2);

        return linearLayout;
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("", text);
        clipboardManager.setPrimaryClip(clipData);
        showToastShort(getString(R.string.copiedToClipboard),getApplicationContext());
    }

    private int dpToPx(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public void openRechenMaxUI(View v) {
        Intent intent = new Intent(HistoryActivity.this, RechenMaxUI.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }

    public static void setMainActivityContext(RechenMaxUI activity) {
        rechenMaxUI = activity;
    }

    public static Context getMainActivityContext() {
        return rechenMaxUI;
    }

}
