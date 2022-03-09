package com.mangpo.bookclub.view.main.record

import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordDetailBinding
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.model.remote.RecordResponse
import com.mangpo.bookclub.utils.PrefsUtils
import com.mangpo.bookclub.utils.convertDpToPx
import com.mangpo.bookclub.utils.getDeviceWidth
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.adpater.LinkVerDetailRVAdapter
import com.mangpo.bookclub.view.adpater.RecordPhotoVPAdapter
import com.mangpo.bookclub.view.dialog.ActionDialogFragment
import com.mangpo.bookclub.viewmodel.PostViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordDetailFragment : BaseFragment<FragmentRecordDetailBinding>(FragmentRecordDetailBinding::inflate) {
    private val postVm: PostViewModel by viewModels<PostViewModel>()
    private val args: RecordDetailFragmentArgs by navArgs()

    private lateinit var recordPhotoVPAdapter: RecordPhotoVPAdapter
    private lateinit var actionDialogFragment: ActionDialogFragment
    private lateinit var record: RecordResponse
    private lateinit var book: Book

    override fun initAfterBinding() {
        setMyEventListener()
        initAdapter()
        initActionDialogFragment()
        observe()

        record = Gson().fromJson(args.record, RecordResponse::class.java)
        book = Gson().fromJson(args.book, Book::class.java)

        bind()
    }

    private fun initAdapter() {
        recordPhotoVPAdapter = RecordPhotoVPAdapter()
        recordPhotoVPAdapter.setMyCLickListener(object : RecordPhotoVPAdapter.MyClickListener {
            override fun goPhotoActivity(photos: List<String>, position: Int) {
                val action = RecordDetailFragmentDirections.actionRecordDetailFragmentToPhotoViewActivity(photos.toTypedArray(), position)
                findNavController().navigate(action)
            }

        })
        binding.recordDetailPhotoVp.adapter = recordPhotoVPAdapter
    }

    private fun initActionDialogFragment() {
        actionDialogFragment = ActionDialogFragment()

        val bundle: Bundle = Bundle()
        bundle.apply {
            putString("title", getString(R.string.msg_delete_memo))
            putString("desc", getString(R.string.msg_delete_memo_desc))
        }
        actionDialogFragment.arguments = bundle

        actionDialogFragment.setMyDialogCallback(object : ActionDialogFragment.MyDialogCallback {
            override fun delete() {
                showLoadingDialog()

                if (!isNetworkAvailable(requireContext())) {
                    dismissLoadingDialog()
                    showNetworkSnackBar()
                } else
                    postVm.deletePost(record.postId)
            }
        })
    }

    private fun setMyEventListener() {
        binding.recordDetailPhotoVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.recordDetailIndicator.setSelected(position)
            }
        })

        binding.recordDetailUpdateBtn.setOnClickListener {
            PrefsUtils.setTempRecord("")
            val action = RecordDetailFragmentDirections.actionRecordDetailFragmentToRecordFragment("UPDATE", Gson().toJson(record), Gson().toJson(book))
            findNavController().navigate(action)
        }

        binding.recordDetailDeleteBtn.setOnClickListener {
            actionDialogFragment.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun bind() {
        binding.recordDetailBookNameTv.text = book.name

        val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val date: LocalDateTime = LocalDateTime.parse(record.modifiedDate.substring(0, 19), formatter1)
        binding.recordDetailRecordDateTv.text = date.format(formatter2)

        binding.recordDetailRecordTitleTv.text = record.title

        binding.recordDetailRecordContentTv.text = record.content

        if (record.postImgLocations.isEmpty()) {
            binding.recordDetailPhotoVp.visibility = View.GONE
            binding.recordDetailIndicator.visibility = View.GONE
        } else {
            val size = getDeviceWidth() - convertDpToPx(requireContext(), 40)
            val params = binding.recordDetailPhotoVp.layoutParams
            params.width = size
            params.height = size
            binding.recordDetailPhotoVp.layoutParams = params

            recordPhotoVPAdapter.setData(record.postImgLocations)
            binding.recordDetailIndicator.count = recordPhotoVPAdapter.itemCount
            binding.recordDetailPhotoVp.visibility = View.VISIBLE
            binding.recordDetailIndicator.visibility = View.VISIBLE
        }

        if (record.location.isBlank()) {
            binding.recordDetailLocationTv.text = getString(R.string.msg_input_location)
            binding.recordDetailLocationTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
        } else {
            binding.recordDetailLocationTv.text = record.location
            binding.recordDetailLocationTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }

        if (record.readTime.isBlank()) {
            binding.recordDetailTimeTv.text = getString(R.string.msg_input_time)
            binding.recordDetailTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))
        } else {
            binding.recordDetailTimeTv.text = record.readTime
            binding.recordDetailTimeTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }

        if (record.linkResponseDtos.isEmpty()) {
            binding.recordDetailLinkHintTv.text = getString(R.string.msg_input_link)
            binding.recordDetailLinkHintTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_dark))

            binding.recordDetailLinkHintTv.visibility = View.VISIBLE
            binding.recordDetailLinkHintIv.visibility = View.VISIBLE
            binding.recordDetailLinkRv.visibility = View.GONE
        } else {
            val linkVerDetailRVAdapter: LinkVerDetailRVAdapter = LinkVerDetailRVAdapter()
            linkVerDetailRVAdapter.setData(record.linkResponseDtos)
            binding.recordDetailLinkRv.adapter = linkVerDetailRVAdapter

            binding.recordDetailLinkHintTv.visibility = View.GONE
            binding.recordDetailLinkHintIv.visibility = View.GONE
            binding.recordDetailLinkRv.visibility = View.VISIBLE
        }
    }

    private fun observe() {
        postVm.deletePostCode.observe(viewLifecycleOwner, Observer {
            Log.d("RecordDetailFragment", "deletePostCode Observe! deletePostCode -> $it")

            if (it==204) {
                if (record.postImgLocations.isEmpty()) {
                    dismissLoadingDialog()
                    findNavController().popBackStack()
                } else
                    postVm.deleteMultiplePhotos(record.postImgLocations)
            } else {
                dismissLoadingDialog()
                showSnackBar(getString(R.string.error_api))
            }
        })

        postVm.deletePhotosCode.observe(viewLifecycleOwner, Observer {
            Log.d("RecordDetailFragment", "deletePhotosCode Observe! deletePhotosCode -> $it")
            showToast(getString(R.string.msg_delete_record))

            if (record.postImgLocations.isNotEmpty())
                postVm.deleteMultiplePhotos(record.postImgLocations)

            dismissLoadingDialog()
            findNavController().popBackStack()
        })
    }
}
