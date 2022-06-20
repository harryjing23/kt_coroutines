package com.derry.kt_coroutines.study.p53

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.derry.kt_coroutines.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// TODO  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 下面是 p53通用代码：
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

// 测试用的而已
/*private suspend fun requestLoadUser(continuation: Continuation<String>) : String {
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
}*/

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

class MainActivity8 : AppCompatActivity() {
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

        GlobalScope.launch(Dispatchers.Main) { // 默认是Default异步

            // TODO >>>>>>>>>>>>>>>>>>>>>>>>  5.3：协程背后状态机原理 ① >>>>>>>>>>>>>>>>>>>>>>>>
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


            // TODO >>>>>>>>>>>>>>>>>>>>>>>>  5.3：协程背后状态机原理 ② >>>>>>>>>>>>>>>>>>>>>>>>
            // 模拟 requestLoadUser 函数的情况
            /*requestLoadUser(object: Continuation<String> {
                override val context: CoroutineContext
                    get() = EmptyCoroutineContext

                override fun resumeWith(result: Result<String>) {
                    // resumeWith 回调函数 负责执行后续的代码

                    var serverResponseInfo = result.getOrNull()
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
            })*/
        }
    }

    // TODO >>>>>>>>>>>>>>>>>>>>>>>>  5.3：协程背后状态机原理 ③ >>>>>>>>>>>>>>>>>>>>>>>>
    suspend fun showCoroutine() {
        val user = requestLoadUser()
        Toast.makeText(this, "更新UI:$user", Toast.LENGTH_SHORT).show()

        val userAssets = requestLoadUserAssets()
        Toast.makeText(this, "更新UI:$userAssets", Toast.LENGTH_SHORT).show()

        val userAssetsDetails = requestLoadUserAssetsDetails()
        Toast.makeText(this, "更新UI:$userAssetsDetails", Toast.LENGTH_SHORT).show()
    }
}