/**
 * 
 */
package jp.neap.gametimer;

/**
 *
 */
public class ButtonRect {

	private final GameTimerSettingsBean settingsBean;

	private final int left;

	private final int top;

	private final int right;

	private final int bottom;

	public ButtonRect(GameTimerSettingsBean settingsBean, int left, int top, int right, int bottom) {
		this.settingsBean = settingsBean;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public GameTimerSettingsBean bean() { return settingsBean; }

	public boolean isHit(int x, int y) {
		return (x >= left) && (x <= right) &&
			   (y >= top) && (y <= bottom);
	}
}
