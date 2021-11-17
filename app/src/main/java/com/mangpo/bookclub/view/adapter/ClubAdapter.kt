package com.mangpo.bookclub.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.ClubItemBinding
import com.mangpo.bookclub.model.ClubModel

class ClubAdapter(clubs: MutableList<ClubModel>, selectedClub: Int, itemClick: OnItemClick) : RecyclerView.Adapter<ClubAdapter.ClubViewHolder>() {

    private lateinit var binding: ClubItemBinding
    private val itemClick: OnItemClick = itemClick
    private var clubs: MutableList<ClubModel> = clubs
    private var selectedClub: Int = selectedClub

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        binding = ClubItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        holder.clubName.text = clubs[position].name
//        holder.clubDesc.text = clubs[position].description

        if (selectedClub==position) {
            holder.clubName.setTextColor(getColor(holder.clubName.context, R.color.light_red))
        }

        if (position==clubs.size-1) {
            holder.lineView.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            itemClick.onClick(position)
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

    fun updateSelectedclub(position: Int) {
        this.selectedClub = position
        notifyDataSetChanged()
    }
}