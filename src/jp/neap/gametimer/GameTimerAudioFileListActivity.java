/**
 * 
 */
package jp.neap.gametimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

/**
 *
 */
public class GameTimerAudioFileListActivity extends Activity {

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
				final File f = Environment.getExternalStorageDirectory();
				sdcardRootDir = f.getPath();
			}
			else {
				sdcardRootDir = "";
			}
		}

		final ListView audioFileListView = (ListView)findViewById(R.id.audiofile_listview);

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		final List<String> audioFileList = listAudioFiles();
		if ( audioFileList.size() > 0 ) {
			// 最初はシステムデフォルト
			adapter.add("システムデフォルト");
			audioFileFullPathList.add("");
			// SDカード上のファイル
			for (int i = 0 ; i < audioFileList.size(); i++) {
				final String audioFilePath = audioFileList.get(i);
				adapter.add(audioFilePath.substring(sdcardRootDir.length()));
				audioFileFullPathList.add(audioFilePath);
			}
		}
		else {
			adapter.add("システムデフォルト");
			audioFileFullPathList.add("");
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
				data.putExtras(bundle);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	private List<String> listAudioFiles() {
		final List<String> fileList = new LinkedList<String>();
		if ( !"".equals(sdcardRootDir) ) {
			listAudioFiles(sdcardRootDir + "/media/audio/notifications", fileList);
		}
		return fileList;
	}

	private void listAudioFiles(String dir, List<String> fileList) {
		final File f = new File(dir);
		if ( f.exists() ) {
			if ( f.isDirectory() ) {
				final File[] c = f.listFiles();
				for (int i = 0 ; i < c.length ; i++) {
					listAudioFiles(c[i].getPath(), fileList);
				}
			}
			else {
				fileList.add(f.getPath());
			}
		}
	}
}
