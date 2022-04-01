package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.HomeFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarItemView
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var mNavbar: BottomNavigationView
    val fragmentManager:FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavbar = findViewById(R.id.bottom_navbar)
        mNavbar.setOnItemSelectedListener { it ->
            var fragmentToShow:Fragment? = null
            when(it.itemId) {
                R.id.action_home -> { fragmentToShow = HomeFragment() }
                R.id.action_profile -> { fragmentToShow = ProfileFragment() }
                R.id.action_compose -> { fragmentToShow = ComposeFragment() }
            }
            fragmentToShow?.let {
                fragmentManager.beginTransaction().replace(R.id.flContainer,fragmentToShow).commit()
            }
            true
        }
        mNavbar.selectedItemId = R.id.action_home
    }

    companion object {
        const val TAG = "mainActivity"
    }
}