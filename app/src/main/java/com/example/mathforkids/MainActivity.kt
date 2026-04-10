package com.example.mathforkids

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mathforkids.databinding.ActivityMainBinding
import com.example.mathforkids.ui.HomeFragment
import com.example.mathforkids.ui.QuizFragment
import com.example.mathforkids.ui.SettingsFragment
import com.example.mathforkids.ui.StatisticsFragment

class MainActivity : AppCompatActivity(), MainNavigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        applyThemePreference()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    showScreen(HomeFragment())
                    true
                }

                R.id.menu_quiz -> {
                    showScreen(QuizFragment())
                    true
                }

                R.id.menu_stats -> {
                    showScreen(StatisticsFragment())
                    true
                }

                R.id.menu_settings -> {
                    showScreen(SettingsFragment())
                    true
                }

                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.menu_home
        }
    }

    override fun openQuiz() {
        binding.bottomNavigation.selectedItemId = R.id.menu_quiz
    }

    override fun openStats() {
        binding.bottomNavigation.selectedItemId = R.id.menu_stats
    }

    override fun openSettings() {
        binding.bottomNavigation.selectedItemId = R.id.menu_settings
    }

    override fun refreshTheme() {
        applyThemePreference()
        recreate()
    }

    private fun showScreen(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun applyThemePreference() {
        val mode = if (QuizRepository.isDarkModeEnabled(this)) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}

interface MainNavigator {
    fun openQuiz()
    fun openStats()
    fun openSettings()
    fun refreshTheme()
}
