package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview

import androidx.recyclerview.widget.DiffUtil
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveActionItem
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveItem
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.RecycleViewBaseItem

class FolderListDiffUtil(
    var oldList: ArrayList<RecycleViewBaseItem>,
    var newList: ArrayList<RecycleViewBaseItem>) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition] is DriveItem && newList[newItemPosition] is DriveItem) {
            return true

        } else return oldList[oldItemPosition] is DriveActionItem && newList[newItemPosition] is DriveActionItem
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition] is DriveItem && newList[newItemPosition] is DriveItem) {
            return ((oldList[oldItemPosition] as DriveItem).title == (newList[newItemPosition] as DriveItem).title) && (oldList[oldItemPosition] as DriveItem).driveId == (newList[newItemPosition] as DriveItem).driveId

        } else if (oldList[oldItemPosition] is DriveActionItem && newList[newItemPosition] is DriveActionItem) {
            return ((oldList[oldItemPosition] as DriveActionItem).title == (newList[newItemPosition] as DriveActionItem).title)
        } else {
            return false
        }
    }
}