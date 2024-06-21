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
            googleApplicationCredentials.set("path/to/credentials.json")
            downloads.set(
                listOf(
                    DownloadConfig(
                        sheetId = "1SVp5gkj-aHZCxfSLKLIzGKyJOY5Tzadqo-_TFuxGBOg",
                        sheetName = "Sheet1",
                        rangeFrom = "A1",
                        rangeTo = "H1000",
                        outputFileName = "test.csv",
                    ),
                ),
            )
            outputDir.set(aFile)
        }

        val task = project.tasks.getByName("gsheetDownload") as GSheetDownloadTask

        assertEquals("path/to/credentials.json", task.googleApplicationCredentials.get())
        assertEquals("1SVp5gkj-aHZCxfSLKLIzGKyJOY5Tzadqo-_TFuxGBOg", task.downloads.get()[0].sheetId)
        assertEquals("Sheet1", task.downloads.get()[0].sheetName)
        assertEquals("A1", task.downloads.get()[0].rangeFrom)
        assertEquals("H1000", task.downloads.get()[0].rangeTo)
        assertEquals("test.csv", task.downloads.get()[0].outputFileName)
        assertEquals(aFile, task.outputDir.get().asFile)
    }
}
