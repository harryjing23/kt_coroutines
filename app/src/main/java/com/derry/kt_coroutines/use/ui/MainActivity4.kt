package com.derry.kt_coroutines.use.ui

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.derry.kt_coroutines.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

/**
 * 请求加载[用户数据]
 * suspend 就是一个提醒作用，提醒用户 当前的函数，是挂起函数，可能会执行异步操作，你注意下
 */
private suspend fun requestLoadUser() : String {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 此协程能够保证在异步执行而已
    // withContext方法声明有suspend，调用它的函数也要声明suspend
    withContext(Dispatchers.IO) {
        delay(3000L)  // 模拟请求服务器 所造成的耗时
    }

    if (isLoadSuccess) {
        return "加载到[用户数据]信息集"
    } else {
        return "加载[用户数据],加载失败,服务器宕机了"
    }
}

/**
 * 请求加载[用户资产数据]
 */
private suspend fun requestLoadUserAssets(): String {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 开启异步线程，去请求加载服务器的数据集
    withContext(Dispatchers.IO) {
        delay(3000L) // 模拟请求服务器 所造成的耗时
    }
    if (isLoadSuccess) {
        return "加载到[用户资产数据]信息集"
    } else {
        return "加载[用户资产数据],加载失败,服务器宕机了"
    }
}

/**
 * 请求加载[用户资产详情数据]
 */
private suspend fun requestLoadUserAssetsDetails() : String {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 开启异步线程，去请求加载服务器的数据集
    withContext(Dispatchers.IO) {
        delay(3000L) // 模拟请求服务器 所造成的耗时
    }

    if (isLoadSuccess) {
        return "加载到[用户资产详情数据]信息集"
    } else {
        return "加载[用户资产详情数据],加载失败,服务器宕机了"
    }
}

// 2.4：协程方式解决三层回调带来的痛点
class MainActivity4 : AppCompatActivity() {

    private val TAG = "Derry"
    var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 点击事件
    fun startRequest(view: View) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        // Dispatchers.Main 包裹一层 Handler切换主线程
        // Dispatchers.IO 包裹一层
        GlobalScope.launch(Dispatchers.Main) {
            // TODO 先执行 异步请求1
            var serverResponseInfo = requestLoadUser()
            textView.text = serverResponseInfo // 更新UI
            textView.setTextColor(Color.GREEN) // 更新UI

            // TODO 更新UI完成后，再去执行 异步请求2
            serverResponseInfo = requestLoadUserAssets()
            textView?.text = serverResponseInfo // 更新UI
            textView?.setTextColor(Color.BLUE) // 更新UI

            // TODO 更新UI完成后，再去执行 异步请求3
            serverResponseInfo = requestLoadUserAssetsDetails()
            textView?.text = serverResponseInfo // 更新UI
            mProgressDialog?.dismiss() // 更新UI
            textView?.setTextColor(Color.RED) // 更新UI
        }
    }
}