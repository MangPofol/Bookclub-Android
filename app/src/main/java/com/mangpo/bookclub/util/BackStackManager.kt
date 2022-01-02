package com.mangpo.bookclub.util

import android.util.Log
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.ArrayList

object BackStackManager {
    private val mainBackStack: ArrayList<Int> = arrayListOf()
    private val mainFrameBackStack: Stack<Fragment> = Stack()
    private val libraryFrameBackStack: Stack<Fragment> = Stack()
    private val bookClubFrameBackStack: Stack<Fragment> = Stack()
    private var menuIdx: Int? = null

    fun pushFragment(idx: Int, fragment: Fragment) {
        when (idx) {
            0 -> {
                mainFrameBackStack.push(fragment)
            }
            1 -> {
                libraryFrameBackStack.push(fragment)
            }
            2 -> {
                bookClubFrameBackStack.push(fragment)
            }
        }
    }

    fun peekFragment(idx: Int): Fragment? {
        when (idx) {
            0 -> mainFrameBackStack.apply {
                return if (isEmpty())
                    null
                else
                    peek()
            }

            1 -> libraryFrameBackStack.apply {
                return if (isEmpty())
                    null
                else
                    peek()
            }

            else -> bookClubFrameBackStack.apply {
                return if (isEmpty())
                    null
                else
                    peek()
            }
        }
    }

    fun popFragment(idx: Int): Fragment? {
        when (idx) {
            0 -> mainFrameBackStack.apply {
                return if (isEmpty())
                    null
                else
                    pop()
            }

            1 -> libraryFrameBackStack.apply {
                return if (isEmpty())
                    null
                else
                    pop()
            }

            else -> bookClubFrameBackStack.apply {
                return if (isEmpty())
                    null
                else
                    pop()
            }
        }
    }

    fun clearFragment(idx: Int) {
        when (idx) {
            0 -> mainFrameBackStack.clear()
            1 -> libraryFrameBackStack.clear()
            2 -> bookClubFrameBackStack.clear()
        }
    }

    fun pushBackstack(idx: Int) {
        if (mainBackStack.contains(idx)) {
            mainBackStack.remove(idx)
        }

        mainBackStack.add(idx)
        Log.d("BackStackManager", "pushFragment -> ${mainBackStack.last()}")
    }

    fun popBackstack(): Int? {
        if (mainBackStack.isEmpty())
            return null
        else
            mainBackStack.apply {
                removeLast()

                return if (isEmpty())
                    null
                else
                    last()
            }
    }

    fun clear() {
        mainBackStack.clear()
        mainFrameBackStack.clear()
        libraryFrameBackStack.clear()
        bookClubFrameBackStack.clear()
        menuIdx = null
    }
}