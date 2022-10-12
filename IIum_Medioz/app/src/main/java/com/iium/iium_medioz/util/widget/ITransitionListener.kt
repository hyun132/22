package com.iium.iium_medioz.util.widget

import androidx.constraintlayout.motion.widget.MotionLayout

interface ITransitionListener {

    /**
     * 동작이 끝나면 호출된다.
     */
    fun onTransitionCompleted(layout: MotionLayout, state: StickySlideState)

}