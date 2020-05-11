package com.prettypasswords.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.model.Tag
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.ContentManager


class TagRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs), MyRecyclerViewAdapter.ItemClickListener{


    val cm: ContentManager = PrettyManager.cm!!



    override fun onItemClick(view: View?, position: Int) {

        val tag: Tag = cm.body.tags.get(position)

        if (!tag.decrypted()){

            XPopup.Builder(context).asCustom(DecryptTagDialogue(context!!, position)).show()

        }else{

            Toast.makeText(context, "$position tag is already decrypted", Toast.LENGTH_LONG).show()

            // TODO jump to show entries
        }
    }


    init {

        val tags = PrettyManager.cm!!.body.tags

        val adapter = MyRecyclerViewAdapter(context, tags)

        adapter.setClickListener(this)

        // create the view for the item in the list
        setAdapter(adapter)


        this.layoutManager = LinearLayoutManager(context)

        // divider between each item
        this.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

}


class MyRecyclerViewAdapter internal constructor(context: Context, tags: ArrayList<Tag>) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    private val context = context
    private var mClickListener: ItemClickListener? = null
    private var tags: ArrayList<Tag> = tags


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        // Every item in the list need to create a new view for it
        val itemView = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false );

        return ViewHolder(itemView)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val tag: Tag = tags.get(position)

        holder.tagNameLabel!!.text = tag.tagName
        holder.lastModifiedLabel!!.text = tag.lastModified
        holder.entriesCountLabel!!.text = tag.entries.size.toString()

    }

    // total number of rows
    override fun getItemCount(): Int {
        return tags.size
    }

    // stores and recycles views as they are scrolled off screen
    /*
        holder need to store all the views that contains data
        the onBindViewHolder method will need access to the view to put in data
     */
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tagNameLabel: TextView? = null
        var entriesCountLabel: TextView? = null
        var lastModifiedLabel: TextView? = null


        init {
            tagNameLabel = itemView.findViewById(R.id.tagNameLabel)
            entriesCountLabel = itemView.findViewById(R.id.entriesCountLabel)
            lastModifiedLabel = itemView.findViewById(R.id.lastModifiedLabel)

            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            println("click event catched")

            mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // convenience method for getting data at click position
    fun getItem(position: Int): Tag {
        return tags.get(position)
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}