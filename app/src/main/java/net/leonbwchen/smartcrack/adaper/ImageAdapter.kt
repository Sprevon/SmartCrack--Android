package net.leonbwchen.smartcrack.adaper

import android.annotation.SuppressLint
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.leonbwchen.smartcrack.R
import net.leonbwchen.smartcrack.entity.PhotoInfo

class ImageAdapter(val imageList: List<PhotoInfo>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image_origin: ImageView = view.findViewById(R.id.image_origin)
        val image_after: ImageView = view.findViewById(R.id.image_after)
        val image_name: TextView = view.findViewById(R.id.image_name)
        val image_numb: TextView = view.findViewById(R.id.image_numb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_info_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageInfo = imageList[position]
        holder.image_origin.setImageBitmap(imageInfo.imageOrigin)
        holder.image_after.setImageBitmap(imageInfo.imageAfter)
        holder.image_name.text = imageInfo.name
        holder.image_numb.text = imageInfo.numb.toString()
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}