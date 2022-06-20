package com.derry.kt_coroutines.use2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derry.kt_coroutines.use2.repository.APIRepository
import com.xiangxue.kotlinproject.entity.LoginRegisterResponse2
import com.xiangxue.kotlinproject.entity.LoginRegisterResponseWrapper2
import kotlinx.coroutines.launch

// 职责：只做这一件事情：管理所有的LiveData数据状态的稳定性
class APIViewModel : ViewModel() {

    var userLiveData = MutableLiveData<LoginRegisterResponseWrapper2<LoginRegisterResponse2>>()

    var stateManager = MutableLiveData<String> () // 绑定布局的显示

    // 我只要修改 userLiveData 就等价于 修改 UI控件

    fun requestLogin(username: String, userpwd: String) {
        // GlobalScope 全局作用域 默认是异步线程
        // viewModelScope.launch 默认是主线程
        viewModelScope.launch /*(Dispatchers.Main)*/ {
            // 左边的是：主线程                右边：异步线程
            userLiveData.value      =      APIRepository().reqeustLogin(username, userpwd, stateManager)
        }
    }
}