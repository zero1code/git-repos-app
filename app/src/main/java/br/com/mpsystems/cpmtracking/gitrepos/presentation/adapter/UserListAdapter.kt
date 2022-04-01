package br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.mpsystems.cpmtracking.gitrepos.databinding.ItemSearchedUserBinding
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Owner
import com.bumptech.glide.Glide
import java.util.*


class UserListAdapter : ListAdapter<Owner, UserListAdapter.ViewHolder>(UserDiffCalback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchedUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemSearchedUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Owner) {
            binding.tvUserName.text = item.login
            Glide.with(binding.root.context).load(item.avatarURL).into(binding.ivOwner)

            val random = Random()
            val color = Color.argb(
                20,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
            )
            binding.cvUser.setCardBackgroundColor(color)
        }
    }
}

class UserDiffCalback : DiffUtil.ItemCallback<Owner>() {
    override fun areItemsTheSame(oldItem: Owner, newItem: Owner) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Owner, newItem: Owner) = oldItem.login == newItem.login

}