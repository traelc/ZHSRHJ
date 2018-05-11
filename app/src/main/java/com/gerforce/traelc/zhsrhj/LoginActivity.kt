package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        pbLogin.visibility = View.VISIBLE
        if (txtName.text.isNullOrEmpty() || txtName.text.isNullOrEmpty()) {
            toast("请输入用户名和密码！")
        }
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                doAsync {
                    try {
                        val client = OkHttpClient()
                        val requestBody = FormBody.Builder().add("userName", txtName.text.toString()).add("password", txtPassword.text.toString()).build()
                        val request = Request.Builder()
                                .url(Util.inst.uri + "User")
                                .post(requestBody)
                                .build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                Util.inst.user = Gson().fromJson(response.body().toString(), User::class.java)
                                //startActivity<MainActivity>()
                            } else {
                                toast("用户名和密码错误！")
                            }
                            response.close()
                        }
                    } catch (e: Exception) {
                        toast("用户名和密码错误！")
                    } finally {
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
