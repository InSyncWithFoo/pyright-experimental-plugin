package com.insyncwithfoo.pyrightls.configuration

import com.insyncwithfoo.pyrightls.configuration.application.LogLevel
import com.insyncwithfoo.pyrightls.configuration.project.WorkspaceFolders
import junit.framework.TestCase
import com.insyncwithfoo.pyrightls.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyrightls.configuration.project.Configurations as ProjectConfigurations


class ConfigurationFieldsTest : TestCase() {
    
    fun `test shape - all`() {
        val allFields = allFields()
        
        assertEquals(allFields.keys, applicationFields().keys + projectFields().keys)
        
        (applicationFields() + projectFields()).forEach { (name, field) ->
            val correspondingFieldInAll = allFields[name]!!
            
            assertEquals(field.returnType, correspondingFieldInAll.type)
        }
    }
    
    fun `test defaults - application`() {
        val configurations = ApplicationConfigurations()
        
        assertEquals(12, applicationFields().size)
        
        configurations.run {
            assertEquals(false, alwaysUseGlobal)
            assertEquals(null, globalExecutable)
            assertEquals(false, useEditorFont)
            assertEquals(false, addTooltipPrefix)
            assertEquals(false, linkErrorCodes)
            
            assertEquals(true, hoverSupport)
            assertEquals(false, completionSupport)
            assertEquals(false, goToDefinitionSupport)
            assertEquals(LogLevel.INFORMATION, logLevel)
            assertEquals(true, taggedHints)
            assertEquals(true, autoImportCompletions)
            assertEquals(false, autocompleteParentheses)
        }
    }
    
    fun `test defaults - project`() {
        val configurations = ProjectConfigurations()
        
        assertEquals(3, projectFields().size)
        
        configurations.run {
            assertEquals(null, projectExecutable)
            assertEquals(true, autoSuggestExecutable)
            
            assertEquals(WorkspaceFolders.PROJECT_BASE, workspaceFolders)
        }
    }
    
    fun `test delegate - application`() {
        val receiver = ApplicationConfigurations()
        
        applicationFields().forEach { (_, property) ->
            assertNotNull(property.getDelegate(receiver))
        }
    }
    
    fun `test delegate - project`() {
        val receiver = ProjectConfigurations()
        
        projectFields().forEach { (_, property) ->
            assertNotNull(property.getDelegate(receiver))
        }
    }
    
}
