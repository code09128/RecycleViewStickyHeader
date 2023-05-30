package com.ezaio.recycleviewstickheader

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.ezaio.recycleviewstickheader.CustomStickyHeaderLayout.Companion.TYPE_STICKY_LAYOUT

/**
 * @auther dustin.hsieh
 * @Date on 2023/1/19
 * @Description
 */
class Adapter(val context: Context) : BaseRecyclerAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val titleColor = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_title, parent, false)
        val optionClose = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)

        return if (viewType == TYPE_STICKY_LAYOUT) {
            ViewHolder(titleColor)
        } else {
            ViewHolder(optionClose)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
//        holder.getView<ImageView>(R.id.tv_text)

        setView(holder)
    }

    private fun setView(holder: ViewHolder) {
        val textText = holder.getView<TextView>(R.id.tv_text)

        if (textText.text == "測試資料"){
//            textText.setBackgroundColor(context.getColor(R.color.purple_200))
        }
        else if (textText.text == "Title"){
            textText.setBackgroundColor(context.getColor(R.color.design_default_color_primary))
        }
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 3) {
            TYPE_STICKY_LAYOUT
        } else {
            super.getItemViewType(position)
        }
    }
}

