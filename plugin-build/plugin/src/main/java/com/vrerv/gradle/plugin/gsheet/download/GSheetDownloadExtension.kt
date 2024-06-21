package com.vrerv.gradle.plugin.gsheet.download

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

const val DEFAULT_OUTPUT_FILE = "template-example.txt"

@Suppress("UnnecessaryAbstractClass")
abstract class GSheetDownloadExtension
    @Inject
    constructor(
        project: Project,
    ) {
        private val objects = project.objects

        val googleApplicationCredentials: Property<String> = objects.property(String::class.java)
        val downloads: ListProperty<DownloadConfig> =
            objects.listProperty(DownloadConfig::class.java)
        val outputDir: DirectoryProperty = objects.directoryProperty()
    }
