package com.github.liupack.flutterriverpod

import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.ktor.util.*
import kotlin.text.isLowerCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/testData/rename"

    fun testRename() {
        val name = "user_center"
        toPascalCase(name,false).apply {
            println(this)
        }
    }

    private fun toPascalCase(input: String, firstCharUpper: Boolean = true): String {
        val result = input.split('_') // 将字符串按下划线分割成数组
            .joinToString("") { part -> // 将数组拼接成一个字符串
                part.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
        return if (firstCharUpper) result.replaceFirstChar { it.uppercaseChar() } else result.replaceFirstChar { it.lowercaseChar() }
    }
}
