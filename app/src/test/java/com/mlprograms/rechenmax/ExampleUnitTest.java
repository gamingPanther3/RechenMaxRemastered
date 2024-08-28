package com.mlprograms.rechenmax;

import org.junit.Test;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;

import java.math.MathContext;
import java.math.RoundingMode;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    @Test
    public void testThirdRoot() {
        BigDecimal x = new BigDecimal("27");
        BigDecimal expected = new BigDecimal("3");
        BigDecimal result = CalculatorEngine.thirdRoot(x);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testNPercentFromM() {
        BigDecimal n = new BigDecimal("25");
        BigDecimal m = new BigDecimal("200");
        BigDecimal expected = new BigDecimal("50");
        BigDecimal result = CalculatorEngine.nPercentFromM(n, m);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testMIsXPercentOfN() {
        BigDecimal n = new BigDecimal("200");
        BigDecimal m = new BigDecimal("50");
        BigDecimal expected = new BigDecimal("25");
        BigDecimal result = CalculatorEngine.mIsXPercentOfN(n, m);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testModulo() {
        int n = 10;
        int m = 3;
        int expected = 1;
        int result = CalculatorEngine.modulo(n, m);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testPermutationBigDecimal() {
        BigDecimal n = new BigDecimal("5");
        BigDecimal k = new BigDecimal("2");
        BigDecimal expected = new BigDecimal("20");
        BigDecimal result = CalculatorEngine.permutationBigDecimal(n, k);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testPermutationInt() {
        int n = 5;
        int k = 2;
        BigDecimal expected = new BigDecimal("20");
        BigDecimal result = CalculatorEngine.permutationInt(n, k);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCombinationBigDecimal() {
        BigDecimal n = new BigDecimal("5");
        BigDecimal k = new BigDecimal("2");
        BigDecimal expected = new BigDecimal("10");
        BigDecimal result = CalculatorEngine.combinationBigDecimal(n, k);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCombinationInt() {
        int n = 5;
        int k = 2;
        BigDecimal expected = new BigDecimal("10");
        BigDecimal result = CalculatorEngine.combinationInt(n, k);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testPolarToCartesian() {
        BigDecimal r = new BigDecimal("5");
        BigDecimal theta = new BigDecimal("0"); // radians
        BigDecimal[] expected = {new BigDecimal("5"), new BigDecimal("0")};
        BigDecimal[] result = CalculatorEngine.polarToCartesian(r, theta);
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testCartesianToPolar() {
        BigDecimal x = new BigDecimal("3");
        BigDecimal y = new BigDecimal("4");
        BigDecimal[] expected = {new BigDecimal("5"), new BigDecimal("53.13010235415599")};
        BigDecimal[] result = CalculatorEngine.cartesianToPolar(x, y);
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testSqrt() {
        BigDecimal value = new BigDecimal("16");
        BigDecimal expected = new BigDecimal("4");
        BigDecimal result = CalculatorEngine.sqrt(value, MC);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testSin() {
        BigDecimal angle = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.sin(angle);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAsin() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.asin(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testSinh() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.sinh(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAsinh() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.asinh(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCos() {
        BigDecimal angle = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("1");
        BigDecimal result = CalculatorEngine.cos(angle);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAcos() {
        BigDecimal value = new BigDecimal("1");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.acos(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCosh() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("1");
        BigDecimal result = CalculatorEngine.cosh(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAcosh() {
        BigDecimal value = new BigDecimal("1");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.acosh(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testTan() {
        BigDecimal angle = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.tan(angle);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAtan() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.atan(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testTanh() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.tanh(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testAtanh() {
        BigDecimal value = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.atanh(value);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testLogX() {
        BigDecimal operand = new BigDecimal("100");
        double base = 10;
        BigDecimal expected = new BigDecimal("2");
        BigDecimal result = CalculatorEngine.logX(operand, base);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testLn() {
        BigDecimal operand = new BigDecimal("1");
        BigDecimal expected = new BigDecimal("0");
        BigDecimal result = CalculatorEngine.ln(operand);
        Assertions.assertEquals(expected, result);
    }
}