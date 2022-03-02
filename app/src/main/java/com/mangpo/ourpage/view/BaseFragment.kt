package com.mangpo.ourpage.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.mangpo.ourpage.R
import com.mangpo.ourpage.utils.Inflate
import com.mangpo.ourpage.view.dialog.LoadingDialogFragment

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    private val loadingDialogFragment = LoadingDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initAfterBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun initAfterBinding()

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun showNetworkSnackBar() {
        Snackbar.make(binding.root, getString(R.string.error_check_network), Snackbar.LENGTH_LONG)
    }

    fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }

    fun hideKeyboard() {
        val mInputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun showLoadingDialog() {
        loadingDialogFragment.show(requireActivity().supportFragmentManager, null)
    }

    fun dismissLoadingDialog() {
        loadingDialogFragment.dismiss()
    }

    fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run {
            navigate(direction)
        }
    }
}