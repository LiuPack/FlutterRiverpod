package com.github.liupack.flutterriverpod.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@com.intellij.openapi.components.State(
    name = "RiverpodDataService",
    storages = [Storage(value = "RiverpodDataService.xml")]
)
class RiverpodData : PersistentStateComponent<RiverpodData> {

    companion object {
        fun getInstance(): RiverpodData = ApplicationManager.getApplication().getService(RiverpodData::class.java)
    }

    var useFolder = RiverpodConfig.USE_FOLDER
    var usePrefix = RiverpodConfig.USE_PREFIX
    val logicName = RiverpodConfig.LOGIC_NAME
    val viewName = RiverpodConfig.VIEW_NAME
    val viewFileName = RiverpodConfig.VIEW_FILE_NAME
    val stateName = RiverpodConfig.STATE_NAME

    override fun getState(): RiverpodData {
        return this
    }

    override fun loadState(state: RiverpodData) {
        XmlSerializerUtil.copyBean(state, this)
    }
}