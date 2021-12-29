package com.mangpo.bookclub.view.write

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mangpo.bookclub.databinding.ActivityPhotoViewBinding
import com.mangpo.bookclub.view.adapter.PhotoViewPagerAdapter

class PhotoViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgs = intent.getStringArrayListExtra("imgs")
        val currentItem = intent.getIntExtra("currentItem", 0)

        val adaptor: PhotoViewPagerAdapter = PhotoViewPagerAdapter(imgs!!)

        binding.photoViewVp.adapter = adaptor
        binding.photoViewVp.setCurrentItem(currentItem, true)
        binding.photoViewIndicator.setViewPager2(binding.photoViewVp)

        binding.exitIv.setOnClickListener {
            finish()
        }
    }
}