package com.gerforce.traelc.zhsrhj

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_collection.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.content.ContentValues
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.graphics.Matrix
import com.google.gson.Gson
import okhttp3.*
import okio.Utf8
import android.util.Base64
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.experimental.and


class CollectionActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_back -> {
                this.finish()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_upload -> {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        photograph()
                    } else {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    }
                } else {
                    photograph()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_send -> {
                if (!txtCount.text.toString().matches(Regex("^[0-9]+([.][0-9]+)?$"))) {
                    alert { "数量不能为空并且必须输入数字或小数，请重新输入！" }
                    return@OnNavigationItemSelectedListener false
                }
                if (txtCount.text.toString() != "0" && uploadPhoto == null) {
                    alert { "数量不为0时必须上传照片！" }
                    return@OnNavigationItemSelectedListener false
                }


                alert("是否发送？") {
                    yesButton {
                        var progress = indeterminateProgressDialog("发送中")
                        progress.show()
                        var add = CollectionSubmit(
                                AssignmentID = assignment.AssignmentID,
                                Count = txtCount.text.toString().toDouble(),
                                Special3ID = 1,
                                Problem = txtProblem.text.toString(),
                                IsFinished = tbFinished.isChecked,
                                PhotoSource = null
                        )

                        if (uploadPhoto != null) {
                            var width = uploadPhoto!!.width
                            var height = uploadPhoto!!.height
                            var matrix = Matrix()
                            matrix.preScale(0.125.toFloat(), 0.125.toFloat());
                            var newBM = Bitmap.createBitmap(uploadPhoto, 0, 0, width, height, matrix, false)
                            if (newBM != uploadPhoto) {
                                uploadPhoto!!.recycle()
                            }
                            var buf = ByteBuffer.allocate(newBM!!.byteCount)
                            newBM!!.copyPixelsToBuffer(buf)
                            val stream = ByteArrayOutputStream()
                            newBM.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                            val b = stream.toByteArray()
                            add.PhotoSource = Base64Coder.encodeLines(b)

                        }
                        doAsync {
                            try {
                                val client = OkHttpClient()
                                val requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), Gson().toJson(add))
                                val request = Request.Builder()
                                        .url(Util.inst.interfaceUrl + "Android")
                                        .post(requestBody)
                                        .build()
                                val response = client.newCall(request).execute()
                                if (response.isSuccessful) {
                                    if (add.IsFinished) {
                                        finish()
                                    } else {
                                        uiThread { alert("发送成功！") {}.show() }
                                    }
                                    response.close()
                                } else {
                                    uiThread { alert("发送失败！") {}.show() }
                                }
                            } catch (e: Exception) {
                                uiThread { alert("网络错误！") {}.show() }
                            } finally {
                                uiThread { progress.hide() }
                            }
                        }
                    }
                    noButton { }
                }
                        .show()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                photograph()
            }
        }
    }

    private fun photograph() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStampFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val filename = timeStampFormat.format(Date())
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, filename)
        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, 0)
    }

    private lateinit var assignment: AssignmentTemplate

    private lateinit var adSp1: ArrayAdapter<Special1Template>
    private lateinit var adSp2: ArrayAdapter<Special2Template>
    private lateinit var adSp3: ArrayAdapter<Special3Template>

    private var uploadPhoto: Bitmap? = null

    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        navi_collection.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        assignment = intent.getParcelableExtra("selectedItem") as AssignmentTemplate
        title = when (assignment.AssignmentType) {
            0 -> "地点位置(居住区)"
            1 -> "地点位置(白天)"
            2 -> "地点位置(早上)"
            3 -> "地点位置(晚上)"
            else -> "信息采集"
        }
        txtAddress.text = assignment.Address
        txtDistinct.text = assignment.DistrictName
        txtStreet.text = assignment.StreetName
        txtName.text = assignment.Name

        adSp1 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Util.inst.special1)
        spSpecial1.adapter = adSp1

        spSpecial1.onItemSelectedListener = Sp1SelectedListener()
        spSpecial2.onItemSelectedListener = Sp2SelectedListener()
        spSpecial3.onItemSelectedListener = Sp3SelectedListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            uploadPhoto = BitmapFactory.decodeStream(contentResolver.openInputStream(photoUri))
            ivPhoto.setImageBitmap(uploadPhoto)
        }
    }

    internal inner class Sp1SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (assignment.Mode == 0) {
                when (assignment.AssignmentType) {
                    0 -> adSp2 = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, adSp1.getItem(position).Special2Template)
                    1 -> adSp2 = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, adSp1.getItem(position).Special2Template.filter { it.Special3Template.any { it.day == true } })
                    2 -> adSp2 = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, adSp1.getItem(position).Special2Template.filter { it.Special3Template.any { it.morning == true } })
                    3 -> adSp2 = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, adSp1.getItem(position).Special2Template.filter { it.Special3Template.any { it.night == true } })
                }
            } else {
                adSp2 = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, adSp1.getItem(position).Special2Template)
            }
            spSpecial2.adapter = adSp2
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    internal inner class Sp2SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            adSp3 = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item, adSp2.getItem(position).Special3Template)
            spSpecial3.adapter = adSp3
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    internal inner class Sp3SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            var item = adSp3.getItem(position)
            txtScore.text = item.score
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}
