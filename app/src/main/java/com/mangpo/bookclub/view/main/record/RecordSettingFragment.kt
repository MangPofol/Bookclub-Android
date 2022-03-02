package com.mangpo.bookclub.view.main.record

import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentRecordSettingBinding
import com.mangpo.bookclub.model.entities.RecordRequest
import com.mangpo.bookclub.model.entities.RecordUpdateRequest
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.model.remote.RecordResponse
import com.mangpo.bookclub.utils.ImgUtils.getAbsolutePathByBitmap
import com.mangpo.bookclub.utils.ImgUtils.uriToBitmap
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel

class RecordSettingFragment : BaseFragment<FragmentRecordSettingBinding>(FragmentRecordSettingBinding::inflate) {
    private val bookVm: BookViewModel by viewModels<BookViewModel>()
    private val postVm: PostViewModel by viewModels<PostViewModel>()
    private val args: RecordSettingFragmentArgs by navArgs()

    private lateinit var book: Book
    private lateinit var recordVerCreate: RecordRequest
    private lateinit var recordVerUpdate: RecordResponse

    override fun initAfterBinding() {
        if (args.mode=="CREATE" && ::recordVerCreate.isInitialized)
            bindRecordVerCreate()
        else if (args.mode=="CREATE" && !::recordVerCreate.isInitialized) {
            recordVerCreate = Gson().fromJson(args.record, RecordRequest::class.java)
        } else if (args.mode=="UPDATE" && ::recordVerUpdate.isInitialized)
            bindRecordVerUpdate()
        else {
            recordVerUpdate = Gson().fromJson(args.record, RecordResponse::class.java)
            bindRecordVerUpdate()
        }
        book = Gson().fromJson(args.book, Book::class.java)

        setMyEventListener()
        observe()
    }

    override fun onStop() {
        super.onStop()

        if (args.mode=="CREATE")
            recordVerCreate = setRecordVerCreate()
        else
            recordVerUpdate = setRecordVerUpdate()
    }

    private fun setMyEventListener() {
        binding.recordSettingTb.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.recordSettingCompleteTv.setOnClickListener {
            hideKeyboard()
            showLoadingDialog()

            if (!isNetworkAvailable(requireContext())) {  //1. 네트워크 상태 확인
                showNetworkSnackBar()
                dismissLoadingDialog()
            } else if (args.mode=="CREATE") {
                when {
                    book.id==null -> bookVm.createBook(book) //2. book.id 가 null -> 책 등록
                    recordVerCreate.postImgLocations.isEmpty() -> postVm.createRecord(setRecordVerCreate()) //3. 이미지 없는 기록 -> createRecord
                    else -> uploadPhotos()    //4. 이미지 있는 기록 -> uploadPost
                }
            } else {
                if (recordVerUpdate.postImgLocations.isEmpty()) //이미지가 없을 때
                    postVm.updatePost(recordVerUpdate.postId, setRecordUpdateRequest())
                else
                    uploadPhotosVerUpdate()
            }
        }
    }

    private fun bindRecordVerCreate() {
        binding.recordSettingLocationEt.setText(recordVerCreate.location)
        binding.recordSettingTimeEt.setText(recordVerCreate.readTime)
        binding.recordSettingLinkTitleEt.setText(recordVerCreate.hyperlinkTitle)
        binding.recordSettingLinkEt.setText(recordVerCreate.hyperlink)
    }

    private fun bindRecordVerUpdate() {
        binding.recordSettingLocationEt.setText(recordVerUpdate.location)
        binding.recordSettingTimeEt.setText(recordVerUpdate.readTime)
        binding.recordSettingLinkTitleEt.setText(recordVerUpdate.hyperlinkTitle)
        binding.recordSettingLinkEt.setText(recordVerUpdate.hyperlink)
    }

    private fun uploadPhotos() {
        val absolutePaths: ArrayList<String> = arrayListOf()
        for (img in recordVerCreate.postImgLocations) {
            absolutePaths.add(getAbsolutePathByBitmap(requireContext(), uriToBitmap(requireContext(), Uri.parse(img))))
        }
        postVm.uploadImgPaths(absolutePaths)
    }

    private fun uploadPhotosVerUpdate() {
        val absolutePaths: ArrayList<String> = arrayListOf()
        for (img in recordVerUpdate.postImgLocations) {
            if (!img.startsWith("https"))
                absolutePaths.add(getAbsolutePathByBitmap(requireContext(), uriToBitmap(requireContext(), Uri.parse(img))))
        }

        if (absolutePaths.isNotEmpty())
            postVm.uploadImgPaths(absolutePaths)
        else
            postVm.updatePost(recordVerUpdate.postId, setRecordUpdateRequest())
    }

    private fun setRecordVerCreate(): RecordRequest {
        recordVerCreate.bookId = book.id
        recordVerCreate.scope = "PRIVATE"
        recordVerCreate.location = binding.recordSettingLocationEt.text.toString()
        recordVerCreate.readTime = binding.recordSettingTimeEt.text.toString()
        recordVerCreate.hyperlinkTitle = binding.recordSettingLinkTitleEt.text.toString()
        recordVerCreate.hyperlink = binding.recordSettingLinkEt.text.toString()

        return recordVerCreate
    }

    private fun setRecordVerUpdate(): RecordResponse {
        recordVerUpdate.location = binding.recordSettingLocationEt.text.toString()
        recordVerUpdate.readTime = binding.recordSettingTimeEt.text.toString()
        recordVerUpdate.hyperlinkTitle = binding.recordSettingLinkTitleEt.text.toString()
        recordVerUpdate.hyperlink = binding.recordSettingLinkEt.text.toString()

        return recordVerUpdate
    }

    private fun setRecordUpdateRequest(): RecordUpdateRequest = RecordUpdateRequest(
        scope = recordVerUpdate.scope,
        isIncomplete = recordVerUpdate.isIncomplete,
        location = binding.recordSettingLocationEt.text.toString(),
        readTime = binding.recordSettingTimeEt.text.toString(),
        hyperlinkTitle = binding.recordSettingLinkTitleEt.text.toString(),
        hyperlink = binding.recordSettingLinkEt.text.toString(),
        title = recordVerUpdate.title,
        content = recordVerUpdate.content,
        postImgLocations = recordVerUpdate.postImgLocations,
        clubIdListForScope = recordVerUpdate.clubIdListForScope
    )

    private fun observe() {
        bookVm.createBookCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()

            if (code!=null) {
                dismissLoadingDialog()

                when (code) {
                    201 -> {
                        book = bookVm.newBook!!

                        if (recordVerCreate.postImgLocations.isEmpty())
                            postVm.createRecord(setRecordVerCreate())
                        else
                            uploadPhotos()
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })

        postVm.newRecord.observe(viewLifecycleOwner, Observer {
            dismissLoadingDialog()

            if (it==null)
                showSnackBar(getString(R.string.error_api))
            else {
                showToast(getString(R.string.msg_create_record_success))
                val action = RecordSettingFragmentDirections.actionRecordSettingFragmentToRecordDetailFragment(Gson().toJson(it), Gson().toJson(book))
                findNavController().safeNavigate(action)
            }
        })

        postVm.updatePostCode.observe(viewLifecycleOwner, Observer {
            dismissLoadingDialog()

            if (it==204) {
                showToast(getString(R.string.msg_create_record_success))

                if (args.prevPhotos!=null) {
                    val prevPhotos: List<String> = args.prevPhotos!!.toList()
                    val deletePhotos: ArrayList<String> = arrayListOf()

                    for (photos in prevPhotos) {
                        if (!recordVerUpdate.postImgLocations.contains(photos))
                            deletePhotos.add(photos)
                    }

                    if (deletePhotos.isNotEmpty())
                        postVm.deleteMultiplePhotos(deletePhotos)
                }

                recordVerUpdate = setRecordVerUpdate()

                val action = RecordSettingFragmentDirections.actionRecordSettingFragmentToRecordDetailFragment(Gson().toJson(recordVerUpdate), Gson().toJson(book))
                findNavController().safeNavigate(action)
            } else
                showSnackBar(getString(R.string.error_api))
        })

        postVm.uploadImgPaths.observe(viewLifecycleOwner, Observer {
            if (it==null || it.isEmpty()) {
                dismissLoadingDialog()
                showSnackBar(getString(R.string.error_api))
            } else {
                if (args.mode=="CREATE") {
                    recordVerCreate.postImgLocations = it
                    postVm.createRecord(setRecordVerCreate())
                } else {
                    val postImgLocations: ArrayList<String> = arrayListOf()

                    var itIdx: Int = 0
                    for (i in 0 until recordVerUpdate.postImgLocations.size) {
                        if (recordVerUpdate.postImgLocations[i].startsWith("https"))
                            postImgLocations.add(recordVerUpdate.postImgLocations[i])
                        else {
                            postImgLocations.add(it[itIdx++])
                        }
                    }

                    recordVerUpdate.postImgLocations = postImgLocations
                    postVm.updatePost(recordVerUpdate.postId, setRecordUpdateRequest())
                }
            }
        })
    }
}