import com.vrerv.gradle.plugin.gsheet.download.DownloadConfig

plugins {
    java
    id("com.vrerv.gradle.plugin.gsheet.download")
}

gsheetDownloadConfig {

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
