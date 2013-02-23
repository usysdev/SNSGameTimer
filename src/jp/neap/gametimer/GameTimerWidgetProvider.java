/**
 * �z�[����ʂɏ풓����Q�[���^�C�}�[��Widget
 */
package jp.neap.gametimer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 *
 */
public class GameTimerWidgetProvider extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		// �ŏ��̃E�B�W�F�b�g���z�u���ꂽ�Ƃ��ɌĂ΂��
		if ( Logger.isDebugEnabled() ) {
			Logger.debug("GameTimerWidgetProvider::onEnabled");
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// �E�B�W�F�b�g���z�u����邽�тɌĂ΂��
		if ( Logger.isDebugEnabled() ) {
			Logger.debug("GameTimerWidgetProvider::onUpdate");
		}

		for (int i = 0 ; i < appWidgetIds.length ; i++) {
			final RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.main);

			final Intent intent = new Intent(context, GameTimerListActivity.class);
			final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mainView.setOnClickPendingIntent(R.id.background_view, pendingIntent);

			final int activeNotifyCount = DrawUtil.getActiveTimerCount(context);
			DrawUtil.drawActiveTimerCount(mainView, activeNotifyCount);

			appWidgetManager.updateAppWidget(appWidgetIds[i], mainView);
		}

		{
			final Intent widgetServiceIntent = new Intent(context, GameTimerWidgetService.class);
			context.startService(widgetServiceIntent);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);

		// �E�B�W�F�b�g���폜����邲�ƂɌĂ΂��
		if ( Logger.isDebugEnabled() ) {
			Logger.debug("GameTimerWidgetProvider::onDeleted");
		}
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		// �Ō�̃E�B�W�F�b�g���폜���ꂽ��Ă΂��
		if ( Logger.isDebugEnabled() ) {
			Logger.debug("GameTimerWidgetProvider::onDisabled");
		}

		final Intent widgetServiceIntent = new Intent(context, GameTimerWidgetService.class);
		context.stopService(widgetServiceIntent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
}
