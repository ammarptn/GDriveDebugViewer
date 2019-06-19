package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview

import androidx.recyclerview.widget.DiffUtil
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveItem

class FolderListDiffUtil(
    var oldList: ArrayList<DriveItem>,
    var newList: ArrayList<DriveItem>) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition].fileId == newList[newItemPosition].fileId
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].fileName == newList[newItemPosition].fileName
    }
}