package com.ammarptn.debug.gdrive.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.FileInfoDialogFragment
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.GdriveDebugViewFragment
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.CreateFolderFragment

class GDriveDebugViewActivity : AppCompatActivity(),FileInfoDialogFragment.OnFragmentInteractionListener,CreateFolderFragment.OnFragmentInteractionListener {



    lateinit var fragment: GdriveDebugViewFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gdrive_debug_view_activity)
        if (savedInstanceState == null) {
            fragment = GdriveDebugViewFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    override fun onBackPressed() {

        if (fragment.isAdded) {
            if (fragment.canGoBack()) {
                super.onBackPressed()
            } else {
                fragment.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }


    }

    override fun onDelete(driveId: String) {

        fragment.onDelete(driveId)
    }

    override fun onCreateFolderDialog(folderName: String) {
        fragment.onCreateFolder(folderName)
    }
}
