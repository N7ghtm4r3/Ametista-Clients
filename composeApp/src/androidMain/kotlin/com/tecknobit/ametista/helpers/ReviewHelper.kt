package com.tecknobit.ametista.helpers

import android.app.Activity
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

/**
 * The **ReviewHelper** class is useful to send a review in-app with the related API
 *
 * @param activity The activity where the review helper has been instantiated
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class ReviewHelper(
    private val activity: Activity
) {

    /**
     * Function to launch the review in-app API
     *
     * @param flowAction The action to execute when the review in-app finished
     */
    fun reviewInApp(
        flowAction: () -> Unit
    ) {
        val reviewManager: ReviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val flow = reviewManager.launchReviewFlow(activity, task.result)
                flow.addOnCompleteListener {
                    flowAction.invoke()
                }
                flow.addOnCanceledListener {
                    flowAction.invoke()
                }
            } else
                flowAction.invoke()
        }
        request.addOnFailureListener {
            flowAction.invoke()
        }
    }

}