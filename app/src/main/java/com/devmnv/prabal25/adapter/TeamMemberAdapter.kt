package com.devmnv.prabal25.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devmnv.prabal25.R
import com.devmnv.prabal25.model.User

class TeamMemberAdapter(
    private var members: MutableList<User>
) : RecyclerView.Adapter<TeamMemberAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.memberNumber.text = "${position + 1}."
        holder.name.text = member.name
        holder.gender.text = member.gender
    }

    override fun getItemCount(): Int = members.size

    fun updateData(newList: List<User>) {
        members.clear()
        members.addAll(newList)
        notifyDataSetChanged()
    }

    fun getUpdatedParticipants(): List<User> = members

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memberNumber: TextView = view.findViewById(R.id.tv_memberNumber)
        val name: TextView = view.findViewById(R.id.tv_memberName)
        val gender: TextView = view.findViewById(R.id.tv_memberGender)
    }
}
