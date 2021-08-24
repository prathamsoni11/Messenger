package com.example.messenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.models.User
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class UserAdapter(options: FirebaseRecyclerOptions<User>):
    FirebaseRecyclerAdapter<User,UserAdapter.UserViewHolder>(options) {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        Glide.with(holder.userImage.context).load(model.profileImage).placeholder(R.drawable.avatar).into(holder.userImage)
        holder.userName.text = model.name
    }
}