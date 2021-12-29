package com.mangpo.bookclub.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ClubFilterItemBinding
import com.mangpo.bookclub.model.ClubModel

//베타 버전 출시 후 사용
class BookClubFilterAdapter(clubs: List<ClubModel>) :
    RecyclerView.Adapter<BookClubFilterAdapter.BookClubFilterViewHolder>() {
    private lateinit var binding: ClubFilterItemBinding
    private var clubs: List<ClubModel> = clubs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookClubFilterViewHolder {
        binding = ClubFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookClubFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookClubFilterViewHolder, position: Int) {
        if (clubs.isNotEmpty())
            holder.clubCheckBox.text = clubs[position].name
    }

    override fun getItemCount(): Int {
        return clubs.size
    }

    class BookClubFilterViewHolder(itemView: ClubFilterItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var clubCheckBox: CheckBox = itemView.clubCheckBox
    }
}