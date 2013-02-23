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

	// �����܂��͖���(�����v�Z)
    public static final int DAY_CALC_AUTO = 0;

	// �����
    public static final int DAY_CALC_AFTER_2 = 1;

	// �������
    public static final int DAY_CALC_AFTER_3 = 2;

	// �O���[
    public static final int SNSTYPE_GREE = 0;

	// ���o�Q�[
    public static final int SNSTYPE_MOBGA = 1;

	// ���̑�
    public static final int SNSTYPE_OTHER = 999;

	private final int id; 
	
	private final String notifyText;

	// �ʒm����(�~���b)
	private final long notifyDateTime;

	private final int laterMinute;

	private final int laterHourMinuteHour;

	private final int laterHourMinuteMinute;

	private final int atTimeDay;

	// �u���������v�̎��̑I���C���f�b�N�X�B0�`24�B0�́u-�v�������B
	private final int atTimeHour;

	private final int atTimeMinute;

	private final int snsType;

	private final int sortOrder;

	private final String textDaysLater;

	private final String textMinutesLater;

	private final String textHours;

	private Boolean isActiveSchedule = null;

	/**
	 * �l�𒼐ڎw�肵�č\�z����R���X�g���N�^
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
	 * UI�œ��͂����l����\�z����R���X�g���N�^
	 *
	 * @param id  ID�l
	 * @param notifyText  ���m�点���e
	 * @param laterMinuteText  �u������v�̕��̓��͒l
	 * @param laterHourMinuteHourText  �u����������v�̎��̓��͒l
	 * @param laterHourMinuteMinuteListIndex  �u����������v�̕��̃��X�g�I���C���f�b�N�X(-��0)
	 * @param atTimeDayListIndex ���v�Z�̃��X�g�I���C���f�b�N�X
	 * @param atTimeHourListIndex  �u���������v�̎��̃��X�g�I���C���f�b�N�X(-��0)
	 * @param atTimeMinuteListIndex  �u���������v�̕��̃��X�g�I���C���f�b�N�X
	 * @param snsTypeIndex  SNS�̎�ނ̃��X�g�I���C���f�b�N�X
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
				// �u������v�Ɍ���
				this.laterMinute = laterMinuteCandidate;
				this.laterHourMinuteHour = 0;
				this.laterHourMinuteMinute = 0;
				this.atTimeDay = 0;
				this.atTimeHour = 0;
				this.atTimeMinute = 0;
				// �ʒm�������v�Z����
				this.notifyDateTime = new Date().getTime() + (this.laterMinute * 60L * 1000L); 
//				this.notifyDateTime = new Date().getTime() + (15L * 1000L); 
				return;
			}

			int laterHourMinuteHourCandidate = StringUtil.toIntegerNumber(laterHourMinuteHourText, 0, 23);
			if ((laterHourMinuteHourCandidate > 0) &&
				((laterHourMinuteMinuteListIndex >= 0) && (laterHourMinuteMinuteListIndex <= 59))) {
				// �u�����ԁ�����v�Ɍ���
				this.laterMinute = 0;
				this.laterHourMinuteHour = laterHourMinuteHourCandidate;
				this.laterHourMinuteMinute = laterHourMinuteMinuteListIndex;
				this.atTimeDay = 0;
				this.atTimeHour = 0;
				this.atTimeMinute = 0;
				// �ʒm�������v�Z����
				this.notifyDateTime = new Date().getTime() + (this.laterHourMinuteHour * 3600L * 1000L) + (this.laterHourMinuteMinute * 60L * 1000L); 
				return;
			}
			if ((laterHourMinuteHourCandidate == 0) &&
				((laterHourMinuteMinuteListIndex > 0) && (laterHourMinuteMinuteListIndex <= 59))) {
				// �u0���ԁ�����v�Ɍ���
				this.laterMinute = 0;
				this.laterHourMinuteHour = laterHourMinuteHourCandidate;
				this.laterHourMinuteMinute = laterHourMinuteMinuteListIndex;
				this.atTimeDay = 0;
				this.atTimeHour = 0;
				this.atTimeMinute = 0;
				// �ʒm�������v�Z����
				this.notifyDateTime = new Date().getTime() + (this.laterHourMinuteHour * 3600L * 1000L) + (this.laterHourMinuteMinute * 60L * 1000L); 
				return;
			}

			if (((atTimeDayListIndex >= 0) && (atTimeDayListIndex <= 2)) &&
				((atTimeHourListIndex >= 1) && (atTimeHourListIndex <= 24)) &&
				((atTimeMinuteListIndex >= 0) && (atTimeMinuteListIndex <= 59))) {
				// �u0�������v�Ɍ���
				this.laterMinute = 0;
				this.laterHourMinuteHour = 0;
				this.laterHourMinuteMinute = 0;
				this.atTimeDay = atTimeDayListIndex;
				this.atTimeHour = atTimeHourListIndex;
				this.atTimeMinute = atTimeMinuteListIndex;
				// �ʒm�������v�Z����
				final Calendar nowDate = Calendar.getInstance();
				nowDate.setTimeInMillis(new Date().getTime());
				final Calendar notifyDate = Calendar.getInstance();
				notifyDate.set(Calendar.HOUR_OF_DAY, this.atTimeHour - 1);
				notifyDate.set(Calendar.MINUTE, this.atTimeMinute);
				notifyDate.set(Calendar.SECOND, 0);
				notifyDate.set(Calendar.MILLISECOND, 0);
				long notifyDateTimeCandidate = notifyDate.getTimeInMillis();
				if ( this.atTimeDay == DAY_CALC_AUTO ) {
					// �����܂��͖����̎����v�Z
					if ( notifyDateTimeCandidate <= nowDate.getTimeInMillis() ) {
						// ����
						final Calendar c = Calendar.getInstance();
						c.setTimeInMillis(notifyDateTimeCandidate);
						c.add(Calendar.HOUR_OF_DAY, 24);
						this.notifyDateTime = c.getTimeInMillis();
					}
					else {
						// ����
						this.notifyDateTime = notifyDateTimeCandidate;
					}
				}
				else {
					// ������ȍ~
					this.notifyDateTime = notifyDateTimeCandidate + ((long)(this.atTimeDay + 1) * 86400L * 1000L);
				}
				return;
			}
		}

		// �ʒm�����͎w�肵�Ȃ��B
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
	 * snsType �� ���X�g�{�b�N�X�̈ʒu
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
			// ������
			return laterMinute + textMinutesLater;
		}
		if ( (laterHourMinuteHour > 0) ||
			 ((laterHourMinuteHour == 0) && (laterHourMinuteMinute > 0)) ) {
			// �����ԁ�����
			return laterHourMinuteHour + textHours + laterHourMinuteMinute + textMinutesLater;
		}
		if ( atTimeHour > 0 ) {
			final StringBuffer buffer = new StringBuffer();
			if ( atTimeDay > DAY_CALC_AUTO ) {
				// ������
				buffer.append((atTimeDay + 1))
					  .append(textDaysLater);
			}
			buffer.append(String.format("%02d:%02d", atTimeHour - 1, atTimeMinute));
			return buffer.toString();
		}
		// ������
		return laterMinute + textMinutesLater;
	}
}