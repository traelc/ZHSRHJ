package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navi_main.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        btCollection.setOnClickListener {
            startActivity<CollectionListActivity>()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_download -> {
                getSpecial()
                alert("更新配置成功！") {}.show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private var special: String by Preference(this, "special", "")

    private fun getSpecial() {
        doAsync {
            special = URL(Util.inst.interfaceUrl + "User").readText()
            Util.inst.special1 = Gson().fromJson<List<Special1Template>>(special, object : TypeToken<List<Special1Template>>() {}.type)
        }
    }
}




