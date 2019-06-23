package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview

import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveActionItem

import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveItem

class DriveItemConverter {

    fun addDriveItem(driveId: String?, title: String?, icon: Int?, mimeType: String?,fileSize: String?,lastUpdate: String?): DriveItem {

        var driveItem = DriveItem()
        driveItem.driveId = driveId
        driveItem.title = title
        driveItem.icon = icon
        driveItem.mimeType = mimeType
        driveItem.fileSize = fileSize
        driveItem.lastUpdate = lastUpdate
        return driveItem

    }

    fun addDriveActionItem(title: String?, icon: Int?): DriveActionItem {

        var driveItem = DriveActionItem()
        driveItem.title = title
        driveItem.icon = icon
        return driveItem

    }




}