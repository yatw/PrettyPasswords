package com.prettypasswords.view.components

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prettypasswords.model.Tag
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.controller.ContentManager
import com.prettypasswords.model.Entry
import com.prettypasswords.view.activities.EntryActivity


class EntryRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs), MyRecyclerViewAdapter2.ItemClickListener{


    val cm: ContentManager = PrettyManager.cm!!
    lateinit var tag: Tag

    fun initData(tag: Tag){

        this.tag = tag

        val adapter = MyRecyclerViewAdapter2(context, tag.entries)

        adapter.setClickListener(this)

        // create the view for the item in the list
        setAdapter(adapter)


        this.layoutManager = LinearLayoutManager(context)

        // divider between each item
        this.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

    override fun onItemClick(view: View?, position: Int) {

        println("position ${position} clicked")

        tag.entries[position]

        val intent = Intent(context, EntryActivity::class.java)
        intent.putExtra("entryPosition", position);
        context.startActivity(intent)

    }

}


class MyRecyclerViewAdapter2 internal constructor(context: Context, entriesList: ArrayList<Entry>) : RecyclerView.Adapter<MyRecyclerViewAdapter2.ViewHolder>() {

    private val context = context
    private var mClickListener: ItemClickListener? = null
    private var entries: ArrayList<Entry> = entriesList


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        // Every item in the list need to create a new view for it
        val itemView = LayoutInflater.from(context).inflate(R.layout.entry_item, parent, false );

        return ViewHolder(itemView)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val entry: Entry = entries.get(position)

        holder.entryName!!.text = entry.name

    }

    // total number of rows
    override fun getItemCount(): Int {
        return entries.size
    }

    // stores and recycles views as they are scrolled off screen
    /*
        holder need to store all the views that contains data
        the onBindViewHolder method will need access to the view to put in data
     */
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var entryName: TextView? = null


        init {
            entryName = itemView.findViewById(R.id.entryNameLabel)


            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            println("click event catched")

            mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // convenience method for getting data at click position
    fun getItem(position: Int): Entry {
        return entries.get(position)
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