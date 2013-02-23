/**
 *
 */
package jp.neap.gametimer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;


public class GameTimerSettingsBean {

	// 今日または明日(自動計算)
    public static final int DAY_CALC_AUTO = 0;

	// 明後日
    public static final int DAY_CALC_AFTER_2 = 1;

	// 明明後日
    public static final int DAY_CALC_AFTER_3 = 2;

	// グリー
    public static final int SNSTYPE_GREE = 0;

	// モバゲー
    public static final int SNSTYPE_MOBGA = 1;

	// その他
    public static final int SNSTYPE_OTHER = 999;

	private final int id; 
	
	private final String notifyText;

	// 通知日時(ミリ秒)
	private final long notifyDateTime;

	private final int laterMinute;

	private final int laterHourMinuteHour;

	private final int laterHourMinuteMinute;

	private final int atTimeDay;

	// 「○時○分」の時の選択インデックス。0～24。0は「-」を示す。
	private final int atTimeHour;

	private final int atTimeMinute;

	private final int snsType;

	private final int sortOrder;

	private final String textDaysLater;

	private final String textMinutesLater;

	private final String textHours;

	private Boolean isActiveSchedule = null;

	/**
	 * 値を直接指定して構築するコンストラクタ
	 */
	public GameTimerSettingsBean(
				int id,
				String notifyText,
				long notifyDateTime,
				int laterMinute,
				int laterHourMinuteHour,
				int laterHourMinuteMinute,
				int atTimeDay,
				int atTimeHour,
				int atTimeMinute,
				int snsType,
				int sortOrder,
				String textDaysLater,
				String textMinutesLater,
				String textHours) {
		this.id = id;
		this.notifyText = notifyText;
		this.notifyDateTime = notifyDateTime;
		this.laterMinute = laterMinute;
		this.laterHourMinuteHour = laterHourMinuteHour;
		this.laterHourMinuteMinute = laterHourMinuteMinute;
		this.atTimeDay = atTimeDay;
		this.atTimeHour = atTimeHour;
		this.atTimeMinute = atTimeMinute;
		this.snsType = snsType;
		this.sortOrder = sortOrder;
		this.textDaysLater = textDaysLater;
		this.textMinutesLater = textMinutesLater; 
		this.textHours = textHours;
	}

	/**
	 * UIで入力した値から構築するコンストラクタ
	 *
	 * @param id  ID値
	 * @param notifyText  お知らせ内容
	 * @param laterMinuteText  「○分後」の分の入力値
	 * @param laterHourMinuteHourText  「○時○分後」の時の入力値
	 * @param laterHourMinuteMinuteListIndex  「○時○分後」の分のリスト選択インデックス(-が0)
	 * @param atTimeDayListIndex 日計算のリスト選択インデックス
	 * @param atTimeHourListIndex  「○時○分」の時のリスト選択インデックス(-が0)
	 * @param atTimeMinuteListIndex  「○時○分」の分のリスト選択インデックス
	 * @param snsTypeIndex  SNSの種類のリスト選択インデックス
	 *
	 */
	public GameTimerSettingsBean(
			int id,
			String notifyText,
			String laterMinuteText,
			String laterHourMinuteHourText,
			int laterHourMinuteMinuteListIndex,
			int atTimeDayListIndex,
			int atTimeHourListIndex,
			int atTimeMinuteListIndex,
			int snsTypeIndex,
			int sortOrder,
			String textDaysLater,
			String textMinutesLater,
			String textHours) {

		this.id = id;
		this.notifyText = notifyText;
		this.snsType = GameTimerSettingsBean.snsTypeListIndexToSnsTypeValue(snsTypeIndex);
		this.sortOrder = sortOrder;
		this.textDaysLater = textDaysLater;
		this.textMinutesLater = textMinutesLater; 
		this.textHours = textHours;

		if (!"".equals(notifyText)) {
			int laterMinuteCandidate = StringUtil.toIntegerNumber(laterMinuteText, 0, 99999999);
			if ( laterMinuteCandidate > 0 ) {
				// 「○分後」に決定
				this.laterMinute = laterMinuteCandidate;
				this.laterHourMinuteHour = 0;
				this.laterHourMinuteMinute = 0;
				this.atTimeDay = 0;
				this.atTimeHour = 0;
				this.atTimeMinute = 0;
				// 通知日時を計算する
				this.notifyDateTime = new Date().getTime() + (this.laterMinute * 60L * 1000L); 
//				this.notifyDateTime = new Date().getTime() + (15L * 1000L); 
				return;
			}

			int laterHourMinuteHourCandidate = StringUtil.toIntegerNumber(laterHourMinuteHourText, 0, 23);
			if ((laterHourMinuteHourCandidate > 0) &&
				((laterHourMinuteMinuteListIndex >= 0) && (laterHourMinuteMinuteListIndex <= 59))) {
				// 「○時間○分後」に決定
				this.laterMinute = 0;
				this.laterHourMinuteHour = laterHourMinuteHourCandidate;
				this.laterHourMinuteMinute = laterHourMinuteMinuteListIndex;
				this.atTimeDay = 0;
				this.atTimeHour = 0;
				this.atTimeMinute = 0;
				// 通知日時を計算する
				this.notifyDateTime = new Date().getTime() + (this.laterHourMinuteHour * 3600L * 1000L) + (this.laterHourMinuteMinute * 60L * 1000L); 
				return;
			}
			if ((laterHourMinuteHourCandidate == 0) &&
				((laterHourMinuteMinuteListIndex > 0) && (laterHourMinuteMinuteListIndex <= 59))) {
				// 「0時間○分後」に決定
				this.laterMinute = 0;
				this.laterHourMinuteHour = laterHourMinuteHourCandidate;
				this.laterHourMinuteMinute = laterHourMinuteMinuteListIndex;
				this.atTimeDay = 0;
				this.atTimeHour = 0;
				this.atTimeMinute = 0;
				// 通知日時を計算する
				this.notifyDateTime = new Date().getTime() + (this.laterHourMinuteHour * 3600L * 1000L) + (this.laterHourMinuteMinute * 60L * 1000L); 
				return;
			}

			if (((atTimeDayListIndex >= 0) && (atTimeDayListIndex <= 2)) &&
				((atTimeHourListIndex >= 1) && (atTimeHourListIndex <= 24)) &&
				((atTimeMinuteListIndex >= 0) && (atTimeMinuteListIndex <= 59))) {
				// 「0時○分」に決定
				this.laterMinute = 0;
				this.laterHourMinuteHour = 0;
				this.laterHourMinuteMinute = 0;
				this.atTimeDay = atTimeDayListIndex;
				this.atTimeHour = atTimeHourListIndex;
				this.atTimeMinute = atTimeMinuteListIndex;
				// 通知日時を計算する
				final Calendar nowDate = Calendar.getInstance();
				nowDate.setTimeInMillis(new Date().getTime());
				final Calendar notifyDate = Calendar.getInstance();
				notifyDate.set(Calendar.HOUR_OF_DAY, this.atTimeHour - 1);
				notifyDate.set(Calendar.MINUTE, this.atTimeMinute);
				notifyDate.set(Calendar.SECOND, 0);
				notifyDate.set(Calendar.MILLISECOND, 0);
				long notifyDateTimeCandidate = notifyDate.getTimeInMillis();
				if ( this.atTimeDay == DAY_CALC_AUTO ) {
					// 今日または明日の自動計算
					if ( notifyDateTimeCandidate <= nowDate.getTimeInMillis() ) {
						// 明日
						final Calendar c = Calendar.getInstance();
						c.setTimeInMillis(notifyDateTimeCandidate);
						c.add(Calendar.HOUR_OF_DAY, 24);
						this.notifyDateTime = c.getTimeInMillis();
					}
					else {
						// 今日
						this.notifyDateTime = notifyDateTimeCandidate;
					}
				}
				else {
					// 明後日以降
					this.notifyDateTime = notifyDateTimeCandidate + ((long)(this.atTimeDay + 1) * 86400L * 1000L);
				}
				return;
			}
		}

		// 通知日時は指定しない。
		this.laterMinute = 0;
		this.laterHourMinuteHour = 0;
		this.laterHourMinuteMinute = 0;
		this.atTimeDay = 0;
		this.atTimeHour = 0;
		this.atTimeMinute = 0;
		this.notifyDateTime = 0;
	}
	
	public int id() { return id; }

	public String notifyText() { return notifyText; }

	public long notifyDateTime() { return notifyDateTime; } 

	public int laterMinute() { return laterMinute; } 

	public int laterHourMinuteHour() { return laterHourMinuteHour; } 

	public int laterHourMinuteMinute() { return laterHourMinuteMinute; } 

	public int atTimeDay() { return atTimeDay; } 

	public int atTimeHour() { return atTimeHour; } 

	public int atTimeMinute() { return atTimeMinute; } 

	public int snsType() { return snsType; } 

	public int sortOrder() { return sortOrder; } 

	public String textDaysLater() { return textDaysLater; };

	public String textMinutesLater() { return textMinutesLater; };

	public String textHours() { return textHours; };
	
	public int getNotifyDateTimeRadioId() {
		if ( laterMinute > 0 ) {
			return R.id.radioLaterMinute;
		}
		if ( laterHourMinuteHour > 0 ) {
			return R.id.radioLaterHourMinute;
		}
		if ( (laterHourMinuteHour == 0) && (laterHourMinuteMinute > 0) ) {
			return R.id.radioLaterHourMinute;
		}
		if ( atTimeHour > 0 ) {
			return R.id.radioAtTime;
		}
		return R.id.radioLaterMinute;
	}

	/**
	 * snsType → リストボックスの位置
	 */
	public int snsTypeListIndex() {
		switch (snsType) {
		case SNSTYPE_GREE:
			return 1;
		case SNSTYPE_MOBGA:
			return 2;
		case SNSTYPE_OTHER:
			return 0;
		}
		return 0;
	}

	private static int snsTypeListIndexToSnsTypeValue(int listIndex) {
		switch (listIndex) {
		case 0:
			return SNSTYPE_OTHER;
		case 1:
			return SNSTYPE_GREE;
		case 2:
			return SNSTYPE_MOBGA;
		}
		return SNSTYPE_OTHER;
	}

	public String getLabel(Context context) {
		if ("".equals(notifyText)) {
			return context.getString(R.string.undefined);
		}
		if ( notifyDateTime > 0 ) {
			final Calendar c = Calendar.getInstance();
			c.setTimeInMillis(notifyDateTime);
			final String dateText = String.format("%d/%d %02d:%02d",
							c.get(Calendar.MONTH)+1,
							c.get(Calendar.DAY_OF_MONTH),
							c.get(Calendar.HOUR_OF_DAY),
							c.get(Calendar.MINUTE));
			final String buttonText;
			if ( isActiveSchedule(context) ) {
				buttonText = notifyText + "<br>" + dateText;
			}
			else {			
				buttonText = notifyText + "<br>" + "<font color=red>" + dateText + "</font>";
			}
			return buttonText;
		}
		return notifyText;
	}

	public boolean isSetSchedule() {
		return (laterMinute > 0) ||
			   (laterHourMinuteHour > 0) ||
			   (laterHourMinuteMinute > 0) ||
			   (atTimeDay > 0) ||
			   (atTimeHour > 0) ||
			   (atTimeMinute > 0) ||
			   (notifyDateTime > 0);
	}

	public boolean isActiveSchedule(Context context) {
		if ( isActiveSchedule != null ) {
			return isActiveSchedule.booleanValue();
		}

		if ( notifyDateTime > 0 ) {
			final Intent raiseIntent = new Intent(context, GameTimerRaiseNotification.class);
			raiseIntent.setAction(GameTimerRaiseNotification.GAMETIMER_RAISE_NOTIFICATION_BASE + id);
			final PendingIntent sender = PendingIntent.getBroadcast(context, 0, raiseIntent, PendingIntent.FLAG_NO_CREATE);
			if ( sender == null ) {
				isActiveSchedule = new Boolean(false);
			}
			else {
				if ( notifyDateTime <= new Date().getTime() ) {
					isActiveSchedule = new Boolean(false);
				}
				else {
					isActiveSchedule = new Boolean(true);
				}
			}
		}
		else {
			isActiveSchedule = new Boolean(false);
		}

		return isActiveSchedule.booleanValue();
	}

	public String getNotifyDateExpression() {
		if ( laterMinute > 0 ) {
			// ○分後
			return laterMinute + textMinutesLater;
		}
		if ( (laterHourMinuteHour > 0) ||
			 ((laterHourMinuteHour == 0) && (laterHourMinuteMinute > 0)) ) {
			// ○時間○分後
			return laterHourMinuteHour + textHours + laterHourMinuteMinute + textMinutesLater;
		}
		if ( atTimeHour > 0 ) {
			final StringBuffer buffer = new StringBuffer();
			if ( atTimeDay > DAY_CALC_AUTO ) {
				// ○日後
				buffer.append((atTimeDay + 1))
					  .append(textDaysLater);
			}
			buffer.append(String.format("%02d:%02d", atTimeHour - 1, atTimeMinute));
			return buffer.toString();
		}
		// ○分後
		return laterMinute + textMinutesLater;
	}
}