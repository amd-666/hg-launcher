package com.fan.hglauncher.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * 账号仓库（本地模拟）。
 *
 * 说明：真正的鹰角账号体系需要走官方 SDK / 服务端授权，第三方启动器
 * 不应、也无法直接对接。这里提供与真实接口同形的签名，接真实后端时
 * 只需替换 login() 的实现即可。
 */
class AuthRepository {

    suspend fun login(account: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            delay(900L)
            when {
                account.isBlank() || password.isBlank() ->
                    Result.failure(IllegalArgumentException("请输入账号与密码"))
                password.length < 6 ->
                    Result.failure(IllegalArgumentException("密码至少 6 位"))
                else -> Result.success(
                    User(
                        id = account.trim(),
                        nick = "博士"
                    )
                )
            }
        }

    fun guest(): User = User(id = "guest", nick = "游客")
}
