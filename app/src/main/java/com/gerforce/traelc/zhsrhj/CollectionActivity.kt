package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_collection.*

class CollectionActivity : AppCompatActivity() {

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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        assingment = intent.getSerializableExtra("selectedItem") as AssignmentTemplate
        txtAddress.text = assingment.Address
        txtDistinct.text = assingment.DistrictName
        txtStreet.text = assingment.StreetName
        txtName.text = assingment.Name


    }
}
