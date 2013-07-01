/**
 * 
 */
package jp.neap.gametimer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class GameTimerPreferenceActivity extends Activity {

	private final List<Map<String,String>> preferenceDataList = new LinkedList<Map<String,String>>();

	protected void onStart() {
		super.onStart();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerPreferenceActivity::onStart");
		}
    }
	protected void onRestart() {
		super.onRestart();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerPreferenceActivity::onRestart");
		}
    }
	protected void onResume() {
		super.onResume();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerPreferenceActivity::onResume");
		}
	}
	protected void onPause() {
		super.onPause();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerPreferenceActivity::onPause");
		}
	}
	protected void onStop() {
		super.onStop();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerPreferenceActivity::onStop");
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerPreferenceActivity::onDestroy");
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.gametimer_preference);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);

		// 現在の設定値を読み込む。
		preferenceDataList.clear();
		{
			GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				// お知らせ通知音・バイブ
				{
					final String audioFileShortPath = dbHelper.getProperty(db, "audioFileShortPath", "");
					final String notifyMethod = dbHelper.getProperty(db, "notifyMethod", "");
					final Map<String,String> map = new HashMap<String,String>();
					map.put("name", getResources().getString(R.string.notification_method));
					if ("".equals(audioFileShortPath)) {
						if ("".equals(notifyMethod) || "system_sound".equals(notifyMethod)) {
							map.put("value", getResources().getString(R.string.system_default_sound));
						}
						else {
							map.put("value", getResources().getString(R.string.system_default_vib));
						}
					}
					else {
						map.put("value", audioFileShortPath);
					}
					preferenceDataList.add(map);
				}
			}
			finally {
				db.close();
			}
		}

		final ListView preferenceListView = (ListView)findViewById(R.id.preference_listview);

		final SimpleAdapter adapter = new SimpleAdapter(
					this,
					preferenceDataList,
					android.R.layout.simple_list_item_2,
					new String[] {"name", "value"},
					new int[] {android.R.id.text1, android.R.id.text2});

		preferenceListView.setAdapter(adapter);
		preferenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final Intent intent = new Intent(getApplicationContext(), GameTimerAudioFileListActivity.class);
				int requestCode = 567;
				startActivityForResult(intent, requestCode);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 567:
			if ( data != null ) {
				final Bundle bundle = data.getExtras();

				// 新しい設定値を受け取る。
				final String audioFileFullPath = bundle.getString("audioFileFullPath");
				final String audioFileShortPath = bundle.getString("audioFileShortPath");
				final String notifyMethod = bundle.getString("notifyMethod");

				// お知らせ通知方法を更新する。
				{
					GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					try {
						dbHelper.setProperty(db, "audioFileFullPath", audioFileFullPath);
						dbHelper.setProperty(db, "audioFileShortPath", audioFileShortPath);
						dbHelper.setProperty(db, "notifyMethod", notifyMethod);
					}
					finally {
						db.close();
					}
				}

				// 表示データを更新する。
				{
					final Map<String,String> map = preferenceDataList.get(0);
					if ("system_sound".equals(notifyMethod)) {
						map.put("value", getResources().getString(R.string.system_default_sound));
					} else if ("system_vib".equals(notifyMethod)) {
						map.put("value", getResources().getString(R.string.system_default_vib));
					} else {
						map.put("value", audioFileShortPath);
					}
				}

				// リストを更新する
				final ListView preferenceListView = (ListView)findViewById(R.id.preference_listview);
				final SimpleAdapter adapter = (SimpleAdapter)preferenceListView.getAdapter();
				adapter.notifyDataSetChanged();
			}
			break;
		}
	}
}