package com.mangpo.ourpage.view.main.record

import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.mangpo.ourpage.databinding.ActivityPhotoViewBinding
import com.mangpo.ourpage.view.BaseActivity
import com.mangpo.ourpage.view.adpater.PhotoViewVPAdapter

class PhotoViewActivity : BaseActivity<ActivityPhotoViewBinding>(ActivityPhotoViewBinding::inflate) {
    private val args: PhotoViewActivityArgs by navArgs()

    private lateinit var photoViewVPAdapter: PhotoViewVPAdapter

    override fun initAfterBinding() {
        initAdapter()

        binding.photoViewCloseIv.setOnClickListener {
            finish()
        }

        binding.photoViewVp.currentItem = args.position
        binding.photoViewIndicator.setSelected(args.position)

        binding.photoViewVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.photoViewIndicator.setSelected(position)
            }
        })
    }

    private fun initAdapter() {
        photoViewVPAdapter = PhotoViewVPAdapter()
        photoViewVPAdapter.setData(args.photos.toList())
        binding.photoViewVp.adapter = photoViewVPAdapter
        binding.photoViewIndicator.count = photoViewVPAdapter.itemCount
    }
}