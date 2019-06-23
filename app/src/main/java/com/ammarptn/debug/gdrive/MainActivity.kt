package com.ammarptn.debug.gdrive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ammarptn.debug.gdrive.lib.GDriveDebugViewActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val openActivity = Intent(applicationContext, GDriveDebugViewActivity::class.java)
        startActivity(openActivity)
        finish()
    }
}
