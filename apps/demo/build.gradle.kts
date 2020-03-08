dependencies {
    implementation(project(":libs:core"))
}


application {
    mainClassName = "micro.apps.demo.AppKt"
    //    applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
}

tasks {
    shadowJar {
        isZip64 = true
        mergeServiceFiles()
    }
}
