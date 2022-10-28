package com.example.aviator.fragments

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aviator.R
import com.example.aviator.databinding.FragmentMenuBinding
import com.example.aviator.navigator

class MenuFragment : Fragment(R.layout.fragment_menu) {

    companion object {
        const val TAG = "Menu"
        fun newInstance() = MenuFragment()
    }

    private val binding by viewBinding(FragmentMenuBinding::bind)
    private val animRocket by lazy {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.anim_rocket
        ) as AnimationDrawable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
    }

    override fun onStart() {
        super.onStart()
        binding.rocketImg.background = animRocket
        animRocket.start()
    }

    override fun onStop() {
        super.onStop()
        animRocket.stop()
    }

    private fun setUpClickListeners() = with(binding) {
        playBtn.setOnClickListener {
            navigator().launchGameFieldFragment()
        }
        settingsBtn.setOnClickListener {
            navigator().launchSettingsFragment()
        }
        infoBtn.setOnClickListener {
            navigator().launchInfoFragment()
        }
    }
}