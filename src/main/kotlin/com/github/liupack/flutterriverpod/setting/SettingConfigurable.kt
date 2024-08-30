package com.github.liupack.flutterriverpod.setting

import com.github.liupack.flutterriverpod.config.RiverpodData
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class SettingConfigurable : Configurable {

    private val data by lazy { RiverpodData.getInstance() }

    private var mSetting: SettingsComponent? = null

    override fun disposeUIResources() {
        mSetting = null
    }

    override fun createComponent(): JComponent {
        if (mSetting == null) {
            mSetting = SettingsComponent()
        }
        return mSetting!!.mainPanel
    }

    override fun isModified(): Boolean {
        return !mSetting?.logicName?.getText().equals(data.logicName)
                || !mSetting?.stateName?.getText().equals(data.stateName)
                || !mSetting?.viewName?.getText().equals(data.viewName)
                || !mSetting?.viewFileName?.getText().equals(data.viewFileName)
    }

    override fun apply() {
        mSetting?.logicName?.text = data.logicName
        mSetting?.stateName?.text = data.stateName
        mSetting?.viewName?.text = data.viewName
        mSetting?.viewFileName?.text = data.viewFileName
    }

    override fun getDisplayName(): String {
        return "Flutter Riverpod Settings"
    }
}