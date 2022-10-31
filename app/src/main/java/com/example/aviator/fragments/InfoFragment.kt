package com.example.aviator.fragments

import android.graphics.drawable.AnimationDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aviator.R
import com.example.aviator.databinding.FragmentInfoBinding

class InfoFragment : Fragment(R.layout.fragment_info) {

    companion object {
        const val TAG = "Info"
        fun newInstance() = InfoFragment()
    }

    private val binding by viewBinding(FragmentInfoBinding::bind)
    private val animRocket by lazy {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.anim_rocket
        ) as AnimationDrawable
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
}