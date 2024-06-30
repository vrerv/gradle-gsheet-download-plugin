# gradle-gsheet-download-plugin 🐘

[![Pre Merge Checks](https://github.com/vrerv/gradle-gsheet-download-plugin/workflows/Pre%20Merge%20Checks/badge.svg)](https://github.com/vrerv/gradle-gsheet-download-plugin/actions?query=workflow%3A%22Pre+Merge+Checks%22)

A Gradle Plugin to download Google Sheets as CSV files.

* You can download multiple sheets at once so that you can include some data in your build process.

## How to use

### Create Google Service Account

* You have to set up a google service account and create application credential key in Google Cloud Console.
* Then, you have to share the Google Sheet with the service account email.
* You can set GOOGLE_APPLICATION_CREDENTIALS environment variable to the path of your application credential file.
  Otherwise, you can set the path in the plugin configuration.

For more details about Google Sheets API, see [Google Sheets API](https://developers.google.com/sheets/api).

### Apply the plugin

Find the [example](example/build.gradle.kts) folder for a working example.

You can run the task with the following command:

```shell

./gradlew gsheetDownload

```

build.gradle.kts

```kotlin
import com.vrerv.gradle.plugin.gsheet.download.DownloadConfig

plugins {
    id("com.vrerv.gradle.plugin.gsheet.download")
}

gsheetDownloadConfig {

    // googleApplicationCredentials = "path/to/your/service-account-key.json"
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
```

build.gradle

```groovy
import com.vrerv.gradle.plugin.gsheet.download.DownloadConfig

plugins {
    id "com.vrerv.gradle.plugin.gsheet.download"
}

gsheetDownloadConfig {

    // googleApplicationCredentials = "path/to/your/service-account-key.json"
    downloads = [
        new DownloadConfig(
            "1SVp5gkj-aHZCxfSLKLIzGKyJOY5Tzadqo-_TFuxGBOg",
            "Sheet1",
            "A1",
            "H1000",
            "test.csv",
        )
    ]

    outputDir = file("build")
}
```

## Development

* [Plugin Development](docs/plugin-development.md)

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.

## License

This plugin is licensed under the MIT License - see the [License](License) file for details.
Please note that the generated template is offering to start with a MIT license but you can change it to whatever you wish, as long as you attribute under the MIT terms that you're using the template.
