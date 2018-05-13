package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.preference.Preference
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        pbLogin.visibility = View.VISIBLE
        var aa = indeterminateProgressDialog("This a progress dialog")
        aa.show()
        aa.dismiss()
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
                            var body = response.body()
                            if (body != null) {
                                Util.inst.user = Gson().fromJson(body.string(), User::class.java)
                                startActivity<MainActivity>()
                            } else {
                                toast("用户名和密码错误！")
                            }
                            response.close()
                        }
                    } catch (e: Exception) {
                        print(e.message)
                        toast("用户名和密码错误！")
                    } finally {
                        pbLogin.visibility = View.INVISIBLE
                    }
                }
            }
        }
        false
    }

    private var special: String by Preference(this, "special", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        doAsync {
            if (special.isNullOrEmpty()) {
                val text = URL(Util.inst.uri + "User").readText()
                val obj = Gson().fromJson(text, Special1Template::class.java)
                //获得该地点的详细地址之后，回到主线程把地址显示在界面上
            }
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}

