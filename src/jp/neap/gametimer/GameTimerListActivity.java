/**
 * アプリケーションタイマー一覧画面
 */
package jp.neap.gametimer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.text.Html;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameTimerListActivity extends Activity
						implements
							View.OnLongClickListener,
							OnTouchListener {

	public static final Map<Integer,Integer> buttonIdToListIndexMap = new HashMap<Integer,Integer>(); 

	public static final Map<Integer,Integer> listIndexToButtonIdMap = new HashMap<Integer,Integer>(); 

	public static final Map<Integer,Integer> listIndexToMoveUpButtonIdMap = new HashMap<Integer,Integer>(); 

	public static final Map<Integer,Integer> listIndexToStartStopButtonIdMap = new HashMap<Integer,Integer>(); 

	private boolean bDragging = false;

	private int draggingBeanId = 0;

	private boolean bMoveItemMode = false;

	private ListButtonDragDropManager ddManager = new ListButtonDragDropManager();

	static {
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_00), new Integer(0));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_01), new Integer(1));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_02), new Integer(2));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_03), new Integer(3));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_04), new Integer(4));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_05), new Integer(5));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_06), new Integer(6));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_07), new Integer(7));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_08), new Integer(8));
		buttonIdToListIndexMap.put(new Integer(R.id.settings_dialog_button_09), new Integer(9));

		listIndexToButtonIdMap.put(new Integer(0), new Integer(R.id.settings_dialog_button_00));
		listIndexToButtonIdMap.put(new Integer(1), new Integer(R.id.settings_dialog_button_01));
		listIndexToButtonIdMap.put(new Integer(2), new Integer(R.id.settings_dialog_button_02));
		listIndexToButtonIdMap.put(new Integer(3), new Integer(R.id.settings_dialog_button_03));
		listIndexToButtonIdMap.put(new Integer(4), new Integer(R.id.settings_dialog_button_04));
		listIndexToButtonIdMap.put(new Integer(5), new Integer(R.id.settings_dialog_button_05));
		listIndexToButtonIdMap.put(new Integer(6), new Integer(R.id.settings_dialog_button_06));
		listIndexToButtonIdMap.put(new Integer(7), new Integer(R.id.settings_dialog_button_07));
		listIndexToButtonIdMap.put(new Integer(8), new Integer(R.id.settings_dialog_button_08));
		listIndexToButtonIdMap.put(new Integer(9), new Integer(R.id.settings_dialog_button_09));

		listIndexToMoveUpButtonIdMap.put(new Integer(0), new Integer(R.id.settings_dialog_move_up_button_00));
		listIndexToMoveUpButtonIdMap.put(new Integer(1), new Integer(R.id.settings_dialog_move_up_button_01));
		listIndexToMoveUpButtonIdMap.put(new Integer(2), new Integer(R.id.settings_dialog_move_up_button_02));
		listIndexToMoveUpButtonIdMap.put(new Integer(3), new Integer(R.id.settings_dialog_move_up_button_03));
		listIndexToMoveUpButtonIdMap.put(new Integer(4), new Integer(R.id.settings_dialog_move_up_button_04));
		listIndexToMoveUpButtonIdMap.put(new Integer(5), new Integer(R.id.settings_dialog_move_up_button_05));
		listIndexToMoveUpButtonIdMap.put(new Integer(6), new Integer(R.id.settings_dialog_move_up_button_06));
		listIndexToMoveUpButtonIdMap.put(new Integer(7), new Integer(R.id.settings_dialog_move_up_button_07));
		listIndexToMoveUpButtonIdMap.put(new Integer(8), new Integer(R.id.settings_dialog_move_up_button_08));
		listIndexToMoveUpButtonIdMap.put(new Integer(9), new Integer(R.id.settings_dialog_move_up_button_09));

		listIndexToStartStopButtonIdMap.put(new Integer(0), new Integer(R.id.settings_dialog_start_stop_button_00));
		listIndexToStartStopButtonIdMap.put(new Integer(1), new Integer(R.id.settings_dialog_start_stop_button_01));
		listIndexToStartStopButtonIdMap.put(new Integer(2), new Integer(R.id.settings_dialog_start_stop_button_02));
		listIndexToStartStopButtonIdMap.put(new Integer(3), new Integer(R.id.settings_dialog_start_stop_button_03));
		listIndexToStartStopButtonIdMap.put(new Integer(4), new Integer(R.id.settings_dialog_start_stop_button_04));
		listIndexToStartStopButtonIdMap.put(new Integer(5), new Integer(R.id.settings_dialog_start_stop_button_05));
		listIndexToStartStopButtonIdMap.put(new Integer(6), new Integer(R.id.settings_dialog_start_stop_button_06));
		listIndexToStartStopButtonIdMap.put(new Integer(7), new Integer(R.id.settings_dialog_start_stop_button_07));
		listIndexToStartStopButtonIdMap.put(new Integer(8), new Integer(R.id.settings_dialog_start_stop_button_08));
		listIndexToStartStopButtonIdMap.put(new Integer(9), new Integer(R.id.settings_dialog_start_stop_button_09));
	}
		
	protected void onStart() {
		super.onStart();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onStart");
		}
    }
	protected void onRestart() {
		super.onRestart();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onRestart");
		}
    }
	protected void onResume() {
		super.onResume();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onResume");
		}
		reCreateList();
	}
	protected void onPause() {
		super.onPause();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onPause");
		}
	}
	protected void onStop() {
		super.onStop();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onStop");
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onDestroy");
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Logger.isDebugEnabled()) {
			Logger.debug("GameTimerListActivity::onCreate");
		}

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.gametimer_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);

		reCreateList();

		// ニックネーム一発変更の起動連携
		{
			final ImageView btnNickNameChanger = (ImageView)findViewById(R.id.title_nickname_changer);
			btnNickNameChanger.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					boolean bNickNameChangerInstalled = false;
					// ニックネーム一発変更がインストールされているか調べる
					{
						final PackageManager packageManager = view.getContext().getPackageManager();
						try {
							packageManager.getApplicationInfo("jp.neap.hanne", 0);
							bNickNameChangerInstalled = true;
						} catch (NameNotFoundException __ignore__) {}
					}
					if (bNickNameChangerInstalled) {
						// ニックネーム一発変更を起動する
						Intent intent = new Intent();
						intent.setClassName("jp.neap.hanne", "jp.neap.hanne.MainActivity");						
						startActivity(intent);
					}
					else {
						// ニックネーム一発変更のダウンロードページ(Google Play)に遷移する
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("market://details?id=jp.neap.hanne"));
						startActivity(intent);
					}
				}
			});
			btnNickNameChanger.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						{
							final ImageView ivNickNameChanger = (ImageView)view.findViewById(R.id.title_nickname_changer);
							ivNickNameChanger.setImageResource(R.drawable.icon_nickname_changer_mo);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					default:
					{
						final ImageView ivNickNameChanger = (ImageView)view.findViewById(R.id.title_nickname_changer);
						ivNickNameChanger.setImageResource(R.drawable.icon_nickname_changer);
					}
					}
					return false;
				}
			});
		}
	}

	@Override
	public boolean onLongClick(View clickedView) {

		// 移動モードを反転する。
		bMoveItemMode = !bMoveItemMode;

		final List<GameTimerSettingsBean> beanList;
		GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			beanList = dbHelper.listAllSorted(db);
		}
		finally {
			db.close();
		}

		// 一覧の各ボタンを設定する。
		assignButtonList(beanList);

		return true;
	}

/*
	public boolean onLongClick(View clickedView) {

		// 初期化
		ddManager.clear();

		// ドラッグ対象のボタンを求める。
		final int dragListIndex = buttonIdToListIndexMap.get(clickedView.getId());
		final List<GameTimerSettingsBean> beanList;
		GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			beanList = dbHelper.listAllSorted(db);
		}
		finally {
			db.close();
		}
		draggingBeanId = beanList.get(dragListIndex).id();

		// ドラッグ開始時の並びを保持する。
		for ( int listIndex = 0 ; listIndex < beanList.size() ; listIndex++ ) {
			final GameTimerSettingsBean settingsBean = beanList.get(listIndex);
			final Button btn = (Button)findViewById(listIndexToButtonIdMap.get(listIndex));
			ddManager.addButtonRect(settingsBean, btn.getLeft(), btn.getTop(), btn.getRight(), btn.getBottom());
		}

		// ドラッグ中...
		bDragging = true;
		return true;
	}
*/

	@Override
	public boolean onTouch(View buttonView, MotionEvent motionEvent) {

		if ( !bDragging ) {
			return false;
		}

		int x = (int)motionEvent.getRawX();
		int y = (int)motionEvent.getRawY();

		if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			if (ddManager.dragging(draggingBeanId, x, y)) {
				drawButtonLabelList(ddManager.getBeanList());
			}
		} else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			if (ddManager.dragging(draggingBeanId, x, y)) {
				drawButtonLabelList(ddManager.getBeanList());
			}
		} else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			try {
				if (ddManager.dragging(draggingBeanId, x, y)) {
					drawButtonLabelList(ddManager.getBeanList());
				}
				final List<GameTimerSettingsBean> newBeanList = ddManager.getBeanList();
				GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try {
					// ソート順を書き込む。
					for (int i = 0 ; i < newBeanList.size(); i++) {
						final GameTimerSettingsBean bean = newBeanList.get(i);
						dbHelper.updateSortOrder(db, bean.id(), i);
					}
					// 一覧の各ボタンにクリックイベントハンドラを割り当てる。
					final List<GameTimerSettingsBean> beanList = dbHelper.listAllSorted(db);
					assignButtonList(beanList);
				}
				finally {
					db.close();
				}
			}
			finally {
				bDragging = false;
				ddManager.clear();
			}
		} else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
			try {
				GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
				SQLiteDatabase db = dbHelper.getReadableDatabase();
				try {
					// 一覧の各ボタンにクリックイベントハンドラを割り当てる。
					final List<GameTimerSettingsBean> beanList = dbHelper.listAllSorted(db);
					assignButtonList(beanList);
				}
				finally {
					db.close();
				}
			}
			finally {
				bDragging = false;
				ddManager.clear();
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.preference_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		boolean retcode = false;
        switch (item.getItemId()) {
        case R.id.global_preference:
			Intent preferenceIntent = new Intent(getApplicationContext() , GameTimerPreferenceActivity.class);
			preferenceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(preferenceIntent);
	        retcode = true;
            break;
        }
        return retcode;
    }

	private void reCreateList() {
		final List<GameTimerSettingsBean> beanList;
		GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			beanList = dbHelper.listAllSorted(db);
		}
		finally {
			db.close();
		}

		// ドラッグ中の位置を取得する。
//		View frameView = (View)findViewById(R.id.gametimer_list_view);
//		frameView.setOnTouchListener(this);

		// 一覧の各ボタンを設定する。
		assignButtonList(beanList);
	}

	private void drawButtonLabelList(List<GameTimerSettingsBean> beanList) {
		for ( int listIndex = 0 ; listIndex < beanList.size() ; listIndex++ ) {
			final GameTimerSettingsBean settingsBean = beanList.get(listIndex);
			final Button btn = (Button)findViewById(listIndexToButtonIdMap.get(listIndex));
			btn.setText(Html.fromHtml(settingsBean.getLabel(getApplicationContext())));
		}
	}

	private void assignButtonList(List<GameTimerSettingsBean> beanList) {
		for ( int listIndex = 0 ; listIndex < beanList.size() ; listIndex++ ) {
			final GameTimerSettingsBean settingsBean = beanList.get(listIndex);
			// 移動ボタン
			{
				final Button btn = (Button)findViewById(listIndexToMoveUpButtonIdMap.get(listIndex));
				if ( bMoveItemMode ) {
					if ( listIndex == 0 ) {
						btn.setVisibility(View.INVISIBLE);
					}
					else {
						btn.setVisibility(View.VISIBLE);
					}
				}
				else {
					btn.setVisibility(View.GONE);
				}

				View.OnClickListener viewOnClickListener = new GameTimeMoveUpButtonOnClickListenerImpl(
										settingsBean.id());
				btn.setOnClickListener(viewOnClickListener);
			}
			// 設定ボタン
			{
				final Button btn = (Button)findViewById(listIndexToButtonIdMap.get(listIndex));
				btn.setText(Html.fromHtml(settingsBean.getLabel(getApplicationContext())));

				// ボタン長押しで移動モードに入る。
				btn.setOnLongClickListener(this);

				View.OnClickListener viewOnClickListener = new GameTimerSettingViewOnClickListenerImpl(
										settingsBean.id(),
										btn,
										(Button)findViewById(listIndexToStartStopButtonIdMap.get(listIndex)));
				btn.setOnClickListener(viewOnClickListener);
			}
			// 開始ボタン
			{
				final Button btn = (Button)findViewById(listIndexToStartStopButtonIdMap.get(listIndex));
				if ( settingsBean.isSetSchedule() ) {
					if ( settingsBean.isActiveSchedule(getApplicationContext()) ) {
						btn.setVisibility(View.INVISIBLE);
					}
					else {
						btn.setVisibility(View.VISIBLE);
					}
				}
				else {
					btn.setVisibility(View.INVISIBLE);
				}

				View.OnClickListener viewOnClickListener = new GameTimeStartStopButtonOnClickListenerImpl(
										settingsBean.id(),
										(Button)findViewById(listIndexToButtonIdMap.get(listIndex)),
										btn);
				btn.setOnClickListener(viewOnClickListener);
			}
		}
	}


	public class GameTimeMoveUpButtonOnClickListenerImpl implements OnClickListener {

		final int sourceBeanId;

		/**
		 * 
		 */
		public GameTimeMoveUpButtonOnClickListenerImpl(int sourceBeanId) {
			// TODO Auto-generated constructor stub
			this.sourceBeanId = sourceBeanId;
		}

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View buttonView) {

			GameTimerDBHelper dbHelper = new GameTimerDBHelper(getApplicationContext(), GameTimerDBHelper.DB_FILENAME, null, GameTimerDBHelper.DB_VERSION);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try {
				final GameTimerSettingsBean sourceBean = dbHelper.getRecordById(db, sourceBeanId);
				if ( sourceBean.sortOrder() == 0 ) {
					return;
				}
				final GameTimerSettingsBean targetBean = dbHelper.getRecordBySortOrder(db, sourceBean.sortOrder() - 1);

				dbHelper.updateSortOrder(db, sourceBean.id(), targetBean.sortOrder());
				dbHelper.updateSortOrder(db, targetBean.id(), sourceBean.sortOrder());

				// リストを再表示する。
				final List<GameTimerSettingsBean> beanList = dbHelper.listAllSorted(db);
				assignButtonList(beanList);
			}
			finally {
				db.close();
			}
		}
	}
}