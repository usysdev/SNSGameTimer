/**
 * 
 */
package jp.neap.gametimer;

import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class ListButtonDragDropManager {

	private final List<ButtonRect> buttonRectList = new LinkedList<ButtonRect>();


	public ListButtonDragDropManager() {}

	public void clear() {
		buttonRectList.clear();
	}

	public void addButtonRect(GameTimerSettingsBean settingsBean, int left, int top, int right, int bottom) {
		buttonRectList.add(new ButtonRect(settingsBean, left, top, right, bottom));
	}

	public boolean dragging(int dragBeanId, int x, int y) {
		final int drawButtonListIndex = getListIndexFromBeanId(dragBeanId);
		if ( drawButtonListIndex == -1 ) {
			return false;
		}
		final int hitButtonListIndex = getHitListIndex(x, y);
		if ( hitButtonListIndex == -1 ) {
			return false;
		}
		if ( drawButtonListIndex == hitButtonListIndex ) {
			return false;
		}
		final ButtonRect targetButtonRect = buttonRectList.remove(drawButtonListIndex);
		buttonRectList.add(hitButtonListIndex, targetButtonRect);
		return true;
	}

	public List<GameTimerSettingsBean> getBeanList() {
		final List<GameTimerSettingsBean>beanList = new LinkedList<GameTimerSettingsBean>();
		for (int i = 0 ; i < buttonRectList.size(); i++) {
			beanList.add(buttonRectList.get(i).bean());
		}
		return beanList;
	}

	private int getListIndexFromBeanId(int beanId) {
		for (int i = 0 ; i < buttonRectList.size(); i++) {
			final ButtonRect rect = buttonRectList.get(i);
			if ( rect.bean().id() == beanId ) {
				return i;
			}
		}
		return -1;
	}

	private int getHitListIndex(int x, int y) {
		for (int i = 0 ; i < buttonRectList.size(); i++) {
			final ButtonRect rect = buttonRectList.get(i);
			if ( rect.isHit(x, y) ) {
				return i;
			}
		}
		return -1;
	}
}
