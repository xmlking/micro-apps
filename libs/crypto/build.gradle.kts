val tinkVersion = libs.versions.googleTink.get()

dependencies {
    implementation(projects.libs.core)
    implementation("com.google.crypto.tink:tink:$tinkVersion")
    // Optional
    implementation("com.google.crypto.tink:tink-gcpkms:$tinkVersion")
    // implementation("com.google.crypto.tink:tink-awskms:$tinkVersion")
    // FIXME: temp workaround https://github.com/google/tink/issues/549
    implementation("com.google.http-client:google-http-client-jackson2:1.43.0")

    // Testing
    testImplementation(testFixtures(projects.libs.test))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
