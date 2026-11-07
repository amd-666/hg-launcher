package com.fan.hglauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fan.hglauncher.ui.theme.Amber
import com.fan.hglauncher.ui.theme.LineSoft
import com.fan.hglauncher.ui.theme.Mono
import com.fan.hglauncher.ui.theme.Muted
import com.fan.hglauncher.ui.theme.MutedDim
import com.fan.hglauncher.ui.theme.TextMain

@Composable
fun LoginScreen(
    loading: Boolean,
    onLogin: (String, String) -> Unit,
    onGuest: () -> Unit,
    onHelp: () -> Unit
) {
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(true) }

    val canSubmit = agreed && account.isNotBlank() && password.isNotBlank() && !loading

    AmbientBackground {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(Modifier.height(46.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HexMark(diameter = 26.dp)
                Spacer(Modifier.width(10.dp))
                Text(
                    "HYPERGRYPH ACCOUNT",
                    fontSize = 11.5.sp,
                    letterSpacing = 3.sp,
                    color = Color(0xFFC7CCD3),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "欢迎回来，博士",
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp,
                color = TextMain
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "登录后可同步游戏库、云端进度与更新通道。\n本启动器仅做本地管理，不传输任何账号数据。",
                fontSize = 12.5.sp,
                lineHeight = 20.sp,
                color = Muted
            )

            Spacer(Modifier.height(28.dp))

            TechField(
                label = "账号 / ACCOUNT",
                prefix = "ID",
                value = account,
                placeholder = "手机号或邮箱",
                isPassword = false,
                onChange = { account = it }
            )
            TechField(
                label = "密码 / PASSWORD",
                prefix = "••",
                value = password,
                placeholder = "请输入密码",
                isPassword = true,
                onChange = { password = it }
            )

            Spacer(Modifier.height(20.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { agreed = !agreed },
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    Modifier
                        .padding(top = 1.dp)
                        .width(15.dp)
                        .height(15.dp)
                        .background(if (agreed) Amber else Color(0xFF0E1014))
                        .border(1.dp, if (agreed) Amber else Color(0xFF3A414B)),
                    contentAlignment = Alignment.Center
                ) {
                    if (agreed) {
                        Box(
                            Modifier
                                .width(7.dp)
                                .height(7.dp)
                                .background(Color(0xFF16181C))
                        )
                    }
                }
                Spacer(Modifier.width(9.dp))
                Text(
                    "我已阅读并同意 《用户协议》 与 《隐私政策》",
                    fontSize = 11.5.sp,
                    lineHeight = 18.sp,
                    color = Muted
                )
            }

            Spacer(Modifier.height(24.dp))

            PrimaryButton(
                text = if (loading) "登 录 中 …" else "登 录",
                enabled = canSubmit,
                onClick = { onLogin(account.trim(), password) }
            )

            Spacer(Modifier.height(18.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "游客模式进入 →",
                    fontSize = 11.5.sp,
                    color = Color(0xFF6A717B),
                    modifier = Modifier.clickable { onGuest() }
                )
                Text(
                    "遇到问题？",
                    fontSize = 11.5.sp,
                    color = Color(0xFF6A717B),
                    modifier = Modifier.clickable { onHelp() }
                )
            }

            Spacer(Modifier.height(40.dp))

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "账号体系为本地模拟 · 不会连接鹰角官方服务器",
                    fontSize = 10.sp,
                    color = Color(0xFF454A52)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "SECURE LOCAL MODE",
                    fontSize = 10.sp,
                    color = Color(0xFF454A52),
                    fontFamily = Mono
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun TechField(
    label: String,
    prefix: String,
    value: String,
    placeholder: String,
    isPassword: Boolean,
    onChange: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Text(
            label,
            fontSize = 10.5.sp,
            color = Color(0xFF737A84),
            letterSpacing = 2.sp,
            fontFamily = Mono,
            modifier = Modifier.padding(bottom = 9.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFF101217))
                .border(1.dp, LineSoft)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(prefix, fontSize = 11.sp, color = Amber, fontFamily = Mono)
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = value,
                onValueChange = onChange,
                singleLine = true,
                textStyle = TextStyle(color = TextMain, fontSize = 14.5.sp),
                cursorBrush = SolidColor(Amber),
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
                ),
                visualTransformation =
                    if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(placeholder, color = Color(0xFF4E545D), fontSize = 14.5.sp)
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}
