package com.example.excusegenerator.fragments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.excusegenerator.R
import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.main.MainActivity
import com.google.android.material.card.MaterialCardView

class FavoritesFragmentAdapter(
    private val context: Context,
    private val data: MutableList<Excuse>
) : RecyclerView.Adapter<FavoritesFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vh_favorites, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.getOrNull(position) ?: return
        with(holder.itemView) {
            findViewById<TextView>(R.id.tv_excuse_text).text = item.Text
            findViewById<TextView>(R.id.tv_category).text = item.Category.CategoryName

            findViewById<ImageButton>(R.id.btn_delete).setOnClickListener {
                (context as? MainActivity)?.deleteFavorite(item.ID.toString())
                removeItem(position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun removeItem(position: Int) {
        if (position in 0 until data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, data.size - position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}