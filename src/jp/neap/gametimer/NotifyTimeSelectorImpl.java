/**
 * 
 */
package jp.neap.gametimer;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 *
 */
public class NotifyTimeSelectorImpl implements OnCheckedChangeListener {

	private final View dialogView;

	public NotifyTimeSelectorImpl(View dialogView) {
		this.dialogView = dialogView;
	}


	/* (non-Javadoc)
	 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup, int)
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		final View notifyLaterMinutesView = dialogView.findViewById(R.id.tableLayout_notifyLaterMinutes);
		final View notifyLaterHourMinuteView = dialogView.findViewById(R.id.tableLayout_notifyLaterHourMinute);
		final View notifyAtTimeHourMinuteView = dialogView.findViewById(R.id.tableLayout_notifyAtTimeHourMinute);
		final View notifyAtTimeHourMinuteDayView = dialogView.findViewById(R.id.tableLayout_notifyAtTimeHourMinute_day);

		switch (checkedId) {
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
	}
}
