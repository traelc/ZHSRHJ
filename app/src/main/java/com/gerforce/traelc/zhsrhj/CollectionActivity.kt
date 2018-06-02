package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_collection.*
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import android.widget.AdapterView.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_collection_list.*


class CollectionActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_back -> {
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_upload -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_send -> {
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    lateinit var assingment: AssignmentTemplate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        navi_collection.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        /*   assingment = intent.getSerializableExtra("selectedItem") as AssignmentTemplate
           txtAddress.text = assingment.Address
           txtDistinct.text = assingment.DistrictName
           txtStreet.text = assingment.StreetName
           txtName.text = assingment.Name

       var adSp1 = ArrayAdapter<Special1Template>(this, android.R.layout.simple_spinner_item, Util.inst.special1)

           spSpecial1.adapter = adSp1

           spSpecial1.setOnItemClickListener { parent, view, position, id ->
               var adSp2 = ArrayAdapter<Special2Template>(this, android.R.layout.simple_spinner_item, adSp1.getItem(position).Special2Template)
               spSpecial2.adapter = adSp2

               spSpecial2.setOnItemClickListener { parent, view, position, id ->
                   var adSp3 = ArrayAdapter<Special3Template>(this, android.R.layout.simple_spinner_item, adSp2.getItem(position).Special3Template)
                   spSpecial3.adapter = adSp3

                   spSpecial3.setOnItemClickListener { parent, view, position, id ->
                       var item = adSp3.getItem(position)

                       txtScore.text = item.score

                   }
               }
           }
   */

    }
}
