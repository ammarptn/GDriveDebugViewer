package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ammarptn.debug.gdrive.lib.R
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.dataClass.DriveHolder
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.CreateFolderFragment
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveItem
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.RecycleViewBaseItem
import com.ammarptn.gdriverest.DriveServiceHelper
import com.ammarptn.gdriverest.DriveServiceHelper.getGoogleDriveService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import com.google.api.client.http.InputStreamContent
import kotlinx.android.synthetic.main.gdrive_debug_view_fragment.*
import kotlinx.android.synthetic.main.gdrive_debug_view_fragment.view.*
import java.text.DecimalFormat


class GdriveDebugViewFragment : Fragment() {

    companion object {
        fun newInstance() = GdriveDebugViewFragment()
        private val REQUEST_CODE_SIGN_IN = 100
        private val OPEN_FILE_PICKER_REQUEST_CODE = 101
        private val REQUEST_READ_STORAGE = 102
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: GdriveDebugViewViewModel
    private lateinit var driveServiceHelper: DriveServiceHelper
    private lateinit var adapter: DriveItemListAdapter
    private lateinit var rootView: View


    private var drivePathHolder: ArrayList<DriveHolder?> = ArrayList()
    var recycleItemArrayList = ArrayList<RecycleViewBaseItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.gdrive_debug_view_fragment, container, false)



        drivePathHolder.add(DriveHolder(null, "Root"))
        rootView.folderList.hasFixedSize()
        rootView.folderList.layoutManager = GridLayoutManager(context, 4)


        adapter = DriveItemListAdapter(recycleItemArrayList, object : DriveItemListAdapter.addClickListener {
            override fun onFileLongClick(position: Int) {

                val driveItem = recycleItemArrayList[position] as DriveItem
                val infoDialogFragment = FileInfoDialogFragment.newInstance(driveItem.driveId!!, drivePathHolder.getPath(), driveItem.mimeType!!, driveItem.title!!, "Size: "+driveItem.fileSize!!.toLong().bytesToMeg(), "Last Update:"+driveItem.lastUpdate!!)
                infoDialogFragment.show(childFragmentManager, "driveInfoDialog")

            }

            override fun onFolderLongClick(position: Int) {
                val driveItem = recycleItemArrayList[position] as DriveItem
                val infoDialogFragment = FileInfoDialogFragment.newInstance(driveItem.driveId!!, drivePathHolder.getPath(), driveItem.mimeType!!, driveItem.title!!, driveItem.fileSize!!.toLong().bytesToMeg(), "Last Update:"+driveItem.lastUpdate!!)
                infoDialogFragment.show(childFragmentManager, "driveInfoDialog")

            }

            override fun onBackClick(position: Int) {
                Log.d("test", "back to " + drivePathHolder[drivePathHolder.size - 2]?.driveId)
                queryDrive(drivePathHolder[drivePathHolder.size - 2]?.driveId)

                drivePathHolder.removeAt(drivePathHolder.size - 1)

                updateTitle()
            }

            override fun onFolderClick(position: Int) {
                Log.d("test", "select to " + (recycleItemArrayList[position] as DriveItem).driveId)
                queryDrive((recycleItemArrayList[position] as DriveItem).driveId)

                drivePathHolder.add(DriveHolder((recycleItemArrayList[position] as DriveItem).driveId, (recycleItemArrayList[position] as DriveItem).title))
                updateTitle()

            }

            override fun onFileClick(position: Int) {

            }


        })

        rootView.add_button.setOnClickListener {

            if (rootView.create_folder.visibility == View.VISIBLE) {
                toggleMenu(false)
            } else {
                toggleMenu(true)
            }

        }

        rootView.create_folder.setOnClickListener {

            val createFolderFragment = CreateFolderFragment.newInstance()
            createFolderFragment.show(childFragmentManager, "createFolderDialog")
            toggleMenu(false)

        }


        rootView.upload_file_button.setOnClickListener {
            val checkSelfPermission = ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.READ_EXTERNAL_STORAGE)

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORAGE)
            } else {
                openFilePicker()
            }
            toggleMenu(false)
        }


        rootView.folderList.adapter = adapter



        rootView.folderList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0 && rootView.add_button.isShown()) {
                    rootView.add_button.hide()
                } else if (dy < 0 && !rootView.add_button.isShown()) {
                    rootView.add_button.show()

                }
            }

//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    rootView.add_button.show()
//                }
//                super.onScrollStateChanged(recyclerView, newState)
//            }

        })
        return rootView
    }

    private fun toggleMenu(isShow: Boolean) {
        if (isShow) {
            rootView.create_folder.visibility = View.VISIBLE
            rootView.upload_file.visibility = View.VISIBLE
        } else {
            rootView.create_folder.visibility = View.GONE
            rootView.upload_file.visibility = View.GONE

        }

    }

    fun ArrayList<DriveHolder?>.getPath(): String {
        val stringBuilder = java.lang.StringBuilder()
        stringBuilder.append("Path:")
        var listItem = this

        for (i in listItem.indices) {
            stringBuilder.append(listItem[i]?.driveTitle)
            if (i != (listItem.size)) {
                stringBuilder.append("/")
            }

        }
        return stringBuilder.toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_READ_STORAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker()
                }
            }
        }
    }

    fun canGoBack(): Boolean {
        return drivePathHolder.size == 1
    }

    fun onBackPressed() {
        if (drivePathHolder.size > 1) {
            queryDrive(drivePathHolder[drivePathHolder.size - 2]?.driveId)

            drivePathHolder.removeAt(drivePathHolder.size - 1)

            updateTitle()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "*/*"
        }

        startActivityForResult(intent, OPEN_FILE_PICKER_REQUEST_CODE)
    }

    private fun updateTitle() {
        var drivePath = StringBuilder()
        for (name in drivePathHolder) {
            drivePath.append(name?.driveTitle).append("/")
        }

        toolbar.title = drivePath.toString()
    }


    override fun onStart() {
        super.onStart()
        val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (lastSignedInAccount == null) {
            signIn()
        } else {

            driveServiceHelper = DriveServiceHelper(getGoogleDriveService(context, lastSignedInAccount, "DebugView"))



            queryDrive(drivePathHolder.last()?.driveId)

        }
        updateTitle()

    }

    private fun queryDrive(driveId: String?) {
        rootView.progress_bar.visibility = View.VISIBLE
        driveServiceHelper.queryFiles(driveId).addOnSuccessListener {
            rootView.progress_bar.visibility = View.GONE
            var newList = ArrayList<RecycleViewBaseItem>()

            if (drivePathHolder.size > 1) {
                newList.add(DriveItemConverter().addDriveActionItem("back", R.drawable.ic_arrow_back))
            }
            for (fileItem in it) {

                Log.d("test", "item id " + fileItem.id)
                newList.add(DriveItemConverter().addDriveItem(fileItem.id, fileItem.name, getFileOrFolderIcon(fileItem.mimeType), fileItem.mimeType, fileItem.size.toString(), fileItem.modifiedTime.toString()))

            }


            val folderListDiffUtil = FolderListDiffUtil(recycleItemArrayList, newList)
            val calculateDiff = DiffUtil.calculateDiff(folderListDiffUtil)

            recycleItemArrayList.clear()
            recycleItemArrayList.addAll(newList)

            calculateDiff.dispatchUpdatesTo(adapter)

        }
    }

    private fun getFileOrFolderIcon(mimeType: String): Int {
        return when (mimeType) {
            DriveServiceHelper.TYPE_GOOGLE_DRIVE_FOLDER -> {
                R.drawable.ic_folder_vd
            }
            else -> {
                R.drawable.ic_file_vd

            }
        }

    }

    val MEGABYTE = (1024L * 1024L).toDouble()
    fun Long.bytesToMeg(): String {
        return DecimalFormat("##.##").format(this / MEGABYTE) + "MB"
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
            OPEN_FILE_PICKER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    rootView.upload_progress_bar.visibility = View.VISIBLE
                    Toast.makeText(context, "Uploading", Toast.LENGTH_SHORT).show()
                    val uri = resultData.getData()
                    val contentResolver = context!!.contentResolver


                    var name = "file_name"
                    val cursor = contentResolver.query(uri!!, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        name = cursor.getString(nameIndex);
                    }
                    cursor?.close()

                    val extensionFromMimeType = MimeTypeMap.getSingleton()
                        .getExtensionFromMimeType(contentResolver.getType(uri))


                    val root: List<String>
                    if (drivePathHolder.last()?.driveId == null) {
                        root = listOf("root")
                    } else {

                        root = listOf(drivePathHolder.last()?.driveId!!)
                    }

                    val metadata = com.google.api.services.drive.model.File()
                        .setParents(root)
                        .setMimeType(extensionFromMimeType)
                        .setName(name)

                    val inputStreamContent = InputStreamContent(null, contentResolver.openInputStream(uri))


                    driveServiceHelper.uploadFile(metadata, inputStreamContent).addOnSuccessListener {
                        rootView.upload_progress_bar.visibility = View.GONE
                        if (drivePathHolder.last()?.driveId != null) {

                            queryDrive(drivePathHolder.last()?.driveId)

                        } else {
                            queryDrive(null)
                        }
                    }


                }

            }
        }

        super.onActivityResult(requestCode, resultCode, resultData)
    }


    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleSignInAccount ->


                driveServiceHelper = DriveServiceHelper(getGoogleDriveService(context, googleSignInAccount, "DebugView"))
                queryDrive(drivePathHolder.last()?.driveId)

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

    fun onDelete(driveId: String) {
        rootView.progress_bar.visibility = View.VISIBLE
        driveServiceHelper.deleteFolderFile(driveId)
            .addOnSuccessListener { queryDrive(drivePathHolder.last()?.driveId) }
    }

    fun onCreateFolder(folderName: String) {
        progress_bar.visibility = View.VISIBLE
        driveServiceHelper.createFolder(folderName, drivePathHolder.last()?.driveId).addOnSuccessListener {
            queryDrive(drivePathHolder.last()?.driveId)
//            drivePathHolder.add(DriveHolder(it.id, folderName))
//            updateTitle()

        }
    }

}
