plugins {
    id("java")
}

group = "com.vrerv.lib.googlecloud.sheets"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.22")
    testCompileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
    // end lombok
    implementation("org.slf4j:slf4j-api:2.0.13")

    implementation("com.opencsv:opencsv:5.9") {
        // ignore commons-collections that has a vulnerability.
        // https://repo1.maven.org/maven2/com/opencsv/opencsv/5.9/opencsv-5.9.pom - uses commons-collections4
        exclude(group = "org.apache.commons", module = "commons-collections4")
    }

    implementation("com.google.api-client:google-api-client:1.25.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.4.0")
    implementation("com.google.apis:google-api-services-sheets:v4-rev612-1.25.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
