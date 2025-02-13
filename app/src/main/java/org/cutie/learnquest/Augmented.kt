package org.cutie.learnquest

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.unity3d.player.UnityPlayerGameActivity

class Augmented: UnityPlayerGameActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mUnityPlayer?.destroy()
                finish()
            }
        })
    }

    // Method to handle Unity unloading
    fun onUnityUnloaded() {
        runOnUiThread {
            mUnityPlayer?.destroy()
            finish()
        }
    }
}