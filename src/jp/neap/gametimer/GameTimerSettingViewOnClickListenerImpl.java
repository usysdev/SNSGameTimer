/**
 * �Q�[���^�C�}�[�ꗗ��ʂɔz�u���Ă���{�^���̃N���b�N�C�x���g�n���h���B
 */
package jp.neap.gametimer;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * �Q�[���^�C�}�[�ꗗ��ʂɔz�u���Ă���{�^���̃N���b�N�C�x���g�n���h���B
 */
public class GameTimerSettingViewOnClickListenerImpl implements
		View.OnClickListener {

	private final int beanId;

	private final Button clickedButton;

	private final Button startStopButton;

	public GameTimerSettingViewOnClickListenerImpl(int beanId, Button clickedButton, Button startStopButton) {
		this.beanId = beanId;
		this.clickedButton = clickedButton;
		this.startStopButton = startStopButton;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View buttonView) {
		// �|�b�v�A�b�v�_�C�A���O�p�ɐV�����r���[���쐬����B
		LayoutInflater inflater = LayoutInflater.from(buttonView.getContext());
        final View dialogView = inflater.inflate(R.layout.gametimer_setting, null);

        final GameTimerSettingsBean settingsBean;

        {
			GameTimerDBHelper dbHelper = new GameTimerDBHelper(dialogView.getContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				settingsBean = dbHelper.getRecordById(db, beanId);
			}
			finally {
				db.close();
			}
        }

        // �ʒm���e
        {
        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editNotifyText);
       		editBox.setText(settingsBean.notifyText());
        }
        // ��������
        {
        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLaterMinute);
        	if (settingsBean.laterMinute()>0) {
        		editBox.setText(String.valueOf(settingsBean.laterMinute()));
        	}
        }
        // �������ԁ�������
        {
        	{
	        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLaterHourMinuteHour);
	        	if (settingsBean.laterHourMinuteHour()>0) {
	        		editBox.setText(String.valueOf(settingsBean.laterHourMinuteHour()));
	        	}
        	}
        	{
				Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerLaterHourMinuteMinute);
				spinner.setSelection(settingsBean.laterHourMinuteMinute());
        	}
        }
        // ��������������
        {
        	{
				Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerAtTimeHour);
				spinner.setSelection(settingsBean.atTimeHour());
        	}
        	{
				Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerAtTimeMinute);
				spinner.setSelection(settingsBean.atTimeMinute());
        	}
        	{
				Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerAtTimeDay);
				spinner.setSelection(settingsBean.atTimeDay());
        	}
        }

		// SNS�̎��
        {
			Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinnerSnsType);
			spinner.setSelection(settingsBean.snsTypeListIndex(StringUtil.isJP(dialogView.getContext())));
			spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

				/* (non-Javadoc)
				 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
				 */
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					final Spinner spinnerTarget = (Spinner)arg0;
					if (spinnerTarget.getSelectedItemPosition() == GameTimerSettingsBean.snsTypeValueToSnsTypeListIndex(GameTimerSettingsBean.SNSTYPE_ANY_URL, StringUtil.isJP(arg1.getContext()))) {
			        	dialogView.findViewById(R.id.tableRow_snsUrlTitle).setVisibility(View.VISIBLE);
			        	dialogView.findViewById(R.id.tableRow_snsUrl).setVisibility(View.VISIBLE);
					}
					else {
			        	dialogView.findViewById(R.id.tableRow_snsUrlTitle).setVisibility(View.GONE);
			        	dialogView.findViewById(R.id.tableRow_snsUrl).setVisibility(View.GONE);
					}
				}

				/* (non-Javadoc)
				 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
				 */
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
        }

        // �w�肵��URL
        {
        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editSnsUrl);
        	editBox.setText(settingsBean.snsUrl());
        }
        // �w�肵��URL�̕\���E��\��
        if (settingsBean.snsType() == GameTimerSettingsBean.SNSTYPE_ANY_URL) {
        	dialogView.findViewById(R.id.tableRow_snsUrlTitle).setVisibility(View.VISIBLE);
        	dialogView.findViewById(R.id.tableRow_snsUrl).setVisibility(View.VISIBLE);
        }
        else {
        	dialogView.findViewById(R.id.tableRow_snsUrlTitle).setVisibility(View.GONE);
        	dialogView.findViewById(R.id.tableRow_snsUrl).setVisibility(View.GONE);
        }
        
        // �ÓI�e�L�X�g
        {
			final TextView textNotifyTextView = (TextView)dialogView.findViewById(R.id.textNotifyText);
			textNotifyTextView.setText(settingsBean.notifyText());

			final TextView textNotifyDateTextView = (TextView)dialogView.findViewById(R.id.textNotifyDateTime);
			textNotifyDateTextView.setText(settingsBean.getNotifyDateExpression());

			final TextView textNotifyLinkView = (TextView)dialogView.findViewById(R.id.textNotifyLink);
			switch (settingsBean.snsType()) {
			case GameTimerSettingsBean.SNSTYPE_GREE:
				textNotifyLinkView.setText(buttonView.getContext().getString(R.string.sns_name_gree));
				break;
			case GameTimerSettingsBean.SNSTYPE_MOBGA:
				textNotifyLinkView.setText(buttonView.getContext().getString(R.string.sns_name_mobage));
				break;
			case GameTimerSettingsBean.SNSTYPE_ANY_URL:
				textNotifyLinkView.setText(settingsBean.snsUrl());
				break;
			default:
				textNotifyLinkView.setText(buttonView.getContext().getString(R.string.this_application));
			}
        }

		// ���m�点�����ݒ��UI��I������
        {
			// �\���E��\���u���b�N��ݒ肷��
			final int notifyDateTimeSelectorId = settingsBean.getNotifyDateTimeRadioId();
			final View notifyLaterMinutesView = dialogView.findViewById(R.id.tableLayout_notifyLaterMinutes);
			final View notifyLaterHourMinuteView = dialogView.findViewById(R.id.tableLayout_notifyLaterHourMinute);
			final View notifyAtTimeHourMinuteView = dialogView.findViewById(R.id.tableLayout_notifyAtTimeHourMinute);
			final View notifyAtTimeHourMinuteDayView = dialogView.findViewById(R.id.tableLayout_notifyAtTimeHourMinute_day);
			switch (notifyDateTimeSelectorId) {
			case R.id.radioLaterMinute:
				notifyLaterMinutesView.setVisibility(View.VISIBLE);
				notifyLaterHourMinuteView.setVisibility(View.GONE);
				notifyAtTimeHourMinuteView.setVisibility(View.GONE);
				notifyAtTimeHourMinuteDayView.setVisibility(View.GONE);
				break;
			case R.id.radioLaterHourMinute:
				notifyLaterMinutesView.setVisibility(View.GONE);
				notifyLaterHourMinuteView.setVisibility(View.VISIBLE);
				notifyAtTimeHourMinuteView.setVisibility(View.GONE);
				notifyAtTimeHourMinuteDayView.setVisibility(View.GONE);
				break;
			case R.id.radioAtTime:
				notifyLaterMinutesView.setVisibility(View.GONE);
				notifyLaterHourMinuteView.setVisibility(View.GONE);
				notifyAtTimeHourMinuteView.setVisibility(View.VISIBLE);
				notifyAtTimeHourMinuteDayView.setVisibility(View.VISIBLE);
				break;
			}
			// ���W�I�{�^����I������
			final RadioGroup radioGroupNotifyTimeSelector = (RadioGroup)dialogView.findViewById(R.id.notifyTimeSelectorGroup);
			radioGroupNotifyTimeSelector.check(notifyDateTimeSelectorId);
			radioGroupNotifyTimeSelector.setOnCheckedChangeListener(new NotifyTimeSelectorImpl(dialogView));
        }

		final String positiveButtonLabel = !settingsBean.isActiveSchedule(dialogView.getContext()) ?
				dialogView.getContext().getString(R.string.timer_start) :
				dialogView.getContext().getString(R.string.timer_stop);

		if (!settingsBean.isActiveSchedule(dialogView.getContext())) {
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyText);
				view.setVisibility(View.VISIBLE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyDateTimeLabel);
				view.setVisibility(View.VISIBLE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_sns);
				view.setVisibility(View.VISIBLE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_showInfo);
				view.setVisibility(View.GONE);
			}
		}
		else {
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyText);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyDateTimeLabel);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyLaterMinutes);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyLaterHourMinute);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyAtTimeHourMinute);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_notifyAtTimeHourMinute_day);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_sns);
				view.setVisibility(View.GONE);
			}
			{
				final View view = dialogView.findViewById(R.id.tableLayout_showInfo);
				view.setVisibility(View.VISIBLE);
			}
		}

        new AlertDialog.Builder(buttonView.getContext())
//			.setTitle("�ʒm�ݒ�")	// �^�C�g���u���b�N���ƕ\�����Ȃ�
        	.setIcon(R.drawable.icon)
        	.setView(dialogView)
        	.setPositiveButton(
        		positiveButtonLabel, 
        		new DialogInterface.OnClickListener() {          
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
						if ( !settingsBean.isActiveSchedule(dialogView.getContext()) ) {
	        				// ���̓R���g���[�����擾����B
    	    				final EditText editNotifyText = (EditText)dialogView.findViewById(R.id.editNotifyText);
        		        	final EditText editLaterMinute = (EditText)dialogView.findViewById(R.id.editLaterMinute);
        		        	final EditText editLaterHourMinuteHour = (EditText)dialogView.findViewById(R.id.editLaterHourMinuteHour);
							final EditText editTextSnsUrl = (EditText)dialogView.findViewById(R.id.editSnsUrl);
        		        	final Spinner spinnerLaterHourMinuteMinute = (Spinner)dialogView.findViewById(R.id.spinnerLaterHourMinuteMinute);
        	        		final Spinner spinnerAtTimeHour = (Spinner)dialogView.findViewById(R.id.spinnerAtTimeHour);
							final Spinner spinnerAtTimeMinute = (Spinner)dialogView.findViewById(R.id.spinnerAtTimeMinute);
							final Spinner spinnerAtTimeDay = (Spinner)dialogView.findViewById(R.id.spinnerAtTimeDay);
							final Spinner spinnerSnsType = (Spinner)dialogView.findViewById(R.id.spinnerSnsType);
							final RadioGroup radioGroupNotifyTimeSelector = (RadioGroup)dialogView.findViewById(R.id.notifyTimeSelectorGroup);
							
							String laterMinute = "";
							String laterHourMinuteHour = "";
							int laterHourMinuteMinute = 0;
							int atTimeDay = 0;
							int atTimeHour = 0;
							int atTimeMinute = 0;

							switch (radioGroupNotifyTimeSelector.getCheckedRadioButtonId()) {
							case R.id.radioLaterMinute:
								laterMinute = editLaterMinute.getText().toString();
								break;
							case R.id.radioLaterHourMinute:
								laterHourMinuteHour = editLaterHourMinuteHour.getText().toString();
								laterHourMinuteMinute = spinnerLaterHourMinuteMinute.getSelectedItemPosition();
								break;
							case R.id.radioAtTime:
								atTimeDay = spinnerAtTimeDay.getSelectedItemPosition();
								atTimeHour = spinnerAtTimeHour.getSelectedItemPosition();
								atTimeMinute = spinnerAtTimeMinute.getSelectedItemPosition();
								break;
							}

	        				// �w�肵��URL�ɑ΂��ăX�L�[�}�␳���K�v�ł���Ε␳����B
							String textSnsUrl = editTextSnsUrl.getText().toString();
							if (!textSnsUrl.startsWith("http://") && !textSnsUrl.startsWith("https://")) {
								textSnsUrl = "http://" + textSnsUrl;
							}

							final GameTimerSettingsBean newSettingsBean = new GameTimerSettingsBean(
									// ID
        		        			settingsBean.id(),
        	    	    			// ���m�点���e
        	        				editNotifyText.getText().toString(),
									// ������
	        	        			laterMinute,
    	    	        			// �����ԁ�����́�����
        		        			laterHourMinuteHour,
									// �����ԁ�����́���
									laterHourMinuteMinute,
									// ���������̓�
									atTimeDay,
									// ���������̎�
									atTimeHour,
									// ���������̕�
        		        			atTimeMinute,
        	    	    			// SNS �̎��
        	        				spinnerSnsType.getSelectedItemPosition(),
        	        				// �\�[�g��
        	        				settingsBean.sortOrder(),
        	        				// �w�肵��URL
        	        				textSnsUrl,
        	        				// ������
        	        				dialogView.getContext().getString(R.string.days_later),
        	        				// ������
        	        				dialogView.getContext().getString(R.string.minutes_later),
        	        				// ������
        	        				dialogView.getContext().getString(R.string.hours),
        	        				// ���ꔻ��
        	        				StringUtil.isJP(dialogView.getContext()));

	        	        	{
    	    	        		GameTimerDBHelper dbHelper = new GameTimerDBHelper(dialogView.getContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
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
        	    				final Intent raiseIntent = new Intent(dialogView.getContext(), GameTimerRaiseNotification.class);
        	    				final String raiseAction = GameTimerRaiseNotification.GAMETIMER_RAISE_NOTIFICATION_BASE + newSettingsBean.id();
	        	    			raiseIntent.setAction(raiseAction);

    	    	    			if (newSettingsBean.notifyDateTime() > 0) {
									// �t������^����
									raiseIntent.putExtra("beanId", newSettingsBean.id());
									raiseIntent.putExtra("notifyText", newSettingsBean.notifyText());
									raiseIntent.putExtra("snsType", newSettingsBean.snsType());
									raiseIntent.putExtra("snsUrl", newSettingsBean.snsUrl());

									if (Logger.isDebugEnabled()) {
										Logger.debug("setNoritication:beanId=" + newSettingsBean.id() + ",notifyText=" + newSettingsBean.notifyText() + ",snsType=" + newSettingsBean.snsType() +
											",snsUrl=" + newSettingsBean.snsUrl());
									}

    	    	    				final PendingIntent sender = PendingIntent.getBroadcast(dialogView.getContext(), 0, raiseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        		    				final AlarmManager alarmManager = (AlarmManager)(dialogView.getContext().getSystemService(Context.ALARM_SERVICE));
									alarmManager.set(AlarmManager.RTC_WAKEUP, newSettingsBean.notifyDateTime(), sender);

									if (Logger.isDebugEnabled()) {
										java.util.Date d = new java.util.Date();
										d.setTime(newSettingsBean.notifyDateTime());
										Logger.debug("action=" + raiseAction + ",WAKEUP=" + d.toString());
									}
	        	    			}
    	    	    			else {
        		    				final PendingIntent sender = PendingIntent.getBroadcast(dialogView.getContext(), 0, raiseIntent, PendingIntent.FLAG_NO_CREATE);
        		    				if (sender != null) {
										// �C���e���g���L�����Z������
        	    						sender.cancel();

										// �A���[���}�l�[�W��������폜����
	        	    					final AlarmManager alarmManager = (AlarmManager)(dialogView.getContext().getSystemService(Context.ALARM_SERVICE));
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
	       		        	clickedButton.setText(Html.fromHtml(newSettingsBean.getLabel(dialogView.getContext())));

							// �J�n�{�^�����X�V����B
							startStopButton.setVisibility(View.INVISIBLE);
        	    		}
						else {
							// �ݒ���e�͂��̂܂܂Œʒm�������������Z�b�g����B
    	    	        	final GameTimerSettingsBean newSettingsBean = new GameTimerSettingsBean(
        		        			settingsBean.id(),
        	        				settingsBean.notifyText(),
									0,
									settingsBean.laterMinute(),
        		        			settingsBean.laterHourMinuteHour(),
									settingsBean.laterHourMinuteMinute(),
									settingsBean.atTimeDay(),
									settingsBean.atTimeHour(),
        		        			settingsBean.atTimeMinute(),
									settingsBean.snsType(),
        	        				settingsBean.sortOrder(),
        	        				settingsBean.snsUrl(),
        	        				// ������
        	        				dialogView.getContext().getString(R.string.days_later),
        	        				// ������
        	        				dialogView.getContext().getString(R.string.minutes_later),
        	        				// ������
        	        				dialogView.getContext().getString(R.string.hours));

	        	        	{
    	    	        		GameTimerDBHelper dbHelper = new GameTimerDBHelper(dialogView.getContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
        		    			SQLiteDatabase db = dbHelper.getReadableDatabase();
								try {
	        		    			dbHelper.updateRecord(db, newSettingsBean);
								}
								finally {
	        	    				db.close();
								}
    	    	    		}

        	    			// �C���e���g���L�����Z������B
        	    			{
        	    				final Intent raiseIntent = new Intent(dialogView.getContext(), GameTimerRaiseNotification.class);
        	    				final String raiseAction = GameTimerRaiseNotification.GAMETIMER_RAISE_NOTIFICATION_BASE + newSettingsBean.id();
	        	    			raiseIntent.setAction(raiseAction);
       		    				final PendingIntent sender = PendingIntent.getBroadcast(dialogView.getContext(), 0, raiseIntent, PendingIntent.FLAG_NO_CREATE);
       		    				if (sender != null) {
									// �C���e���g���L�����Z������
       	    						sender.cancel();

									// �A���[���}�l�[�W��������폜����
        	    					final AlarmManager alarmManager = (AlarmManager)(dialogView.getContext().getSystemService(Context.ALARM_SERVICE));
									alarmManager.cancel(sender);
       		    				}
        	    			}

	       		        	// �ꗗ�̃{�^���L���v�V�������X�V����B
	       		        	clickedButton.setText(Html.fromHtml(newSettingsBean.getLabel(dialogView.getContext())));

							// �J�n�{�^�����X�V����B
							startStopButton.setVisibility(View.VISIBLE);
						}

						// �E�B�W�F�b�g�̃A�C�R���摜���X�V����B
						DrawUtil.updateWidgetActiveTimerCount(dialogView.getContext());
        			}
        		})
        	.setNegativeButton(
        			dialogView.getContext().getString(R.string.timer_cancel), 
        		new DialogInterface.OnClickListener() {          
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        			}
        		})
        	.show();
	}
}
