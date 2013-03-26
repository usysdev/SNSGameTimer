/**
 * ゲームタイマー一覧画面に配置しているボタンのクリックイベントハンドラ。
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
 * ゲームタイマー一覧画面に配置しているボタンのクリックイベントハンドラ。
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
		// ポップアップダイアログ用に新しいビューを作成する。
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

        // 通知内容
        {
        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editNotifyText);
       		editBox.setText(settingsBean.notifyText());
        }
        // ○○分後
        {
        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editLaterMinute);
        	if (settingsBean.laterMinute()>0) {
        		editBox.setText(String.valueOf(settingsBean.laterMinute()));
        	}
        }
        // ○○時間○○分後
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
        // ○○時○○分に
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

		// SNSの種類
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

        // 指定したURL
        {
        	final EditText editBox = (EditText)dialogView.findViewById(R.id.editSnsUrl);
        	editBox.setText(settingsBean.snsUrl());
        }
        // 指定したURLの表示・非表示
        if (settingsBean.snsType() == GameTimerSettingsBean.SNSTYPE_ANY_URL) {
        	dialogView.findViewById(R.id.tableRow_snsUrlTitle).setVisibility(View.VISIBLE);
        	dialogView.findViewById(R.id.tableRow_snsUrl).setVisibility(View.VISIBLE);
        }
        else {
        	dialogView.findViewById(R.id.tableRow_snsUrlTitle).setVisibility(View.GONE);
        	dialogView.findViewById(R.id.tableRow_snsUrl).setVisibility(View.GONE);
        }
        
        // 静的テキスト
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

		// お知らせ日時設定のUIを選択する
        {
			// 表示・非表示ブロックを設定する
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
			// ラジオボタンを選択する
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
//			.setTitle("通知設定")	// タイトルブロックごと表示しない
        	.setIcon(R.drawable.icon)
        	.setView(dialogView)
        	.setPositiveButton(
        		positiveButtonLabel, 
        		new DialogInterface.OnClickListener() {          
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
						if ( !settingsBean.isActiveSchedule(dialogView.getContext()) ) {
	        				// 入力コントロールを取得する。
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

	        				// 指定したURLに対してスキーマ補正が必要であれば補正する。
							String textSnsUrl = editTextSnsUrl.getText().toString();
							if (!textSnsUrl.startsWith("http://") && !textSnsUrl.startsWith("https://")) {
								textSnsUrl = "http://" + textSnsUrl;
							}

							final GameTimerSettingsBean newSettingsBean = new GameTimerSettingsBean(
									// ID
        		        			settingsBean.id(),
        	    	    			// お知らせ内容
        	        				editNotifyText.getText().toString(),
									// ○分後
	        	        			laterMinute,
    	    	        			// ○時間○分後の○時間
        		        			laterHourMinuteHour,
									// ○時間○分後の○分
									laterHourMinuteMinute,
									// ○時○分の日
									atTimeDay,
									// ○時○分の時
									atTimeHour,
									// ○時○分の分
        		        			atTimeMinute,
        	    	    			// SNS の種類
        	        				spinnerSnsType.getSelectedItemPosition(),
        	        				// ソート順
        	        				settingsBean.sortOrder(),
        	        				// 指定したURL
        	        				textSnsUrl,
        	        				// ○日後
        	        				dialogView.getContext().getString(R.string.days_later),
        	        				// ○分後
        	        				dialogView.getContext().getString(R.string.minutes_later),
        	        				// ○時間
        	        				dialogView.getContext().getString(R.string.hours),
        	        				// 言語判定
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

        	    			// インテントをスケジュールする。
        	    			{
        	    				final Intent raiseIntent = new Intent(dialogView.getContext(), GameTimerRaiseNotification.class);
        	    				final String raiseAction = GameTimerRaiseNotification.GAMETIMER_RAISE_NOTIFICATION_BASE + newSettingsBean.id();
	        	    			raiseIntent.setAction(raiseAction);

    	    	    			if (newSettingsBean.notifyDateTime() > 0) {
									// 付加情報を与える
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
										// インテントをキャンセルする
        	    						sender.cancel();

										// アラームマネージャからも削除する
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
	       		        	// 一覧のボタンキャプションを更新する。
	       		        	clickedButton.setText(Html.fromHtml(newSettingsBean.getLabel(dialogView.getContext())));

							// 開始ボタンを更新する。
							startStopButton.setVisibility(View.INVISIBLE);
        	    		}
						else {
							// 設定内容はそのままで通知日時だけをリセットする。
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
        	        				// ○日後
        	        				dialogView.getContext().getString(R.string.days_later),
        	        				// ○分後
        	        				dialogView.getContext().getString(R.string.minutes_later),
        	        				// ○時間
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

        	    			// インテントをキャンセルする。
        	    			{
        	    				final Intent raiseIntent = new Intent(dialogView.getContext(), GameTimerRaiseNotification.class);
        	    				final String raiseAction = GameTimerRaiseNotification.GAMETIMER_RAISE_NOTIFICATION_BASE + newSettingsBean.id();
	        	    			raiseIntent.setAction(raiseAction);
       		    				final PendingIntent sender = PendingIntent.getBroadcast(dialogView.getContext(), 0, raiseIntent, PendingIntent.FLAG_NO_CREATE);
       		    				if (sender != null) {
									// インテントをキャンセルする
       	    						sender.cancel();

									// アラームマネージャからも削除する
        	    					final AlarmManager alarmManager = (AlarmManager)(dialogView.getContext().getSystemService(Context.ALARM_SERVICE));
									alarmManager.cancel(sender);
       		    				}
        	    			}

	       		        	// 一覧のボタンキャプションを更新する。
	       		        	clickedButton.setText(Html.fromHtml(newSettingsBean.getLabel(dialogView.getContext())));

							// 開始ボタンを更新する。
							startStopButton.setVisibility(View.VISIBLE);
						}

						// ウィジェットのアイコン画像を更新する。
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
