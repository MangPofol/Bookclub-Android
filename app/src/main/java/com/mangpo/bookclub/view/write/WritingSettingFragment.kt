package com.mangpo.bookclub.view.write

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mangpo.bookclub.databinding.FragmentWritingSettingBinding
import com.mangpo.bookclub.model.BookModel
import com.mangpo.bookclub.model.PostDetailModel
import com.mangpo.bookclub.view.LoadingDialogFragment
import com.mangpo.bookclub.view.main.MainActivity
import com.mangpo.bookclub.viewmodel.BookViewModel
import com.mangpo.bookclub.viewmodel.PostViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WritingSettingFragment(private val isUpdate: Boolean) : Fragment() {

    private lateinit var binding: FragmentWritingSettingBinding

    private val postVm: PostViewModel by sharedViewModel()
    private val bookVm: BookViewModel by sharedViewModel()
    private val loadingDialogFragment = LoadingDialogFragment()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("WritingSettingFragment", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WritingSettingFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("WritingSettingFragment", "onCreateView")
        binding = FragmentWritingSettingBinding.inflate(inflater, container, false)
        observe()

        bind()

        //완료 버튼 클릭 리스너
        binding.completeBtn.setOnClickListener {
            loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
            CoroutineScope(Dispatchers.Main).launch {
                (requireActivity() as MainActivity).hideKeyBord(requireView())
                val post = addPostData() //기록 데이터 저장하는 함수 호출

                if (isUpdate)   //수정하기
                    updatePost(post)
                else if (!isUpdate && post.book!!.id == null)  //새로운 책에 대해 기록하기
                    createBook(post)
                else    //기록 추가하기
                    createPost(post)
            }
        }

        //뒤로가기 버튼 클릭 리스너
        binding.backIvView.setOnClickListener {
            addPostData()
            (requireActivity() as MainActivity).onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WritingSettingFragment", "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("WritingSettingFragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WritingSettingFragment", "onResume")

//        initUI(post)
    }

    override fun onPause() {
        super.onPause()
        Log.d("WritingSettingFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("WritingSettingFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("WritingSettingFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WritingSettingFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("WritingSettingFragment", "onDetach")

        //데이터 초기화
//        bookVm.setBook(BookModel())
//        postVm.setPost(PostModel())
//        postVm.setPost(PostDetailModel())
    }

    //post 데이터를 화면에 보여주기
    private fun bind() {
        val post = postVm.getPost()!!
        Log.d("WritingSettingFragment", "bind $post")

        binding.readWhereEt.setText(post.location)
        binding.readTimeEt.setText(post.readTime)
        binding.linkTitleEt.setText(post.hyperlinkTitle)
        binding.linkEt.setText(post.hyperlink)
    }

    //책 등록 함수
    private fun createBook(post: PostDetailModel) {
        CoroutineScope(Dispatchers.Main).launch {
            //책 등록하기
            val code = CoroutineScope(Dispatchers.Main).async {
                bookVm.createBook(post.book!!)
            }

            if (code.await() == 201) {  //책이 등록됐으면
                post.book!!.id = bookVm.getBook()!!.id    //post 의 bookId 저장

                if (isUpdate)
                    updatePost(post)
                else
                    createPost(post)  //기록 추가 함수 호출
            } else if (code.await() == 400) {
                Toast.makeText(requireContext(), "책을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "책 등록 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //기록 추가 함수
    /*private fun createPost(post: PostModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val postDetail = postVm.createPost(post)

            loadingDialogFragment.dismiss()

            if (postDetail != null) {
                if (book.category == "BEFORE") {  //책 카테고리가 "읽고 싶은" 이면 "읽는 중" 으로 변경하기
                    bookVm.updateBook(book.id!!, "NOW")
                    initBook()  //읽는중, 읽고싶은 책 목록 업데이트
                }

                postVm.setPostDetail(postDetail)
                (requireActivity() as MainActivity).moveToPostDetail(book)
            } else {
                Toast.makeText(requireContext(), "게시글 업로드 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/
    private fun createPost(post: PostDetailModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val resPost = postVm.createPost(post)   //응답 데이터

            loadingDialogFragment.dismiss()

            if (resPost != null) {
//                val book = bookVm.getBook()!!
                val book = post.book!!
                if (book.category == "BEFORE") {  //책 카테고리가 "읽고 싶은" 이면 "읽는 중" 으로 업데이트
                    if (bookVm.updateBook(book.id!!, "NOW")) {
                        book.category = "NOW"    //책 카테고리를 "읽는중" 으로 변경
                        initBook()  //읽는중, 읽고싶은 책 목록 업데이트
                    }
                }

                resPost.book = book

//                postVm.setPostDetail(postDetail)
                //이동하면서 post, book 데이터 초기화
                postVm.setPost(PostDetailModel())
                bookVm.setBook(BookModel())
                Log.d("WritingSettingFragment", "createPost post -> ${postVm.getPost()}")
                Log.d("WritingSettingFragment", "createPost book -> ${bookVm.getBook()}")
                //저장된 post 데이터와 함께 PostDetailFragment 로 이동하기
                (requireActivity() as MainActivity).moveToPostDetail(post)
            } else {
                Toast.makeText(requireContext(), "게시글 업로드 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //기록 수정하기
    /*private fun updatePost(post: PostModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val isUpdate = postVm.updatePost(postVm.getPostDetail()!!.postId, post)

            if (isUpdate) {
                setPostDetail()

                val delImgList = arguments?.getStringArrayList("delImgList")
                if (delImgList!=null)
                    deleteMultiImg(delImgList)

                loadingDialogFragment.dismiss()
                (requireActivity() as MainActivity).moveToPostDetail(book)
            } else {
                loadingDialogFragment.dismiss()
                Toast.makeText(requireContext(), "게시글 수정 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/
    private fun updatePost(post: PostDetailModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val isUpdate = postVm.updatePost(postVm.getPostDetail()!!.postId!!, post)

            if (isUpdate) {
//                setPostDetail()

                val delImgList = arguments?.getStringArrayList("delImgList")
                if (delImgList!=null)
                    deleteMultiImg(delImgList)

                loadingDialogFragment.dismiss()
//                (requireActivity() as MainActivity).moveToPostDetail(book)
            } else {
                loadingDialogFragment.dismiss()
                Toast.makeText(requireContext(), "게시글 수정 중 오류 발생. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //기록 데이터 저장하는 함수
    /*private fun addPostData() {
        post.scope = "PRIVATE"

        if (binding.readWhereEt.text.isNotBlank())
            post.location = binding.readWhereEt.text.toString()
        else
//            post.location = null
            post.location = ""

        if (binding.readTimeEt.text.isNotBlank())
            post.readTime = binding.readTimeEt.text.toString()
        else
//            post.readTime = null
            post.readTime = ""

        if (binding.linkTitleEt.text.isNotBlank())
            post.hyperlinkTitle = binding.linkTitleEt.text.toString()
        else
//            post.hyperlinkTitle = null
            post.hyperlinkTitle = ""

        if (binding.linkEt.text.isNotBlank())
            post.hyperlink = binding.linkEt.text.toString()
        else
//            post.hyperlink = null
            post.hyperlink = ""
        postVm.setPost(post)
    }*/
    private fun addPostData(): PostDetailModel {
        val post = postVm.getPost()!!

        post.scope = "PRIVATE"

        if (binding.readWhereEt.text.isNotBlank())
            post.location = binding.readWhereEt.text.toString()
        else
            post.location = ""

        if (binding.readTimeEt.text.isNotBlank())
            post.readTime = binding.readTimeEt.text.toString()
        else
            post.readTime = ""

        if (binding.linkTitleEt.text.isNotBlank())
            post.hyperlinkTitle = binding.linkTitleEt.text.toString()
        else
            post.hyperlinkTitle = ""

        if (binding.linkEt.text.isNotBlank())
            post.hyperlink = binding.linkEt.text.toString()
        else
            post.hyperlink = ""

        postVm.setPost(post)

        return post
    }

    /*private fun setPostDetail() {
        val postDetail: PostDetailModel = postVm.getPostDetail()!!
        postDetail.scope = post.scope!!
        postDetail.isIncomplete = post.isIncomplete
        postDetail.title = post.title!!
        postDetail.content = post.content!!
        postDetail.location = post.location
        postDetail.readTime = post.readTime
        postDetail.hyperlinkTitle = post.hyperlinkTitle
        postDetail.hyperlink = post.hyperlink
        postDetail.postImgLocations = post.postImgLocations

        postVm.setPostDetail(postDetail)
    }*/

    private fun initBook() {
        CoroutineScope(Dispatchers.Main).launch {
            bookVm.requestBookList("BEFORE")
            bookVm.requestBookList("NOW")
        }
    }

    //다중 이미지 삭제
    private fun deleteMultiImg(imgList: ArrayList<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            postVm.deleteMultiImg(imgList)
        }
    }

    private fun observe() {
        /*bookVm.book.observe(viewLifecycleOwner, Observer {
            book = it
        })*/

        /*postVm.post.observe(viewLifecycleOwner, Observer {
            post = it
        })*/
    }

}