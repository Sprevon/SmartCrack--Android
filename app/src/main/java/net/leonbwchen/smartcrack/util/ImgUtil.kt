package net.leonbwchen.smartcrack.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun bitmapToByteArray(bitmap: Bitmap): ByteArray{
    val byteArrayOutputStream  = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.WEBP, 10, byteArrayOutputStream )
    return byteArrayOutputStream .toByteArray()
}

fun bitmapToBase64(bitmap: Bitmap): String{
    val byteArray = bitmapToByteArray(bitmap)
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun base64ToBitmap(code: String): Bitmap{
    val decStr: ByteArray = Base64.decode(code, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decStr, 0 ,decStr.size)
}