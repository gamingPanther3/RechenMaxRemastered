package com.mlprograms.rechenmaxremastered;

/*
 * Copyright (c) 2024 by Max Lemberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.tasks.Task;
import static com.mlprograms.rechenmax.ToastHelper.*;

import android.app.Activity;
*/

public class InAppReview {
    /*
    private ReviewInfo reviewInfo;
    private ReviewManager reviewManager;
    private final Activity parentActivity;

    public InAppReview(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void activateReviewInfo() {
        Task<ReviewInfo> reviewManagerInfoTask = reviewManager.requestReviewFlow();
        reviewManagerInfoTask.addOnCompleteListener((task -> {
            if(task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else {
                showToastShort(parentActivity.getString(R.string.inAppReviewCouldntStart), parentActivity);
            }
        }));
    }

    public void startReviewFlow() {
        if(reviewInfo != null) {
            Task<Void> flow = reviewManager.launchReviewFlow(parentActivity, reviewInfo);
            flow.addOnCompleteListener(task -> {
                showToastShort(parentActivity.getString(R.string.inAppReviewFinished), parentActivity);
            });
        }
    }
     */
}
