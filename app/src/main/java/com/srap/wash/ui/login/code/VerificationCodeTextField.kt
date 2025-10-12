package com.srap.wash.ui.login.code

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private enum class CodeState {
    ENTERED,//表示验证码已经完全输入
    INPUTTING,//表示验证码正在输入中,还未完成。
    PENDING,//表示验证码尚未开始输入。
}

@Composable
fun BaseVerificationCodeTextField(
    codeLength: Int,
    onVerify: (String) -> Unit = {},
    codeBox: @Composable RowScope.(index: Int, code: String) -> Unit
) {

    //存储文本输入的值
    var text by remember { mutableStateOf("") }
    //管理当前获得焦点的文本框
    val focusManager = LocalFocusManager.current
    //用于请求焦点以显示软键盘
    val focusRequester = remember { FocusRequester() }
    //它控制软键盘的显示和隐藏。
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    BasicTextField(
        value = text,
        singleLine = true,
        onValueChange = { newText ->
            // 限制最大长度为6且只能输入数字
            if (newText.length <= codeLength && newText.all { it.isDigit() }) {
                text = newText

                if (newText.length == codeLength) {
                    onVerify(newText)
                    focusManager.clearFocus()
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .padding(horizontal = 26.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    keyboardController?.show()
                }
            }
            .wrapContentHeight(),
        readOnly = false,
        decorationBox = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (i in 0 until codeLength) {
                    codeBox(i, text)
                }
            }
        }
    )
}

@Composable
fun VerificationCodeTextField(
    codeLength: Int = 6,
    onVerify: (String) -> Unit = {}
) {

    val baseSize = 276
    BaseVerificationCodeTextField(
        codeLength = codeLength,
        onVerify = onVerify
    ) { index, code ->
        // 判断当前位置是否有字符
        val isHasCode = index < code.length
        val fontSize = (144 / codeLength).sp

        val codeState = when {
            isHasCode -> CodeState.ENTERED
            (index == code.length) -> CodeState.INPUTTING
            else -> CodeState.PENDING
        }
        val cardColor = when (codeState) {
            CodeState.ENTERED -> Color(0xFF466Eff)
            CodeState.INPUTTING -> Color.White
            CodeState.PENDING -> Color(0xFFF5F5F5)
        }
        val elevation = when (codeState) {
            CodeState.ENTERED -> 3.dp
            CodeState.INPUTTING -> 6.dp
            CodeState.PENDING -> 0.dp
        }
        val textColor = when (codeState) {
            CodeState.ENTERED -> Color.White
            CodeState.INPUTTING -> Color.Gray
            CodeState.PENDING -> Color.Gray
        }

        val blinkInterval = 1000L
        var isVisible by remember { mutableStateOf(true) }
        LaunchedEffect(blinkInterval) {
            while (true) {
                isVisible = !isVisible
                delay(blinkInterval)
            }
        }
        key(elevation) {
            Card(
                Modifier
                    .size((baseSize / codeLength).dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = elevation,
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (codeState) {
                        CodeState.ENTERED -> {
                            Text(
                                code[index].toString(), style = TextStyle(
                                    fontSize = fontSize,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        CodeState.INPUTTING -> {
                            if (isVisible) {
                                Text(
                                    "_", style = TextStyle(
                                        fontSize = fontSize,
                                        color = textColor,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }

                        CodeState.PENDING -> {
                            // Text(
                            //            "_", style = TextStyle(
                            //                fontSize = fontSize,
                            //                color = textColor,
                            //                 textAlign = TextAlign.Center
                            //            )
                            //        )
                        }
                    }
                }
            }
        }
    }
}

//版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//原文链接：https://blog.csdn.net/jiulang9/article/details/131437240