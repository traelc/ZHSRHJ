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
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private fun getSpecial() {
        doAsync {
            special = URL(Util.inst.interfaceUrl + "User").readText()
            Util.inst.special1 = Gson().fromJson<List<Special1Template>>(special, object : TypeToken<List<Special1Template>>() {}.type)
        }
    }

    private var special: String by Preference(this, "special", "")
    private var name: String by Preference(this, "userName", "")
    private var password: String by Preference(this, "password", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvVersion.text = this.packageManager.getPackageInfo(this.packageName, 0).versionName

        if (!name.isNullOrEmpty()) {
            txtName.setText(name)
        }
        if (!password.isNullOrEmpty()) {
            txtPassword.setText(password)
        }
        if (special.isNullOrEmpty()) {
            getSpecial()
        } else {
            Util.inst.special1 = Gson().fromJson<List<Special1Template>>(special, object : TypeToken<List<Special1Template>>() {}.type)
        }

        btLogin.setOnClickListener {
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
                            name = txtName.text.toString()
                            password = txtPassword.text.toString()
                            startActivity<CollectionListActivity>()
                        } else {
                            uiThread { alert("用户名或密码错误！") {}.show() }
                        }
                        response.close()
                    } else {
                        uiThread { alert("用户名或密码错误！") {}.show() }
                    }
                } catch (e: Exception) {
                    uiThread { alert("网络错误！") {}.show() }
                } finally {
                    uiThread { progress.hide() }
                }
            }
        }
    }
}

