package com.example.halfsubmission.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.halfsubmission.data.preferences.SettingPreferences
import com.example.halfsubmission.data.preferences.dataStore
import com.example.halfsubmission.databinding.FragmentSettingBinding
import java.util.concurrent.TimeUnit

/**
 * Fragment untuk mengatur tema dan pengingat harian.
 */
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingViewModel: SettingViewModel
    private val workManager by lazy { WorkManager.getInstance(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        // Observasi pengaturan tema dan pengingat
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = isDarkModeActive
        }

        settingViewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            binding.switchReminder.isChecked = isReminderActive
        }

        // Atur listener untuk switch tema dan pengingat
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked -> settingViewModel.saveThemeSetting(isChecked) }
        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveReminderSetting(isChecked)
            if (isChecked) startDailyReminder() else cancelDailyReminder()
        }
    }

    /**
     * Memulai pengingat harian.
     */
    private fun startDailyReminder() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val dailyWorkRequest = PeriodicWorkRequest.Builder(EventReminderWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork("DailyEventReminder", ExistingPeriodicWorkPolicy.UPDATE, dailyWorkRequest)
        Toast.makeText(requireContext(), "Daily Reminder Aktif!", Toast.LENGTH_SHORT).show()
    }

    /**
     * Membatalkan pengingat harian.
     */
    private fun cancelDailyReminder() {
        workManager.cancelUniqueWork("DailyEventReminder")
        Toast.makeText(requireContext(), "Daily Reminder Dinonaktifkan!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
