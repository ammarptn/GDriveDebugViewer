package com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ammarptn.debug.gdrive.lib.R
import com.ammarptn.debug.gdrive.lib.ui.gdrivedebugview.viewObject.RecycleViewBaseItem
import java.util.*


class DriveListAdapter( recycleItemArrayListDrive: ArrayList<RecycleViewBaseItem>, internal var callback: addOnClickListener) :
    RecyclerView.Adapter<DriveListAdapter.DriveListViewHolder>() {
    internal var recycleItemArrayList = ArrayList<RecycleViewBaseItem>()


    init {
        this.recycleItemArrayList = recycleItemArrayListDrive

    }



    public interface addOnClickListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriveListViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.drive_item, parent, false)


        return DriveListViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: DriveListViewHolder, position: Int) {

//        holder.title.text = recycleItemArrayList[position].fileName
//        if (recycleItemArrayList[position].isFolder) {
//            Glide.with(holder.icon.context).load(R.drawable.ic_folder_vd).into(holder.icon)
//        } else {
//            Glide.with(holder.icon.context).load(R.drawable.ic_file_vd).into(holder.icon)
//
//        }
//
//        holder.layout.setOnClickListener {
//            callback.onClick(holder.adapterPosition)
//        }



    }


    override fun getItemCount(): Int {
        return recycleItemArrayList.size
    }

    class DriveListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var icon: ImageView = itemView.findViewById(R.id.drive_icon)
        internal var title: TextView = itemView.findViewById(R.id.drive_name)
        internal var layout: LinearLayout = itemView.findViewById(R.id.drive_layout)


    }
}