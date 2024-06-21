package com.vrerv.gradle.plugin.gsheet.download

import org.gradle.api.Plugin
import org.gradle.api.Project

const val EXTENSION_NAME = "gsheetDownloadConfig"
const val TASK_NAME = "gsheetDownload"

abstract class GSheetDownloadPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, GSheetDownloadExtension::class.java, project)

        // Add a task that uses configuration from the extension object
        project.tasks.register(TASK_NAME, GSheetDownloadTask::class.java) {
            it.googleApplicationCredentials.set(extension.googleApplicationCredentials)
            it.downloads.set(extension.downloads)
            it.outputDir.set(extension.outputDir)
        }
    }
}
