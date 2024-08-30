package com.mlprograms.rechenmax;

/*
 * Copyright (c) 2024 by Max Lemberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DataManager
 * @author Max Lemberg
 * @version 1.4.7
 * @date 17.05.2024
 */

//  | Names                            | Values                           | Context                              |
//  |----------------------------------|----------------------------------|--------------------------------------|
//  | selectedSpinnerSetting           | System / Dark / Light            | MainActivity                         |
//  | functionMode                     | Deg / Rad                        | MainActivity                         |
//  | settingReleaseNotesSwitch        | true / false                     | SettingsActivity                     |
//  | removeValue                      | true / false                     | MainActivity                         |
//  | settingsTrueDarkMode             | true / false                     | MainActivity -> SettingsActivity     |
//  | showPatchNotes                   | true / false                     | MainActivity -> SettingsActivity     |
//  | showScienceRow                   | true / false                     | MainActivity                         |
//  | rotate_op                        | true / false                     | MainActivity                         |
//  | lastnumber                       | Integer                          | MainActivity                         |
//  | historyTextViewNumber            | Integer                          | MainActivity                         |
//  | result_text                      | String                           | MainActivity                         |
//  | calculate_text                   | String                           | MainActivity                         |
//  | lastop                           | String                           | MainActivity                         |
//  | isNotation                       | true / false                     | MainActivity                         |
//  | eNotation                        | true / false                     | MainActivity                         |
//  | showShiftRow                     | true / false                     | MainActivity                         |
//  | shiftRow                         | true / false                     | MainActivity                         |
//  | logX                             | true / false                     | MainActivity                         |
//  | currentVersion                   | String                           | MainActivity                         |
//  | old_version                      | String                           | MainActivity                         |
//  | returnToCalculator               | true / false                     | MainActivity                         |
//  | notificationSent                 | true / false                     | BackgroundService                    |
//  | pressedCalculate                 | true / false                     | MainActivity                         |
//  | allowNotification                | true / false                     | SettingsActivity                     |
//  | allowRememberNotifications       | true / false                     | SettingsActivity                     |
//  | allowDailyNotifications          | true / false                     | SettingsActivity                     |
//  | allowRememberNotificationsActive | true / false                     | SettingsActivity                     |
//  | allowDailyNotificationsActive    | true / false                     | SettingsActivity                     |
//  | refactorPI                       | true / false                     | MainActivity                         |
//  | historyAnimation                 | true / false                     | HistoryActivtiy                      |
//  | historyMode                      | single / multiple                | MainActivity                         |
//  | historyModeAdvanced              | true / false                     | MainActivity                         |
//  | showConverterDevelopmentMessage  | true / false                     | ConvertActivity                      |
//  | startApp                         | true / false                     | MainActivity                         |
//  | speech_recognition               | true / false                     | MainActivity                         |
//  | report                           | 'name' of person
//                                       'title' of bug
//                                       'text' (description) of bug      | ConvertActivity                      |
//  | convertMode                      | Winkel
//                                       Fläche
//                                       Speicher
//                                       Entfernung
//                                       Volumen
//                                       MasseGewicht
//                                       Zeit
//                                       Temperatur
//                                       StromSpannung
//                                       StromStärke
//                                       Geschwindigkeit
//                                       Druck
//                                       Drehmoment
//                                       Arbeit
//                                       Energie                         | SettingsActivity                     |
//  | WinkelCurrent
//    FlächeCurrent
//    SpeicherCurrent
//    EntfernungCurrent
//    VolumenCurrent
//    MasseGewichtCurrent
//    ZeitCurrent
//    TemperaturCurrent
//    StromSpannungCurrent
//    StromStärkeCurrent
//    GeschwindigkeitCurrent
//    EnergieCurrent
//    DruckCurrent
//    DrehmomentCurrent
//    ArbeitCurrent                    | 0 - ...                          | ConvertActivity                      |
//  | numberOfDecimals                 | 0 - 10                           | Converter                            |
//  | lastActivity                     | Main, Set, Rep, Con, Help, His   | MainActivity                         |
//  | calculationCount                 | 0 - ...                          | MainActivity                         |
//  | openedApp                        | 0 - ...                          | MainActivity                         |
//  | maxNumbersWithoutScrolling       | 1 - ...                          | MainActivity                         |
//  | variable_a                       | String                           | MainActivity                         |
//  | variable_b                       | String                           | MainActivity                         |
//  | variable_c                       | String                           | MainActivity                         |
//  | variable_d                       | String                           | MainActivity                         |
//  | variable_e                       | String                           | MainActivity                         |
//  | variable_f                       | String                           | MainActivity                         |
//  | variable_g                       | String                           | MainActivity                         |
//  | variable_x                       | String                           | MainActivity                         |
//  | variable_y                       | String                           | MainActivity                         |
//  | variable_z                       | String                           | MainActivity                         |
//  | appLanguage                      | String                           | MainActivity, SettingsActivity       |

public class DataManager {

    // Declare a MainActivity object
    private RechenMaxUI rechenMaxUI;

    // Define the names of the files
    private static final String JSON_FILE = "settings.json";
    static final String HISTORY_FILE = "history.json";

    /**
     * This constructor is used to create a DataManager object for the MainActivity.
     *
     * @param rechenMaxUI The MainActivity instance that this DataManager will be associated with.
     */
    public DataManager(RechenMaxUI rechenMaxUI) {
        this.rechenMaxUI = rechenMaxUI;
    }

    /**
     * This constructor is used to create a DataManager object.
     *
     */
    public DataManager() {
        // Declare a SettingsActivity object
    }

    public DataManager(ConvertActivity convertActivity) {
    }

    public DataManager(HistoryActivity historyActivity) {
    }

    public DataManager(Converter converter) {
    }

    /**
     * This method is used to create a new JSON file in the application's file directory.
     *
     * @param applicationContext The application context, which is used to get the application's file directory.
     */
    public void createJSON(Context applicationContext) {
        File file = new File(applicationContext.getFilesDir(), JSON_FILE);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToJSONSettings(String name, String value, Context applicationContext) {
        JSONObject jsonObj = new JSONObject();
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("saveToHistory", "Failed to create new file");
                    return;
                }
            }
            String content = new String(Files.readAllBytes(file.toPath()));
            if (!content.isEmpty()) {
                jsonObj = new JSONObject(new JSONTokener(content));
            }

            JSONObject dataObj = new JSONObject();
            dataObj.put("value", value);

            jsonObj.put(name, dataObj);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveToJSONSettings(String name, boolean value, Context applicationContext) {
        JSONObject jsonObj = new JSONObject();
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("saveToHistory", "Failed to create new file");
                    return;
                }
            }
            String content = new String(Files.readAllBytes(file.toPath()));
            if (!content.isEmpty()) {
                jsonObj = new JSONObject(new JSONTokener(content));
            }

            JSONObject dataObj = new JSONObject();
            dataObj.put("value", String.valueOf(value));

            jsonObj.put(name, dataObj);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJSONSettingsData(String name, Context applicationContext) throws JSONException {
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    return jsonObj.getJSONObject(name);
                } else {
                    Log.e("getDataForName", "Data with name " + name + " not found. Trying to Create ...");
                }
            } else {
                Log.e("getDataForName", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, JSONObject> getAllDataFromJSONSettings(Context applicationContext) {
        Map<String, JSONObject> allData = new HashMap<>();
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                Iterator<String> keys = jsonObj.keys();
                while (keys.hasNext()) {
                    String name = keys.next();
                    JSONObject dataObj = jsonObj.getJSONObject(name);
                    allData.put(name, dataObj);
                }
            } else {
                Log.e("getAllData", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void clearJSONSettings(Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteNameFromJSONSettings(String name, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    jsonObj.remove(name); // Entferne den Namen aus dem JSON-Objekt
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jsonObj.toString());
                    fileWriter.flush();
                    fileWriter.close();
                    Log.d("deleteNameFromHistory", "Name " + name + " deleted successfully.");
                } else {
                    Log.e("deleteNameFromHistory", "Data with name " + name + " not found.");
                }
            } else {
                Log.e("deleteNameFromHistory", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateValuesInJSONSettingsData(String name, String valueName, String newValue, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    JSONObject dataObj = jsonObj.getJSONObject(name);
                    dataObj.put(valueName, newValue);
                    jsonObj.put(name, dataObj);

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jsonObj.toString());
                    fileWriter.flush();
                    fileWriter.close();

                    Log.d("updateDetailsInHistoryData", "Details for " + name + " updated successfully.");
                } else {
                    Log.e("updateDetailsInHistoryData", "Data with name " + name + " not found.");
                }
            } else {
                Log.e("updateDetailsInHistoryData", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void addValueWithCustomNameToJSONSettings(String name, String valueName, String value, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), JSON_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                JSONObject dataObj;
                if (jsonObj.has(name)) {
                    dataObj = jsonObj.getJSONObject(name);
                } else {
                    dataObj = new JSONObject();
                }

                if (!dataObj.has(valueName)) {
                    dataObj.put(valueName, value);
                    jsonObj.put(name, dataObj);

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jsonObj.toString());
                    fileWriter.flush();
                    fileWriter.close();

                    Log.d("addValueWithCustomName", "Value " + value + " with name " + valueName + " added successfully to " + name + ".");
                } else {
                    Log.d("addValueWithCustomName", "Value with name " + valueName + " already exists for " + name + ".");
                }
            } else {
                Log.e("addValueWithCustomName", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method initializes the settings by saving default values to the JSON file.
     *
     * @param applicationContext The application context, which is used to get the application's file directory.
     */
    public void initializeSettings(Context applicationContext) {
        try {
            initializeSetting("selectedSpinnerSetting", "System", applicationContext);
            initializeSetting("functionMode", "Deg", applicationContext);
            initializeSetting("settingReleaseNotesSwitch", "true", applicationContext);
            initializeSetting("removeValue", "false", applicationContext);
            initializeSetting("settingsTrueDarkMode", "false", applicationContext);
            initializeSetting("tempShowScienceRow", "false", applicationContext);
            initializeSetting("showScienceRow", "false", applicationContext);
            initializeSetting("rotate_op", "false", applicationContext);
            initializeSetting("lastnumber", "0", applicationContext);
            initializeSetting("historyTextViewNumber", "0", applicationContext);
            initializeSetting("result_text", "0", applicationContext);
            initializeSetting("calculate_text", "", applicationContext);
            initializeSetting("lastop", "+", applicationContext);
            initializeSetting("isNotation", "false", applicationContext);
            initializeSetting("showShiftRow", "false", applicationContext);
            initializeSetting("showPatchNotes", "false", applicationContext);
            initializeSetting("shiftRow", "1", applicationContext);
            initializeSetting("logX", "false", applicationContext);
            initializeSetting("currentVersion", "1.7.0", applicationContext);
            initializeSetting("old_version", "0", applicationContext);
            initializeSetting("returnToCalculator", "false", applicationContext);
            initializeSetting("allowNotification", "false", applicationContext);
            initializeSetting("allowDailyNotifications", "false", applicationContext);
            initializeSetting("allowRememberNotifications", "false", applicationContext);
            initializeSetting("allowDailyNotificationsActive", "true", applicationContext);
            initializeSetting("allowRememberNotificationsActive", "true", applicationContext);
            initializeSetting("notificationSent", "false", applicationContext);
            initializeSetting("pressedCalculate", "false", applicationContext);
            initializeSetting("refactorPI", "false", applicationContext);
            initializeSetting("historyAnimation", "true", applicationContext);
            initializeSetting("historyMode", "single", applicationContext);
            initializeSetting("historyModeAdvanced", "false", applicationContext);
            initializeSetting("dayPassed", "true", applicationContext);
            initializeSetting("convertMode", "Entfernung", applicationContext);
            initializeSetting("numberOfDecimals", "3", applicationContext);
            initializeSetting("showConverterDevelopmentMessage", "true", applicationContext);
            initializeSetting("report", "", applicationContext);
            initializeSetting("lastActivity", "Main", applicationContext);
            initializeSetting("calculationCount", "1", applicationContext);
            initializeSetting("maxNumbersWithoutScrolling", "6", applicationContext);
            initializeSetting("startApp", "true", applicationContext);
            initializeSetting("speech_recognition", "false", applicationContext);

            initializeSetting("variable_a", "", applicationContext);
            initializeSetting("variable_b", "", applicationContext);
            initializeSetting("variable_c", "", applicationContext);
            initializeSetting("variable_d", "", applicationContext);
            initializeSetting("variable_e", "", applicationContext);
            initializeSetting("variable_f", "", applicationContext);
            initializeSetting("variable_g", "", applicationContext);
            initializeSetting("variable_x", "", applicationContext);
            initializeSetting("variable_y", "", applicationContext);
            initializeSetting("variable_z", "", applicationContext);

            initializeSetting("appLanguage", "de", applicationContext);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeSetting(String key, String defaultValue, Context applicationContext) throws JSONException {
        if(key.equals("currentVersion")) {
            saveToJSONSettings(key, defaultValue, applicationContext);
        } else if(getJSONSettingsData(key, applicationContext) == null) {
            if(key.equals("historyTextViewNumber")) {
                saveToHistory(key, defaultValue, applicationContext);
            }

            saveToJSONSettings(key, defaultValue, applicationContext);

            if(key.equals("convertMode")) {
                addValueWithCustomNameToJSONSettings("convertMode", "WinkelCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "FlächeCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "SpeicherCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "EntfernungCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "VolumenCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "MasseGewichtCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "ZeitCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "TemperaturCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "StromSpannungCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "StromStärkeCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "GeschwindigkeitCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "EnergieCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "DruckCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "DrehmomentCurrent", "0", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "ArbeitCurrent", "0", applicationContext);

                addValueWithCustomNameToJSONSettings("convertMode", "WinkelNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "FlächeNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "SpeicherNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "EntfernungNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "VolumenNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "MasseGewichtNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "ZeitNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "TemperaturNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "StromSpannungNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "StromStärkeNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "GeschwindigkeitNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "EnergieNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "DruckNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "DrehmomentNumber",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("convertMode", "ArbeitNumber",  "", applicationContext);

            } else if(key.equals("report")) {
                addValueWithCustomNameToJSONSettings("report", "name",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("report", "title",  "", applicationContext);
                addValueWithCustomNameToJSONSettings("report", "text",  "", applicationContext);
            }
        }
    }

    /**
     * This method saves numbers to two files.
     *
     * @param applicationContext The application context.
     */
    public void saveNumbers(Context applicationContext) {
        if (rechenMaxUI != null) {
            try {
                // Save calculate_text using dataManager
                saveToJSONSettings("calculate_text", rechenMaxUI.getCalculateText(), applicationContext);

                // Save result_text using dataManager
                saveToJSONSettings("result_text", rechenMaxUI.getResultText(), applicationContext);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method loads numbers from two files and sets the text of two TextViews.
     */
    public void loadNumbers() throws JSONException {
        if (rechenMaxUI != null) {
            JSONObject calculateText = getJSONSettingsData("calculate_text", rechenMaxUI.getApplicationContext());
            JSONObject resultText = getJSONSettingsData("result_text", rechenMaxUI.getApplicationContext());

            TextView calculate_textview = rechenMaxUI.findViewById(R.id.calculate_textview);
            EditText result_edittext = rechenMaxUI.findViewById(R.id.result_textview);

            if (calculate_textview != null && result_edittext != null) {
                try {
                    calculate_textview.setText(calculateText.getString("value"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    final String value = resultText.getString("value");
                    if (!value.replace(" ", "").isEmpty()) {
                        try {
                            result_edittext.setText(resultText.getString("value"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        result_edittext.setText("0");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * This method loads numbers from two files and sets the text of two TextViews.
     */
    public void loadResultText() throws JSONException {
        if (rechenMaxUI != null) {
            JSONObject resultText = getJSONSettingsData("result_text", rechenMaxUI.getApplicationContext());
            TextView resultlabel = rechenMaxUI.findViewById(R.id.result_textview);

            if (resultlabel != null) {
                try {
                    final String value = resultText.getString("value");
                    if (!value.replace(" ", "").isEmpty()) {
                        try {
                            rechenMaxUI.setResultText(resultText.getString("value"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        rechenMaxUI.setResultText("0");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * This method loads numbers from two files and sets the text of two TextViews.
     */
    public void loadCalculateText() throws JSONException {
        if (rechenMaxUI != null) {
            JSONObject calculateText = getJSONSettingsData("calculate_text", rechenMaxUI.getApplicationContext());
            TextView calculatelabel = rechenMaxUI.findViewById(R.id.calculate_textview);

            if (calculatelabel != null) {
                try {
                    rechenMaxUI.setCalculateText(calculateText.getString("value"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateValuesInHistoryData(String name, String valueName, String newValue, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    JSONObject dataObj = jsonObj.getJSONObject(name);
                    dataObj.put(valueName, newValue);
                    jsonObj.put(name, dataObj);

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jsonObj.toString());
                    fileWriter.flush();
                    fileWriter.close();

                    Log.d("updateDetailsInHistoryData", "Details for " + name + " updated successfully.");
                } else {
                    Log.e("updateDetailsInHistoryData", "Data with name " + name + " not found.");
                }
            } else {
                Log.e("updateDetailsInHistoryData", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveToHistory(String name, String date, String details, String calculation, String result, Context applicationContext) {
        JSONObject jsonObj = new JSONObject();
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("saveToHistory", "Failed to create new file");
                    return;
                }
            }
            String content = new String(Files.readAllBytes(file.toPath()));
            if (!content.isEmpty()) {
                jsonObj = new JSONObject(new JSONTokener(content));
            }

            JSONObject dataObj = new JSONObject();
            dataObj.put("date", date);
            dataObj.put("details", details);
            dataObj.put("calculation", calculation);
            dataObj.put("result", result);

            jsonObj.put(name, dataObj);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveToHistory(String name, String value, Context applicationContext) {
        JSONObject jsonObj = new JSONObject();
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e("saveToHistory", "Failed to create new file");
                    return;
                }
            }
            String content = new String(Files.readAllBytes(file.toPath()));
            if (!content.isEmpty()) {
                jsonObj = new JSONObject(new JSONTokener(content));
            }

            JSONObject dataObj = new JSONObject();
            dataObj.put("value", value);

            jsonObj.put(name, dataObj);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonObj.toString());
                fileWriter.flush();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getHistoryData(String name, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    return jsonObj.getJSONObject(name);
                } else {
                    //Log.e("getDataForName", "Data with name " + name + " not found.");
                }
            } else {
                //Log.e("getDataForName", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, JSONObject> getAllData(Context applicationContext) {
        Map<String, JSONObject> allData = new HashMap<>();
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                Iterator<String> keys = jsonObj.keys();
                while (keys.hasNext()) {
                    String name = keys.next();
                    JSONObject dataObj = jsonObj.getJSONObject(name);
                    allData.put(name, dataObj);
                }
            } else {
                //Log.e("getAllData", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void clearHistory(Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteNameFromHistory(String name, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    jsonObj.remove(name); // Entferne den Namen aus dem JSON-Objekt
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jsonObj.toString());
                    fileWriter.flush();
                    fileWriter.close();
                    //Log.d("deleteNameFromHistory", "Name " + name + " deleted successfully.");
                } else {
                    //Log.e("deleteNameFromHistory", "Data with name " + name + " not found.");
                }
            } else {
                //Log.e("deleteNameFromHistory", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<String, JSONObject> getAllDataFromHistory(Context applicationContext) {
        Map<String, JSONObject> allData = new HashMap<>();
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                Iterator<String> keys = jsonObj.keys();
                while (keys.hasNext()) {
                    String name = keys.next();
                    JSONObject dataObj = jsonObj.getJSONObject(name);
                    allData.put(name, dataObj);
                }
            } else {
                Log.e("getAllData", "History file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void updateDetailsInHistoryData(String name, String newDetails, Context applicationContext) {
        try {
            File file = new File(applicationContext.getFilesDir(), HISTORY_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject jsonObj = new JSONObject(new JSONTokener(content));

                if (jsonObj.has(name)) {
                    JSONObject dataObj = jsonObj.getJSONObject(name);
                    dataObj.put("details", newDetails);
                    jsonObj.put(name, dataObj);

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(jsonObj.toString());
                    fileWriter.flush();
                    fileWriter.close();

                    //Log.d("updateDetailsInHistoryData", "Details for " + name + " updated successfully.");
                } else {
                    //Log.e("updateDetailsInHistoryData", "Data with name " + name + " not found.");
                }
            } else {
                //Log.e("updateDetailsInHistoryData", "JSON file not found.");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}