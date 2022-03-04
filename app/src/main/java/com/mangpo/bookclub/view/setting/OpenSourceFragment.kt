package com.mangpo.bookclub.view.setting

import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentOpenSourceBinding
import com.mangpo.bookclub.model.entities.OpenSourceModel
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.OpenSourceRVAdapter

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
                "Retrofit",
                "https://github.com/square/retrofit",
                "Apache 2.0",
                "Copyright 2013 Square, Inc."
            ),
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
                "TedImagePicker",
                "https://github.com/ParkSangGwon/TedImagePicker",
                "Apache 2.0",
                "Copyright 2019 Ted Park"
            ),
            OpenSourceModel(
                "Gson",
                "https://github.com/google/gson",
                "Apache 2.0",
                "Copyright 2008 Google Inc."
            ),
            OpenSourceModel(
                "Security-crypto",
                "https://github.com/android/security-samples",
                "Apache 2.0",
                ""
            ),
            OpenSourceModel(
                "AndroidX-Navigation",
                "https://github.com/android/architecture-components-samples",
                "Apache 2.0",
                "Copyright 2018 The Android Open Source Project, Inc."
            ),
            OpenSourceModel(
                "TedPermission",
                "https://github.com/ParkSangGwon/TedPermission",
                "Apache 2.0",
                "Copyright 2021 Ted Park"
            ),
            OpenSourceModel(
                "AndroidX-Room",
                "https://github.com/android/architecture-components-samples/tree/main/BasicRxJavaSampleKotlin",
                "Apache 2.0",
                "Copyright (C) 2017 The Android Open Source Project"
            ),
            OpenSourceModel(
                "PageIndicatorView",
                "https://github.com/romandanylyk/PageIndicatorView",
                "Apache 2.0",
                "Copyright 2017 Roman Danylyk"
            ),
            OpenSourceModel(
                "PhotoView",
                "https://github.com/Baseflow/PhotoView",
                "Apache 2.0",
                "Copyright 2018 Chris Banes"
            )
        )

        val adapter: OpenSourceRVAdapter = OpenSourceRVAdapter()
        adapter.setData(openSources)
        binding.openSourceRv.adapter = adapter
    }
}