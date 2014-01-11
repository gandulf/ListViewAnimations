package com.haarman.listviewanimations.view;

import android.widget.AdapterView;

public interface OnItemCheckedListener {
	public void onItemChecked(AdapterView<?> list, int position, boolean checked);
}