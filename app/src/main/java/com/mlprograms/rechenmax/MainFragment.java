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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calculator_ui, container, false);

        TextView buttonOpenHistory = view.findViewById(R.id.actionbar_history_textview);
        buttonOpenHistory.setOnClickListener(v -> {
            // Zum vorherigen Fragment (HistoryFragment) im Backstack zurückkehren
            Toast.makeText(getActivity(), "Button clicked!", Toast.LENGTH_SHORT).show(); // Toast-Nachricht anzeigen (die nicht angezeigt wird) sonst funktioniert das nicht (keine Ahnung warum). Danke an ChatGPT für den Tipp
            ((RechenMaxUI) getActivity()).loadHistoryActivity();
        });

        return view;
    }
}