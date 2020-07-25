package com.prettypasswords.view.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import com.prettypasswords.model.Entry


class EntryAdapter(val context: Context, private val entries: ArrayList<Entry>): RecyclerView.Adapter<EntryAdapter.ViewHolder>() {



    var editClickListener: ItemClickListener? = null
    var cardClickListener: StickClickListener? = null

    override fun getItemCount(): Int {

        return entries.size
    }



    // here you can put different layout for different viewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Every item in the list need to create a new view for it
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_entry, parent, false );

        return ViewHolder(itemView)
    }

    // binds the data to the view in every row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val entry: Entry = entries.get(position)

        holder.siteNameLabel!!.text = entry.siteName

        if (entry.userName.isNotEmpty()){
            holder.userNameGroup!!.visibility = View.VISIBLE
            holder.userNameDisplay!!.setText(entry.userName)
        }


        holder.passwordDisplay!!.setText(entry.password)
        holder.passwordDisplay!!.transformationMethod = PasswordTransformationMethod()   //hide the password


        if (entry.email.isNotEmpty()){
            holder.emailGroup!!.visibility = View.VISIBLE
            holder.emailDisplay!!.setText(entry.email)
        }

        if (entry.others.isNotEmpty()){
            holder.othersGroup!!.visibility = View.VISIBLE
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

        var userNameGroup: RelativeLayout? = null
        var userNameLabel: TextView? = null
        var userNameDisplay: TextView? = null

        var password_toggle: ImageButton? = null
        var passwordDisplay: TextView? = null

        var emailGroup: RelativeLayout? = null
        var emailLabel: TextView? = null
        var emailDisplay: TextView? = null

        var othersGroup: RelativeLayout? = null
        var othersLabel: TextView? = null
        var othersDisplay: TextView? = null

        var editButton: ImageButton? = null

        var passwordHidden = true

        init {

            siteNameLabel = itemView.findViewById(R.id.siteNameLabel)

            userNameGroup = itemView.findViewById(R.id.userNameGroup)
            userNameLabel = itemView.findViewById(R.id.userNameLabel)
            userNameDisplay = itemView.findViewById(R.id.userNameDisplay)

            password_toggle = itemView.findViewById(R.id.password_toggle)
            passwordDisplay = itemView.findViewById(R.id.passwordDisplay)

            emailGroup = itemView.findViewById(R.id.emailGroup)
            emailLabel = itemView.findViewById(R.id.emailLabel)
            emailDisplay = itemView.findViewById(R.id.emailDisplay)

            othersGroup = itemView.findViewById(R.id.othersGroup)
            othersLabel = itemView.findViewById(R.id.othersLabel)
            othersDisplay = itemView.findViewById(R.id.othersDisplay)

            editButton = itemView.findViewById(R.id.editButton)


            // create listener, in the listener, call the interface function onItemClick
            val editClickListener = View.OnClickListener {
                editClickListener!!.onItemClick(itemView, adapterPosition)
            }

            editButton!!.setOnClickListener(editClickListener)


            // pop up builder
            // 必须在事件发生前，调用这个方法来监视View的触摸
            val builder = XPopup.Builder(context)
                .watchView(itemView)
                .hasShadowBg(false)


            // handle card click, stick to where u click
            val cardClickListener = View.OnClickListener {
                cardClickListener!!.onItemClick(builder, adapterPosition)
            }

            itemView.setOnClickListener(cardClickListener)


            // toggle password click
            password_toggle!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {

                    passwordHidden = !passwordHidden

                    val drawable: Drawable?
                    var color: Int = Color.GRAY


                    if (passwordHidden){
                        passwordDisplay!!.transformationMethod = PasswordTransformationMethod()   //hide the password
                        drawable = ContextCompat.getDrawable(context, R.drawable.icon_hide)
                    }else{
                        passwordDisplay!!.transformationMethod = null;
                        color = ContextCompat.getColor(context, R.color.colorPrimary)
                        drawable = ContextCompat.getDrawable(context, R.drawable.icon_show)
                    }

                    password_toggle!!.setImageDrawable(drawable)
                    passwordDisplay!!.setTextColor(color)

                }
            })

        }


    }


    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface StickClickListener {
        fun onItemClick(builder: XPopup.Builder, position: Int): Boolean
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.editClickListener = listener
    }

    fun setStickClickListener(listener: StickClickListener?) {
        this.cardClickListener = listener
    }
}