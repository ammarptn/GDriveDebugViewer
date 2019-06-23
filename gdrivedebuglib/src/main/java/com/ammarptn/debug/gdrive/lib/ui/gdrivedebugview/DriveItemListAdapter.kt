package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ammarptn.debug.gdrive.lib.R
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveActionItem
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.DriveItem
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.RecycleViewBaseItem
import com.ammarptn.gdriverest.DriveServiceHelper
import com.bumptech.glide.Glide
import java.util.*


class DriveItemListAdapter(recycleViewBaseItems: ArrayList<RecycleViewBaseItem>, private val callback: addClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var recycleViewBaseItems = ArrayList<RecycleViewBaseItem>()


    init {
        this.recycleViewBaseItems = recycleViewBaseItems


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View

        when (viewType) {
            DriveItemType.DRIVE_ITEM_TYPE -> {
                itemView = LayoutInflater.from(parent.context).inflate(R.layout.drive_item, parent, false)
                return DriveItemViewHolder(itemView)
            }
            DriveItemType.DRIVE_ACTION_TYPE -> {
                itemView = LayoutInflater.from(parent.context).inflate(R.layout.drive_item, parent, false)
                return DriveActionItemViewHolder(itemView)
            }

            else -> return null!!
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder is DriveItemViewHolder) {
            val dataItem = recycleViewBaseItems[position] as DriveItem

            holder.driveName.text = dataItem.title

            Glide.with(holder.driveIcon.context).load(dataItem.icon).into(holder.driveIcon)

            holder.driveLayout.setOnClickListener {
                when (dataItem.mimeType) {
                    DriveServiceHelper.TYPE_GOOGLE_DRIVE_FOLDER -> {
                        callback.onFolderClick(holder.adapterPosition)
                    }
                    else -> {
                        callback.onFileClick(holder.adapterPosition)
                    }
                }

            }

            holder.driveLayout.setOnLongClickListener {
                when (dataItem.mimeType) {
                    DriveServiceHelper.TYPE_GOOGLE_DRIVE_FOLDER -> {
                        callback.onFolderLongClick(holder.adapterPosition)
                    }
                    else -> {
                        callback.onFileLongClick(holder.adapterPosition)
                    }
                }
                true

            }


        } else if (holder is DriveActionItemViewHolder) {
            val dataItem = recycleViewBaseItems[position] as DriveActionItem
            holder.driveName.text = dataItem.title
            Glide.with(holder.driveIcon.context).load(dataItem.icon).into(holder.driveIcon)

            holder.driveLayout.setOnClickListener {

                callback.onBackClick(holder.adapterPosition)

            }

        }

        else if (holder is DriveActionItemViewHolder) {

            holder.driveLayout.visibility = View.INVISIBLE

        }


    }

    override fun getItemViewType(position: Int): Int {

        return recycleViewBaseItems[position].type
    }

    override fun getItemCount(): Int {
        return recycleViewBaseItems.size
    }


    interface addClickListener {
        fun onBackClick(position: Int)
        fun onFolderClick(position: Int)
        fun onFileClick(position: Int)
        fun onFileLongClick(position: Int)
        fun onFolderLongClick(position: Int)

    }

    class DriveItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val driveLayout: LinearLayout
        val driveIcon: ImageView
        val driveName: TextView

        init {
            driveLayout = itemView.findViewById(R.id.drive_layout)
            driveIcon = itemView.findViewById(R.id.drive_icon)
            driveName = itemView.findViewById(R.id.drive_name)

        }


    }

    class DriveActionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val driveLayout: LinearLayout
        val driveIcon: ImageView
        val driveName: TextView

        init {
            driveLayout = itemView.findViewById(R.id.drive_layout)
            driveIcon = itemView.findViewById(R.id.drive_icon)
            driveName = itemView.findViewById(R.id.drive_name)

        }


    }






}
