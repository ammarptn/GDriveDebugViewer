package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ammarptn.debug.gdrive.lib.R
import kotlinx.android.synthetic.main.fragment_create_folder.*
import kotlinx.android.synthetic.main.fragment_create_folder.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateFolderFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateFolderFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateFolderFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_create_folder, container, false)

        rootView.cancel.setOnClickListener {
            dismiss()
        }

        rootView.submit.setOnClickListener {
            if (folder_name_edit_text.text.toString().isEmpty()) {
                Toast.makeText(context, "Please enter folder name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listener?.onCreateFolderDialog(folder_name_edit_text.text.toString())
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

        fun onCreateFolderDialog(folderName: String)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CreateFolderFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
