plugins {
    id("java")
    id("maven-publish")
    id("eu.kakde.gradle.sonatype-maven-central-publisher") version "1.0.6"

// eu.kakde.gradle.sonatype-maven-central-publisher plugin defined this, so not define manually
//
//    signing
}

group = "com.vrerv.lib.googlecloud.sheets"
version = property("library.version")!!.toString()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

// eu.kakde.gradle.sonatype-maven-central-publisher plugin defined this, so not define manually
//
// signing {
//    val signingKeyId: String? by project
//    val signingKey: String? by project
//    val signingPassword: String? by project
//    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
//    println(publishing.publications.asMap)
//    sign(publishing.publications["java"])
// }

publishing {
    publications {
// eu.kakde.gradle.sonatype-maven-central-publisher plugin defined this, so not define manually
//
//        create<MavenPublication>("maven") {
//            groupId = group.toString()
//            artifactId = project.name
//            version = version
//
//            from(components["java"])
//        }
    }
}

//
// - eu.kakde.gradle.sonatype-maven-central-publisher plugin settings
// - 1.0.6 version only runs with Java 21 or higher
//

@Suppress("ktlint:standard:property-naming")
val COMPONENT_TYPE = "java"

// "java" or "versionCatalog"
@Suppress("ktlint:standard:property-naming")
val ARTIFACT_ID = project.name

@Suppress("ktlint:standard:property-naming")
val PUBLISHING_TYPE = "AUTOMATIC"

// USER_MANAGED or AUTOMATIC
@Suppress("ktlint:standard:property-naming")
val SHA_ALGORITHMS =
    listOf(
        "SHA-256",
        "SHA-512",
    )

// sha256 and sha512 are supported but not mandatory. Only sha1 is mandatory but it is supported by default.
@Suppress("ktlint:standard:property-naming")
val DESC = "Download Google sheet as CSV file using Google Sheets API"

@Suppress("ktlint:standard:property-naming")
val LICENSE = "MIT"

@Suppress("ktlint:standard:property-naming")
val LICENSE_URL = "https://opensource.org/license/mit"

@Suppress("ktlint:standard:property-naming")
val GITHUB_REPO = "vrerv/gradle-gsheet-download-plugin.git"

@Suppress("ktlint:standard:property-naming")
val DEVELOPER_ID = "vrerv"

@Suppress("ktlint:standard:property-naming")
val DEVELOPER_NAME = "Soonoh Jung"

@Suppress("ktlint:standard:property-naming")
val DEVELOPER_ORGANIZATION = "VReRV"

@Suppress("ktlint:standard:property-naming")
val DEVELOPER_ORGANIZATION_URL = "https://vrerv.com"

val sonatypeUsername: String? by project // this is defined in ~/.gradle/gradle.properties
val sonatypePassword: String? by project // this is defined in ~/.gradle/gradle.properties

println(version.toString())

val artifactVersion = version.toString()

sonatypeCentralPublishExtension {
    // Set group ID, artifact ID, version, and other publication details
    groupId.set(group.toString())
    artifactId.set(project.name)
    version.set(artifactVersion)
    componentType.set(COMPONENT_TYPE) // "java" or "versionCatalog"
    publishingType.set(PUBLISHING_TYPE) // USER_MANAGED or AUTOMATIC

    // Set username and password for Sonatype repository
    username.set(System.getenv("SONATYPE_USERNAME") ?: sonatypeUsername)
    password.set(System.getenv("SONATYPE_PASSWORD") ?: sonatypePassword)

    // Configure POM metadata
    pom {
        name.set(artifactId)
        description.set(DESC)
        url.set("https://github.com/${GITHUB_REPO}")
        licenses {
            license {
                name.set(LICENSE)
                url.set(LICENSE_URL)
            }
        }
        developers {
            developer {
                id.set(DEVELOPER_ID)
                name.set(DEVELOPER_NAME)
                organization.set(DEVELOPER_ORGANIZATION)
                organizationUrl.set(DEVELOPER_ORGANIZATION_URL)
            }
        }
        scm {
            url.set("https://github.com/${GITHUB_REPO}")
            connection.set("scm:git:https://github.com/${GITHUB_REPO}")
            developerConnection.set("scm:git:https://github.com/${GITHUB_REPO}")
        }
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/${GITHUB_REPO}/issues")
        }
    }
}

dependencies {

    // lombok
    val lombokVersion = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    // end lombok
    implementation("org.slf4j:slf4j-api:2.0.13")

    implementation("com.opencsv:opencsv:5.9") {
        // ignore commons-collections that has a vulnerability.
        // https://repo1.maven.org/maven2/com/opencsv/opencsv/5.9/opencsv-5.9.pom - uses commons-collections4
        exclude(group = "org.apache.commons", module = "commons-collections")
    }

    implementation("com.google.api-client:google-api-client:1.35.2")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.4.0")
    implementation("com.google.apis:google-api-services-sheets:v4-rev612-1.25.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
