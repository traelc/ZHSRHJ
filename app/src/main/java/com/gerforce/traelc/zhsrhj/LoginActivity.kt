package com.gerforce.traelc.zhsrhj

import android.os.Bundle
import android.preference.Preference
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.*
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_login -> {
                if (txtName.text.isNullOrEmpty() || txtName.text.isNullOrEmpty()) {
                    alert("用户名或密码不能为空！") {}.show()
                }
                var progress = indeterminateProgressDialog("登录中")
                progress.show()
                doAsync {
                    try {
                        val client = OkHttpClient()
                        val requestBody = FormBody.Builder().add("userName", txtName.text.toString()).add("password", txtPassword.text.toString()).build()
                        val request = Request.Builder()
                                .url(Util.inst.interfaceUrl + "User")
                                .post(requestBody)
                                .build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            var body = response.body()
                            if (body != null) {
                                Util.inst.user = Gson().fromJson(body.string(), User::class.java)
                                startActivity<MainActivity>()
                            } else {
                                uiThread { alert("用户名和密码错误！") {}.show() }
                            }
                            response.close()
                        }
                    } catch (e: Exception) {
                        uiThread { alert("网络错误！") {}.show() }
                    } finally {
                        uiThread { progress.hide() }
                    }
                }
            }
            R.id.navigation_update -> {
                getSpecial()
                alert("更新配置成功！") {}.show()
            }
        }
        true
    }

    private fun getSpecial() {
        doAsync {
            special = URL(Util.inst.interfaceUrl + "User").readText()
            Util.inst.special1 = Gson().fromJson<List<Special1Template>>(special, object : TypeToken<List<Special1Template>>() {}.type)
        }
    }

    private var special: String by Preference(this, "special", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        navigation.selectedItemId = navigation.menu.getItem(1).itemId
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (special.isNullOrEmpty()) {
            getSpecial()
        } else {
            Util.inst.special1 = Gson().fromJson<List<Special1Template>>(special, object : TypeToken<List<Special1Template>>() {}.type)
        }
    }
}

