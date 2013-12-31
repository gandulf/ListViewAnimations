package com.haarman.listviewanimations.itemmanipulation;

/*
 * Copyright 2013 Gandulf Kohlweiss
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.haarman.listviewanimations.BaseAdapterDecorator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * A {@link BaseAdapterDecorator} class that provides animations to the adding and dismissal of items in the given
 * {@link BaseAdapter} .
 */
public class AnimateAdapter<T> extends AnimateDismissAdapter<T> {

	private OnAnimateCallback mCallback;

	private List<Integer> animatePositions;

	/**
	 * Create a new AnimateShowAdapter based on the given {@link BaseAdapter} .
	 * 
	 * @param callback
	 *            The {@link OnAnimateCallback} to trigger when the user has indicated that she would like to add one or
	 *            more list items.
	 */
	public AnimateAdapter(BaseAdapter baseAdapter, OnAnimateCallback callback) {
		super(baseAdapter, callback);
		mCallback = callback;
		animatePositions = new ArrayList<Integer>();
	}

	/**
	 * Animate adding of the item at given position.
	 */
	public void animateShow(int position) {
		animateShow(Arrays.asList(position));
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);

		if (animatePositions.contains(position)) {

			if (getAbsListView() == null) {
				throw new IllegalStateException(
						"Call setAbsListView() on this AnimateShowAdapter before calling setAdapter()!");
			}

			// if (!views.isEmpty()) {
			List<Animator> animators = new ArrayList<Animator>();
			// for (final View view : views) {
			animators.add(createAnimatorForView(view));
			// }

			AnimatorSet animatorSet = new AnimatorSet();

			Animator[] animatorsArray = new Animator[animators.size()];
			for (int i = 0; i < animatorsArray.length; i++) {
				animatorsArray[i] = animators.get(i);
			}

			animatorSet.playTogether(animatorsArray);
			animatorSet.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationStart(Animator animation) {
					animatePositions.remove((Integer) position);
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					invokeCallback(Arrays.asList(position));
				}

			});
			animatorSet.start();

		}

		return view;
	}

	/**
	 * Animate adding of the items at given positions.
	 */
	public void animateShow(Collection<Integer> positions) {
		animatePositions.addAll(positions);
	}

	private void invokeCallback(Collection<Integer> positions) {
		ArrayList<Integer> positionsList = new ArrayList<Integer>(positions);
		Collections.sort(positionsList);

		int nrHeaders = 0;
		if (getAbsListView() instanceof ListView) {
			nrHeaders = ((ListView) getAbsListView()).getHeaderViewsCount();
		}

		int[] addPositions = new int[positionsList.size()];
		for (int i = 0; i < positionsList.size(); i++) {
			addPositions[i] = positionsList.get(positionsList.size() - 1 - i - nrHeaders);
		}
		mCallback.onShow(getAbsListView(), addPositions);
	}

	private Animator createAnimatorForView(final View view) {
		final ViewGroup.LayoutParams lp = view.getLayoutParams();

		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		final int originalHeight = view.getMeasuredHeight();

		lp.height = 1;
		view.setLayoutParams(lp);

		ValueAnimator animator = ValueAnimator.ofInt(1, Math.max(64, originalHeight));
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animator) {
				lp.height = LayoutParams.WRAP_CONTENT;
				view.setLayoutParams(lp);
			}

		});

		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				view.setLayoutParams(lp);
			}
		});

		return animator;
	}
}