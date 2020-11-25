package com.example.kotlinhw_rabtur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    //建立兩個計數器，計算烏龜及兔子的速度
    private var rabprogress = 0
    private var torprogress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //開始監聽按鈕
        btn_start.setOnClickListener {
            btn_start.isEnabled = false
            rabprogress = 0//初始化兔子計數器
            torprogress = 0//初始化烏龜計數器
            seekBar.progress = 0
            seekBar2.progress = 0
            runTread()//執行副程式Tread
            runCoroutine()//執行副程式Coroutine
        }
    }

    private fun runTread() {
        object : Thread() {
            override fun run() {
                while (rabprogress <= 100 && torprogress <100) {
                    try {
                        Thread.sleep(100)//延遲0.1秒
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    rabprogress += (Math.random() * 3).toInt()//隨機增加計數器0~2值
                    val msg = Message()//建立Message物件
                    msg.what = 1 //加入代號
                    mHandler.sendMessage(msg) //透過sendMessage傳送訊息
                }
            }
        }.start() //啟動Tread
    }
    //建立Handler等待接收
    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) { //判斷代號，寫入計數器的值到seekBar
            1 -> seekBar.progress = rabprogress
        }
        //重複執行到不小於100
        if (rabprogress >= 100 && torprogress < 100) {
            Toast.makeText(this,"兔子勝利", Toast.LENGTH_SHORT).show()
            btn_start!!.isEnabled = true
        }
        false
    })

    private fun runCoroutine() {
        GlobalScope.launch {
            //重複執行到不小於100
            while (torprogress <= 100 && rabprogress < 100) {
                try {
                    delay(100L)//延遲0.1秒
                    val msg = Message()//建立Message物件
                    msg.what = 1//加入代號
                    torprogress += (Math.random() * 3).toInt()//隨機增加計數器0~2值
                    mHandler2.sendMessage(msg)//透過sendMessage傳送訊息
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
    //建立Handler等待接收
    private val mHandler2 = Handler(Handler.Callback { msg ->
        when (msg.what) {
            1 -> seekBar2!!.progress = torprogress
        }
        if (torprogress >= 100 && rabprogress < 100) {
            Toast.makeText(this,"烏龜勝利", Toast.LENGTH_SHORT).show()
            btn_start!!.isEnabled = true
        }
        false
    })
}
