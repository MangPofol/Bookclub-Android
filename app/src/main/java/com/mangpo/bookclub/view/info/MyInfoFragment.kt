package com.mangpo.bookclub.view.info

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentMyInfoBinding
import com.mangpo.bookclub.model.entities.User
import com.mangpo.bookclub.model.remote.UserResponse
import com.mangpo.bookclub.utils.ImgUtils
import com.mangpo.bookclub.utils.isNetworkAvailable
import com.mangpo.bookclub.view.BaseFragment
import com.mangpo.bookclub.view.dialog.IntroduceDialogFragment
import com.mangpo.bookclub.viewmodel.UserViewModel
import gun0912.tedimagepicker.builder.TedImagePicker

class MyInfoFragment : BaseFragment<FragmentMyInfoBinding>(FragmentMyInfoBinding::inflate) {
    private val userVm: UserViewModel by activityViewModels<UserViewModel>()

    private lateinit var introduceDialogFragment: IntroduceDialogFragment

    //퍼미션 확인 후 콜백 리스너
    private var permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            goGallery()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
        }
    }

    override fun initAfterBinding() {
        setMyEventListener()
        initIntroduceDialogFragment()
        observe()

        (requireActivity() as InfoActivity).changeToolbar(getString(R.string.title_my_info), false)
    }

    override fun onResume() {
        super.onResume()

        if (!isNetworkAvailable(requireContext()))
            Snackbar.make(binding.root, getString(R.string.error_check_network), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.action_retry)) {
                getInfo()
            }
        else
            getInfo()
    }

    private fun setMyEventListener() {
        binding.myInfoProfileSettingIv.setOnClickListener {
            checkPermission()
        }

        binding.myInfoIntroduceTv.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putString("introduce", userVm.getUser()!!.introduce)
            introduceDialogFragment.arguments = bundle
            introduceDialogFragment.show(requireActivity().supportFragmentManager, null)
        }

        binding.myInfoGenreIv.setOnClickListener {
            goYourTaste()
        }

        binding.myInfoReadingStyleBtn.setOnClickListener {
            goYourTaste()
        }

        binding.myInfoGoalSettingBtn.setOnClickListener {
            val action = MyInfoFragmentDirections.actionMyInfoFragmentToGoalManagementFragment(Gson().toJson(userVm.getUser()))
            findNavController().navigate(action)
        }

        binding.myInfoChecklistSettingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_myInfoFragment_to_checklistManagementActivity2)
        }
    }

    private fun initIntroduceDialogFragment() {
        introduceDialogFragment = IntroduceDialogFragment()
        introduceDialogFragment.setMyDialogCallback(object : IntroduceDialogFragment.MyDialogCallback {
            override fun getIntroduce(introduce: String) {
                if (!isNetworkAvailable(requireContext()))
                    showNetworkSnackBar()
                else {
                    val user = userVm.getUser()!!
                    user.introduce = introduce
                    userVm.updateUser(userResponseToUser(user), user.userId)
                }
            }

        })
    }

    private fun getInfo() {
        userVm.getUserInfo()
        userVm.getTotalMemoCnt()
        userVm.getTotalBookCnt()
    }

    //카메라, 저장소 퍼미션 확인
    private fun checkPermission() {
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage(getString(R.string.error_permission_denied))
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check();
    }

    private fun goGallery() {
        TedImagePicker.with(requireContext())
            .title(R.string.title_photos)
            .backButton(R.drawable.ic_back)
            .buttonBackground(R.color.white)
            .buttonTextColor(R.color.primary)
            .savedDirectoryName("Ourpage")
            .start { uri ->
                userVm.uploadImgFile(ImgUtils.getAbsolutePathByBitmap(requireContext(), ImgUtils.uriToBitmap(requireContext(), uri)))
            }
    }

    private fun bindUser(user: UserResponse) {
        if (user.profileImgLocation!=null)
            Glide.with(requireView()).load(user.profileImgLocation).circleCrop().into(binding.myInfoProfileIv)
        else
            binding.myInfoProfileIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_default_profile))

        binding.myInfoNicknameTv.text = user.nickname
        binding.myInfoEmailTv.text = user.email

        if (user.introduce==null)
            binding.myInfoIntroduceTv.text = getString(R.string.msg_express_me)
        else
            binding.myInfoIntroduceTv.text = user.introduce

        if (user.genres.isEmpty()) {
            binding.myInfoGenreCg.removeAllViews()
            binding.myInfoGenreCg.visibility = View.INVISIBLE
            binding.myInfoSelectGenreTv.visibility = View.VISIBLE
        } else {
            binding.myInfoGenreCg.visibility = View.VISIBLE
            binding.myInfoSelectGenreTv.visibility = View.INVISIBLE
            addGenreChip(user.genres)
        }
    }

    private fun addGenreChip(genres: List<String>) {
        binding.myInfoGenreCg.removeAllViews()

        for (genre in genres) {
            val chip: Chip =
                layoutInflater.inflate(R.layout.chip_genre_my_info, binding.myInfoGenreCg, false) as Chip
            chip.text = genre
            binding.myInfoGenreCg.addView(chip)
        }
    }

    private fun goYourTaste() {
        val action = MyInfoFragmentDirections.actionMyInfoFragmentToYourTasteFragment(Gson().toJson(userVm.getUser()))
        findNavController().navigate(action)
    }

    private fun userResponseToUser(userResponse: UserResponse): User {
        val user = User(
            email = userResponse.email,
            nickname = userResponse.nickname,
            sex = userResponse.sex,
            birthdate = userResponse.birthdate,
            introduce = userResponse.introduce,
            style = userResponse.style,
            goal = userResponse.goal,
            genres = userResponse.genres
        )

        if (userVm.getImgPath()==null)
            user.profileImgLocation = userResponse.profileImgLocation
        else
            user.profileImgLocation = userVm.getImgPath()

        return user
    }

    private fun observe() {
        userVm.getUserCode.observe(viewLifecycleOwner, Observer {
            var code: Int? = if (it.hasBeenHandled)
                it.peekContent()
            else
                it.getContentIfNotHandled()

            when (code) {
                200 -> bindUser(userVm.getUser()!!)
                else -> Snackbar.make(requireView(), getString(R.string.error_api), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.action_retry)) { userVm.getUserInfo() }
            }
        })

        userVm.totalMemoCnt.observe(viewLifecycleOwner, Observer {
            binding.myInfoTotalRecordCntTv.text = "\t$it pages"
        })

        userVm.totalBookCnt.observe(viewLifecycleOwner, Observer {
            binding.myInfoTotalReadingBooksCntTv.text = "\t$it books"
        })

        userVm.uploadImgFileCode.observe(viewLifecycleOwner, Observer {
            val code = it.getContentIfNotHandled()

            if (code!=null) {
                when (code) {
                    200 -> {
                        val userResponse = userVm.getUser()!!
                        userVm.updateUser(userResponseToUser(userResponse), userResponse.userId)
                    }
                    else -> showSnackBar(getString(R.string.error_api))
                }
            }
        })

        userVm.updateUserCode.observe(viewLifecycleOwner, Observer {
            var code: Int? = if (it.hasBeenHandled)
                it.peekContent()
            else
                it.getContentIfNotHandled()

            when (code) {
                204 -> {
                    if (introduceDialogFragment.isAdded)
                        introduceDialogFragment.dismiss()

                    userVm.getUserInfo()
                }
                else -> showSnackBar(getString(R.string.error_api))
            }
        })
    }
}