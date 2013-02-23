/**
 * 
 */
package jp.neap.gametimer;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 *
 */
public class GameTimerWidgetService extends Service {

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if ( Logger.isDebugEnabled() ) {
			Logger.debug("GameTimerWidgetService::onDestroy");
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if ( Logger.isDebugEnabled() ) {
			Logger.debug("GameTimerWidgetService::onConfigurationChanged");
		}

		final Context context = getApplicationContext();

		final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, GameTimerWidgetProvider.class));

		for (int i = 0 ; i < appWidgetIds.length ; i++) {
			final RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.main);

			final Intent intent = new Intent(context, GameTimerListActivity.class);
			final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mainView.setOnClickPendingIntent(R.id.background_view, pendingIntent);

			final int activeNotifyCount = DrawUtil.getActiveTimerCount(context);
			DrawUtil.drawActiveTimerCount(mainView, activeNotifyCount);

			appWidgetManager.updateAppWidget(appWidgetIds[i], mainView);
		}
	}
}
