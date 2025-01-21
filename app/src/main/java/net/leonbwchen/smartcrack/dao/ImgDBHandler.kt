package net.leonbwchen.smartcrack.dao

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random


@SuppressLint("SimpleDateFormat")
fun getTime(): String {
    val dayTime = Date()
    val timeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    return timeFormat.format(dayTime)
}

fun getAddress(): String {
    return "1"
}

@SuppressLint("SimpleDateFormat", "DefaultLocale")
fun getId(): String{
    val dayTime = Date()
    val timeFormat = SimpleDateFormat("yyyyMMddhhmmss") //14位
    val prefix = timeFormat.format(dayTime)
    val postfix = String.format("%06d", Random.nextInt(100000, 1000000))
    val code = postfix + prefix
    return code
}