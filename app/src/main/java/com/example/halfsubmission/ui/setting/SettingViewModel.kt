package com.example.halfsubmission.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.halfsubmission.data.preferences.SettingPreferences
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola pengaturan tema dan pengingat.
 *
 * @param pref Preferences untuk menyimpan pengaturan.
 */
class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {

    /**
     * Mengambil pengaturan tema.
     *
     * @return LiveData berisi status tema (gelap/terang).
     */
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    /**
     * Menyimpan pengaturan tema.
     *
     * @param isDarkModeActive True untuk tema gelap, false untuk tema terang.
     */
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    /**
     * Mengambil pengaturan pengingat.
     *
     * @return LiveData berisi status pengingat (aktif/non-aktif).
     */
    fun getReminderSettings(): LiveData<Boolean> {
        return pref.getReminderSetting().asLiveData()
    }

    /**
     * Menyimpan pengaturan pengingat.
     *
     * @param isReminderActive True untuk mengaktifkan pengingat, false untuk menonaktifkan.
     */
    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }
}
