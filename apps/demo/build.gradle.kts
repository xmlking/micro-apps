dependencies {
    implementation(project(":libs:dlib"))
}


application {
    mainClassName = "micro.apps.demo.AppKt"
    //    applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
}
