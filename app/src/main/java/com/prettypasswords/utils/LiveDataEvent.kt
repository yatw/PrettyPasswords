package com.prettypasswords.utils

import java.lang.IllegalStateException

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
open class LiveDataEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again. (pop)
     */
    fun getContentIfNotHandled(): T {
        return if (hasBeenHandled) {
            throw IllegalStateException("content has been handled")
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled. (peek)
     */
    fun peekContent(): T = content
}