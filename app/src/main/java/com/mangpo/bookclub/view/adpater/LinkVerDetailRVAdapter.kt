package com.mangpo.bookclub.view.adpater

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemLinkVerDetailBinding
import com.mangpo.bookclub.model.entities.Link
import java.util.regex.Pattern

class LinkVerDetailRVAdapter(): RecyclerView.Adapter<LinkVerDetailRVAdapter.LinkVerDetailViewHolder>() {
    private var links: List<Link> = listOf()

    private lateinit var binding: ItemLinkVerDetailBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LinkVerDetailRVAdapter.LinkVerDetailViewHolder {
        binding = ItemLinkVerDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LinkVerDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LinkVerDetailRVAdapter.LinkVerDetailViewHolder,
        position: Int
    ) {
        holder.link.text = links[position].hyperlinkTitle

        val mTransform: Linkify.TransformFilter = Linkify.TransformFilter { p0, p1 -> "" }
        val pattern: Pattern = Pattern.compile(links[position].hyperlinkTitle)
        Linkify.addLinks(holder.link, pattern, links[position].hyperlink, null, mTransform)
    }

    override fun getItemCount(): Int = links.size

    inner class LinkVerDetailViewHolder(itemView: ItemLinkVerDetailBinding): RecyclerView.ViewHolder(itemView.root) {
        val link: TextView = itemView.linkTv
    }

    fun setData(links: List<Link>) {
        this.links = links
        notifyDataSetChanged()
    }
}