# RecycleViewStickyHeader
StickyHeaderLayout demo 

package com.ezaio.recycleviewstickheader

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @auther dustin.hsieh
 * @Date on 2023/1/19
 * @Description
 */
@SuppressLint("ViewConstructor")
class CustomStickyHeaderLayout : FrameLayout {
    private var mContext: Context? = null
    private var mRecyclerView: RecyclerView? = null

    //替換的layout布局
    var mStickyLayout: FrameLayout? = null
    private var showStickLayout = false
    private var showStickItemPosition = 0

    //型態參數
    companion object{
        val TYPE_STICKY_LAYOUT = 1
    }

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context,attrs) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context,attrs,defStyleAttr) {
        mContext = context
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        //外界只能向StickyHeaderLayout添加一個RecyclerView,而且只能添加RecyclerView(建立優先聲明)
        require(!(childCount > 0 || child !is RecyclerView)) {
            "StickyHeaderLayout can host only one direct child，it must be RecyclerView"
        }
        super.addView(child, index, params)
        mRecyclerView = child
        addOnScrollListener()
        addStickyLayout()
    }

    /**滾動監聽*/
    private fun addOnScrollListener() {
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 滾動的時候隨時監聽。
                Log.d("TAG", "onScrolled: dy-->$dy")
                updateStickyView(dy)
            }
        })
    }

    /**增加置頂的layout件置*/
    private fun addStickyLayout() {
        mStickyLayout = FrameLayout(mContext!!)
        val framelayout = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        mStickyLayout!!.layoutParams = framelayout
        mStickyLayout!!.visibility = View.GONE
        super.addView(mStickyLayout, 1, framelayout)
    }

    /**更新頭頂位置*/
    private fun updateStickyView(dy: Int) {
        val adapter = mRecyclerView!!.adapter
        //獲取列表的第一個項次
        val firstVisibleItemPosition: Int
        if (dy < 0) {
            // 向下滑動時吸頂佈局在列表中的位置的上一個位置滑出屏幕時才隱藏頭部吸頂佈局，否則會導致頭部吸頂佈局還沒有隱藏，列表吸頂佈局就滑出
            firstVisibleItemPosition = getFirstVisibleItemPosition() + 1
        } else {
            firstVisibleItemPosition = getFirstVisibleItemPosition()
        }

        if (adapter != null) {
            if (mStickyLayout!!.childCount == 0) {
                val holder = adapter.onCreateViewHolder(
                    mStickyLayout!!,
                    TYPE_STICKY_LAYOUT
                )
                mStickyLayout!!.addView(holder.itemView)
            }
            showStickLayout(firstVisibleItemPosition, dy)
        }

        // 這是是處理第一次打開時，吸頂佈局已經添加到StickyLayout，但StickyLayout的高依然為0的情況
        if (mStickyLayout!!.childCount > 0 && mStickyLayout!!.height == 0) {
            mStickyLayout!!.requestLayout()
        }
    }

    /**
     * 設置顯示跟隱藏的layout
     * @param firstVisibleItemPosition 第一個可見的item位置
     * @param dy                       recyclerView滑動的偏移位子
     */
    @SuppressLint("ResourceAsColor")
    fun showStickLayout(firstVisibleItemPosition: Int, dy: Int) {
        if (firstVisibleItemPosition > 0) {
            if (showStickLayout && dy < 0 && firstVisibleItemPosition <= showStickItemPosition) {
                showStickLayout = false
                mStickyLayout!!.visibility = GONE
                Log.d("TAG", "updateStickyView: --------------------gone")
            }
            else if (!showStickLayout && dy >= 0 && firstVisibleItemPosition >= showStickItemPosition) {
                showStickLayout = true
                mStickyLayout!!.visibility = VISIBLE
                mStickyLayout!!.setBackgroundColor(R.color.black) //設定背景色
                Log.d("TAG", "updateStickyView: --------------------visible")
            }
        }
        else if (showStickItemPosition == 0) {
            showStickLayout = true
            mStickyLayout!!.visibility = VISIBLE
        }
    }
		
    /**獲取當下第一個顯示的item*/
    private fun getFirstVisibleItemPosition(): Int {
        var firstVisibleItem = -1

        val layout = mRecyclerView!!.layoutManager //控制RecycleView layout manager定義

        if (layout != null) {
            if (layout is GridLayoutManager) {
                firstVisibleItem = layout.findFirstVisibleItemPosition()
            } else if (layout is LinearLayoutManager) {
                firstVisibleItem = layout.findFirstVisibleItemPosition()
            } else if (layout is StaggeredGridLayoutManager) {
                val firstPositions = IntArray(layout.spanCount)
                layout.findFirstVisibleItemPositions(firstPositions)
                firstVisibleItem = getMin(firstPositions)
            }
        }
        return firstVisibleItem
    }

    /**取得項次*/
    private fun getMin(arr: IntArray): Int {
        var min = arr[0]
        for (x in 1 until arr.size) {
            if (arr[x] < min) min = arr[x]
        }
        return min
    }

    /**設定第幾項改變動作設定*/
    fun setShowStickItemPosition(showStickItemPosition: Int) {
        this.showStickItemPosition = showStickItemPosition
    }
}

---------------------------------------------------------------------------------------------
package com.ezaio.recycleviewstickheader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        r_recycler.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        r_recycler!!.layoutManager = layoutManager
        adapter = Adapter(this)
        r_customStickyHeader!!.setShowStickItemPosition(4) //第幾項設定
        r_recycler!!.adapter = adapter
    }
}
