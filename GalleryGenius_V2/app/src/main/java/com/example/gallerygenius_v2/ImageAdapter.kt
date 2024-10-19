package com.example.gallerygenius_v2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(private var imageList: MutableList<Image>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val description: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        Glide.with(holder.imageView.context).load(image.url).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val intent = Intent(holder.imageView.context, DetallesImg::class.java)
            intent.putExtra("IMAGE_URL", image.url)
            intent.putExtra("IMAGE_DESCRIPTION", image.description)
            holder.imageView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = imageList.size

    // Método para actualizar la lista de imágenes
    fun updateList(newList: List<Image>) {
        imageList.clear()
        imageList.addAll(newList)
        notifyDataSetChanged()
    }
}
