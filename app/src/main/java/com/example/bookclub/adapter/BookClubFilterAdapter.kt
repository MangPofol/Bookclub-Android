package com.example.bookclub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.bookclub.databinding.ClubFilterItemBinding
import com.example.bookclub.model.ClubDTO

class BookClubFilterAdapter(clubs: List<ClubDTO>) : RecyclerView.Adapter<BookClubFilterAdapter.BookClubFilterViewHolder>() {
    private lateinit var binding: ClubFilterItemBinding
    private var clubs: List<ClubDTO> = clubs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookClubFilterViewHolder {
        binding = ClubFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookClubFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookClubFilterViewHolder, position: Int) {
        if (clubs.isNotEmpty())
            holder.clubCheckBox.text = clubs[position].clubName
    }

    override fun getItemCount(): Int {
        return clubs.size
    }

    class BookClubFilterViewHolder(itemView: ClubFilterItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        var clubCheckBox: CheckBox = itemView.clubCheckBox
    }
}