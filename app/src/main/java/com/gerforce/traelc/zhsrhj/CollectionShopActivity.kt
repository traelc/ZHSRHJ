package com.gerforce.traelc.zhsrhj

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_collection_shop.*
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

class CollectionShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_shop)

        navi_collection.selectedItemId = navi_collection.menu.getItem(2).itemId
        navi_collection.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        tbCollection.inflateMenu(R.menu.toolbar_collection)
        tbCollection.setOnMenuItemClickListener {
            if (main_drawer_layout.isDrawerOpen(main_right_drawer_layout)) {
                main_drawer_layout.closeDrawer(main_right_drawer_layout)
            } else {
                main_drawer_layout.openDrawer(main_right_drawer_layout)
            }
            return@setOnMenuItemClickListener true
        }

        tbCollection.setNavigationOnClickListener {
            finish()
        }

        adSp1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, Util.inst.special1.filter { it.mode.toInt() == 2 })
        spSpecial1.adapter = adSp1
        spSpecial1.onItemSelectedListener = Sp1SelectedListener()
        spSpecial2.onItemSelectedListener = Sp2SelectedListener()
        spSpecial3.onItemSelectedListener = Sp3SelectedListener()

        adDistinct = ArrayAdapter(this, android.R.layout.simple_list_item_1, Util.inst.distinct)
        spDistinct.adapter = adDistinct
        spDistinct.onItemSelectedListener = Sp4SelectedListener()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_album -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        visitAlbum()
                    } else {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    }
                } else {
                    visitAlbum()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_upload -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        photograph()
                    } else {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                    }
                } else {
                    photograph()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_send -> {
                if (!txtCount.text.toString().matches(Regex("^[0-9]+([.][0-9]+)?$"))) {
                    alert("数量不能为空并且必须输入数字或小数，请重新输入！").show()
                    return@OnNavigationItemSelectedListener false
                }
                if (txtCount.text.toString() != "0" && uploadPhoto == null) {
                    alert("数量不为0时必须上传照片！").show()
                    return@OnNavigationItemSelectedListener false
                }

                //main_drawer_layout.openDrawer(main_right_drawer_layout)

                alert("是否发送？") {
                    yesButton {
                        val progress = indeterminateProgressDialog("发送中")
                        progress.show()
                        val add = CollectionShopSubmit(
                                StreetID = (spStreet.selectedItem as StreetTemplate).StreetID,
                                Count = txtCount.text.toString().toDouble(),
                                Special3ID = (spSpecial3.selectedItem as Special3Template).id,
                                Memo = txtMemo.text.toString(),
                                ShopSN = txtShopSN.text.toString(),
                                PhotoSource = null,
                                UserID = Util.inst.user.UserID
                        )

                        if (uploadPhoto != null) {
                            val width = uploadPhoto!!.width
                            val height = uploadPhoto!!.height
                            val matrix = Matrix()
                            matrix.preScale(0.125.toFloat(), 0.125.toFloat())
                            val newBM = Bitmap.createBitmap(uploadPhoto, 0, 0, width, height, matrix, false)
                            /*if (newBM != uploadPhoto) {
                                uploadPhoto!!.recycle()
                            }*/
                            val buf = ByteBuffer.allocate(newBM!!.byteCount)
                            newBM.copyPixelsToBuffer(buf)
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
                                        .url(Util.inst.interfaceUrl + "Shop")
                                        .post(requestBody)
                                        .build()
                                val response = client.newCall(request).execute()
                                if (response.isSuccessful) {
                                    uiThread {
                                        alert("发送成功！").show()
                                        txtDistinctPrevious.text = spDistinct.selectedItem.toString()
                                        txtStreetPrevious.text = spStreet.selectedItem.toString()
                                        txtShopSNPrevious.text = txtShopSN.text

                                        spSpecial1Previous.text = spSpecial1.selectedItem.toString()
                                        spSpecial2Previous.text = spSpecial2.selectedItem.toString()
                                        spSpecial3Previous.text = spSpecial3.selectedItem.toString()
                                        txtMemoPrevious.text = txtMemo.text
                                        txtScorePrevious.text = txtScore.text
                                        txtCountPrevious.text = txtCount.text
                                        ivPhotoPrevious.setImageBitmap(uploadPhoto)
                                        txtCount.setText("0")
                                        ivPhoto.setImageResource(R.mipmap.n1)
                                        uploadPhoto = null
                                    }
                                    response.close()
                                } else {
                                    uiThread { alert("发送失败！").show() }
                                }
                            } catch (e: Exception) {
                                uiThread { alert("网络错误！").show() }
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
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                visitAlbum()
            }
        }
    }


    private fun visitAlbum() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun photograph() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val filename = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA).format(Date())
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, filename)
        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    uploadPhoto = BitmapFactory.decodeStream(contentResolver.openInputStream(photoUri))
                    uploadPhoto = toTurn(uploadPhoto!!, getPath(photoUri))
                    ivPhoto.setImageBitmap(uploadPhoto)
                }
            }

            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    var imagePath: String? = null
                    val uri = data.data
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        //如果是document类型的uri,则通过document id来处理
                        val docId = DocumentsContract.getDocumentId(uri)
                        if ("com.android.providers.media.documents" == uri.authority) {
                            val id = docId.split(":")[1]
                            val selection = MediaStore.Images.Media._ID + "=" + id
                            imagePath = this.getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)!!
                        } else if ("com.android.providers.downloads.documents" == uri.authority) {
                            val contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"), docId.toLong())
                            imagePath = this.getImagePath(contentUri, null.toString())!!
                        }
                    } else if ("content" == uri.scheme) {
                        //如果是content类型的Uri,则使用普通的方式处理
                        imagePath = this.getImagePath(uri, null.toString())!!
                    } else if (uri.scheme == "file") {
                        //如果是file类型的Uri，则直接获取图片路径即可
                        imagePath = uri.path
                    }
                    if (imagePath != null) {
                        uploadPhoto = BitmapFactory.decodeFile(imagePath)
                        ivPhoto.setImageBitmap(uploadPhoto)
                    }
                }
            }
        }
    }

    private fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    private fun readPictureDegree(path: String): Int {
        var degree: Int
        var exifInterface = ExifInterface(path)
        var orientation: Int = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        degree = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        return degree
    }

    private fun toTurn(img: Bitmap, path: String): Bitmap {
        var img = img
        val matrix = Matrix()
        matrix.postRotate(readPictureDegree(path).toFloat())
        val width = img.width
        val height = img.height
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true)
        return img
    }

    private fun getImagePath(uri: Uri, selection: String): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    internal inner class Sp1SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            adSp2 = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, adSp1.getItem(position).Special2Template)
            spSpecial2.adapter = adSp2
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    internal inner class Sp2SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            adSp3 = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, adSp2.getItem(position).Special3Template)
            spSpecial3.adapter = adSp3
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    internal inner class Sp3SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = adSp3.getItem(position)
            txtScore.text = item.score
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    internal inner class Sp4SelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            adStreet = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, adDistinct.getItem(position).StreetTemplate)
            spStreet.adapter = adStreet
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private lateinit var adSp1: ArrayAdapter<Special1Template>
    private lateinit var adSp2: ArrayAdapter<Special2Template>
    private lateinit var adSp3: ArrayAdapter<Special3Template>

    private lateinit var adDistinct: ArrayAdapter<DistinctTemplate>
    private lateinit var adStreet: ArrayAdapter<StreetTemplate>

    private var uploadPhoto: Bitmap? = null
    private lateinit var photoUri: Uri

}
