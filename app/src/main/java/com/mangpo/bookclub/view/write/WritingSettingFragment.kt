package com.mangpo.bookclub.view.write

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mangpo.bookclub.databinding.FragmentWritingSettingBinding
import com.mangpo.bookclub.model.PostModel
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WritingSettingFragment : Fragment() {

    private lateinit var binding: FragmentWritingSettingBinding
    private val postVm: PostViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WritingSettingFragment", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWritingSettingBinding.inflate(inflater, container, false)
        Log.d("WritingSettingFragment", "onCreateView")

        binding.completeBtn.setOnClickListener {
            (requireActivity() as MainActivity).hideKeyBord(requireView())
            addRecordData()

            when {
                postVm.getPost()!!.postImgLocations.size == 1 -> {
                    uploadImg(postVm.getPost()!!.postImgLocations[0])
                }
                postVm.getPost()!!.postImgLocations.size > 1 -> {
                    uploadMultiImg(postVm.getPost()!!.postImgLocations)
                }
                else -> {
                    createPost(postVm.getPost()!!)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WritingSettingFragment", "onViewCreated")
    }

    private fun createPost(post: PostModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val postDetail = postVm.createPost(post)

            if (postDetail != null) {
                postVm.clearImg()
                postVm.setPost(null)
                Log.d("WritingSettingFragment", "CreatePost is success!")
                (parentFragment as PostFragment).moveToPostDetail()
            } else {
                Log.d("WritingSettingFragment", "CreatePost is fail!")
                Toast.makeText(requireContext(), "게시글 업로드 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun uploadImg(imgPath: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val path = CoroutineScope(Dispatchers.IO).async {
                postVm.uploadImg(imgPath)
            }

            if (path.await() != null) {
                postVm.getPost()!!.postImgLocations = listOf(path.await()!!)
                createPost(postVm.getPost()!!)
            } else {
                Log.d("WritingSettingFragment", "uploadImg error!")
            }
        }
    }

    private fun uploadMultiImg(imgPaths: List<String>) {
        CoroutineScope(Dispatchers.Main).launch {
            val path = CoroutineScope(Dispatchers.IO).async {
                postVm.uploadMultiImg(imgPaths)
            }

            if (path.await() != null) {
                postVm.getPost()!!.postImgLocations = path.await()!!
                createPost(postVm.getPost()!!)
            } else {
                Log.d("WritingSettingFragment", "uploadMultiImg error!")
            }
        }
    }

    private fun addRecordData() {
        postVm.getPost()!!.scope = "PRIVATE"
        if (binding.readWhereEt.text.isNotBlank())
            postVm.getPost()!!.location = binding.readWhereEt.text.toString()
        if (binding.readTimeEt.text.isNotBlank())
            postVm.getPost()!!.readTime = binding.readTimeEt.text.toString()
        if (binding.linkEt.text.isNotBlank())
            postVm.getPost()!!.hyperlink = binding.linkEt.text.toString()
    }
}