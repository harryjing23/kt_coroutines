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

// import kotlinx.android.synthetic.main.activity_main.* // 默认其实会自动导入，不需要写 findv

/**
 * 模拟请求服务器后 响应结果信息  相当于续体 (我这个就是续体)
 */
interface ResponseCallback {

    /**
     * 请求服务器后 登录成功  加载成功
     * @param serverResponseInfo 登录成功后的信息集
     */
    fun responseSuccess(serverResponseInfo : String)

    /**
     * 请求服务器后 登录失败/登录错误  加载失败
     * @param serverResponseErrorMsg 登录失败后的简述
     */
    fun responseError(serverResponseErrorMsg: String)
}

/**
 * 请求加载[用户数据]
 * @param responseCallback 请求加载[用户数据]后 回调给外界的接口
 */
private fun requestLoadUser(responseCallback: ResponseCallback) {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 开启异步线程，去请求加载服务器的数据集
    object:Thread() {
        override fun run() {
            super.run()
            try {
                sleep(3000L) // 模拟请求服务器 所造成的耗时

                if (isLoadSuccess) {
                    responseCallback.responseSuccess("加载到[用户数据]信息集")
                } else {
                    responseCallback.responseError("加载[用户数据],加载失败,服务器宕机了")
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }.start()
}

/**
 * 请求加载[用户资产数据]
 * @param responseCallback 请求加载[用户资产数据]后 回调给外界的接口
 */
private fun requestLoadUserAssets(responseCallback: ResponseCallback) {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 开启异步线程，去请求加载服务器的数据集
    object:Thread() {
        override fun run() {
            super.run()
            try {
                sleep(3000L) // 模拟请求服务器 所造成的耗时

                if (isLoadSuccess) {
                    responseCallback.responseSuccess("加载到[用户资产数据]信息集")
                } else {
                    responseCallback.responseError("加载[用户资产数据],加载失败,服务器宕机了")
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }.start()
}

/**
 * 请求加载[用户资产详情数据]
 * @param responseCallback 请求加载[用户资产详情数据]后 回调给外界的接口
 */
private fun requestLoadUserAssetsDetails(responseCallback: ResponseCallback) {
    val isLoadSuccess = true // 加载成功，和，加载失败，的标记

    // 开启异步线程，去请求加载服务器的数据集
    object:Thread() {
        override fun run() {
            super.run()
            try {
                sleep(3000L) // 模拟请求服务器 所造成的耗时

                if (isLoadSuccess) {
                    responseCallback.responseSuccess("加载到[用户资产详情数据]信息集")
                } else {
                    responseCallback.responseError("加载[用户资产详情数据],加载失败,服务器宕机了")
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }.start()
}

// 2.3：传统方式完成三层回调带来的痛点
class MainActivity3 : AppCompatActivity() {

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

        // TODO 先执行 异步请求 1
        requestLoadUser(object: ResponseCallback{

            override fun responseSuccess(serverResponseInfo: String) {

                // 从异步 切换成 Android主线程   Handler
                val handler = object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        super.handleMessage(msg)
                        textView.text = serverResponseInfo // 更新UI
                        textView.setTextColor(Color.GREEN) // 更新UI

                        // TODO 先执行 异步请求 2
                        requestLoadUserAssets(object: ResponseCallback{
                            override fun responseSuccess(serverResponseInfo: String) {

                                // 从异步 切换成 Android主线程   Handler
                                val handler = object : Handler(Looper.getMainLooper()) {
                                    override fun handleMessage(msg: Message) {
                                        super.handleMessage(msg)
                                        textView.text = serverResponseInfo // 更新UI
                                        textView.setTextColor(Color.BLUE) // 更新UI

                                        // TODO 先执行 异步请求 3
                                        requestLoadUserAssetsDetails(object: ResponseCallback{
                                            override fun responseSuccess(serverResponseInfo: String) {

                                                // 从异步 切换成 Android主线程   Handler
                                                val handler = object : Handler(Looper.getMainLooper()) {
                                                    override fun handleMessage(msg: Message) {
                                                        super.handleMessage(msg)
                                                        textView.text = serverResponseInfo // 更新UI
                                                        textView.setTextColor(Color.RED) // 更新UI
                                                        mProgressDialog?.dismiss()  // 更新UI

                                                        // TODO 省略更多代码 ...
                                                    }
                                                }
                                                handler.sendEmptyMessage(0)
                                            }

                                            override fun responseError(serverResponseErrorMsg: String) {
                                                // TODO 失败的情况，就不考虑了，不然代码就太多了
                                                //  省略很多行代码...
                                            }
                                        })
                                    }
                                }
                                handler.sendEmptyMessage(0)
                            }

                            override fun responseError(serverResponseErrorMsg: String) {
                                // TODO 失败的情况，就不考虑了，不然代码就太多了
                                //  省略很多行代码...
                            }
                        })
                    }
                }
                handler.sendEmptyMessage(0)
            }

            override fun responseError(serverResponseErrorMsg: String) {
                // TODO 失败的情况，就不考虑了，不然代码就太多了
                //  省略很多行代码...
            }

        })
    }
}