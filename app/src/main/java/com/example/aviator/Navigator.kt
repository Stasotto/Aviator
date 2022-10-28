package com.example.aviator

import androidx.fragment.app.Fragment

fun Fragment.navigator() = requireActivity() as Navigator
interface Navigator {

    fun launchGameFieldFragment()
    fun launchSettingsFragment()
    fun launchInfoFragment()
    fun showToast(message: String)
    fun back()
}