package com.vrerv.gradle.plugin.gsheet.download

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File

class GSheetDownloadPluginTest {
    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.vrerv.gradle.plugin.gsheet.download")

        assert(project.tasks.getByName("gsheetDownload") is GSheetDownloadTask)
    }

    @Test
    fun `extension templateExampleConfig is created correctly`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.vrerv.gradle.plugin.gsheet.download")

        assertNotNull(project.extensions.getByName("gsheetDownloadConfig"))
    }

    @Test
    fun `parameters are passed correctly from extension to task`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.vrerv.gradle.plugin.gsheet.download")
        val aFile = File(project.projectDir, ".tmp")
        (project.extensions.getByName("gsheetDownloadConfig") as GSheetDownloadExtension).apply {
            tag.set("a-sample-tag")
            message.set("just-a-message")
            outputFile.set(aFile)
        }

        val task = project.tasks.getByName("gsheetDownload") as GSheetDownloadTask

        assertEquals("a-sample-tag", task.tag.get())
        assertEquals("just-a-message", task.message.get())
        assertEquals(aFile, task.outputFile.get().asFile)
    }
}
