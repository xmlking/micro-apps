val gsonJavatimeSerialiserVersion: String by project
val floggerVersion: String by project

dependencies {
    implementation("com.fatboyindustrial.gson-javatime-serialisers:gson-javatime-serialisers:$gsonJavatimeSerialiserVersion")

    // Testing
    testImplementation("com.google.flogger:flogger-testing:$floggerVersion")
}
