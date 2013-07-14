/**
 * 
 */
package jp.neap.gametimer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 */
public class GameTimerAudioFileListActivity extends Activity {

	// 音源ファイルの外部ストレージディレクトリ以下のルートディレクトリ
	private final String AUDIOFILE_ROOT_OF_SDCARD = "/media";
	
	// 音源ファイルの拡張子
	private final String[] AUDIOFILE_EXT_LIST = new String[] {
			"acd",
			"aif",
			"asf",
			"asx",
			"au",
			"avi",
			"dig",
			"iff",
			"lso",
			"m4a",
			"mid",
			"midi",
			"mov",
			"mp3",
			"mpg",
			"msf",
			"qt",
			"ra",
			"ram",
			"rm",
			"rpm",
			"sd",
			"sdn",
			"svx",
			"vqe",
			"vqf",
			"vql",
			"wav",
			"wma",
			"wrk",
	};

	private final List<String> audioFileFullPathList = new LinkedList<String>();

	private String sdcardRootDir = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// タイトルバーのカスタマイズ
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.gametimer_audio_file_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);

		// 初期化
		audioFileFullPathList.clear();
		{
			final String status = Environment.getExternalStorageState();
			if ( Environment.MEDIA_MOUNTED.equals(status) ||
				 Environment.MEDIA_MOUNTED_READ_ONLY.equals(status) ) {
				// Environment.MEDIA_MOUNTED
				//   SDカードが装着されていてかつ読み書き可能である。
				// Environment.MEDIA_MOUNTED_READ_ONLY
				//   SDカードが装着されているが読み取り専用である。
				final File f = Environment.getExternalStorageDirectory();
				sdcardRootDir = f.getPath();
			}
			else {
				sdcardRootDir = "";
			}
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("外部ストレージ=" + sdcardRootDir);
		}

		final Set<String> acceptExtSet = new HashSet<String>();
		for (int i = 0 ; i < AUDIOFILE_EXT_LIST.length ; i++) {
			acceptExtSet.add(AUDIOFILE_EXT_LIST[i]);
		}
		
		{
			GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				final String fullPath = dbHelper.getProperty(db, "audioFileFullPath", null);
				if (fullPath != null) {
					final String ext = getExt(fullPath);
					if (ext != null) {
						if (!acceptExtSet.contains(ext)) {
							acceptExtSet.add(ext);
						}
					}
				}
			}
			finally {
				db.close();
			}
		}
		
		final ListView audioFileListView = (ListView)findViewById(R.id.audiofile_listview);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

		final List<String> audioFileList = listAudioFiles(acceptExtSet);
		if ( audioFileList.size() > 0 ) {
			// １番目はシステムデフォルトサウンド
			adapter.add(getResources().getString(R.string.system_default_sound));
			audioFileFullPathList.add("");

			// ２番目はシステムデフォルトバイブレーション
			adapter.add(getResources().getString(R.string.system_default_vib));
			audioFileFullPathList.add("");

			// ３番目以降にSDカード上のファイルを加える
			for (int i = 0 ; i < audioFileList.size(); i++) {
				final String audioFilePath = audioFileList.get(i);
				adapter.add(audioFilePath.substring(sdcardRootDir.length()));
				audioFileFullPathList.add(audioFilePath);
			}

			audioFileListView.setAdapter(adapter);

			audioFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final Intent data = new Intent();
					final Bundle bundle = new Bundle();
					final String audioFileFullPath = audioFileFullPathList.get(position);
					final String audioFileShortPath;
					if ( !"".equals(audioFileFullPath) ) {
						audioFileShortPath = audioFileFullPath.substring(sdcardRootDir.length());
					}
					else {
						audioFileShortPath = "";
					}
					bundle.putString("audioFileFullPath", audioFileFullPath);
					bundle.putString("audioFileShortPath", audioFileShortPath);
					switch (position) {
					case 0:
						bundle.putString("notifyMethod", "system_sound");
						break;
					case 1:
						bundle.putString("notifyMethod", "system_vib");
						break;
					default:
						bundle.putString("notifyMethod", "user_sound");
					}
					data.putExtras(bundle);
					setResult(RESULT_OK, data);
					finish();
				}
			});
		}
		else {
			// １番目はシステムデフォルトサウンド
			adapter.add(getResources().getString(R.string.system_default_sound));
			audioFileFullPathList.add("");

			// ２番目はシステムデフォルトバイブレーション
			adapter.add(getResources().getString(R.string.system_default_vib));
			audioFileFullPathList.add("");

			// ３番目はサウンドファイルが見つからない場合の案内文
			if ("".equals(sdcardRootDir)) {
				adapter.add(getString(R.string.usage_no_sdcard));
			}
			else {
				adapter.add(getString(R.string.usage_no_sound_file, AUDIOFILE_ROOT_OF_SDCARD));
			}

			audioFileListView.setAdapter(adapter);

			audioFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final Intent data = new Intent();
					final Bundle bundle = new Bundle();
					switch (position) {
					case 0:
						bundle.putString("audioFileFullPath", "");
						bundle.putString("audioFileShortPath", "");
						bundle.putString("notifyMethod", "system_sound");
						break;
					case 1:
						bundle.putString("audioFileFullPath", "");
						bundle.putString("audioFileShortPath", "");
						bundle.putString("notifyMethod", "system_vib");
						break;
					}
					if ((position == 0) || (position == 1)) {
						data.putExtras(bundle);
						setResult(RESULT_OK, data);
						finish();
					}
				}
			});
		}
	}

	private List<String> listAudioFiles(Set<String> acceptExtSet) {
		final List<String> fileList = new LinkedList<String>();
		if ( !"".equals(sdcardRootDir) ) {
			listAudioFiles(sdcardRootDir + AUDIOFILE_ROOT_OF_SDCARD, fileList, acceptExtSet);
		}
		return fileList;
	}

	private void listAudioFiles(String dir, List<String> fileList, Set<String> acceptExtSet) {
		final File f = new File(dir);
		if ( f.exists() ) {
			if ( f.isDirectory() ) {
				final File[] c = f.listFiles();
				for (int i = 0 ; i < c.length ; i++) {
					listAudioFiles(c[i].getPath(), fileList, acceptExtSet);
				}
			}
			else {
				final String fullPath = f.getPath();
				final String ext = getExt(fullPath);
				if (ext != null) {
					if (acceptExtSet.contains(ext)) {
						fileList.add(fullPath);
					}
				}
			}
		}
	}

	private String getExt(String filePath) {
		final int sepIndex = filePath.lastIndexOf(".");
		if (sepIndex > 0) {
			return filePath.substring(sepIndex + 1).toLowerCase();
		}
		return null;
	}
}
