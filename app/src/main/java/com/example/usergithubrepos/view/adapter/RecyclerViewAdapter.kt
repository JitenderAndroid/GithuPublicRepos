package com.example.usergithubrepos.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.usergithubrepos.R
import com.example.usergithubrepos.models.repos.UserReposResponse
import kotlinx.android.synthetic.main.item_repo.view.*

class RecyclerViewAdapter(var context: Context, var listRepos: ArrayList<UserReposResponse?>): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_repo,parent,false))
    }

    override fun getItemCount(): Int {
        return listRepos.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData()
    }

    inner class ItemViewHolder(var item: View): RecyclerView.ViewHolder(item){
        fun bindData() {
            item.text_reponame.text = listRepos[adapterPosition]?.full_name
            item.text_watchers.text = "Watchers count "+ listRepos[adapterPosition]?.watchers_count
            item.text_default_branch.text = "Default branch "+ listRepos[adapterPosition]?.default_branch
            item.text_open_issues.text = "open issues "+ listRepos[adapterPosition]?.open_issues_count
        }
    }
}