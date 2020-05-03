package com.prettypasswords.View.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.PrettyManager
import com.prettypasswords.R
import com.prettypasswords.Utilities.ContentManager
import org.json.JSONArray
import org.json.JSONObject


class TagRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs), MyRecyclerViewAdapter.ItemClickListener{


    val cm: ContentManager = PrettyManager.cm!!

    var adapter: MyRecyclerViewAdapter = MyRecyclerViewAdapter(context)

    override fun onItemClick(view: View?, position: Int) {

        val tagName = cm.sectionBody!!.getString(position)

        // tag have not been decrypted
        if (cm.tags.get(tagName) == null){

            XPopup.Builder(context).asCustom(DecryptTagDialogue(context!!, position)).show()

        }else{

            // TODO jump to show entries
        }
    }


    init {

        // create the view for the item in the list
        setAdapter(adapter)

        this.layoutManager = LinearLayoutManager(context)


        adapter.setClickListener(this)

        // divider between each item
        this.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

}


class MyRecyclerViewAdapter internal constructor(context: Context) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    private val context = context
    private var mClickListener: ItemClickListener? = null
    private var tags: JSONArray = PrettyManager.cm!!.sectionBody!!


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

        val tag: JSONObject = tags.getJSONObject(position)

        holder.tagNameLabel!!.text = tag.getString("tagName")
        holder.entriesCountLabel!!.text = tag.getInt("count").toString()
        holder.lastModifiedLabel!!.text = tag.getString("lastModified")

    }

    // total number of rows
    override fun getItemCount(): Int {
        return tags.length()
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
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // convenience method for getting data at click position
    fun getItem(position: Int): JSONObject {
        return tags.getJSONObject(position)
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