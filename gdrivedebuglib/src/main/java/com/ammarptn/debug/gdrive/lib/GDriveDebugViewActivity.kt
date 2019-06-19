package com.ammarptn.debug.gdrive.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.GdriveDebugViewFragment

class GDriveDebugViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gdrive_debug_view_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GdriveDebugViewFragment.newInstance())
                .commitNow()
        }
    }

}
