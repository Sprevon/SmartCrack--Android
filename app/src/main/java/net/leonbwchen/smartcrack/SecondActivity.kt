package net.leonbwchen.smartcrack

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.leonbwchen.smartcrack.adaper.ImageAdapter
import net.leonbwchen.smartcrack.dao.ImgDBHelper
import net.leonbwchen.smartcrack.dao.getAddress
import net.leonbwchen.smartcrack.dao.getId
import net.leonbwchen.smartcrack.dao.getTime
import net.leonbwchen.smartcrack.entity.PhotoInfo
import net.leonbwchen.smartcrack.model.OnnxService
import net.leonbwchen.smartcrack.util.base64ToBitmap
import net.leonbwchen.smartcrack.util.bitmapToBase64
import java.io.File

class SecondActivity : AppCompatActivity() {
    val takePhoto = 1
    lateinit var imageUri: Uri
    lateinit var outPutImage: File
    private val imageList = ArrayList<PhotoInfo>()
    private val dbVersion = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_layout)

        //TakePhoto
        val takePhotoBut: Button = findViewById(R.id.takePhoto)
        takePhotoBut.setOnClickListener {
            outPutImage = File(externalCacheDir, "output_image.webp")
//            Log.d("Path", outPutImage.absolutePath)
            if (outPutImage.exists()) {
                outPutImage.delete()
            }
            outPutImage.createNewFile()
            imageUri = FileProvider.getUriForFile(this, "net.leonbwchen.smartcrack", outPutImage)
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
        }

        //Display photo
        initPhoto()
        val layoutManager = LinearLayoutManager(this)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        val adapter = ImageAdapter(imageList)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.log_out -> logout()
        }
        return true
    }

    private fun logout() {
        try {
            val sharedPreferences = getSharedPreferences("userPreference", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    val bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(imageUri)
                    )
//                    val str:String = bitmapToBase64(bitmap)
                    insertImg(bitmap)
                    recreate()
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun initPhoto() {
        val imgDBHelper = ImgDBHelper(content = this, version = dbVersion)
        val db = imgDBHelper.writableDatabase
        val cursor = db.query("pic_info", null, null,
            null, null, null, null)
        val cursorAfter = db.query("pic_info_after", null,
            null, null, null, null, null)

        val tempList = ArrayList<PhotoInfo>()
        if (cursor.moveToFirst() && cursorAfter.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                val num = cursor.getInt(cursor.getColumnIndex("pic_num"))
                val src = cursor.getString(cursor.getColumnIndex("pic_src"))
                val picId = cursor.getString(cursor.getColumnIndex("pic_id"))
                val time = cursor.getString(cursor.getColumnIndex("pic_time"))
                val base64Ori = base64ToBitmap(src)
                val srcAfter = cursorAfter.getString(cursorAfter.getColumnIndex("pic_src"))
                val base64After = base64ToBitmap(srcAfter)
                tempList.add(PhotoInfo(num, picId, time, base64Ori, base64After))
            } while (cursor.moveToNext() && cursorAfter.moveToNext())
        }
        cursor.close()
        cursorAfter.close()
        imageList.clear()
        imageList.addAll(tempList.reversed())
    }

    private fun insertImg(bitmap: Bitmap) {
        //原始图片
        val imgDBHelper = ImgDBHelper(content = this, version = dbVersion)
        val db = imgDBHelper.writableDatabase
        val base64String: String = bitmapToBase64(bitmap)
        val time: String = getTime()
        val sharedPreference = getSharedPreferences("userPreference", MODE_PRIVATE)
        val operator = sharedPreference.getString("username", "")
        val address = getAddress()
        val picId = getId()
        val value = ContentValues().apply {
            put("pic_id", picId)
            put("pic_src", base64String)
            put("pic_time", time)
            put("pic_operator", operator)
            put("pic_address", address)
        }
        db.insert("pic_info", null, value)

        //识别后的
        val onnxService = OnnxService()
        val afterBase64 = onnxService.classifyBase64(base64String)
        val valueAfter = ContentValues().apply {
            put("pic_src", afterBase64)
        }
        db.insert("pic_info_after", null, valueAfter)
    }
}

