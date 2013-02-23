/**
 * 
 */
package jp.neap.gametimer;

import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.RemoteViews;


public class DrawUtil {

	/**
	 * ウィジェットに表示している進行中のタイマー数を更新する。
	 */
	public static void updateWidgetActiveTimerCount(Context context) {

		final RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.main);
		final int activeNotifyCount = DrawUtil.getActiveTimerCount(context);
		DrawUtil.drawActiveTimerCount(mainView, activeNotifyCount);

		final AppWidgetManager manager = AppWidgetManager.getInstance(context);
		final ComponentName provider = new ComponentName(context, GameTimerWidgetProvider.class);
		manager.updateAppWidget(provider, mainView);
	}

	public static void drawActiveTimerCount(RemoteViews mainView, int activeScheduleCount) {
		if ( activeScheduleCount > 0 ) {
			mainView.setInt(R.id.background_view, "setImageResource", R.drawable.bg_talk);
			mainView.setTextViewText(R.id.background_notify_count_view, String.valueOf(activeScheduleCount));
			mainView.setViewVisibility(R.id.background_notify_count_view, View.VISIBLE);
		}
		else {
			mainView.setInt(R.id.background_view, "setImageResource", R.drawable.bg);
			mainView.setTextViewText(R.id.background_notify_count_view, "");
			mainView.setViewVisibility(R.id.background_notify_count_view, View.GONE);
		}
	}

	public static int getActiveTimerCount(Context context) {
		final List<GameTimerSettingsBean> beanList;
		GameTimerDBHelper dbHelper = new GameTimerDBHelper(context.getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			beanList = dbHelper.listAllSorted(db);
		}
		finally {
			db.close();
		}

		int activeScheduleCount = 0;
		for (int i = 0 ; i < beanList.size() ; i++) {
			final GameTimerSettingsBean settingsBean = beanList.get(i);
			if ( settingsBean.isActiveSchedule(context) ) {
				activeScheduleCount = activeScheduleCount + 1;
			}
		}

		return activeScheduleCount;
	}
}
