package com.derry.kt_coroutines.study.p5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.derry.kt_coroutines.R
import kotlinx.coroutines.*

// TODO >>>>>>>>>>>>>>>>>>>>>>>> 下面：5.1：函数类型的变换 ①/② >>>>>>>>>>>>>>>>>>>>>>>>

/*
 *【挂起函数 大概做了什么事情，画图描述清楚】 ↓ 切换 ↑
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

// 上面函数 suspend关键字的原理，其实就是 ResponseCallBack
// 同学们肯定会思考，上面函数 根本就没有 ResponseCallBack字眼呀？
// 把上面函数，反编译成Java的代码，是这个样子，请看下面代码：
/*                                           ResponseCallBack 其实就是 Continuation
                                           只不过这个Continuation名字更准确而已，他就是 ResponseCallBack
public static final Object requestLoadUser(Continuation $ completion) {

    ... 省略大量代码

    if (isLoadSuccess) {
        return "加载到[用户数据]信息集"
    } else {
        return "加载[用户数据],加载失败,服务器宕机了"
    }
}*/

// TODO >>>>>>>>>>>>>>>>>>>>>>>>    5.2：协程背后状态机原理 ③ >>>>>>>>>>>>>>>>>>>>>>>> start
/*public interface Continuation<in T> {
    public val context: CoroutineContext
      相当于 responseSuccess     结果
                 ↓               ↓
    public fun resumeWith(result: Result<T>)
}*/

/**
 * 模拟请求服务器后 响应结果信息
 */
interface ResponseCallback {

    /**
     * 请求服务器后 登录成功
     * @param serverResponseInfo 登录成功后的信息集
     */
    fun responseSuccess(serverResponseInfo : String)

    /**
     * 请求服务器后 登录失败/登录错误
     * @param serverResponseErrorMsg 登录失败后的简述
     */
    fun responseError(serverResponseErrorMsg: String)
}
// TODO >>>>>>>>>>>>>>>>>>>>>>>>    5.2：协程背后状态机原理 ③ >>>>>>>>>>>>>>>>>>>>>>>> end


// TODO 5.协程背后状态机原理
class MainActivity7 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// suspend 修饰的函数
// ↓ 切换 ↑ ，suspend起到提醒的作用，经此而已
suspend fun noSuspendFriendList(user: String): String{
    // 函数体跟普通函数一样
    return "Derry"
}