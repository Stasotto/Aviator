package com.example.aviator.fragments

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aviator.R
import com.example.aviator.databinding.SettingsLayoutBinding
import com.example.aviator.navigator

typealias MusicStateListener = (Boolean) -> Unit
typealias VibrationStateListener = (Boolean) -> Unit

class SettingsFragment : Fragment(R.layout.settings_layout) {

    companion object {
        private const val MUSIC = "music"
        private const val VIBRATION = "Vibration"
        private var mListener: MusicStateListener? = null
        private var vListener: VibrationStateListener? = null

        const val TAG = "Settings"
        fun newInstance(musicStatus: Boolean, vibrationStatus: Boolean) = SettingsFragment().apply {
            arguments = bundleOf(MUSIC to musicStatus, VIBRATION to vibrationStatus)
        }

        fun getMusicState(listener: MusicStateListener) {
            mListener = listener
        }

        fun getVibrationState(listener: VibrationStateListener) {
            vListener = listener
        }
    }

    private var musicStatus: Boolean? = null
    private var vibrationState: Boolean? = null
    private val binding by viewBinding(SettingsLayoutBinding::bind)
    private val animRocket by lazy {
        ContextCompat.getDrawable(
            requireContext(),
            R.drawable.anim_rocket
        ) as AnimationDrawable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rocketImg.background = animRocket
        musicStatus = requireArguments().getBoolean(MUSIC)
        vibrationState = requireArguments().getBoolean(VIBRATION)
        setMusicStatusImg()
        setVibrationStatusImg()
        binding.soundsImg.setOnClickListener {
            if (musicStatus == true) {
                musicStatus = false
                navigator().showToast(getString(R.string.soundsOFF))
            } else {
                musicStatus = true
                navigator().showToast(getString(R.string.soundsON))
            }
            setMusicStatusImg()
            mListener?.invoke(musicStatus!!)
        }
        binding.vibrationImg.setOnClickListener {
            if (vibrationState == true) {
                vibrationState = false
                navigator().showToast(getString(R.string.vibrationOFF))
            } else {
                vibrationState = true
                navigator().showToast(getString(R.string.vibrationON))
            }
            setVibrationStatusImg()
            vListener?.invoke(vibrationState!!)
        }
    }

    override fun onStart() {
        super.onStart()
        animRocket.start()
    }

    override fun onStop() {
        super.onStop()
        animRocket.stop()
    }

    private fun setMusicStatusImg() {
        with(binding) {
            if (musicStatus!!) {
                soundsImg.setImageResource(R.drawable.ic_baseline_volume_up_24)
            } else {
                soundsImg.setImageResource(R.drawable.ic_baseline_volume_off_24)
            }
        }
    }

    private fun setVibrationStatusImg() {
        with(binding) {
            if (vibrationState!!) {
                vibrationImg.setImageResource(R.drawable.ic_baseline_vibration_24)
            } else {
                vibrationImg.setImageResource(R.drawable.ic_baseline_no_cell_24)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mListener = null
    }
}