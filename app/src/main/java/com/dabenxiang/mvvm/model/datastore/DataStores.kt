package com.dabenxiang.mvvm.model.datastore

import android.content.Context
import android.text.TextUtils
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.dabenxiang.mvvm.DATA_STORE_NAME
import com.dabenxiang.mvvm.model.vo.ProfileItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class DataStores(context: Context, private val gson: Gson) {

    companion object {
        val PROFILE_PREF_KEY = preferencesKey<String>("PROFILE")
    }

    private val dataStore = context.createDataStore(name = DATA_STORE_NAME)

    val profileFlow: Flow<ProfileItem> = dataStore.data
        .catch {
            if (it is IOException) {
                Timber.e("Error reading preferences: $it")
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            val profilePref = it[PROFILE_PREF_KEY] ?: ""
            if (!TextUtils.isEmpty(profilePref)) {
                gson.fromJson(profilePref, ProfileItem::class.java)
            } else {
                ProfileItem()
            }
        }

    suspend fun setupProfile(profileItem: ProfileItem) {
        dataStore.edit {
            it[PROFILE_PREF_KEY] = gson.toJson(profileItem)
        }
    }

}