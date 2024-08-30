package com.github.liupack.flutterriverpod.setting

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class SettingsComponent {
    var mainPanel: JPanel
    var logicName: JBTextField = JBTextField()
    var stateName: JBTextField = JBTextField()
    var viewName: JBTextField = JBTextField()
    var viewFileName: JBTextField = JBTextField()

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("NotifierName"), logicName)
            .addLabeledComponent(JBLabel("StateName: "), stateName)
            .addLabeledComponent(JBLabel("ViewName: "), viewName)
            .addLabeledComponent(JBLabel("ViewFileName: "), viewFileName)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}