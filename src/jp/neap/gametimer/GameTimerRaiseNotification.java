/**
 * ステータスバー通知クラス
 */
package jp.neap.gametimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import java.io.File;


/**
 *
 */
public class GameTimerRaiseNotification extends BroadcastReceiver {

	public static final String GAMETIMER_RAISE_NOTIFICATION_BASE = "jp.neap.gametimer.GAMETIMER_RAISE_NOTIFICATION_";

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if ( Logger.isDebugEnabled() ) {
			Logger.debug("action=" + action);
		}

		// 付加情報を取得する。
		final int beanId = intent.getIntExtra("beanId", 0);
		final String notifyText = intent.getStringExtra("notifyText");
		final int snsType = intent.getIntExtra("snsType", GameTimerSettingsBean.SNSTYPE_OTHER);

		if (Logger.isDebugEnabled()) {
			Logger.debug("raiseNoritication:beanId=" + beanId + ",notifyText=" + notifyText + ",snsType=" + snsType);
		}

		// エラーチェック。
		if ( beanId == 0 ) {
			return;
		}

		final String audioFileFullPath;
		final GameTimerDBHelper dbHelper = new GameTimerDBHelper(context.getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			audioFileFullPath = dbHelper.getProperty(db, "audioFileFullPath", "");
		}
		finally {
			db.close();
		}			

		final boolean isValidAudioFileFullPath;
		if ( "".equals(audioFileFullPath) ) {
			isValidAudioFileFullPath = false;
		}
		else {
			final File f = new File(audioFileFullPath);
			isValidAudioFileFullPath = f.isFile();
		}

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		String snsUri = "";
		switch (snsType) {
		case GameTimerSettingsBean.SNSTYPE_GREE:
			snsUri = "http://tgames.gree.jp/";
			break;
		case GameTimerSettingsBean.SNSTYPE_MOBGA:
			snsUri = "http://sp.mbga.jp/";
			break;
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("raiseNoritication:snsUri=" + snsUri);
		}
 
		final Intent snsIntent;
		if ( "".equals(snsUri) ) {
			snsIntent = new Intent(context.getApplicationContext(), GameTimerListActivity.class);
			snsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		else {
			snsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(snsUri));
			snsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		// PendingIntent.FLAG_CANCEL_CURRENT だと、「このアプリ」のとき、ウィジェットに貼り付けた GameTimerListActivity のインテントをキャンセルしてしまい、
		// ウィジェットをクリックしても一覧が開かなくなる。
		PendingIntent pendingSnsIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, snsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
 
		notification.icon = R.drawable.icon;
		notification.tickerText = "ゲームタイマーからのお知らせです";
		notification.number = 1;
		notification.flags = notification.flags | Notification.FLAG_AUTO_CANCEL;

		if ( isValidAudioFileFullPath ) {
			notification.audioStreamType = AudioManager.STREAM_NOTIFICATION;
			notification.sound = Uri.parse("file://" + audioFileFullPath);
//			notification.sound = Uri.parse("android.resource://jp.neap.gametimer/" + R.raw.schoolchime);
		}
		else {
			notification.defaults = notification.defaults | Notification.DEFAULT_SOUND;
		}

		// settingsBean.id() が通知単位となる。
		notification.setLatestEventInfo(context.getApplicationContext(), "GameTimer", notifyText, pendingSnsIntent);
		notificationManager.notify(beanId, notification);

		// ウィジェットのアイコン画像を更新する。
		DrawUtil.updateWidgetActiveTimerCount(context);
	}
}
