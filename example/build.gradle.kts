import com.vrerv.gradle.plugin.gsheet.download.DownloadConfig

buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    java
    id("com.vrerv.gradle.plugin.gsheet.download")
}

repositories {
    mavenCentral()
}

gsheetDownloadConfig {

    // googleApplicationCredentials = "/Users/soonoh/service/key/api-project-502110011071-d5a05d3385ff.json"
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

    outputDir = file("build")
}
