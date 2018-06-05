package com.gerforce.traelc.zhsrhj

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_collection_list.*
import android.view.ViewGroup

import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_collection.*
import org.jetbrains.anko.*
import java.net.URL


class CollectionListActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_return -> {
                this.finish()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_refresh -> {
                refresh()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_detail -> {
                when (tlMain.selectedTabPosition) {
                    0 -> {
                        if (sel0 != -1) {
                            startActivityForResult<CollectionActivity>(0,"selectedItem" to adapter0.getItem(sel0) as AssignmentTemplate)
                        } else {
                            alert("请选择对应记录！") {}.show()
                        }
                    }
                    1 -> {
                        if (sel1 != -1) {
                            startActivityForResult<CollectionActivity>(0,"selectedItem" to adapter1.getItem(sel1) as AssignmentTemplate)
                        } else {
                            alert("请选择对应记录！") {}.show()
                        }
                    }
                    2 -> {
                        if (sel2 != -1) {
                            startActivityForResult<CollectionActivity>(0,"selectedItem" to adapter2.getItem(sel2) as AssignmentTemplate)
                        } else {
                            alert("请选择对应记录！") {}.show()
                        }
                    }
                    3 -> {
                        if (sel3 != -1) {
                            startActivityForResult<CollectionActivity>(0,"selectedItem" to adapter3.getItem(sel3) as AssignmentTemplate)
                        } else {
                            alert("请选择对应记录！") {}.show()
                        }
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == 100) {
            refresh()
        }
    }

    private lateinit var adapter0: CollectionListAdapter
    private lateinit var adapter1: CollectionListAdapter
    private lateinit var adapter2: CollectionListAdapter
    private lateinit var adapter3: CollectionListAdapter

    private lateinit var listView0: ListView
    private lateinit var listView1: ListView
    private lateinit var listView2: ListView
    private lateinit var listView3: ListView

    private var sel0: Int = -1
    private var sel1: Int = -1
    private var sel2: Int = -1
    private var sel3: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_list)

        tlMain.setupWithViewPager(vpDetail)

        this.navi_collection_list.selectedItemId = this.navi_collection_list.menu.getItem(2).itemId
        this.navi_collection_list.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val viewList = ArrayList<View>()

        val view0 = layoutInflater.inflate(R.layout.list_view, null)
        val view1 = layoutInflater.inflate(R.layout.list_view, null)
        val view2 = layoutInflater.inflate(R.layout.list_view, null)
        val view3 = layoutInflater.inflate(R.layout.list_view, null)

        viewList.add(view0)
        viewList.add(view1)
        viewList.add(view2)
        viewList.add(view3)

        vpDetail.adapter = (object : PagerAdapter() {
            override fun getCount(): Int {
                return viewList.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                //该方法判断是否由该对象生成界面。
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                vpDetail.addView(viewList[position])
                return viewList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                vpDetail.removeView(viewList[position])
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> "居住区"
                    1 -> "道路(白天)"
                    2 -> "道路(早上)"
                    3 -> "道路(晚上)"
                    else -> super.getPageTitle(position)
                }
            }
        })

        listView0 = view0.find(R.id.listview)
        listView1 = view1.find(R.id.listview)
        listView2 = view2.find(R.id.listview)
        listView3 = view3.find(R.id.listview)

        listView0.setOnItemClickListener { parent, view, position, id ->
            sel0 = position
        }
        listView1.setOnItemClickListener { parent, view, position, id ->
            sel1 = position
        }
        listView2.setOnItemClickListener { parent, view, position, id ->
            sel2 = position
        }
        listView3.setOnItemClickListener { parent, view, position, id ->
            sel3 = position
        }
        refresh()
    }

    private fun refresh() {
        doAsync {
            val data = Gson().fromJson<List<AssignmentTemplate>>(URL(Util.inst.interfaceUrl + "Android?UserID=" + Util.inst.user.UserID).readText(), object : TypeToken<List<AssignmentTemplate>>() {}.type)
            adapter0 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 0 })
            adapter1 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 1 })
            adapter2 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 2 })
            adapter3 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 3 })

            uiThread {
                listView0.adapter = adapter0
                listView1.adapter = adapter1
                listView2.adapter = adapter2
                listView3.adapter = adapter3


            }
        }
    }
}
