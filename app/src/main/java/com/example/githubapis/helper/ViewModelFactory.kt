package com.example.githubapis.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapis.ui.detail.DetailViewModel
import com.example.githubapis.ui.favoriteUser.FavoriteUserViewModel
import com.example.githubapis.ui.main.MainViewModel
import com.example.githubapis.ui.settings.SettingPreferences
import com.example.githubapis.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val settingPreferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application,
            settingPreferences: SettingPreferences
        ): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, settingPreferences)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
