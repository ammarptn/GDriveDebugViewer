package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.ammarptn.debug.gdrive.lib.R
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveItem
import com.ammarptn.gdriverest.DriveServiceHelper
import com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import kotlinx.android.synthetic.main.gdrive_debug_view_fragment.view.*
import java.util.*


class GdriveDebugViewFragment : Fragment() {

    companion object {
        fun newInstance() = GdriveDebugViewFragment()
        private val REQUEST_CODE_SIGN_IN = 100
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: GdriveDebugViewViewModel
    private lateinit var driveServiceHelper: DriveServiceHelper
    private lateinit var adapter: DriveListAdapter
    private var driveId: String? = null
    var recycleItemArrayList = ArrayList<DriveItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.gdrive_debug_view_fragment, container, false)

        rootView.folderList.hasFixedSize()
        rootView.folderList.layoutManager = GridLayoutManager(context, 5)
        recycleItemArrayList.add(DriveItem("test", "testFolder", true, 0))
        recycleItemArrayList.add(DriveItem("test", "testFolder", true, 0))
        recycleItemArrayList.add(DriveItem("test", "testFile", false, 20))

        adapter = DriveListAdapter(recycleItemArrayList, object : DriveListAdapter.addOnClickListener {
            override fun onClick(position: Int) {

                queryDrive(recycleItemArrayList.get(position).fileId)
            }

        })

        rootView.folderList.adapter = adapter

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (lastSignedInAccount == null) {
            signIn()
        } else {
            Toast.makeText(context, "Welcome back", Toast.LENGTH_SHORT).show()
            driveServiceHelper = DriveServiceHelper(getGoogleDriveService(context, lastSignedInAccount, "DebugView"))



            queryDrive(driveId)

        }

    }

    private fun queryDrive(driveId: String?) {
        driveServiceHelper.queryFiles(driveId).addOnSuccessListener {

            var newList = ArrayList<DriveItem>()


            for (fileItem in it) {
                newList.add(DriveItem(fileItem.id, fileItem.name, true, fileItem.size))
            }

            val folderListDiffUtil = FolderListDiffUtil(recycleItemArrayList, newList)
            val calculateDiff = DiffUtil.calculateDiff(folderListDiffUtil)

            recycleItemArrayList.clear()
            recycleItemArrayList.addAll(newList)

            calculateDiff.dispatchUpdatesTo(adapter)

        }
    }

    private fun signIn() {

        mGoogleSignInClient = buildGoogleSignInClient()
        startActivityForResult(buildGoogleSignInClient().getSignInIntent(), REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        when (requestCode) {
            REQUEST_CODE_SIGN_IN -> if (resultCode == Activity.RESULT_OK && resultData != null) {
                handleSignInResult(resultData)
            }
        }

        super.onActivityResult(requestCode, resultCode, resultData)
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleSignInAccount ->


                driveServiceHelper = DriveServiceHelper(getGoogleDriveService(context, googleSignInAccount, "DebugView"))


            }
            .addOnFailureListener { e -> Log.e(TAG, "Unable to sign in.", e) }
    }

    private fun buildGoogleSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Drive.SCOPE_FILE)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context!!, signInOptions)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GdriveDebugViewViewModel::class.java)

    }

}
