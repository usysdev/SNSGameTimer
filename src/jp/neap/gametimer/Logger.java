/**
 * 
 */
package jp.neap.gametimer;

import android.util.Log;


/**
 *
 */
public class Logger {

	public static boolean isDebugEnabled() {
		return false;
	}

	public static void debug(String message) {
		Log.v("GAMETIMER", message);
	}
}
