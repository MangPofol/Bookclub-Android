package com.mangpo.bookclub.util

import android.util.Log
import androidx.fragment.app.Fragment
import com.mangpo.bookclub.view.bookclub.BookClubFragment
import com.mangpo.bookclub.view.library.MyLibraryFragment
import com.mangpo.bookclub.view.write.MainFragment
import java.util.*

object BackStackManager {
    private val mainBackStack: Stack<Int> = Stack()
    private val writeBackStack: Stack<Fragment> = Stack()
    private val libraryBackStack: Stack<Fragment> = Stack()
    private val clubBackStack: Stack<Fragment> = Stack()
    private var menuIdx: Int? = null

    fun switchFragment(idx: Int): Fragment {
        var fragment: Fragment? = null
        mainBackStack.push(idx)

        when (idx) {
            0 -> {
                if (writeBackStack.isEmpty()) {
                    fragment = MainFragment()
                    writeBackStack.push(fragment)
                } else {
                    fragment = writeBackStack.peek()
                }
            }
            1 -> {
                if (libraryBackStack.isEmpty()) {
                    fragment = MyLibraryFragment()
                    libraryBackStack.push(fragment)
                } else {
                    fragment = libraryBackStack.peek()
                }
            }
            else -> {
                if (clubBackStack.isEmpty()) {
                    fragment = BookClubFragment()
                    clubBackStack.push(fragment)
                } else {
                    fragment = clubBackStack.peek()
                }
            }
        }

        return fragment!!
    }

    fun popFragment(): Fragment? {
        var fragment: Fragment? = null

        if (mainBackStack.size == 1) {
            menuIdx = mainBackStack.pop()
        } else if (!mainBackStack.isEmpty()) {
            mainBackStack.pop()
            menuIdx = mainBackStack.peek()

            when (menuIdx) {
                0 -> {
                    fragment = if (writeBackStack.size == 1) {
                        writeBackStack.peek()
                    } else {
                        writeBackStack.pop()
                        writeBackStack.peek()
                    }
                }

                1 -> {
                    fragment = if (libraryBackStack.size == 1) {
                        libraryBackStack.peek()
                    } else {
                        libraryBackStack.pop()
                        libraryBackStack.peek()
                    }
                }

                else -> {
                    fragment = if (clubBackStack.size == 1) {
                        clubBackStack.peek()
                    } else {
                        clubBackStack.pop()
                        clubBackStack.peek()
                    }
                }
            }
        }
        Log.d("BackStackManager", "popFragment -> ${fragment?.javaClass}")
        return fragment
    }

    fun pushFragment(idx: Int, fragment: Fragment) {
        mainBackStack.push(idx)

        when (idx) {
            0 -> writeBackStack.push(fragment)
            1 -> libraryBackStack.push(fragment)
            else -> clubBackStack.push(fragment)
        }

        Log.d("BackStackManager", "pushFragment -> $writeBackStack")
        Log.d("BackStackManager", "pushFragment -> $libraryBackStack")
    }

    fun getMenu(): Int = menuIdx!!

    fun clear() {
        mainBackStack.clear()
        writeBackStack.clear()
        libraryBackStack.clear()
        clubBackStack.clear()
        menuIdx = null
    }
}