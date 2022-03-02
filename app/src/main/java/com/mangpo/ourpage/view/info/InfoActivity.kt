package com.mangpo.ourpage.view.info

import android.view.View
import androidx.navigation.findNavController
import com.mangpo.ourpage.R
import com.mangpo.ourpage.databinding.ActivityInfoBinding
import com.mangpo.ourpage.view.BaseActivity

class InfoActivity : BaseActivity<ActivityInfoBinding>(ActivityInfoBinding::inflate) {
    override fun initAfterBinding() {
        binding.myInfoTb.setNavigationOnClickListener {
            if (findNavController(binding.myInfoNavHostFragment.id).currentDestination?.id == R.id.myInfoFragment)
                finish()
            else
                findNavController(binding.myInfoNavHostFragment.id).popBackStack()
        }

        binding.myInfoTbActionTv.setOnClickListener {
            when (findNavController(binding.myInfoNavHostFragment.id).currentDestination?.id) {
                R.id.yourTasteFragment -> (supportFragmentManager.findFragmentById(binding.myInfoNavHostFragment.id)?.childFragmentManager?.fragments?.get(0) as YourTasteFragment).updateUser()
                R.id.goalManagementFragment -> (supportFragmentManager.findFragmentById(binding.myInfoNavHostFragment.id)?.childFragmentManager?.fragments?.get(0) as GoalManagementFragment).updateUser()
            }
        }
    }

    fun changeToolbar(title: String, actionTvVisibility: Boolean) {
        binding.myInfoTb.title = title

        if (actionTvVisibility)
            binding.myInfoTbActionTv.visibility = View.VISIBLE
        else
            binding.myInfoTbActionTv.visibility = View.INVISIBLE
    }
}