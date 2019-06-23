package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject

import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.DriveItemType


class DriveItem() :
    RecycleViewBaseItem(DriveItemType.DRIVE_ITEM_TYPE) {

    var driveId: String? = null
    var title: String? = null
    var icon: Int? = null
    var mimeType: String? = null
    var fileSize: String? = null
    var lastUpdate: String? = null


}
