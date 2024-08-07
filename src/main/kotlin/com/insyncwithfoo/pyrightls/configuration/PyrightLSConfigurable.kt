package com.insyncwithfoo.pyrightls.configuration

import com.insyncwithfoo.pyrightls.isNormal
import com.insyncwithfoo.pyrightls.pyrightLSConfigurations
import com.insyncwithfoo.pyrightls.server.PyrightLSSupportProvider
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.platform.lsp.api.LspServerManager
import com.intellij.util.xmlb.XmlSerializerUtil


@Suppress("UnstableApiUsage")
private val Project.lspServerManager: LspServerManager
    get() = LspServerManager.getInstance(this)


internal fun <T : BaseState> T.copy(): T {
    return XmlSerializerUtil.createCopy(this)
}


internal abstract class PyrightLSConfigurable<State : BaseState> : Configurable {
    
    protected abstract val service: SimplePersistentStateComponent<State>
    protected abstract val state: State
    
    protected abstract val panel: DialogPanel
    
    override fun createComponent() = panel
    
    override fun isModified() = panel.isModified()
    
    override fun apply() {
        panel.apply()
        XmlSerializerUtil.copyBean(state, service.state)
    }
    
    override fun reset() {
        panel.reset()
    }
    
    @Suppress("UnstableApiUsage")
    protected fun Project.restartPyrightServersIfSoChoose() {
        if (this.isNormal && pyrightLSConfigurations.autoRestartServer) {
            lspServerManager.stopAndRestartIfNeeded(PyrightLSSupportProvider::class.java)
        }
    }
    
}
