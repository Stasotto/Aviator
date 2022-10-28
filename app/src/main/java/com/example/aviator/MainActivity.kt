package com.example.aviator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aviator.databinding.ActivityMainBinding
import com.example.aviator.databinding.SettingsLayoutBinding
import com.example.aviator.fragments.GameFieldFragment
import com.example.aviator.fragments.InfoFragment
import com.example.aviator.fragments.MenuFragment
import com.example.aviator.fragments.SettingsFragment

class MainActivity : AppCompatActivity(R.layout.activity_main), Navigator {

    private var musicStatus = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MenuFragment.newInstance(), MenuFragment.TAG)
            .commit()
    }

    override fun launchGameFieldFragment() {
        openFragment(GameFieldFragment.newInstance(musicStatus), GameFieldFragment.TAG)
    }

    override fun launchSettingsFragment() {
        openFragment(SettingsFragment.newInstance(musicStatus), SettingsFragment.TAG)
        SettingsFragment.getMusicState {
            musicStatus = it
        }
    }

    override fun launchInfoFragment() {
        openFragment(InfoFragment.newInstance(), InfoFragment.TAG)
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun back() {
        onBackPressed()
    }

    private fun openFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(tag)
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }
}