package com.vrerv.gradle.plugin.gsheet.download

import com.vrerv.lib.googlecloud.sheets.GoogleSheetToCSV
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.OutputStreamWriter

data class DownloadConfig(
    val sheetId: String,
    val sheetName: String,
    val rangeFrom: String,
    val rangeTo: String,
    val outputFileName: String,
)

abstract class GSheetDownloadTask : DefaultTask() {
    init {
        description = "A Google sheet download task that downloads file from Google sheet and save it as a CSV file."

        // Don't forget to set the group here.
        // group = BasePlugin.BUILD_GROUP
    }

    @get:Input
    @get:Option(option = "googleApplicationCredentials", description = "The google application credentials file path for google api")
    @get:Optional
    abstract val googleApplicationCredentials: Property<String>

    @get:Input
    @get:Option(option = "downloads", description = "List of download configuration to download from Google sheet")
    abstract val downloads: ListProperty<DownloadConfig>

    @get:InputDirectory
    @get:Option(
        option = "outputDir",
        description = "Folder where to save the downloaded files",
    )
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun download() {
        logger.lifecycle("GOOGLE_APPLICATION_CREDENTIALS is: ${System.getenv("GOOGLE_APPLICATION_CREDENTIALS")}")
        logger.lifecycle("googleApplicationCredentials is: ${googleApplicationCredentials.orNull}")
        logger.lifecycle("downloads is: ${downloads.orNull}")
        logger.lifecycle("outputDir is: ${outputDir.orNull}")

        val downloader = GoogleSheetToCSV()
        downloader.loadGoogleCredentials(googleApplicationCredentials.orNull)

        val outputDir = outputDir.get().asFile

        downloads.get().forEach { downloadConfig ->
            val outputFile = outputDir.resolve(downloadConfig.outputFileName)
            logger.lifecycle("Downloading ${downloadConfig.sheetName} to ${outputFile.absolutePath}")
            downloader.writeCsv(
                downloadConfig.sheetId,
                "${downloadConfig.sheetName}!${downloadConfig.rangeFrom}:${downloadConfig.rangeTo}",
                OutputStreamWriter(outputFile.outputStream()),
            )
        }
    }
}
