package com.github.liupack.flutterriverpod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.Dimension
import javax.swing.JComponent

class ComposeAction : DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val rootPath = event.getData(PlatformDataKeys.PSI_ELEMENT)?.toString()
        ComposeDialog(event.project, rootPath).show()
    }

    class ComposeDialog(project: Project?, private val rootPath: String?) : DialogWrapper(project, true) {
        init {
            init()
            title = "Flutter Riverpod Template"
        }

        override fun createCenterPanel(): JComponent {

            @Composable
            fun checkBoxWithText(checked: Boolean, text: String, onCheckedChange: (Boolean) -> Unit) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                    Text(text = text)
                }
            }

            val root = rootPath?.indexOf(':')?.plus(1)?.let { rootPath.substring(it) }
            return ComposePanel().apply {
                preferredSize = Dimension(430, 450)
                minimumSize = Dimension(430, 450)
                maximumSize = Dimension(430, 450)
                setContent {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = root.toString())
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            checkBoxWithText(checked = false, onCheckedChange = {}, text = "Use Folder")
                            Box(modifier = Modifier.padding(12.dp))
                            checkBoxWithText(checked = false, onCheckedChange = {}, text = "Use Prefix")
                        }
                    }
                }
            }

        }
    }
}