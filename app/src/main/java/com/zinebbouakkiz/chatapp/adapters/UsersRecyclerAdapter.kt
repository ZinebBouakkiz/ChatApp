package com.zinebbouakkiz.chatapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zinebbouakkiz.chatapp.R
import com.zinebbouakkiz.chatapp.activities.ChatActivity
import com.zinebbouakkiz.chatapp.models.User

class UsersRecyclerAdapter: RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>(), Filterable {

    var items: MutableList<User> = mutableListOf()
        set(value) {
            field = value
            usersFiltredList = value
            notifyDataSetChanged()
        }

    private var usersFiltredList: MutableList<User> = mutableListOf()

    override fun getFilter(): Filter {
        return object: Filter(){
            // faire le filtrage
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // la recuperation de se qu'on a dans la bare de recherche
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    usersFiltredList = items
                }else{
                    val resultList = items.filter{ it.fullname.lowercase().contains(charSearch.lowercase()) }
                    usersFiltredList = resultList as MutableList<User>
                }
                val filterResult = FilterResults()
                filterResult.values = usersFiltredList
                return filterResult
            }

            // publer le filtrage
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFiltredList = results?.values as MutableList<User>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = usersFiltredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = usersFiltredList[position]
        holder.bind(friend)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvShortName: TextView = itemView.findViewById(R.id.tvShortName)
        val tvName: TextView = itemView.findViewById(R.id.tvName)

        fun bind(user: User){
            tvShortName.text = user.fullname[0].toString()
            tvName.text = user.fullname

            itemView.setOnClickListener {
                Intent(itemView.context, ChatActivity::class.java).also {
                    it.putExtra("friend",user.uuid)
                    itemView.context.startActivity(it)
                }
            }
        }

    }

}