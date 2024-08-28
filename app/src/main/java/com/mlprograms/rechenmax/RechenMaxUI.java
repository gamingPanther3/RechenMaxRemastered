package com.mlprograms.rechenmax;

import static com.mlprograms.rechenmax.CalculatorEngine.containsAnyVariable;
import static com.mlprograms.rechenmax.CalculatorEngine.fixExpression;
import static com.mlprograms.rechenmax.CalculatorEngine.getVariables;
import static com.mlprograms.rechenmax.CalculatorEngine.isFixExpression;
import static com.mlprograms.rechenmax.CalculatorEngine.isNumber;
import static com.mlprograms.rechenmax.CalculatorEngine.isOperator;
import static com.mlprograms.rechenmax.NumberHelper.PI;
import static com.mlprograms.rechenmax.NumberHelper.e;
import static com.mlprograms.rechenmax.ParenthesesBalancer.balanceParentheses;
import static com.mlprograms.rechenmax.ToastHelper.showToastLong;
import static com.mlprograms.rechenmax.ToastHelper.showToastShort;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

// force push to RechenMax Repo:
// git push --force --set-upstream https://github.com/gamingPanther3/RechenMax master
// git push --force --set-upstream https://github.com/gamingPanther3/RechenMaxRemastered master

public class RechenMaxUI extends AppCompatActivity {

    private boolean isFormatting;

    public Context rechenMaxUI = this;
    private DataManager dataManager;

    private final LinearLayout[] scientificLayouts = new LinearLayout[10];
    private boolean isExpanded;

    private TextView textView;
    private int characterCount = 0;
    private final StringBuilder textBuilder = new StringBuilder();

    private ClipboardManager clipboardManager;
    private EditText calculation_edittext;

    private HorizontalScrollView calculateScrollView;
    private HorizontalScrollView resultScrollView;
    private TextView resultTextview;

    private final String hexColorErrorMessageRed = "#F05F5D";
    private final String hexColorDefaultTextWhite = "#FFFFFF";

    private final String[] variableKeysCharacter = {"A", "B", "C", "D", "E", "F", "G", "X", "Y", "Z"};

    private final String[] variableKeys = {"variable_a", "variable_b", "variable_c", "variable_d", "variable_e",
            "variable_f", "variable_g", "variable_x", "variable_y", "variable_z"};

    private final int[] textViewIds = {R.id.variable_a_textview, R.id.variable_b_textview, R.id.variable_c_textview,
            R.id.variable_d_textview, R.id.variable_e_textview, R.id.variable_f_textview,
            R.id.variable_g_textview, R.id.variable_x_textview, R.id.variable_y_textview,
            R.id.variable_z_textview};

    private static final String[] operatorsAdjustCursor = {
            "³√(", "ln(", "tanh⁻¹(", "cosh⁻¹(", "sinh⁻¹(", "tan⁻¹(", "cos⁻¹(", "sin⁻¹(",
            "tanh(", "cosh(", "sinh(", "tan(", "cos(", "sin(", "√(", "Pol(", "Rec(",
            "RanInt(", "Ran#", ".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9", "!",
            "%", "×", "÷", "+", "-", "½", "⅓", "¼", "⅕", "⅒", "%×", "%*", "⁒", "π", "е", "E",
            "log₀(", "log₁(", "log₂(", "log₃(", "log₄(", "log₅(", "log₆(", "log₇(", "log₈(", "log₉("
    };

    private static final String[] operatorsFormatCalculationText = {
            "³√(", "ln(", "tanh⁻¹(", "cosh⁻¹(", "sinh⁻¹(", "tan⁻¹(", "cos⁻¹(", "sin⁻¹(",
            "tanh(", "cosh(", "sinh(", "tan(", "cos(", "sin(", "√(", "Pol(", "Rec(",
            "RanInt(", "Ran#", "!", "(", ")",
            "log₀(", "log₁(", "log₂(", "log₃(", "log₄(", "log₅(", "log₆(", "log₇(", "log₈(", "log₉(",
            "%", "×", "÷", "+", "-", "½", "⅓", "¼", "⅕", "⅒", "%×", "%*", "⁒", "π", "е", "E"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator_ui);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RechenMaxUI), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calculateScrollView = findViewById(R.id.calculation_horizontal_scroll_view);
        calculation_edittext = findViewById(R.id.calculation_edittext);
        resultScrollView = findViewById(R.id.result_horizontal_scroll_view);
        resultTextview = findViewById(R.id.result_textview);

        calculation_edittext.setShowSoftInputOnFocus(false);

        // Initialize DataManager ...
        dataManager = new DataManager(this);
        dataManager.initializeSettings(getApplicationContext());
        CalculatorEngine.setRechenMaxUI(this);

        //Log.e("DEBUG", String.valueOf(dataManager.getAllData(getApplicationContext())));

        try {
            if (dataManager.getJSONSettingsData("maxNumbersWithoutScrolling", getApplicationContext()).getString("value").isEmpty()) {
                findMaxCharactersWithoutScrolling();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        CalculatorEngine.setRechenMaxUI(this);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // TODO: inAppUpdate = new InAppUpdate(this);

        try {
            isExpanded = dataManager.getJSONSettingsData("showScienceRow", getApplicationContext()).getBoolean("value");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        setupButtonListeners();
        calculateScrollView.setHorizontalScrollBarEnabled(false);
        resultScrollView.setVerticalScrollBarEnabled(false);

        calculation_edittext.findFocus();
        calculation_edittext.setSelection(calculation_edittext.getText().length());

        try {
            String scientificMode = dataManager.getJSONSettingsData("functionMode", getApplicationContext()).getString("value");

            TextView actionbarScienticModeTextview = findViewById(R.id.actionbar_scientic_mode_textview);
            actionbarScienticModeTextview.setText(scientificMode);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            JSONObject resultText = dataManager.getJSONSettingsData("result_text", rechenMaxUI.getApplicationContext());
            JSONObject calculateText = dataManager.getJSONSettingsData("calculate_text", rechenMaxUI.getApplicationContext());

            setResultText(resultText.getString("value"));
            setCalculateText(calculateText.getString("value"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if(dataManager.getJSONSettingsData("startApp", getApplicationContext()).getString("value").equals("true")) {
                String savedLocale = dataManager.getJSONSettingsData("appLanguage", getApplicationContext()).getString("value");
                if(!getLocale().equals(savedLocale)) {
                    setLocale(this, savedLocale);
                }
            }
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }

        formatCalculationText();
    }

    public Context getApplicationContext() {
        return rechenMaxUI;
    }

    public void loadHistoryFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Lade das neue Fragment mit Animationen
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        transaction.replace(R.id.RechenMaxUI, new HistoryFragment());
        transaction.addToBackStack(null); // Optional, um die Rückkehr zum vorherigen Fragment zu ermöglichen
        transaction.commit();
    }

    public void loadHistoryActivity() {
        Intent intent = new Intent(RechenMaxUI.this, HistoryActivity.class);
        startActivity(intent);
        // Setze die Animationen für den Wechsel von MainActivity zu HistoryActivity
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startBackgroundService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopBackgroundService();
        formatCalculationText();
    }

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

    public static void setLocale(Activity activtiy, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activtiy.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        activtiy.finish();
        activtiy.startActivity(activtiy.getIntent());
    }

    public String getLocale() {
        return getResources().getConfiguration().locale.toString();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupButtonListeners() {
        // DropDown Button
        if(findViewById(R.id.dropdown_button_textview) != null) {
            scientificLayouts[0] = findViewById(R.id.button_row_layout_scientific2);
            scientificLayouts[1] = findViewById(R.id.button_row_layout_scientific3);
            scientificLayouts[2] = findViewById(R.id.button_row_layout_scientific4);
            scientificLayouts[3] = findViewById(R.id.button_row_layout_scientific5);
            scientificLayouts[4] = findViewById(R.id.button_row_layout_scientific6);
            scientificLayouts[5] = findViewById(R.id.button_row_layout_scientific7);
            scientificLayouts[6] = findViewById(R.id.button_row_layout_scientific8);

            setActionButtonListener(R.id.dropdown_button_textview, this::toggleScientificLayouts);
            toggleScientificLayouts();
        }

        calculation_edittext.setOnClickListener(v -> adjustCursorPosition(calculation_edittext));

        calculation_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(getCalculateText().isEmpty()) {
                    setResultText("");
                    return;
                }

                if (!isFormatting) {
                    isFormatting = true;
                    formatCalculationText();
                    isFormatting = false;
                }
                adjustCursorPosition(calculation_edittext);
                calculateIfIsNotInvalidCalculation();
            }
        });

        setActionButtonListener(R.id.actionbar_scientic_mode_textview, this::changeScientificMode);
        setActionButtonListener(R.id.actionbar_history_textview, this::openHistory);
        setActionButtonListener(R.id.actionbar_menu_textview, this::openMenu);

        setActionButtonListener(R.id.clear_clipboard_textview, this::clearClipboard);
        setActionButtonListener(R.id.paste_clipboard_textview, this::pasteClipboard);
        setActionButtonListener(R.id.copy_to_clipboard_textview, this::copyToClipboard);

        setActionButtonListener(R.id.square_root_textview, () -> addStringToCalculation(getString(R.string.square_root_)));
        setActionButtonListener(R.id.factorial_textview, () -> addStringToCalculation(getString(R.string.factorial_)));
        setActionButtonListener(R.id.power_textview, () -> addStringToCalculation(getString(R.string.power_)));
        setActionButtonListener(R.id.percentage_textview, () -> addStringToCalculation(getString(R.string.percentage_)));
        // setActionButtonListener(R.id.dropdown_button, () -> (getString(R.dropdown_button_.);
        setActionButtonListener(R.id.qubic_root_textview, () -> addStringToCalculation(getString(R.string.qubic_root_)));
        setActionButtonListener(R.id.log_textview, () -> addStringToCalculation(getString(R.string.log_)));
        setActionButtonListener(R.id.semicolon_textview, () -> addStringToCalculation(getString(R.string.semicolon_)));
        setActionButtonListener(R.id.eulers_number_textview, () -> addStringToCalculation(getString(R.string.eulers_number_)));
        setActionButtonListener(R.id.pi_textview, () -> addStringToCalculation(getString(R.string.pi_)));
        setActionButtonListener(R.id.sine_textview, () -> addStringToCalculation(getString(R.string.sine_)));
        setActionButtonListener(R.id.cosine_textview, () -> addStringToCalculation(getString(R.string.cosine_)));
        setActionButtonListener(R.id.tangent_textview, () -> addStringToCalculation(getString(R.string.tangent_)));
        setActionButtonListener(R.id.permutation_textview, () -> addStringToCalculation(getString(R.string.permutation_)));
        setActionButtonListener(R.id.combination_textview, () -> addStringToCalculation(getString(R.string.combination_)));
        setActionButtonListener(R.id.a_sine_textview, () -> addStringToCalculation(getString(R.string.a_sine_)));
        setActionButtonListener(R.id.a_cosine_textview, () -> addStringToCalculation(getString(R.string.a_cosine_)));
        setActionButtonListener(R.id.a_tangent_textview, () -> addStringToCalculation(getString(R.string.a_tangent_)));
        setActionButtonListener(R.id.rectangular_to_polar_textview, () -> addStringToCalculation(getString(R.string.rectangular_to_polar_)));
        setActionButtonListener(R.id.polar_to_rectangular_textview, () -> addStringToCalculation(getString(R.string.polar_to_rectangular_)));
        setActionButtonListener(R.id.sine_h_textview, () -> addStringToCalculation(getString(R.string.sine_h_)));
        setActionButtonListener(R.id.cosine_h_textview, () -> addStringToCalculation(getString(R.string.cosine_h_)));
        setActionButtonListener(R.id.tangent_h_textview, () -> addStringToCalculation(getString(R.string.tangent_h_)));
        setActionButtonListener(R.id.log2_textview, () -> addStringToCalculation(getString(R.string.log2_)));
        setActionButtonListener(R.id.ln_textview, () -> addStringToCalculation(getString(R.string.ln_)));
        setActionButtonListener(R.id.a_sine_h_textview, () -> addStringToCalculation(getString(R.string.a_sine_h_)));
        setActionButtonListener(R.id.a_cosine_h_textview, () -> addStringToCalculation(getString(R.string.a_cosine_h_)));
        setActionButtonListener(R.id.a_tangent_h_textview, () -> addStringToCalculation(getString(R.string.a_tangent_h_)));
        setActionButtonListener(R.id.random_integer_textview, () -> addStringToCalculation(getString(R.string.random_integer_)));
        setActionButtonListener(R.id.random_number_textview, () -> addStringToCalculation(getString(R.string.random_number_)));
        //setActionButtonListener(R.id.logx_textview, () -> addStringToCalculation(getString(R.string.logx_)));
        setActionButtonListener(R.id.half_textview, () -> addStringToCalculation(getString(R.string.half_)));
        setActionButtonListener(R.id.third_textview, () -> addStringToCalculation(getString(R.string.third_)));
        setActionButtonListener(R.id.quarter_textview, () -> addStringToCalculation(getString(R.string.quarter_)));
        setActionButtonListener(R.id.fifth_textview, () -> addStringToCalculation(getString(R.string.fifth_)));
        setActionButtonListener(R.id.tenth_textview, () -> addStringToCalculation(getString(R.string.tenth_)));

        setActionButtonListener(R.id.one_textview, () -> addStringToCalculation(getString(R.string.one)));
        setActionButtonListener(R.id.two_textview, () -> addStringToCalculation(getString(R.string.two)));
        setActionButtonListener(R.id.three_textview, () -> addStringToCalculation(getString(R.string.three)));
        setActionButtonListener(R.id.four_textview, () -> addStringToCalculation(getString(R.string.four)));
        setActionButtonListener(R.id.five_textview, () -> addStringToCalculation(getString(R.string.five)));
        setActionButtonListener(R.id.six_textview, () -> addStringToCalculation(getString(R.string.six)));
        setActionButtonListener(R.id.seven_textview, () -> addStringToCalculation(getString(R.string.seven)));
        setActionButtonListener(R.id.eight_textview, () -> addStringToCalculation(getString(R.string.eight)));
        setActionButtonListener(R.id.nine_textview, () -> addStringToCalculation(getString(R.string.nine)));
        setActionButtonListener(R.id.zero_textview, () -> addStringToCalculation(getString(R.string.zero)));

        setActionButtonListener(R.id.variable_a_textview, () -> addStringToCalculation(getString(R.string.variable_a)));
        setActionButtonListener(R.id.variable_b_textview, () -> addStringToCalculation(getString(R.string.variable_b)));
        setActionButtonListener(R.id.variable_c_textview, () -> addStringToCalculation(getString(R.string.variable_c)));
        setActionButtonListener(R.id.variable_d_textview, () -> addStringToCalculation(getString(R.string.variable_d)));
        setActionButtonListener(R.id.variable_e_textview, () -> addStringToCalculation(getString(R.string.variable_e)));
        setActionButtonListener(R.id.variable_f_textview, () -> addStringToCalculation(getString(R.string.variable_f)));
        setActionButtonListener(R.id.variable_g_textview, () -> addStringToCalculation(getString(R.string.variable_g)));
        setActionButtonListener(R.id.variable_x_textview, () -> addStringToCalculation(getString(R.string.variable_x)));
        setActionButtonListener(R.id.variable_y_textview, () -> addStringToCalculation(getString(R.string.variable_y)));
        setActionButtonListener(R.id.variable_z_textview, () -> addStringToCalculation(getString(R.string.variable_z)));

        for (int i = 0; i < variableKeys.length; i++) {
            int finalI = i;
            findViewById(textViewIds[i]).setOnClickListener(new ClickListener() {
                @Override
                public void onTripleClick(View v) {
                    dataManager.saveToJSONSettings(variableKeys[finalI], "", getApplicationContext());
                    updateTooltipText(finalI);

                    showToastShort(getString(R.string.variable_is_now_undefined), getApplicationContext());
                }
                @Override
                public void onDoubleClick(View v) {
                    if(getCalculateText().contains(variableKeysCharacter[finalI])) {
                        showToastShort(getString(R.string.variable_cannot_contain_itself), getApplicationContext());
                        return;
                    }

                    if(!getCalculateText().isEmpty()) {
                        dataManager.saveToJSONSettings(variableKeys[finalI], getCalculateText(), getApplicationContext());
                        updateTooltipText(finalI);

                        showToastShort(getString(R.string.variable_is_now_defined) + getCalculateText() + "')", getApplicationContext());
                    }
                }
                @Override
                public void onSingleClick(View v) {
                    try {
                        String value = dataManager.getJSONSettingsData(variableKeys[finalI], getApplicationContext()).getString("value");
                        if(value.isEmpty()) {
                            showToastShort(getString(R.string.variable_is_not_defined), getApplicationContext());
                            return;
                        }

                        addStringToCalculation(variableKeysCharacter[finalI]);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            updateTooltipText(i);
        }

        setActionButtonListener(R.id.clear_all_textview, this::clearCalculation);
        setActionButtonListener(R.id.add_parentheses_textview, this::addParentheses);

        setActionButtonListener(R.id.backspace_textview, this::deleteCharacter);
        setActionButtonListener(R.id.divide_textview, () -> addStringToCalculation(getString(R.string.divide_)));
        setActionButtonListener(R.id.multiply_textview, () -> addStringToCalculation(getString(R.string.multiply_)));
        setActionButtonListener(R.id.subtract_textview, () -> addStringToCalculation(getString(R.string.subtract_)));
        setActionButtonListener(R.id.add_textview, () -> addStringToCalculation(getString(R.string.add_)));

        setActionButtonListener(R.id.comma_textview, () -> addStringToCalculation(getString(R.string.comma_)));
        setActionButtonListener(R.id.negate_textview, this::negate);
        setCalculateButtonListener(R.id.calculate_textview, this::calculate);
    }

    private void updateTooltipText(int index) {
        try {
            String value = dataManager.getJSONSettingsData(variableKeys[index], getApplicationContext()).getString("value");
            String tooltipText = value.isEmpty() ? getString(R.string.variable_is_not_defined) : value;
            findViewById(textViewIds[index]).setTooltipText(tooltipText);
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void openMenu() {
        TextView actionbarMenuTextview = findViewById(R.id.actionbar_menu_textview);
        PopupMenu popup = new PopupMenu(this, actionbarMenuTextview);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.calculator_ui_app_menu, popup.getMenu());
        popup.show();
    }

    private void openHistory() {

        HistoryActivity.rechenMaxUI = this;
        Intent intent = new Intent(RechenMaxUI.this, HistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

/*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.RechenMaxUI, new NewFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }

    public void openAbout(MenuItem item) {
        Intent intent = new Intent(RechenMaxUI.this, AboutActivity.class);
        startActivity(intent);
    }

    public void openHelp(MenuItem item) {
        // TODO: open help activity
        ToastHelper.showToastShort(getString(R.string.actionbar_function_is_not_available), getApplicationContext());
    }

    public void openSettings(MenuItem item) {
        SettingsActivity.setMainActivityContext(this);
        Intent intent = new Intent(RechenMaxUI.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void clearHistory(MenuItem item) {
        try {
            dataManager.clearHistory(this);
            dataManager.saveToHistory("historyTextViewNumber", "0", getApplicationContext());
            ToastHelper.showToastShort(getString(R.string.cleared_history), getApplicationContext());
        } catch (Exception e) {
            ToastHelper.showToastShort(getString(R.string.cleared_history_error), getApplicationContext());
        }
    }

    public void openConverter(MenuItem item) {
        ConvertActivity.setMainActivityContext(this);
        Intent intent = new Intent(RechenMaxUI.this, ConvertActivity.class);
        startActivity(intent);
    }

    // do not touch it
    private void adjustCursorPosition(EditText editText) {
        int cursorPosition = editText.getSelectionStart();
        String text = editText.getText().toString();

        for (String operator : operatorsAdjustCursor) {
            int index = text.indexOf(operator);
            while (index != -1) {
                int start = index;
                int end = index + operator.length();

                if (cursorPosition > start && cursorPosition < end) {
                    // Move the cursor to the left
                    editText.setSelection(start);
                    return; // Cursor adjusted, no need to check further
                }

                // Continue searching for the next occurrence of the operator
                index = text.indexOf(operator, index + 1);
            }
        }
    }

    private void addStringToCalculation(String s) {
        EditText editText = findViewById(R.id.calculation_edittext);

        if(!s.equals(",") && !s.equals(";")) {
            try {
                String[] variables = {"A", "B", "C", "D", "E", "F", "G", "X", "Y", "Z"};
                if(Arrays.toString(variables).contains(s)) {
                    if(dataManager.getJSONSettingsData("variable_" + s.toLowerCase(), getApplicationContext()).getString("value").isEmpty()) {
                        ToastHelper.showToastLong(getString(R.string.variable_is_not_defined), getApplicationContext());
                        return;
                    }
                }
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(isErrorMessage(getCalculateText())) {
            setCalculateText("");
        }

        try {
            // do not touch it too
            if(s.equals("4") && Boolean.parseBoolean(dataManager.getJSONSettingsData("refactorPI", getApplicationContext()).getString("value"))) {
                String beforePI = getCalculateText().substring(0, Math.max(editText.getSelectionStart() - 3, 0));
                String afterPI = getCalculateText().substring(editText.getSelectionStart());

                if(getCalculateText().length() == 3 && getCalculateText().startsWith("3,1", editText.getSelectionStart() - 3)) { // 3,1    -> "4" wird angefügt
                    setCalculateText(getString(R.string.pi));
                    editText.setSelection(editText.length());
                } else if(getCalculateText().length() > 3 && editText.getSelectionStart() >= 3) {
                    //Log.e("DEBUG", "debug:" + getCalculateText().charAt(editText.getSelectionStart() - 3));

                    if(getCalculateText().startsWith("3,1", editText.getSelectionStart() - 3) && !isNumber(String.valueOf(getCalculateText().charAt(editText.getSelectionStart() - 4)))) {
                        int num = editText.getSelectionStart();
                        setCalculateText(beforePI + getString(R.string.pi) + afterPI);
                        editText.setSelection(Math.min(editText.length(), num + 3));
                    } else {
                        addCalculateText(s);
                    }
                } else {
                    addCalculateText(s);
                }

                // setCalculateText(beforePI + getString(R.string.pi) + afterPI);
                //Log.e("DEBUG", beforePI + getString(R.string.pi) + afterPI);
                formatCalculationText();
            } else {
                isFormatting = true;
                calculateIfIsNotInvalidCalculation();

                if(Character.isDigit(s.charAt(0)) && getCalculateText().endsWith(",")) {
                    formatCalculationText();
                    //addCalculateText(",");
                    addCalculateText(s);
                } else if(s.equals(",")) {
                    addCalculateText(",");
                } else {
                    addCalculateText(s);
                    formatCalculationText();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int count = Integer.parseInt(String.valueOf(fixExpressionWithCount(getCalculateText()).charAt(0)));
        if(count != 0) {
            setCalculateText(fixExpression(getCalculateText()));
            editText.setSelection(editText.getSelectionStart() + count);
        }

        setTextColorAccordingToCalculation();
        resetCalculatePressed();

        if(!calculation_edittext.isFocused()) {
            scrollToEnd(findViewById(R.id.calculation_horizontal_scroll_view));
        } else {
            scrollToCursor(
                    findViewById(R.id.calculation_horizontal_scroll_view),
                    (calculation_edittext.isFocused() ? calculation_edittext.getSelectionStart() : calculation_edittext.getText().length())
            );
        }

        dataManager.saveNumbers(getApplicationContext());

        if(!editText.isFocused()) {
            editText.setSelection(editText.getText().length());
        }
    }

    public static String fixExpressionWithCount(String input) {
        //Log.i("fixExpression", "Input fixExpression: " + input);

        int appendedMultiply = 0;

        // Step 1: Fix the expression using the original logic
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String currentChar = String.valueOf(input.charAt(i));
            String nextChar = "";

            if (i + 1 < input.length()) {
                nextChar = String.valueOf(input.charAt(i + 1));
            }

            stringBuilder.append(currentChar);
            //Log.e("fixExpression", "CurrentChar: " + currentChar + " NextChar: " + nextChar);
            //Log.e("fixExpression", "stringBuilder: " + stringBuilder);

            if (!nextChar.isEmpty() && isFixExpression(currentChar, nextChar)) {
                appendedMultiply++;
                stringBuilder.append('×');
            }
        }

        // Step 2: Handle the specific case of "-+"
        String fixedExpression = stringBuilder.toString();
        fixedExpression = fixedExpression.replaceAll("-\\+", "-");

        // Log.e("fixExpression", "Fixed Expression: " + fixedExpression);
        return stringBuilder.toString().isEmpty() ? appendedMultiply + input : appendedMultiply + fixedExpression;
    }

    private void setTextColorAccordingToCalculation() {
        if(!getResultText().isEmpty() && (isErrorMessage(getResultText()) || isErrorMessage(getCalculateText()))) {
            calculation_edittext.setTextColor(Color.parseColor(hexColorErrorMessageRed));
            resultTextview.setTextColor(Color.parseColor(hexColorErrorMessageRed));

            resultTextview.setAlpha(0.8f);
        } else {
            calculation_edittext.setTextColor(Color.parseColor(hexColorDefaultTextWhite));
            resultTextview.setTextColor(Color.parseColor(hexColorDefaultTextWhite));

            resultTextview.setAlpha(0.6f);
        }
    }

    private void clearCalculation() {
        setResultText("");
        setCalculateText("");
    }

    private void addParentheses() {
        EditText input = findViewById(R.id.calculation_edittext);
        int cursorPosition = input.getSelectionStart();
        int textLength = input.getText().length();

        int openParentheses = 0;
        int closeParentheses = 0;

        String text = input.getText().toString();

        for (int i = 0; i < cursorPosition; i++) {
            if (text.charAt(i) == '(') {
                openParentheses++;
            }
            if (text.charAt(i) == ')') {
                closeParentheses++;
            }
        }

        if (!(textLength > cursorPosition && "×÷+-^".indexOf(text.charAt(cursorPosition)) != -1)
                && (openParentheses == closeParentheses
                || text.charAt(cursorPosition - 1) == '('
                || "×÷+-^".indexOf(text.charAt(cursorPosition - 1)) != -1)) {
            updateDisplay("(");
        } else {
            updateDisplay(")");
        }
    }

    private void updateDisplay(String value) {
        EditText input = findViewById(R.id.calculation_edittext);
        int cursorPosition = input.getSelectionStart();
        String formerValue = input.getText().toString();

        String leftValue = formerValue.substring(0, cursorPosition);
        String rightValue = formerValue.substring(cursorPosition);

        String newValue = leftValue + value + rightValue;

        input.setText(newValue);
        input.setSelection(cursorPosition + value.length());
    }

    private void deleteCharacter() {
        EditText editText = findViewById(R.id.calculation_edittext);
        Editable editable = editText.getText();
        int cursorPosition = editText.getSelectionStart();

        if(editText.getText().toString().isEmpty()) {
            return;
        }

        if(getCalculateText().length() <= 1) {
            setCalculateText("");
            return;
        }

        if (!editText.isFocused()) {
            editText.setSelection(editText.getText().length());
            scrollToEnd(findViewById(R.id.calculation_horizontal_scroll_view));
        }

        if(cursorPosition > 0) {
            int deleteFrom = cursorPosition - 1;

            if (editable.charAt(deleteFrom) != '(' && editable.charAt(deleteFrom) != '#') {
                editable.delete(deleteFrom, deleteFrom + 1);
                editText.setSelection(Math.min(editText.getText().length(), deleteFrom));

            } else {
                while (deleteFrom >= 0) {
                    char charToDelete = editText.getText().charAt(deleteFrom);

                    if (isOperator(String.valueOf(charToDelete)) || isOperator(String.valueOf(charToDelete))) {
                        break;
                    }

                    if (Character.isLowerCase(charToDelete) || Character.isUpperCase(charToDelete) || charToDelete == '(' || charToDelete == '⁻' || charToDelete == '¹' || charToDelete == '₂' || charToDelete == '#') {
                        editText.setText(editable.delete(deleteFrom, deleteFrom + 1));
                        editText.setSelection(deleteFrom);
                    }
                    deleteFrom--;

                    if (deleteFrom - 1 > 0) {
                        if(editable.charAt(deleteFrom) == '(' || editable.charAt(deleteFrom) == '#') {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void negate() {
        String calculateText = fixExpression(getCalculateText())
                .replace(" ", "")
                .replace("½", "0,5")
                .replace("⅓", "0,33333333333")
                .replace("¼", "0,25")
                .replace("⅕", "0,2")
                .replace("⅒", "0,1")
                .replace("π", PI)
                .replace("е", e)
        ;

        if(isErrorMessage(calculateText) && !calculateText.isEmpty()) {
            return;
        }

        EditText editText = findViewById(R.id.calculation_edittext);
        if(!editText.isFocused()) {
            editText.setSelection(editText.getText().length());
        }

        int cursorPosition = editText.getSelectionStart() - 1;

        ArrayList<Integer> numberPositions = new ArrayList<>();
        for(int n = 0; n < calculateText.length(); n++) {
            if(Character.isDigit(calculateText.charAt(n)) || calculateText.charAt(n) == '.' || calculateText.charAt(n) == ',') {
                numberPositions.add(n);
            }
        }

        if(numberPositions.contains(cursorPosition)) {
            int numberStart = cursorPosition;
            int numberEnd = cursorPosition;

            for(int n = cursorPosition; n < calculateText.length(); n++) {
                if(numberPositions.contains(n)) {
                    numberEnd = n + 1;
                } else {
                    break;
                }
            }

            for(int m = cursorPosition; m > -1; m--) {
                if(numberPositions.contains(m)) {
                    numberStart = m;
                } else {
                    break;
                }
            }

            //System.out.println("numberStart: " + numberStart);
            //System.out.println("numberEnd: " + numberEnd);
            //System.out.println("number: " + calculateText.substring(numberStart, numberEnd));
            //System.out.println("cursorPosition: " + cursorPosition);

            String number = calculateText.substring(numberStart, numberEnd);

            if (numberStart - 2 >= 0 && calculateText.charAt(numberStart - 2) == '(' && calculateText.charAt(numberStart - 1) == '-' && numberEnd < calculateText.length() && calculateText.charAt(numberEnd) == ')') {
                calculateText = calculateText.substring(0, numberStart - 2) + number + calculateText.substring(numberEnd + 1);
                cursorPosition -= 2; // Korrigiere die Cursorposition
            } else {
                calculateText = new StringBuilder(calculateText).insert(numberEnd, ")").insert(numberStart, "(-").toString();
                cursorPosition += 2; // Korrigiere die Cursorposition
            }

            setCalculateText(calculateText);
            formatCalculationText();
            editText.setSelection(Math.min(cursorPosition + 1, editText.length()));
        }

        Log.e("DEBUG", "numberPositions: " + numberPositions);
    }

    /**
     * This method is responsible for toggling between two function modes, namely "Deg" (Degrees)
     * and "Rad" (Radians). It retrieves the current function mode from the application's stored data
     * using a DataManager and then switches it to the opposite mode. After updating the mode, it
     * updates the displayed text in a TextView with the new mode. Additionally, it logs the change
     * using Android's Log class for debugging purposes.
     * <p>
     * Note: The function mode is stored and retrieved from persistent storage to ensure that the
     * selected mode persists across application sessions.
     */
    private void changeScientificMode() {
        try {
            // Get reference to the TextView for displaying function mode
            final TextView function_mode_text = findViewById(R.id.actionbar_scientic_mode_textview);

            // Read the current function mode from the stored data
            final String mode;
            mode = dataManager.getJSONSettingsData("functionMode", getApplicationContext()).getString("value");

            // Toggle between "Deg" and "Rad" modes
            switch (mode) {
                case "Deg":
                    dataManager.saveToJSONSettings("functionMode", "Rad", getApplicationContext());
                    break;
                case "Rad":
                    dataManager.saveToJSONSettings("functionMode", "Deg", getApplicationContext());
                    break;
            }

            if(!getCalculateText().isEmpty()) {
                setResultText(CalculatorEngine.calculate(getCalculateText()));
            }
            function_mode_text.setText(dataManager.getJSONSettingsData("functionMode", getApplicationContext()).getString("value"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculate() {
        if(getCalculateText().isEmpty()) {
            return;
        }

        String calculation = CalculatorEngine.calculate(getCalculateText());
        addToHistory(fixExpression(balanceParentheses(getCalculateText())), calculation);

        if(!getCalculateText().contains("Ran")) {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setCalculateText(calculation);
            } else {
                if(!isErrorMessage(calculation)) {
                    setCalculateText(calculation);
                    setResultText("");
                } else {
                    setResultText(calculation);
                }
            }
        } else {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setResultText(calculation);
            } else {
                setCalculateText(calculation);
            }
        }
        resultTextview.setAlpha(0.6f);

        formatCalculationText();
        setTextColorAccordingToCalculation();
        calculation_edittext.findFocus();
        calculation_edittext.setSelection(getCalculateText().length());
    }

    private void addToHistory(String calculation, String result) {
        if(isErrorMessage(calculation) || isErrorMessage(result)) {
            return;
        }

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());

        // Code snippet to save calculation to history
        final Context context = getApplicationContext();

        new Thread(() -> runOnUiThread(() -> {
            try {
                int old_value = Integer.parseInt(dataManager.getHistoryData("historyTextViewNumber", context).getString("value"));
                int new_value = old_value + 1;

                dataManager.updateValuesInHistoryData("historyTextViewNumber", "value", Integer.toString(new_value), context);

                String calculate_text = calculation;
                if (calculate_text.isEmpty()) {
                    calculate_text = "0";
                }
                if(calculate_text.contains("Rec") || calculate_text.contains("Pol")) {
                    calculate_text = calculate_text.replace("=", ": ");
                }

                dataManager.saveToHistory(String.valueOf(old_value + 1), formattedDate, "",
                        balanceParentheses(fixExpression(calculate_text)), (result.contains("=") ? result : formatNumber(result)), context);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        })).start();
    }

    private void calculateIfIsNotInvalidCalculation() {
        String calculation = getCalculateText();
        while (containsAnyVariable(calculation, "ABCDEFGWXYZ")) {
            calculation = getVariables(fixExpression(calculation));
        }

        if(containsNumber(calculation) || calculation.contains("е") || calculation.contains("π")) {
            if(!isErrorMessage(String.valueOf(CalculatorEngine.calculate(getCalculateText())))) {
                setResultText(CalculatorEngine.calculate(getCalculateText()));
            } else {
                setResultText("");
            }
        }
    }

    public static boolean containsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }return false;
    }

    private void clearClipboard() {
        ClipData clipData = ClipData.newPlainText("", "");
        clipboardManager.setPrimaryClip(clipData);
        showToastLong(getString(R.string.clipboardCleared), getApplicationContext());
    }

    private void pasteClipboard() {
        final String calculateText = getCalculateText();
        ClipData clipData = clipboardManager.getPrimaryClip();

        if (clipData == null || clipData.getItemCount() == 0) {
            // Handle the case where clipboard data is null or empty
            showToastShort(getString(R.string.clipboardIsEmpty), getApplicationContext());
            return;
        }

        ClipData.Item item = clipData.getItemAt(0);
        String clipText = String.valueOf(item.getText());

        if(!clipText.isEmpty()) {
            addCalculateText(clipText.replace(" ", ""));
            showToastShort(getString(R.string.pastedClipboard), getApplicationContext());
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            calculateIfIsNotInvalidCalculation();
        }

        if(calculateText.isEmpty()) {
            calculation_edittext.findFocus();
            calculation_edittext.setSelection(calculation_edittext.getText().length());
            scrollToEnd(findViewById(R.id.calculation_horizontal_scroll_view));
        }
    }

    private void copyToClipboard() {
        ClipData clipData = ClipData.newPlainText("", getResultText());
        clipboardManager.setPrimaryClip(clipData);
        showToastShort(getString(R.string.copiedToClipboard), getApplicationContext());
    }

    /**
     * Sets up the listener for all buttons
     *
     * @param textViewId The ID of the button to which the listener is to be set.
     * @param action The action which belongs to the button.
     */
    private void setActionButtonListener(int textViewId, Runnable action) {
        TextView textView = findViewById(textViewId);
        if(textView != null) {
            textView.setOnClickListener(v -> {
                action.run();

                if(rechenMaxUI != null && !isErrorMessage(String.valueOf(CalculatorEngine.calculate(getCalculateText())))) {
                    setResultText(CalculatorEngine.calculate(getCalculateText()));
                } else {
                    setResultText("");
                }

                dataManager.saveNumbers(getApplicationContext());
                setTextColorAccordingToCalculation();
            });
        }
    }
    private void setCalculateButtonListener(int textViewId, Runnable action) {
        TextView textView = findViewById(textViewId);
        if(textView != null) {
            textView.setOnClickListener(v -> {
                action.run();
                dataManager.saveNumbers(getApplicationContext());
            });
        }
    }

    /**
     * Scrolls a ScrollView to the bottom of its content.
     * <p>
     * This method posts a Runnable to the ScrollView's message queue, which
     * ensures that the scrolling operation is executed after the view is
     * created and laid out. It uses the fullScroll method with FOCUS_DOWN
     * parameter to scroll the ScrollView to the bottom.
     *
     * @param scrollView The ScrollView to be scrolled to the bottom.
     */
    private void scrollToStart(final HorizontalScrollView scrollView) {
        // Executes the scrolling to the bottom of the ScrollView in a Runnable.
        if(scrollView != null) {
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_LEFT));
        }
    }
    private void scrollToEnd(final HorizontalScrollView scrollView) {
        if(scrollView != null) {
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_RIGHT));
        }
    }
    private void scrollToTop(final HorizontalScrollView scrollView) {
        // Executes the scrolling to the bottom of the ScrollView in a Runnable.
        if(scrollView != null) {
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_UP));
        }
    }
    private void scrollToBottom(final HorizontalScrollView scrollView) {
        // Executes the scrolling to the bottom of the ScrollView in a Runnable.
        if(scrollView != null) {
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }
    private void scrollToCursor(final HorizontalScrollView scrollView, final int position) {
        if (scrollView != null) {
            scrollView.post(() -> scrollView.scrollTo(position, 0));
        }
    }

    public void formatCalculationText() {
        if(isErrorMessage(getCalculateText()) && !getCalculateText().isEmpty()) {
            return;
        }

        String calculateText = getCalculateText().replace(".", "");
        int cursorPosition = calculation_edittext.getSelectionStart();

        if(cursorPosition == getCalculateText().length()) {
            calculation_edittext.clearFocus();
        }

        ArrayList<String> calculationTextParts = new ArrayList<>();
        ArrayList<Integer> numberPositionInCalculationTextParts = new ArrayList<>(); // Neue Liste für Positionen
        StringBuilder tempToken = new StringBuilder();

        int dotsBeforeFormatting = 0;
        for(int x = 0; x < cursorPosition; x++) {
            if(getCalculateText().charAt(x) == '.') {
                dotsBeforeFormatting++;
            }
        }

        int n;
        for (n = 0; n < calculateText.length(); n++) {
            String currentSubstring = calculateText.substring(n);
            boolean operatorFound = false;

            for (String operator : operatorsFormatCalculationText) {
                if (currentSubstring.startsWith(operator)) {
                    if (tempToken.length() > 0) {
                        calculationTextParts.add(tempToken.toString());
                        tempToken = new StringBuilder();
                    }
                    calculationTextParts.add(operator);
                    n += operator.length() - 1;
                    operatorFound = true;
                    break;
                }
            }

            if (!operatorFound) {
                tempToken.append(calculateText.charAt(n));
            }

            // Überprüfen, ob tempToken eine Zahl ist und Position hinzufügen
            if ((tempToken.length() > 0) && tempToken.toString().matches("\\d+(,\\d+)?")) {
                numberPositionInCalculationTextParts.add(calculationTextParts.size());
            }
        }

        if (tempToken.length() > 0) {
            calculationTextParts.add(tempToken.toString());
        }

        int addedDots = 0;
        ArrayList<Integer> uniqueNumberPositions = removeDuplicates(numberPositionInCalculationTextParts);

        for (int i = 0; i < uniqueNumberPositions.size(); i++) {
            int numberIndex = uniqueNumberPositions.get(i);
            if (numberIndex < calculationTextParts.size()) {
                String number = calculationTextParts.get(numberIndex);
                String formattedNumber = formatNumber(number);
                int originalLength = number.length();
                int formattedLength = formattedNumber.length();

                calculationTextParts.set(numberIndex, formattedNumber); // Ersetzen in der Liste

                if (cursorPosition > numberIndex) {
                    addedDots += formattedLength - originalLength;
                } else if (cursorPosition <= numberIndex + originalLength) {
                    addedDots += Math.min(formattedLength - originalLength, cursorPosition - numberIndex);
                    break;
                }
            }
        }

        StringBuilder formattedText = new StringBuilder();
        for(int x = 0; x < calculationTextParts.size(); x++) {
            formattedText.append(calculationTextParts.get(x));
        }

        setCalculateText(formattedText.toString());

        int newCursorPosition = getNewCursorPosition(cursorPosition, formattedText.toString(), dotsBeforeFormatting);

        if(formattedText.length() >= 1) {
            calculation_edittext.setSelection(Math.min(formattedText.length() - 1, newCursorPosition));
        }
        adjustCursorPosition(calculation_edittext);
        setTextColorAccordingToCalculation();
    }

    private int getNewCursorPosition(int cursorPosition, String formattedText, int dotsBeforeFormatting) {
        int dotsAfterFormatting = 0;

        if (formattedText.length() > 0) {
            int limit = Math.min(cursorPosition, formattedText.length());
            for (int x = 1; x < limit; x++) {
                if (formattedText.charAt(x - 1) == '.') {
                    dotsAfterFormatting++;
                }
            }
        }

        int newCursorPosition = cursorPosition + dotsAfterFormatting - dotsBeforeFormatting;
        if (newCursorPosition > formattedText.length()) {
            newCursorPosition = formattedText.length();
        } else if (newCursorPosition < 0) {
            newCursorPosition = 0;
        }
        return newCursorPosition;
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    /**
     * Formats a numerical input string by:
     *   - Removing any existing decimal points (periods)
     *   - Separating the decimal portion (if present)
     *   - Grouping the integer portion into blocks of three digits, separated by dots
     *   - Preserving the negative sign if present
     *
     * @param input The raw numerical string to format
     * @return The formatted string with dot-separated digit groups
     */
    public String formatNumber(String input) {
        input = input.replace(".", "");

        if (input.contains(",")) {
            String[] resultParts = input.split(",", -1); // Use -1 to keep trailing empty strings

            String integerPart = resultParts.length > 0 ? extractDigitsWithGrouping(resultParts[0]) : "";
            String decimalPart = resultParts.length > 1 ? resultParts[1] : "";

            return integerPart + (decimalPart.isEmpty() ? "" : "," + decimalPart);
        } else {
            return extractDigitsWithGrouping(input);
        }
    }

    /**
     * Extracts digits from an input string and groups them with dots every three digits.
     *
     * @param input is the input string from which digits are to be extracted.
     * @return the extracted digits as a string.
     */
    private String extractDigitsWithGrouping(String input) {
        StringBuilder resultBuilder = new StringBuilder();
        boolean isNegative = input.contains("-");
        input = input.replace(".", "").replace("-", "");
        int count = 0;

        if(input.isEmpty()) {
            return "";
        }

        for (int x = input.length(); x > 0; x--) {
            if (Character.isDigit(input.charAt(x - 1))) {
                count++;
                resultBuilder.insert(0, input.charAt(x - 1));
                if (count == 3 && x != 1) {
                    resultBuilder.insert(0, ".");
                    count = 0;
                }
            } else {
                resultBuilder.insert(0, input.charAt(x - 1));
            }
        }

        return (isNegative ? "-" : "") + resultBuilder.toString();
    }

    /**
     * Determines the maximum number of characters that can fit within a TextView
     * without triggering horizontal scrolling in its parent HorizontalScrollView.
     * <p>
     * This method dynamically adds characters to the TextView and measures its width.
     * Once the TextView's width exceeds the HorizontalScrollView's width, it signals
     * that the maximum character count has been reached.
     * <p>
     * The calculated maximum character count is then stored in the app's settings
     * for later use (likely to adjust display parameters or limit input). The method
     * also clears and reloads existing calculation data to ensure consistency with
     * the newly determined character limit.
     */
    private void findMaxCharactersWithoutScrolling() {
        textView = findViewById(R.id.result_textview);

        if(textView == null) {
            return;
        }

        resultScrollView.post(() -> {
            // Loop to find the minimum number of characters needed to scroll
            while (true) {
                // Add one character to the TextView
                textBuilder.append("0");
                textView.setText(textBuilder.toString());

                // Measure the TextView width
                textView.measure(0, 0);
                int textViewWidth = textView.getMeasuredWidth();

                // Check if scrolling is possible
                if (textViewWidth > resultScrollView.getWidth()) {
                    break;
                }

                // Increase character count
                characterCount++;
            }
            characterCount -= 4;

            // Print the result
            //System.out.println("Minimum number of characters needed to scroll: " + characterCount);
            dataManager.saveToJSONSettings("maxNumbersWithoutScrolling", String.valueOf(characterCount), getApplicationContext());
        });
    }

    /**
     * Toggles the visibility of the scientific layouts (rows) in the calculator interface
     * using an animation. The method animates the row weights of these layouts to
     * smoothly expand or collapse them. It also updates the dropdown button's text
     * to reflect the current state and saves this preference to the app's settings.
     */
    private void toggleScientificLayouts() {
        TextView dropdownButton = findViewById(R.id.dropdown_button_textview);
        if (isExpanded) {
            animateLayoutWeights(0.8f, 0.0f); // Collapse
            dropdownButton.setText(R.string.dropdown_arrow_down);
        } else {
            animateLayoutWeights(0.0f, 0.8f); // Expand
            dropdownButton.setText(R.string.dropdown_arrow_up);
        }

        dataManager.saveToJSONSettings("showScienceRow", isExpanded, getApplicationContext());
        isExpanded = !isExpanded;
    }

    /**
     * Animates the row weights of a set of scientific layouts within a GridLayout.
     * This animation is used to smoothly expand or collapse these layouts,
     * creating a dynamic and visually appealing transition in the UI.
     *
     * @param startWeight The initial row weight of the layouts.
     * @param endWeight   The final row weight of the layouts after the animation.
     */
    private void animateLayoutWeights(float startWeight, float endWeight) {
        ValueAnimator animator = ValueAnimator.ofFloat(startWeight, endWeight);
        animator.setDuration(200);
        animator.setInterpolator(new LinearOutSlowInInterpolator());

        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            for (LinearLayout layout : scientificLayouts) {
                if(layout != null) {
                    GridLayout.LayoutParams params = (GridLayout.LayoutParams) layout.getLayoutParams();
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, animatedValue);
                    layout.setLayoutParams(params);
                    layout.setVisibility(animatedValue == 0 ? View.GONE : View.VISIBLE);
                }
            }
        });

        animator.start();
    }

    /**
     * This method checks if the input text is invalid.
     *
     * @param text The text to be checked. This should be a string containing the text input from the user or the result of a calculation.
     * @return Returns true if the text is invalid (contains "Ungültige Eingabe", "Unendlich", "Syntax Fehler", or "Domainfehler" ...), and false otherwise.
     */
    public boolean isErrorMessage(String text) {
        return  text.contains(getString(R.string.errorMessage1))  ||
                text.contains(getString(R.string.errorMessage2))  ||
                text.contains(getString(R.string.errorMessage3))  ||
                text.contains(getString(R.string.errorMessage4))  ||
                text.contains(getString(R.string.errorMessage5))  ||
                text.contains(getString(R.string.errorMessage6))  ||
                text.contains(getString(R.string.errorMessage7))  ||
                text.contains(getString(R.string.errorMessage8))  ||
                text.contains(getString(R.string.errorMessage9))  ||
                text.contains(getString(R.string.errorMessage10)) ||
                text.contains(getString(R.string.errorMessage11)) ||
                text.contains(getString(R.string.errorMessage12)) ||
                text.contains(getString(R.string.errorMessage13)) ||
                text.contains(getString(R.string.errorMessage14)) ||
                text.contains(getString(R.string.errorMessage15)) ||
                text.contains(getString(R.string.errorMessage16)) ||
                text.contains(getString(R.string.errorMessage17)) ||
                text.contains(getString(R.string.errorMessage18)) ||
                text.contains(getString(R.string.errorMessage19)) ||
                text.contains(getString(R.string.errorMessage20)) ||
                text.contains(getString(R.string.errorMessage21)) ||
                text.contains(getString(R.string.errorMessage22)) ||
                text.contains("for");
    }

    /**
     * This method is called when the back button is pressed.
     * It calls the superclass's onBackPressed method and then finishes the activity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        finishActivity(1);
    }

    private void resetCalculatePressed() {
        dataManager.saveToJSONSettings("pressedCalculate", "false", getApplicationContext());
    }

    /**
     * The following methods are simple getter and setter methods for various properties.
     */
    public String getResultText() {
        TextView resulttext = findViewById(R.id.result_textview);
        if(resulttext != null) {
            return resulttext.getText().toString();
        }
        return "";
    }
    public String getCalculateText() {
        if(findViewById(R.id.calculation_edittext) != null) {
            EditText calculatetext = findViewById(R.id.calculation_edittext);
            return calculatetext.getText().toString();
        } else {
            return "";
        }
    }
    public void setResultText(final String s) {
        TextView resulttext = findViewById(R.id.result_textview);
        if(resulttext != null) { resulttext.setText(s); }
    }
    public void setCalculateText(final String s) {
        if(findViewById(R.id.calculation_edittext) != null) {
            EditText calculatetext = findViewById(R.id.calculation_edittext);
            calculatetext.setText(s);
        }

        if(isErrorMessage(getCalculateText())) {
            calculation_edittext.setTextColor(Color.parseColor(hexColorErrorMessageRed));
            calculation_edittext.setSelection(getCalculateText().length());
        }
    }
    public void addCalculateText(final String s) {
        EditText editText = findViewById(R.id.calculation_edittext);
        calculation_edittext.setTextColor(Color.parseColor(hexColorDefaultTextWhite));

        if(editText.isFocused()) {
            int cursorPosition = editText.getSelectionStart();

            Editable editable = editText.getText();
            editable.insert(cursorPosition, s);
            return;
        }

        editText.append(s);
    }

}