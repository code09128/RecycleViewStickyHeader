package com.ezaio.recycleviewstickheader

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @author dustin.hsieh
 * @Date on 2020/9/30
 * @Description BaseAdapter封裝
 */
open class BaseRecyclerAdapter() : RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder>(){
    //初始化為null,使用lateinit來修飾，點擊跟長按用 lambda 表達式
    private lateinit var mListener: (position: Int) -> Unit
    private lateinit var mLongListener: (position: Int) -> Unit

    fun setOnItemClickListener(mListener: (position: Int) -> Unit) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    /**如果需要回傳點擊事件的話，adapter 複寫onBindViewHolder 裡面要呼叫super.onBindViewHolder(holder, position)*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mListener.invoke(position)
        }
    }

    interface OnSelectedItemListener {
        fun onSelected(type: String)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //使用SparseArrayCompat是為了重複視圖，不用每次getView都去findViewById
        private val mViews = SparseArrayCompat<View>()

        /**
         * 通過resId獲取view
         * 將獲取到的視圖轉換成具體的視圖，例如：TextView，Button等
         * 這裡主要用到的是Kotlin裡面的as操作符
         */
        fun <V : View> getView(id: Int): V {
            var view = mViews[id]

            if (view == null) {
                view = itemView.findViewById(id)
                mViews.put(id, view)
            }

            return view as V
        }
    }
}