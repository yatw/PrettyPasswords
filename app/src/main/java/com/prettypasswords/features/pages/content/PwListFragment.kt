package com.prettypasswords.features.pages.content

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import com.prettypasswords.databinding.FragmentPwListBinding
import com.prettypasswords.databinding.ItemEntryBinding
import com.prettypasswords.model.Password
import com.prettypasswords.view.popups.AddEntry
import timber.log.Timber
import java.util.*

class PwListFragment: Fragment() {

    private lateinit var binding: FragmentPwListBinding

    private val viewModel: PwViewModel by navGraphViewModels(R.id.nav_password)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPwListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val submitCallback = object: AddEntry.SubmitCallback{
            override fun onSubmit(password: Password) {
                viewModel.addPassword(requireContext(), password)
            }
        }

        binding.addPwBtn.setOnClickListener {
            XPopup.Builder(context).asCustom(AddEntry(requireContext(), submitCallback)).show()
        }

        val adapter = PasswordListAdapter(requireContext())
        binding.pwList.adapter = adapter

        viewModel.pwList.observe(viewLifecycleOwner){ list ->

            if (list.isEmpty()){
                binding.pwList.visibility = View.GONE
                binding.noPw.visibility = View.VISIBLE
                return@observe
            }
            adapter.updateData(list)
            adapter.notifyDataSetChanged()

            binding.pwList.visibility = View.VISIBLE
            binding.noPw.visibility = View.GONE
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
        }
    }



    // inner = Non Static Nested classes
    inner class PasswordListAdapter(val context: Context): RecyclerView.Adapter<PasswordListAdapter.PwViewHolder>() {

//        var editClickListener: ItemClickListener? = null
//        var cardClickListener: StickClickListener? = null

        private var dataSet = listOf<Password>()

        fun updateData(pwList: List<Password>){
            dataSet = pwList
            this.notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PwViewHolder {
            return PwViewHolder(ItemEntryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
        }

        inner class PwViewHolder(binding: ItemEntryBinding) : RecyclerView.ViewHolder(binding.root){

            val cardContent: ConstraintLayout = binding.cardContent

            val siteNameLabel: TextView = binding.siteNameLabel

            val userNameGroup: RelativeLayout = binding.userNameGroup
            val userNameLabel: TextView = binding.userNameLabel
            val userNameDisplay: TextView = binding.userNameDisplay

            val password_toggle: ImageButton = binding.passwordToggle
            val passwordDisplay: TextView = binding.passwordDisplay

            val emailGroup: RelativeLayout = binding.emailGroup
            val emailLabel: TextView = binding.emailLabel
            val emailDisplay: TextView = binding.emailDisplay

            val othersGroup: RelativeLayout = binding.othersGroup
            val othersLabel: TextView = binding.othersLabel
            val othersDisplay: TextView = binding.othersDisplay

            val lastModified: TextView = binding.lastModified

            var passwordHidden = true

            init {

                // pop up builder
                // 必须在事件发生前，调用这个方法来监视View的触摸
                val builder = XPopup.Builder(context)
                    .watchView(itemView)
                    .hasShadowBg(false)


                // toggle password click
                password_toggle.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {

                        passwordHidden = !passwordHidden

                        val drawable: Drawable?
                        var color: Int = Color.GRAY


                        if (passwordHidden){
                            passwordDisplay.transformationMethod = PasswordTransformationMethod()   //hide the password
                            drawable = ContextCompat.getDrawable(context, R.drawable.ic_hide)
                        }else{
                            passwordDisplay.transformationMethod = null;
                            color = ContextCompat.getColor(context, R.color.colorPrimary)
                            drawable = ContextCompat.getDrawable(context, R.drawable.ic_show)
                        }

                        password_toggle.setImageDrawable(drawable)
                        passwordDisplay.setTextColor(color)
                    }
                })

            }

        }


        override fun onBindViewHolder(holder: PwViewHolder, position: Int) {

            val pw: Password = dataSet.get(position)

            holder.siteNameLabel.text = pw.siteName

            if (pw.userName.isNotEmpty()){
                holder.userNameGroup.visibility = View.VISIBLE
                holder.userNameDisplay.text = pw.userName
            }


            holder.passwordDisplay.setText(pw.password)
            holder.passwordDisplay.transformationMethod = PasswordTransformationMethod()   //hide the password


            if (pw.email.isNotEmpty()){
                holder.emailGroup.visibility = View.VISIBLE
                holder.emailDisplay.text = pw.email
            }

            if (!pw.others.isNullOrBlank()){
                holder.othersGroup.visibility = View.VISIBLE
                holder.othersDisplay.text = pw.others
            }

            holder.lastModified.text = pw.lastModified

            holder.cardContent.setOnClickListener {
                val action = PwListFragmentDirections.actionPwListFragmentToPwEditFragment(position)
                findNavController().navigate(action)
            }
        }

    }
}