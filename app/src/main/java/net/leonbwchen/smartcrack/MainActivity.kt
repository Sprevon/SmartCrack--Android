package net.leonbwchen.smartcrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private var messageRes: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_layout)
        val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener {
            getConnect()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocus = currentFocus
            if (currentFocus != null) {
                // 关闭软键盘
                inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
        }
    }

    private fun getConnect() {
        thread {
            try {
                val account: EditText = findViewById(R.id.edit_account)
                val password: EditText = findViewById(R.id.edit_password)
                if (account.text.toString() == "" || password.text.toString() == ""){
                    noticeToast("请输入用户名和密码！")
                    return@thread
                }
                val client = OkHttpClient()
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val json = "{\"username\": \"" + account.text + "\"," +
                        " \"password\": \""+ password.text + "\"}" // 请求体中的 JSON 数据
                val requestBody: RequestBody = RequestBody.create(mediaType, json)
                val request = Request.Builder()
                    .url("http://10.0.2.2:8088/sc/login")
                    .method("POST", requestBody)
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null) {
                    parseJSONWithJSONObject(responseData)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                noticeToast("网络故障！")
            }
        }
    }

    private fun parseJSONWithJSONObject(jsonData: String) {
        try {
            val jsonObject = JSONObject(jsonData)
            val message = jsonObject.getString("message")
            val code = jsonObject.getString("code")
            val total = jsonObject.getString("total")
            Log.d("MainActivity", "message is $message")
            Log.d("MainActivity", "code is $code")
            Log.d("MainActivity", "total is $total")
            messageRes = message
            noticeToast(message)
            if (messageRes == "登录成功"){
                Log.d("login", "success!!!")
                turnNext()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun noticeToast(message: String){
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post{
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun turnNext(){
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
        finish()
    }
}
