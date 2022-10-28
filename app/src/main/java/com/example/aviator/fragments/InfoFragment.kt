package com.example.aviator.fragments

import androidx.fragment.app.Fragment
import com.example.aviator.R

class InfoFragment : Fragment(R.layout.fragment_info) {

    companion object {
        const val TAG = "Info"
        fun newInstance() = InfoFragment()
    }
}