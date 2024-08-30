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

import java.util.HashMap;
import java.util.Map;

public class VariableHelper {

    public static Map<String, Integer> replacements = new HashMap<>() {{
        put("eins", R.string.one);
        put("zwei", R.string.two);
        put("drei", R.string.three);
        put("vier", R.string.four);
        put("fünf", R.string.five);
        put("sechs", R.string.six);
        put("sieben", R.string.seven);
        put("acht", R.string.eight);
        put("neun", R.string.nine);
        put("null", R.string.zero);
        put("*", R.string.multiply_);
        put("x", R.string.multiply_);
        put("mal", R.string.multiply_);
        put("/", R.string.divide_);
        put("geteilt durch", R.string.divide_);
        put("geteilt", R.string.divide_);
        put("plus", R.string.add_);
        put("addiert mit", R.string.add_);
        put("minus", R.string.subtract_);
        put("subtrahiert mit", R.string.subtract_);
        put("hoch", R.string.power_);
        put("klammerauf", R.string.add_parentheses_on);
        put("klammerzu", R.string.add_parentheses_off);
        put("komma", R.string.comma_);
        put("semikolon", R.string.semicolon_);
        put("modulo", R.string.percentage_);
        put("modulu", R.string.percentage_);
        put("modulus", R.string.percentage_);
        put("modul", R.string.percentage_);
        put("prozent", R.string.percentage_);
        put("einhalb", R.string.half_);
        put("hälfte", R.string.half_);
        put("halbe", R.string.half_);
        put("halb", R.string.half_);
        put("eindrittel", R.string.third_);
        put("drittel", R.string.third_);
        put("einviertel", R.string.quarter_);
        put("viertel", R.string.quarter_);
        put("einfünftel", R.string.fifth_);
        put("fünftel", R.string.fifth_);
        put("einzehntel", R.string.tenth_);
        put("zehntel", R.string.tenth_);
        put("pi", R.string.pi_);
        put("eulersche zahl", R.string.eulers_number_);
        put("eulersche", R.string.eulers_number_);
        put("permutation", R.string.permutation_);
        put("kombination", R.string.combination_);
        put("fakultät", R.string.factorial_);
        put("wurzel", R.string.square_root_);
        put("quadratwurzel", R.string.square_root_);
        put("kubikwurzel", R.string.qubic_root_);
        put("logarithmus", R.string.log_);
        put("logarithmus10", R.string.log_);
        put("logarithmus 10", R.string.log_);
        put("logarithmuszurbasis10", R.string.log_);
        put("logarithmuszehn", R.string.log_);
        put("logarithmuszurbasiszehn", R.string.log_);
        put("logarithmus2", R.string.log2_);
        put("logarithmus 2", R.string.log2_);
        put("logarithmuszurbasis2", R.string.log2_);
        put("logarithmuszwei", R.string.log2_);
        put("logarithmuszurbasiszwei", R.string.log2_);
        put("polarkoordinaten", R.string.rectangular_to_polar_);
        put("rechteckkoordinaten", R.string.polar_to_rectangular_);
        put("zufälligeganzzahl", R.string.random_integer_);
        put("zufälligezahl", R.string.random_number_);
        put("ln", R.string.ln_);
        put("natürlicherlogarithmus", R.string.ln_);
        put("natürlichelogarithmusfunktion", R.string.ln_);

        put("hyperbolischerarkussinus", R.string.a_sine_h_);
        put("hyperbolischerarcussinus", R.string.a_sine_h_);
        put("hyperbolischersinus", R.string.sine_h_);
        put("Arcussinus", R.string.a_sine_);
        put("Arkussinus", R.string.a_sine_);
        put("arcsinus", R.string.a_sine_);
        put("arksinus", R.string.a_sine_);
        put("asinush", R.string.a_sine_h_);
        put("sinush", R.string.sine_h_);
        put("asinus", R.string.a_sine_);
        put("sinus", R.string.sine_);

        put("hyperbolischerarkuscosinus", R.string.a_cosine_h_);
        put("hyperbolischerarcuscosinus", R.string.a_cosine_h_);
        put("hyperbolischercosinus", R.string.cosine_h_);
        put("Arcuscosinus", R.string.a_cosine_);
        put("Arkuscosinus", R.string.a_cosine_);
        put("arccosinus", R.string.a_cosine_);
        put("arkcosinus", R.string.a_cosine_);
        put("acosinush", R.string.a_cosine_h_);
        put("cosinush", R.string.cosine_h_);
        put("acosinus", R.string.a_cosine_);
        put("cosinus", R.string.cosine_);

        put("hyperbolischerarkustangens", R.string.a_tangent_h_);
        put("hyperbolischerarcustangens", R.string.a_tangent_h_);
        put("hyperbolischertangens", R.string.tangent_h_);
        put("Arcustangens", R.string.a_tangent_);
        put("Arkustangens", R.string.a_tangent_);
        put("arctangens", R.string.a_tangent_);
        put("arktangens", R.string.a_tangent_);
        put("atangensh", R.string.a_tangent_h_);
        put("tangensh", R.string.tangent_h_);
        put("atangens", R.string.a_tangent_);
        put("tangens", R.string.tangent_);
    }};

}
