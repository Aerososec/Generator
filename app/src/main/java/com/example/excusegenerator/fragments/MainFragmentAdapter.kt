package com.example.excusegenerator.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.excusegenerator.R
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.main.MainActivity
import com.google.android.material.card.MaterialCardView

class MainFragmentAdapter(private val context : Context, private val data : List<String>)
    : CategoriesView.ExcuseAdapter, RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {
    val appPreferences = AppPreferences.create(context)
    val listIconItems = listOf(
        R.drawable.category_item_one,
        R.drawable.category_item_two,
        R.drawable.category_item_three
    )

    override fun showExcuse(categoryName : String){
        (context as MainActivity).showExcuseByCategory(categoryName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.vh_category, parent, false)
            .findViewById<MaterialCardView>(R.id.card_container)

        return ViewHolder(item)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = data[position]
        val itemLayout = holder.itemLayout

        val iconPosition = position % listIconItems.size
        itemLayout.findViewById<TextView>(R.id.tv_category_name).text = item
        itemLayout.findViewById<ImageView>(R.id.categoty_icon).setImageResource(listIconItems[iconPosition])


        itemLayout.setOnClickListener {
            showExcuse(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val itemLayout : MaterialCardView) : RecyclerView.ViewHolder(itemLayout)
}