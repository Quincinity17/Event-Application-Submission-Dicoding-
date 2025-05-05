package com.example.halfsubmission.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.halfsubmission.R
import com.example.halfsubmission.data.preferences.SettingPreferences
import com.example.halfsubmission.data.preferences.dataStore
import com.example.halfsubmission.databinding.ActivityMainBinding
import com.example.halfsubmission.ui.setting.SettingViewModel
import com.example.halfsubmission.ui.setting.SettingViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Activity utama yang menampilkan BottomNavigationView dan mengelola navigasi antar fragment.
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Konfigurasi AppBar dan BottomNavigationView
        setupActionBarWithNavController(navController, AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_Upcoming,
            R.id.navigation_Finished,
            R.id.navigation_bookmark,
            R.id.navigation_setting
        )))
        navView.setupWithNavController(navController)

        // Inisialisasi ViewModel untuk pengaturan tema
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(SettingPreferences.getInstance(applicationContext.dataStore)))[SettingViewModel::class.java]

        // Observasi pengaturan tema
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Atur listener untuk item BottomNavigationView
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> navController.navigate(R.id.navigation_home)
                R.id.navigation_Upcoming -> navController.navigate(R.id.navigation_Upcoming)
                R.id.navigation_Finished -> navController.navigate(R.id.navigation_Finished)
                R.id.navigation_bookmark -> navController.navigate(R.id.navigation_bookmark)
                R.id.navigation_setting -> navController.navigate(R.id.navigation_setting)
                else -> false
            } != false
        }
    }
}
