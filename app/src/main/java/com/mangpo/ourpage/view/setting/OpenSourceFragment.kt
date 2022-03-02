package com.mangpo.ourpage.view.setting

import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.FragmentOpenSourceBinding
import com.mangpo.ourpage.model.entities.OpenSourceModel
import com.mangpo.ourpage.view.BaseFragment
import com.mangpo.ourpage.view.adpater.OpenSourceRVAdapter

class OpenSourceFragment : BaseFragment<FragmentOpenSourceBinding>(FragmentOpenSourceBinding::inflate) {
    override fun initAfterBinding() {
        (requireActivity() as SettingActivity).changeToolbarText(getString(R.string.title_open_source_license), false)
        setAdapter()
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
                "CircleIndicator",
                "https://github.com/ongakuer/CircleIndicator",
                "Apache 2.0",
                ""
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