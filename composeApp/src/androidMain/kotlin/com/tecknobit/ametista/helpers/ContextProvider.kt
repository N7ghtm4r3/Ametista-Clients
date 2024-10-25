package com.tecknobit.ametista.helpers

import android.app.Activity
import java.lang.ref.WeakReference

object ContextProvider {

    private var activityRef: WeakReference<Activity>? = null

    fun setCurrentActivity(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun getCurrentActivity(): Activity? = activityRef?.get()

}