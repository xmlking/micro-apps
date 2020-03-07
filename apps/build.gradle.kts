subprojects {
    apply {
        plugin("application")
    }

    tasks {
        shadowJar {
            isZip64 = true
            mergeServiceFiles()
        }
    }
}
