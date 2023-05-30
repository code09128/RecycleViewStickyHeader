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