package com.mangpo.bookclub.view.adpater

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.bookclub.databinding.ItemLinkBinding
import com.mangpo.bookclub.model.entities.Link

class LinkRVAdapter(): RecyclerView.Adapter<LinkRVAdapter.LinkViewHolder>() {
    interface MyClickListener {
        fun removeLink(position: Int)
    }

    private var links: ArrayList<Link> = arrayListOf()

    private lateinit var binding: ItemLinkBinding
    private lateinit var myClickListener: MyClickListener
    private lateinit var linkViewHolder: LinkViewHolder

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LinkRVAdapter.LinkViewHolder {
        binding = ItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        linkViewHolder = LinkViewHolder(binding)

        return linkViewHolder
    }

    override fun onBindViewHolder(holder: LinkRVAdapter.LinkViewHolder, position: Int) {
        if (links[position].hyperlinkTitle.isNotBlank())
            holder.linkTitle.setText(links[position].hyperlinkTitle)
        else
            holder.linkTitle.text.clear()

        if (links[position].hyperlink.isNotBlank())
            holder.link.setText(links[position].hyperlink)
        else
            holder.link.text.clear()

        holder.removeIv.setOnClickListener {
            myClickListener.removeLink(position)
        }
    }

    override fun getItemCount(): Int = links.size

    inner class LinkViewHolder(itemView: ItemLinkBinding): RecyclerView.ViewHolder(itemView.root) {
        val linkTitle: EditText = itemView.linkTitleEt
        val link: EditText = itemView.linkEt
        val removeIv: ImageView = itemView.linkRemoveIv

        init {
            linkTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    links[adapterPosition].hyperlinkTitle = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            link.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    links[adapterPosition].hyperlink = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    fun setMyClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    fun addLink() {
        links.add(Link(null, "", ""))
        notifyDataSetChanged()
    }

    fun removeLink(position: Int) {
        links.removeAt(position)
        notifyDataSetChanged()
    }

    fun getLinks(): List<Link> = links

    fun setLinks(links: ArrayList<Link>) {
        this.links = links
        notifyDataSetChanged()
    }
}