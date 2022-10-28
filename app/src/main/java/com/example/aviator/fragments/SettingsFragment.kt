package com.example.aviator.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aviator.R
import com.example.aviator.databinding.SettingsLayoutBinding

typealias MusicStateListener = (Boolean) -> Unit

class SettingsFragment : Fragment(R.layout.settings_layout) {

    companion object {
        private const val MUSIC = "music"
        private var mListener: MusicStateListener? = null
        const val TAG = "Settings"
        fun newInstance(musicStatus: Boolean) = SettingsFragment().apply {
            arguments = bundleOf(MUSIC to musicStatus)
        }

        fun getMusicState(listener: MusicStateListener) {
            mListener = listener
        }
    }

    private val binding by viewBinding(SettingsLayoutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switch1.isChecked = requireArguments().getBoolean(MUSIC)
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            mListener?.invoke(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mListener = null
    }
}