/*
 * Copyright (c) 2024 by Max Lemberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mlprograms.rechenmax;

import android.os.Handler;
import androidx.core.widget.NestedScrollView;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.OverScroller;

public class ScrollUtils {

    public static void smoothScrollToBottom(final NestedScrollView nestedScrollView, final int duration, int delay) {
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        int scrollViewHeight = nestedScrollView.getHeight();
                        int childHeight = nestedScrollView.getChildAt(0).getHeight();
                        final int targetScrollY = Math.max(0, childHeight - scrollViewHeight);

                        final OverScroller scroller = new OverScroller(nestedScrollView.getContext(), new AccelerateDecelerateInterpolator());
                        scroller.startScroll(0, nestedScrollView.getScrollY(), 0, targetScrollY - nestedScrollView.getScrollY(), duration);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (scroller.computeScrollOffset()) {
                                    nestedScrollView.scrollTo(0, scroller.getCurrY());
                                    handler.post(this);
                                } else {
                                    nestedScrollView.scrollTo(0, targetScrollY);
                                }
                            }
                        });
                    }
                });
            }
        }, delay); // Delay in milliseconds
    }

}
