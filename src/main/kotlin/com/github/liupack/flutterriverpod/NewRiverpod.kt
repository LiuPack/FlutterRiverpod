package com.github.liupack.flutterriverpod

import com.github.liupack.flutterriverpod.config.RiverpodData
import com.google.common.base.CaseFormat
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.ui.JBColor
import io.ktor.util.*
import java.awt.Container
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*
import javax.swing.*


class NewRiverpod : AnAction() {

    private var psiPath: String? = null

    private var project: Project? = null

    private lateinit var data: RiverpodData
    private lateinit var jDialog: JDialog
    private lateinit var folderBox: JCheckBox
    private lateinit var prefixBox: JCheckBox
    private lateinit var nameTextField: JTextField

    override fun actionPerformed(event: AnActionEvent) {
        project = event.project
        psiPath = event.getData(PlatformDataKeys.PSI_ELEMENT)?.toString()
        psiPath = psiPath?.indexOf(':')?.plus(1)?.let { psiPath?.substring(it) }
        initData()
        initView()
    }

    private fun initData() {
        data = RiverpodData.getInstance()
        jDialog = JDialog(JFrame(), "Riverpod Template Code Produce")
    }

    private fun initView() {
        val container: Container = jDialog.contentPane
        container.layout = BoxLayout(container, BoxLayout.Y_AXIS)
        setCodeFile(container)
        setModuleAndConfirm(container)
        setJDialog()
    }

    private fun setJDialog() {
        jDialog.isModal = true
        (jDialog.contentPane as JPanel).border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        jDialog.setSize(430, 450)
        jDialog.setLocationRelativeTo(null)
        jDialog.isVisible = true
    }

    private fun setModuleAndConfirm(container: Container) {
        JPanel().apply {
            layout = FlowLayout()
            border = BorderFactory.createTitledBorder("Module Name")
            nameTextField = JTextField().apply {
                preferredSize = Dimension(300, 50)
            }
            nameTextField.addKeyListener(keyListener)
            add(nameTextField)
            container.add(this)
        }
        JPanel().apply {
            layout = FlowLayout()
            setDivision(container)
            val cancelButton = JButton("Cancel")
            cancelButton.addActionListener(actionListener)
            val okButton = JButton("OK")
            okButton.foreground = JBColor.RED
            okButton.addActionListener(actionListener)
            add(cancelButton)
            add(okButton)
            container.add(this)
        }
    }

    private fun setCodeFile(container: Container) {
        JPanel().apply {
            layout = GridLayout(1, 2)
            border = BorderFactory.createTitledBorder("Select Function")
            folderBox = JCheckBox("useFolder", data.useFolder)
            setMargin(folderBox, 5, 10)
            add(folderBox)
            prefixBox = JCheckBox("usePrefix", data.usePrefix)
            setMargin(prefixBox, 5, 10)
            add(prefixBox)
            container.add(this)
            setDivision(container)
        }
    }

    private fun createFile() {
        data.useFolder = folderBox.isSelected
        data.usePrefix = prefixBox.isSelected
        val name = upperCase(nameTextField.text)
        val prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)
        var folder = ""
        var prefixName = ""
        if (data.useFolder) {
            folder = "/$prefix"
        }
        if (data.usePrefix) {
            prefixName = prefix + "_"
        }
        generateHigh(folder, prefixName)
    }

    private fun generateHigh(folder: String, prefixName: String) {
        val path = psiPath + folder
        generateFile("view.dart", path, prefixName + data.viewFileName.lowercase(Locale.getDefault()) + ".dart")
        generateFile("notifier.dart", path, prefixName + data.logicName.lowercase(Locale.getDefault()) + ".dart")
        generateFile("state.dart", path, prefixName + data.stateName.lowercase(Locale.getDefault()) + ".dart")
    }

    private fun generateFile(inputFileName: String, filePath: String, outFileName: String) {
        val content = dealContent(inputFileName, outFileName)
        kotlin.runCatching {
            val folder = File(filePath)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val file = File("$filePath/$outFileName")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fw = FileWriter(file.absoluteFile)
            BufferedWriter(fw).use {
                it.write(content)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun dealContent(inputFileName: String, outFileName: String): String {
        val baseFolder = "/templates/"
        var content = ""
        kotlin.runCatching {
            val inputStream = javaClass.getResourceAsStream(baseFolder + inputFileName)
            content = inputStream?.bufferedReader()?.readText().orEmpty()
        }
        var prefixName = ""
        if (data.usePrefix) {
            prefixName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, upperCase(nameTextField.text) + "_")
        }
        if (outFileName.contains(data.logicName.lowercase())) {
            content = content.replace("state.dart", prefixName + data.stateName.lowercase() + ".dart")
            content = content.replace("final \$name", "final ${toPascalCase(nameTextField.text, false)}")
            content = content.replace("Notifier", toPascalCase(data.logicName))
            content = content.replace("State", toPascalCase(data.stateName))
        }
        if (outFileName.contains(data.stateName.lowercase())) {
            content = content.replace("state.freezed.dart", prefixName + data.stateName.lowercase() + ".freezed.dart")
            content = content.replace("State", toPascalCase(data.stateName))
        }
        if (outFileName.contains(data.viewFileName.lowercase())) {
            content = content.replace("Page", toPascalCase(data.viewName))
        }

        content = content.replace("\$name", toPascalCase(nameTextField.getText()))
        return content
    }

    private val keyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent?) {

        }

        override fun keyPressed(e: KeyEvent?) {
            if (e?.keyCode == KeyEvent.VK_ENTER) save()
            if (e?.keyCode == KeyEvent.VK_ESCAPE) dispose()

        }

        override fun keyReleased(e: KeyEvent?) {

        }

    }

    private val actionListener = ActionListener {
        if (it.actionCommand.equals("Cancel")) {
            dispose()
        } else {
            save()
        }
    }

    private fun save() {
        if (nameTextField.text.isNullOrEmpty() || nameTextField.text.trim().isEmpty()) {
            Messages.showInfoMessage(project, "Please input the module name", "Info")
            return
        }
        dispose()
        createFile()
        project?.workspaceFile?.refresh(false, true)
    }

    private fun dispose() {
        jDialog.dispose()
    }

    @Suppress("SameParameterValue")
    private fun setMargin(jCheckBox: JCheckBox, top: Int, bottom: Int) {
        jCheckBox.border = BorderFactory.createEmptyBorder(top, 10, bottom, 0)
    }

    private fun setDivision(container: Container) {
        container.add(JPanel())
    }

    private fun upperCase(content: String): String {
        return content.substring(0, 1).uppercase() + content.substring(1)
    }

    private fun toPascalCase(input: String, firstCharUpper: Boolean = true): String {
        val result = input.split('_') // 将字符串按下划线分割成数组
            .joinToString("") { part -> // 将数组拼接成一个字符串
                part.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
        return if (firstCharUpper) result.replaceFirstChar { it.uppercaseChar() } else result.replaceFirstChar { it.lowercaseChar() }
    }

}