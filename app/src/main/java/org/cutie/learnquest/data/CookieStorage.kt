package org.cutie.learnquest.data

import android.content.Context

class CookieStorage(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("cookie_storage", Context.MODE_PRIVATE);

    fun saveCookie(key: String, value: String) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getCookie(key: String): String? {
        return sharedPreferences.getString(key, null);
    }

    fun deleteCookie(key: String) {
        sharedPreferences.edit().apply {
            remove(key)
            commit()
        }
    }
}