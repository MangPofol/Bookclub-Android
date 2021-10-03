package com.mangpo.bookclub.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.RecordScopeClubItemBinding
import com.mangpo.bookclub.model.ClubModel

class RecordScopeClubAdapter(clubs: List<ClubModel>, itemClick: OnItemClick): RecyclerView.Adapter<RecordScopeClubAdapter.RecordScopeClubViewHolder>() {
    private lateinit var binding: RecordScopeClubItemBinding
    private val onItemClick: OnItemClick = itemClick

    private val clubs: List<ClubModel> = clubs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordScopeClubViewHolder {
        binding = RecordScopeClubItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecordScopeClubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordScopeClubViewHolder, position: Int) {
        holder.clubCB.text = clubs[position].name

        holder.clubCB.setOnClickListener {
            onItemClick.onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return clubs.size
    }

    class RecordScopeClubViewHolder(itemView: RecordScopeClubItemBinding): RecyclerView.ViewHolder(itemView.root) {
        val clubCB: CheckBox = itemView.clubCb
    }
}