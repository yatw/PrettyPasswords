package com.prettypasswords.view.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.prettypasswords.R
import com.prettypasswords.model.Entry


class EntryAdapter(val context: Context, var entries: ArrayList<Entry>): RecyclerView.Adapter<EntryAdapter.ViewHolder>() {



    var listener: ItemClickListener? = null
    var longListener: LongItemClickListener? = null

    override fun getItemCount(): Int {

        return entries.size
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

        if (entry.others.isNotEmpty()){
            holder.othersLabel!!.visibility = View.VISIBLE
            holder.othersDisplay!!.setText(entry.others)
        }

    }


    // stores and recycles views as they are scrolled off screen
    /*
        holder need to store all the views that contains data
        the onBindViewHolder method will need access to the view to put in data
     */

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var siteNameLabel: TextView? = null

        var userNameLabel: TextInputLayout? = null
        var userNameDisplay: TextInputEditText? = null

        var passwordDisplay: TextInputEditText? = null

        var emailLabel: TextInputLayout? = null
        var emailDisplay: TextInputEditText? = null

        var othersLabel: TextInputLayout? = null
        var othersDisplay: TextInputEditText? = null

        var editButton: ImageButton? = null


        init {

            siteNameLabel = itemView.findViewById(R.id.siteNameLabel)

            userNameLabel = itemView.findViewById(R.id.userNameLabel)
            userNameDisplay = itemView.findViewById(R.id.userNameDisplay)

            passwordDisplay = itemView.findViewById(R.id.passwordDisplay)

            emailLabel = itemView.findViewById(R.id.emailLabel)
            emailDisplay = itemView.findViewById(R.id.emailDisplay)

            othersLabel = itemView.findViewById(R.id.othersLabel)
            othersDisplay = itemView.findViewById(R.id.othersDisplay)

            editButton = itemView.findViewById(R.id.editButton)


            // create listener, in the listener, call the interface function onItemClick
            val editClickListener = View.OnClickListener {
                listener!!.onItemClick(itemView, adapterPosition)
            }

            editButton!!.setOnClickListener(editClickListener)


            // long click pop up builder
            // 必须在事件发生前，调用这个方法来监视View的触摸
            val builder = XPopup.Builder(context)
                .watchView(itemView)
                .hasShadowBg(false)



            // handle long click
            val longClickListener = OnLongClickListener {
                longListener!!.onItemClick(builder, adapterPosition)
            }

            itemView.setOnLongClickListener(longClickListener)
        }


    }


    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface LongItemClickListener {
        fun onItemClick(builder: XPopup.Builder, position: Int): Boolean
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    fun setItemLongClickListener(listener: LongItemClickListener?) {
        this.longListener = listener
    }
}