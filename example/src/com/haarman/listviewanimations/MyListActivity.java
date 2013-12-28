/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haarman.listviewanimations;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.haarman.listviewanimations.itemmanipulation.AnimateAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnAnimateCallback;

public class MyListActivity extends BaseActivity implements OnAnimateCallback {

	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylist);
		mListView = (ListView) findViewById(R.id.activity_mylist_listview);
		mListView.setAdapter(new AnimateAdapter(createListAdapter(), this));
		mListView.setDivider(null);
	}

	public ListView getListView() {
		return mListView;
	}

	protected ArrayAdapter<Integer> createListAdapter() {
		return new MyListAdapter(this, getItems());
	}

	public static ArrayList<Integer> getItems() {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < 1000; i++) {
			items.add(i);
		}
		return items;
	}

	@Override
	public void onDismiss(AbsListView arg0, int[] arg1) {

	}

	@Override
	public void onShow(AbsListView arg0, int[] arg1) {

	}

	private static class MyListAdapter extends ArrayAdapter<Integer> {

		private Context mContext;

		public MyListAdapter(Context context, ArrayList<Integer> items) {
			super(items);
			mContext = context;
		}

		@Override
		public long getItemId(int position) {
			return getItem(position).hashCode();
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position) % 2;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			switch (getItemViewType(position)) {
			case 0:
				ViewHolder1 holder1;
				if (convertView == null) {
					holder1 = new ViewHolder1();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);
					holder1.text = (TextView) convertView;
					convertView.setTag(holder1);
				} else {
					holder1 = (ViewHolder1) convertView.getTag();
				}
				holder1.text.setText("This is row number " + getItem(position));
				break;
			case 1:
				ViewHolder2 holder2;
				if (convertView == null) {
					holder2 = new ViewHolder2();
					convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row2, parent, false);
					holder2.text = (TextView) convertView.findViewById(android.R.id.text1);
					convertView.setTag(holder2);
				} else {
					holder2 = (ViewHolder2) convertView.getTag();
				}
				holder2.text.setText("This is row number " + getItem(position));
				break;
			}

			return convertView;
		}
	}

	private static class ViewHolder1 {
		TextView text;
	}

	private static class ViewHolder2 {
		TextView text;
	}
}
