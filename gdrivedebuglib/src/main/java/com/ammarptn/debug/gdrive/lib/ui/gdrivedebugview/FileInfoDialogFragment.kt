package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ammarptn.debug.gdrive.lib.R
import com.ammarptn.gdriverest.DriveServiceHelper
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_file_info_dialog.view.*

private const val DRIVE_ID = "driveId"
private const val MIME_TYPE = "mimeType"
private const val TITLE = "title"
private const val FILE_SIZE = "fileSize"
private const val LAST_UPDATE = "lastUpdate"
private const val DRIVE_PATH = "drivePath"


class FileInfoDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var mimeType: String? = null
    private var driveTitle: String? = null
    private var fileSize: String? = null
    private var driveLastUpdate: String? = null
    private var driveId: String? = null
    private var drivePath: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            driveId = it.getString(DRIVE_ID)
            drivePath = it.getString(DRIVE_PATH)
            mimeType = it.getString(MIME_TYPE)
            driveTitle = it.getString(TITLE)
            fileSize = it.getString(FILE_SIZE)
            driveLastUpdate = it.getString(LAST_UPDATE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_file_info_dialog, container, false)

        when (mimeType) {
            DriveServiceHelper.TYPE_GOOGLE_DRIVE_FOLDER -> {
                Glide.with(context!!).load(R.drawable.ic_folder_vd).into(rootView.icon)
                rootView.size.visibility = View.GONE
            }
            else -> {
                Glide.with(context!!).load(R.drawable.ic_file_vd).into(rootView.icon)
                rootView.size.visibility = View.VISIBLE
            }


        }

        rootView.title.text = driveTitle
        rootView.location.text = drivePath
        rootView.size.text = fileSize
        rootView.last_update.text = driveLastUpdate
        rootView.delete.setOnClickListener {
            listener?.onDelete(driveId!!)
            dismiss()
        }



        return rootView
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {

        fun onDelete(driveId: String)
    }

    companion object {

        @JvmStatic
        fun newInstance(driveId: String, drivePath: String, mimeType: String, title: String, fileSize: String?, lastMotifiedDate: String?) =
            FileInfoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(DRIVE_ID, driveId)
                    putString(DRIVE_PATH, drivePath)
                    putString(MIME_TYPE, mimeType)
                    putString(TITLE, title)
                    putString(FILE_SIZE, fileSize)
                    putString(LAST_UPDATE, lastMotifiedDate)
                }
            }
    }
}

