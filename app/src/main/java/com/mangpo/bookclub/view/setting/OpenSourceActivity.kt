package com.mangpo.bookclub.view.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mangpo.bookclub.databinding.ActivityOpenSourceBinding
import com.mangpo.bookclub.model.OpenSourceModel
import com.mangpo.bookclub.view.adapter.OpenSourceRVAdapter

class OpenSourceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenSourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("OpenSourceActivity", "onCreate")
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()

        //뒤로가기 클릭리스너
        binding.backIvView.setOnClickListener {
            finish()
        }
    }

    private fun setAdapter() {
        val openSources: ArrayList<OpenSourceModel> = arrayListOf(
            OpenSourceModel(
                "Androidx constraintlayout",
                "https://github.com/androidx/constraintlayout",
                "Apache 2.0",
                ""
            ),
            OpenSourceModel(
                "Material-components-android",
                "https://github.com/material-components/material-components-android",
                "Apache 2.0",
                ""
            ),
            OpenSourceModel(
                "Koin",
                "https://github.com/InsertKoinIO/koin",
                "Apache 2.0",
                ""),
            OpenSourceModel(
                "Retrofit",
                "https://github.com/square/retrofit",
                "Apache 2.0",
                "Copyright 2013 Square, Inc."
            ),
            OpenSourceModel(
                "OkHttp",
                "",
                "Apache 2.0",
                "Copyright 2019 Square, Inc."),
            OpenSourceModel(
                "Coroutines",
                "https://github.com/Kotlin/kotlinx.coroutines",
                "Apache 2.0",
                ""
            ),
            OpenSourceModel(
                "Glide",
                "https://github.com/bumptech/glide",
                "Apache 2.0",
                "Copyright 2012 Jake Wharton\nCopyright 2011 The Android Open Source Project"
            ),
            OpenSourceModel(
                "Dotsindicator",
                "https://github.com/tommybuonomo/dotsindicator",
                "Apache 2.0",
                "Copyright 2016 Tommy Buonomo"
            ),
            OpenSourceModel(
                "TedImagePicker",
                "https://github.com/ParkSangGwon/TedImagePicker",
                "Apache 2.0",
                "Copyright 2019 Ted Park"
            ),
            OpenSourceModel(
                "Robolectric",
                "https://github.com/robolectric/robolectric",
                "Apache 2.0",
                ""
            ),
            OpenSourceModel(
                "Gson",
                "https://github.com/google/gson",
                "Apache 2.0",
                "Copyright 2008 Google Inc."
            )
        )

        val adapter: OpenSourceRVAdapter = OpenSourceRVAdapter()
        adapter.setData(openSources)
        binding.openSourceRv.adapter = adapter
    }
}