/**
 * ï∂éöóÒëÄçÏ
 */
package jp.neap.gametimer;

import android.content.Context;


public class StringUtil {
	public static int toIntegerNumber(String text, long minValue, long maxValue) {
		int value;
		try {
			value = Integer.valueOf(text).intValue();
			if ((value < minValue) || (value > maxValue)) {
				value = 0;
			}
		}
		catch (NumberFormatException e) {
			value = 0;
		}
		return value;
	}

	public static String[] integerToDigitText(int number) {
		final String numberText = String.format("%08d", number);
		final String[] values = new String[numberText.length()];
		for (int i = 0 ; i < numberText.length() ; i++) {
			values[i] = numberText.substring(i, i + 1);
		}
		return values;
	}

	public static boolean isJP(Context context) {
		if ("JP".equals(context.getString(R.string.lang_test))) {
			return true;
		}
		return false;
	}
}