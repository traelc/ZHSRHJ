package com.gerforce.traelc.zhsrhj

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
import org.jetbrains.anko.doAsync
import java.net.URL


class CollectionListActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                // message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_list)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        tlMain.setupWithViewPager(vpDetail)
        val viewList = ArrayList<View>()
        val view1 = layoutInflater.inflate(R.layout.list_view, null)
        val view2 = layoutInflater.inflate(R.layout.list_view, null)
        val view3 = layoutInflater.inflate(R.layout.list_view, null)
        val view4 = layoutInflater.inflate(R.layout.list_view, null)
        viewList.add(view1)
        viewList.add(view2)
        viewList.add(view3)
        viewList.add(view4)

        vpDetail.adapter = (object : PagerAdapter() {
            override fun getCount(): Int {
                //这个方法是返回总共有几个滑动的页面（）
                return viewList.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                //该方法判断是否由该对象生成界面。
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                //这个方法返回一个对象，该对象表明PagerAapter选择哪个对象放在当前的ViewPager中。这里我们返回当前的页面
                vpDetail.addView(viewList[position])
                return viewList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                //这个方法从viewPager中移动当前的view。（划过的时候）
                vpDetail.removeView(viewList[position])
            }
        })

        //——————————————————————————————————重点理解——————————————————————————————————
        // 原来findviewById是View这个类中的方法，默认调用时其实应该是：this.findviewById();
        //由于listview标签的声明并不在当前的viewPager所在的xml布局中，所以直接通过findviewById方法是不能得到该listview的实例的。所以我们要用view1.findViewById（）方法找到listview
        val listView1 = view1.findViewById(R.id.listview) as ListView
        val listView2 = view2.findViewById(R.id.listview) as ListView
        val listView3 = view3.findViewById(R.id.listview) as ListView
        val listView4 = view4.findViewById(R.id.listview) as ListView
        //———————————————————————————————————重点理解——————————————————————————————————

        doAsync {
            val data = Gson().fromJson<List<AssignmentTemplate>>(URL(Util.inst.interfaceUrl + "Collection?UserID=" + Util.inst.user.UserID).readText(), object : TypeToken<List<AssignmentTemplate>>() {}.type)

            val adapter1 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 0 })
            val adapter2 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 1 })
            val adapter3 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 2 })
            val adapter4 = CollectionListAdapter(baseContext, data.filter { it.AssignmentType == 3 })
            //为ListView设置适配器
            listView1.adapter = adapter1
            listView2.adapter = adapter2
            listView3.adapter = adapter3
            listView4.adapter = adapter4
        }

    }
}
