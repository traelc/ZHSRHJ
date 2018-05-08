package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync

class LoginActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        pbLogin.visibility = View.VISIBLE
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                doAsync {
                    try {
                        val POST_FORM = "http://203.156.245.74:10003/api/User"
                        val client = OkHttpClient()
                        val requestBody = FormBody.Builder().add("userName", txtName.text.toString()).add("password",txtPassword.text.toString()).build()
                        val request = Request.Builder()
                                .url(POST_FORM)
                                .post(requestBody)
                                .build()

                        val call = client.newCall(request)
                        val response = call.execute()
                        if (response.isSuccessful) {
                            var aa = response.body()
                            if (aa != null) {
                                //startActivity<MainActivity>()
                            } else {

                            }
                            response.close()
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                    finally {
                        pbLogin.visibility = View.INVISIBLE
                    }
                }
            }

        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
