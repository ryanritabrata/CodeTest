package com.tradeapplication.utilities;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by Raghavendra on 06/11/17.
 * All frequently used Preference key's and the pop up code
 */

public class CommonForAll {
    private static final String CURRENCY_IND_WITH_DOT = "##,##,##,##,##,##,##,##,###.##";

    public static String changeRupeeFormatToIndiaFormatWithDot(String amt) {
        DecimalFormat formatter;
        formatter = new DecimalFormat(CommonForAll.CURRENCY_IND_WITH_DOT);
        return formatter.format(Float.parseFloat(amt));
    }

    public static String returnValueRoundedOffToTwoDigits(String value) {
        Double a = Double.parseDouble(value);
        double roundOff = Math.round(a * 100.0) / 100.0;
        return String.format(Locale.ROOT, "%.2f", roundOff);
    }

}