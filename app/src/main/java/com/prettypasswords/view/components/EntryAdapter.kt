package com.prettypasswords.view.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.prettypasswords.R
import com.prettypasswords.model.Entry

class EntryAdapter(val context: Context, var entries: ArrayList<Entry>): RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    companion object{
        val TYPE_Entry=1
    }

    var listener: ItemClickListener? = null

    override fun getItemCount(): Int {

        return entries.size
    }

    // every row is type entry
    override fun getItemViewType(position: Int): Int {
        return TYPE_Entry
    }

    // here you can put different layout for different viewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Every item in the list need to create a new view for it
        val itemView = LayoutInflater.from(context).inflate(R.layout.entry_item, parent, false );

        return ViewHolder(itemView)
    }

    // binds the data to the view in every row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val entry: Entry = entries.get(position)

        holder.siteNameLabel!!.text = entry.siteName

        if (entry.userName.isNotEmpty()){
            holder.userNameLabel!!.visibility = View.VISIBLE
            holder.userNameDisplay!!.setText(entry.userName)
        }

        holder.passwordDisplay!!.setText(entry.password)


        if (entry.email.isNotEmpty()){
            holder.emailLabel!!.visibility = View.VISIBLE
            holder.emailDisplay!!.setText(entry.email)
        }

    }


    // stores and recycles views as they are scrolled off screen
    /*
        holder need to store all the views that contains data
        the onBindViewHolder method will need access to the view to put in data
     */

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var siteNameLabel: TextView? = null

        var userNameLabel: TextInputLayout? = null
        var userNameDisplay: TextInputEditText? = null

        var passwordDisplay: TextInputEditText? = null

        var emailLabel: TextInputLayout? = null
        var emailDisplay: TextInputEditText? = null

        init {

            siteNameLabel = itemView.findViewById(R.id.siteNameLabel)

            userNameLabel = itemView.findViewById(R.id.userNameLabel)
            userNameDisplay = itemView.findViewById(R.id.userNameDisplay)

            passwordDisplay = itemView.findViewById(R.id.passwordDisplay)

            emailLabel = itemView.findViewById(R.id.emailLabel)
            emailDisplay = itemView.findViewById(R.id.emailDisplay)

            //itemView.setOnClickListener(this)
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