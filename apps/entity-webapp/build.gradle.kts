import com.github.gradle.node.npm.task.NpmTask

// import com.github.gradle.node.npm.task.NpxTask
// import com.github.gradle.node.task.NodeTask

plugins {
    // Ref: https://github.com/node-gradle/gradle-node-plugin/blob/master/docs/faq.md
    id("com.github.node-gradle.node")
}

node {
    val isCI = System.getenv("CI").isNullOrBlank().not()
    npmInstallCommand.set(if (isCI) "ci" else "install")
    version.set("16.4.2")
    npmVersion.set("7.18.1")
    download.set(false)
}

tasks {
    jar {
        dependsOn("npmBuild")
        from("build/dist") {
            into("static")
        }
    }

    register<NpmTask>("run") {
        npmCommand.set(listOf("run", "dev"))
        args.addAll("--", "--open")
    }

    register<NpmTask>("npmBuild") {
        dependsOn(npmInstall)
        npmCommand.set(listOf("run", "build"))
    }

    register<Delete>("npmClean") {
        delete("build/dist")
    }
}

