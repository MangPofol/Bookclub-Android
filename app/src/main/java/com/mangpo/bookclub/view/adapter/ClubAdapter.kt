package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ClubItemBinding
import com.mangpo.bookclub.model.ClubModel

class ClubAdapter(clubs: MutableList<ClubModel>) : RecyclerView.Adapter<ClubAdapter.ClubViewHolder>() {

    private lateinit var binding: ClubItemBinding
    private var clubs: MutableList<ClubModel> = clubs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        binding = ClubItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        holder.clubName.text = clubs[position].name
        holder.clubDesc.text = clubs[position].description

        if (position==clubs.size-1) {
            holder.lineView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return clubs.size
    }

    class ClubViewHolder(itemView: ClubItemBinding): RecyclerView.ViewHolder(itemView.root) {
        val clubImg: ImageView = itemView.clubProfileIv
        val clubName: TextView = itemView.clubNameTv
        val clubDesc: TextView = itemView.clubDescriptionTv
        val lineView: View = itemView.clubItemLine
    }
}