package com.derry.kt_coroutines.study.p4

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.derry.kt_coroutines.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

// TODO >>>>>>>>>>>>>>>>>>>>>>>>>> 4.1：使用协程的要点 >>>>>>>>>>>>>>>>>>>>>> start
/*【挂起函数 大概做了什么事情，画图描述清楚】 ↓ 切换 ↑
 * 请求加载[用户数据]
 */
private suspend fun requestLoadUser() : String {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 开启异步线程，去请求加载服务器的数据集
    withContext(Dispatchers.IO) {
        delay(3000L) // 模拟请求服务器 所造成的耗时
    }

    if (isLoadSuccess) {
        return "加载到[用户数据]信息集"
    } else {
        return "加载[用户数据],加载失败,服务器宕机了"
    }
}

/*【挂起函数 大概做了什么事情，画图描述清楚】 ↓ 切换 ↑
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

/*【挂起函数 大概做了什么事情，画图描述清楚】 ↓ 切换 ↑
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
// TODO >>>>>>>>>>>>>>>>>>>>>>>>>> 4.1：使用协程的要点 >>>>>>>>>>>>>>>>>>>>>> end

// TODO 4.协程挂起与恢复流程
//  4.1：使用协程的要点
//  4.2：挂起到IO线程执行，恢复到Main线程显示的流程分析
class MainActivity6 : AppCompatActivity() {
    private val TAG = "Derry"
    var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startRequest(view: View) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        // TODO >>>>>>>>>>>>>>>>>> 4.2：挂起到IO线程执行，恢复到Main线程显示 的流程分析 >>>>>>>>>>>>>>>>
        GlobalScope.launch(Dispatchers.Main) { // 默认是Default异步





            var serverResponseInfo0 = requestLoadUser()





            // TODO 先执行 异步请求1
            var serverResponseInfo = requestLoadUser()
            textView?.text = serverResponseInfo // 更新UI
            textView?.setTextColor(Color.GREEN) // 更新UI

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