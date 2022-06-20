package com.derry.kt_coroutines.study.p53.state

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import kotlin.coroutines.Continuation
@SuppressLint("StaticFieldLeak") val context: Context? = null

// 模仿编译成Java的流程原理
// TODO >>>>>>>>>>>>>>>>>>>>>>>>>> 5.3：协程背后状态机原理 ⑤ >>>>>>>>>>>>>>>>>>>>>>>>
fun showCoroutine(completion: Continuation<Any?>): Any? {  //  Any? == Object

    class TestContinuation(completion: Continuation<Any?>?) : ContinuationImpl(completion) {
        // 表示协程状态机当前的状态
        var label: Int = 0
        // 协程返回结果
        var result: Any? = null

        // 用于保存之前协程的计算结果
        var user: Any? = null
        var userAssets: Any? = null

        // invokeSuspend 是协程的关键
        // 它最终会调用 showCoroutine(this) 开启协程状态机
        // 状态机相关代码就是后面的 when 语句
        // 协程的本质，可以说就是 CPS + 状态机
        override fun invokeSuspend(_result: Result<Any?>): Any? {
            result = _result
            label = label or Int.Companion.MIN_VALUE
            return showCoroutine(this)
        }
    }

    val continuation = if (completion is TestContinuation) {
        completion
    } else {
        TestContinuation(completion) // 如果是初次运行，会把completion: Continuation<Any?>作为参数传递进去
    }

    // 三个变量，对应原函数的三个变量
    lateinit var user: String
    lateinit var userAssets: String
    lateinit var userAssetsDetails: String

    // result 接收协程的运行结果
    var result = continuation.result

    // suspendReturn 接收挂起函数的返回值
    var suspendReturn: Any? = null

    // CoroutineSingletons 是个枚举类
    // COROUTINE_SUSPENDED 代表当前函数被挂起了
    val sFlag = CoroutineSingletons.COROUTINE_SUSPENDED

    var loop = true

    while (loop) {
        // 协程状态机核心代码
        when (continuation.label) {
            0 -> {
                // 检测异常
                throwOnFailure(result)

                // "start"

                // 将 label 置为 1，准备进入下一次状态
                continuation.label = 1

                // 执行 requestLoadUser
                suspendReturn = requestLoadUser(continuation) // withContent(IO)执行耗时任务

                // 判断是否挂起
                if (suspendReturn == sFlag) {
                    return suspendReturn // suspend挂起了，执行耗时任务完成后，返回suspendReturn == 加载到[用户数据]信息集
                } else {
                    result = suspendReturn // 未挂起，result 接收协程的运行结果
                    //go to next state
                }
            }

            1 -> {
                throwOnFailure(result)

                // 获取 user 值
                user = result as String
                toast("更新UI:$user")
                // 将协程结果存到 continuation 里
                continuation.user = user
                // 准备进入下一个状态
                continuation.label = 2

                // 执行 requestLoadUserAssets
                suspendReturn = requestLoadUserAssets(continuation) // withContent(IO)执行耗时任务

                // 判断是否挂起
                if (suspendReturn == sFlag) {
                    return suspendReturn // suspend挂起了，执行耗时任务完成后，返回suspendReturn == 加载到[用户资产数据]信息集
                } else {
                    result = suspendReturn // 未挂起，result 接收协程的运行结果
                    //go to next state
                }
            }

            2 -> {
                throwOnFailure(result)

                user = continuation.user as String

                // 获取 friendList userAssets 的值
                userAssets = result as String
                toast("更新UI:$userAssets")

                // 将协程结果存到 continuation 里
                continuation.user = user
                continuation.userAssets = userAssets

                // 准备进入下一个状态
                continuation.label = 3

                // 执行 requestLoadUserAssetsDetails
                suspendReturn = requestLoadUserAssetsDetails(continuation) // withContent(IO)执行耗时任务

                // 判断是否挂起
                if (suspendReturn == sFlag) {
                    return suspendReturn // suspend挂起了，执行耗时任务完成后，返回suspendReturn == 加载到[用户资产详情数据]信息集
                } else {
                    result = suspendReturn // 未挂起，result 接收协程的运行结果
                    //go to next state
                }
            }

            3 -> {
                throwOnFailure(result)

                user = continuation.user as String
                userAssets = continuation.userAssets as String
                userAssetsDetails = continuation.result as String
                toast("更新UI:$userAssetsDetails")
                loop = false
            }
        }
    }

    return Unit
}

private fun throwOnFailure(value: Any?){
    if (value is Result<*>) {
        value.exceptionOrNull()
    }
}

fun requestLoadUser(completion: Continuation<Any?>): Any? {
    // no implement
    // 调用 invokeSuspend
    return CoroutineSingletons.COROUTINE_SUSPENDED
}
fun requestLoadUserAssets(completion: Continuation<Any?>): Any? {
    // no implement
    // 调用 invokeSuspend
    return CoroutineSingletons.COROUTINE_SUSPENDED
}
fun requestLoadUserAssetsDetails(completion: Continuation<Any?>): Any? {
    // no implement
    // 调用 invokeSuspend
    return CoroutineSingletons.COROUTINE_SUSPENDED
}

// Toast.makeText(this, "更新UI:$userAssets", Toast.LENGTH_SHORT).show()
fun toast(msg: Any) {
    Toast.makeText(context, "${Thread.currentThread().name} msg=$msg", Toast.LENGTH_SHORT).show()
}
