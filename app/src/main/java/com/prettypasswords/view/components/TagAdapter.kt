package com.prettypasswords.view.components

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prettypasswords.R
import com.prettypasswords.utilities.LetterTileProvider
import com.prettypasswords.model.Tag


class TagAdapter(val context: Context, var tags: ArrayList<Tag>): RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    companion object{
        val TYPE_Tag=1
    }

    var listener: ItemClickListener? = null


    override fun getItemCount(): Int {
        return tags.size
    }

    // every row is type tag
    override fun getItemViewType(position: Int): Int {
        return TYPE_Tag
    }

    // here you can put different layout for different viewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        System.out.println("Creating View Holder");

        // Every item in the list need to create a new view for it
        val itemView = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false );

        return ViewHolder(itemView)
    }

    // binds the data to the view in every row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tag: Tag = tags.get(position)


        holder.tagNameLabel!!.text = tag.tagName
        holder.lastModifiedLabel!!.text = tag.lastModified

        val res: Resources = context.resources;
        val tileSize: Int = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        val provider: LetterTileProvider = LetterTileProvider(context)

        val letterTile: Bitmap = provider.getLetterTile(tag.tagName.first(), tag.tagName, tileSize, tileSize, true);
        holder.tagIcon!!.setImageBitmap(letterTile)

    }



    // stores and recycles views as they are scrolled off screen
    /*
        holder need to store all the views that contains data
        the onBindViewHolder method will need access to the view to put in data
     */

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        var tagIcon: ImageView? = null
        var tagNameLabel: TextView? = null
        var lastModifiedLabel: TextView? = null


        init {
            tagIcon = itemView.findViewById(R.id.tagIcon)
            tagNameLabel = itemView.findViewById(R.id.tagNameLabel)
            lastModifiedLabel = itemView.findViewById(R.id.lastModifiedLabel)


            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener!!.onItemClick(itemView, adapterPosition)
        }

    }


    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

}