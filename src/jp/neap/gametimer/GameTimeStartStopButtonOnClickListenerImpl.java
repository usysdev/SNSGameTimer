package jp.neap.gametimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameTimeStartStopButtonOnClickListenerImpl implements
		OnClickListener {

	private final int beanId;

	private final Button settingButton;

	private final Button startStopButton;

	public GameTimeStartStopButtonOnClickListenerImpl(int beanId, Button settingButton, Button startStopButton) {
		// TODO Auto-generated constructor stub
		this.beanId = beanId;
		this.settingButton = settingButton;
		this.startStopButton = startStopButton;
	}

	@Override
	public void onClick(View buttonView) {
		// TODO Auto-generated method stub
        final GameTimerSettingsBean settingsBean;

        {
			GameTimerDBHelper dbHelper = new GameTimerDBHelper(buttonView.getContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				settingsBean = dbHelper.getRecordById(db, beanId);
			}
			finally {
				db.close();
			}
        }

		if ( !settingsBean.isSetSchedule() ) {
			return;
		}
		if ( settingsBean.isActiveSchedule(buttonView.getContext()) ) {
			return;
		}

		// ���݂̐ݒ���e�Œʒm�������Z�b�g����B
		{
			final GameTimerSettingsBean newSettingsBean = new GameTimerSettingsBean(
						settingsBean.id(),
						settingsBean.notifyText(),
						String.valueOf(settingsBean.laterMinute()),
						String.valueOf(settingsBean.laterHourMinuteHour()),
						settingsBean.laterHourMinuteMinute(),
						settingsBean.atTimeDay(),
						settingsBean.atTimeHour(),
       		        	settingsBean.atTimeMinute(),
       		        	GameTimerSettingsBean.snsTypeValueToSnsTypeListIndex(settingsBean.snsType(), StringUtil.isJP(buttonView.getContext())),
        				settingsBean.sortOrder(),
        				settingsBean.snsUrl(),
        				settingsBean.textDaysLater(),
        				settingsBean.textMinutesLater(),
        				settingsBean.textHours(),
        				StringUtil.isJP(buttonView.getContext()));

			{
				GameTimerDBHelper dbHelper = new GameTimerDBHelper(buttonView.getContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
       		    SQLiteDatabase db = dbHelper.getReadableDatabase();
				try {
        			dbHelper.updateRecord(db, newSettingsBean);
				}
				finally {
        	    	db.close();
				}
    		}
			
			// �C���e���g���X�P�W���[������B
       	    {
       	    	final Intent raiseIntent = new Intent(buttonView.getContext(), GameTimerRaiseNotification.class);
       	    	final String raiseAction = GameTimerRaiseNotification.GAMETIMER_RAISE_NOTIFICATION_BASE + newSettingsBean.id();
        	    raiseIntent.setAction(raiseAction);

   	    	    if (newSettingsBean.notifyDateTime() > 0) {
					// �t������^����
					raiseIntent.putExtra("beanId", newSettingsBean.id());
					raiseIntent.putExtra("notifyText", newSettingsBean.notifyText());
					raiseIntent.putExtra("snsType", newSettingsBean.snsType());
					raiseIntent.putExtra("snsUrl", newSettingsBean.snsUrl());
					
					if (Logger.isDebugEnabled()) {
						Logger.debug("setNoritication:beanId=" + newSettingsBean.id() + ",notifyText=" + newSettingsBean.notifyText() + ",snsType=" + newSettingsBean.snsType());
					}

   	    	    	final PendingIntent sender = PendingIntent.getBroadcast(buttonView.getContext(), 0, raiseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
       		    	final AlarmManager alarmManager = (AlarmManager)(buttonView.getContext().getSystemService(Context.ALARM_SERVICE));
					alarmManager.set(AlarmManager.RTC_WAKEUP, newSettingsBean.notifyDateTime(), sender);

					if (Logger.isDebugEnabled()) {
						java.util.Date d = new java.util.Date();
						d.setTime(newSettingsBean.notifyDateTime());
						Logger.debug("action=" + raiseAction + ",WAKEUP=" + d.toString());
					}
        	    }
   	    	    else {
       		    	final PendingIntent sender = PendingIntent.getBroadcast(buttonView.getContext(), 0, raiseIntent, PendingIntent.FLAG_NO_CREATE);
       		    	if (sender != null) {
						// �C���e���g���L�����Z������
       	    			sender.cancel();

						// �A���[���}�l�[�W��������폜����
        	    		final AlarmManager alarmManager = (AlarmManager)(buttonView.getContext().getSystemService(Context.ALARM_SERVICE));
						alarmManager.cancel(sender);

						if (Logger.isDebugEnabled()) {
							java.util.Date d = new java.util.Date();
							d.setTime(newSettingsBean.notifyDateTime());
							Logger.debug("action=" + raiseAction + ",CANCEL=" + d.toString());
						}
       	    		}
       	    	}
       	    }
       		// �ꗗ�̃{�^���L���v�V�������X�V����B
       		settingButton.setText(Html.fromHtml(newSettingsBean.getLabel(buttonView.getContext())));
		}

		// �J�n�{�^�����X�V����B
		startStopButton.setVisibility(View.INVISIBLE);

		// �E�B�W�F�b�g�̃A�C�R���摜���X�V����B
		DrawUtil.updateWidgetActiveTimerCount(buttonView.getContext());
	}
}
