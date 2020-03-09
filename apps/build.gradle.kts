import com.google.cloud.tools.jib.api.ImageFormat

val gcloudProject: String by project
val baseDockerImage: String by project

plugins {
    application
    // Make fat runnable jars
    // gradle shadowJar
    // gradle runShadow
    id("com.github.johnrengelman.shadow") version "5.2.0"
    // Build & Publish docker images
    // gradle jib
    id("com.google.cloud.tools.jib") version "2.0.0"
}

subprojects {
    apply {
        plugin("application")
        plugin("com.github.johnrengelman.shadow")
        plugin("com.google.cloud.tools.jib")
    }

    tasks {
        shadowJar {
            isZip64 = true
            mergeServiceFiles()
        }
    }

    jib {
        setAllowInsecureRegistries(true)
        from {
            if (project.hasProperty("baseDockerImage")) {
                image = baseDockerImage
            }
        }
        to {
            image = "xmlking/${rootProject.name}-${project.name}:${project.version}"
            // image = "us.gcr.io/${gcloudProject}/${rootProject.name}/${project.name}:${project.version}"

            /**
            gcr: Google Container Registry (GCR)
            osxkeychain: Docker Hub
             */
            credHelper = "osxkeychain"
            /**
            auth {
            username = "*******"
            password = "*******"
            }
             */
            tags = setOf("${project.version}")
        }
        container {
            jvmFlags = listOf("-Djava.security.egd=file:/dev/./urandom", "-Xms512m", "-server")
            creationTime = "USE_CURRENT_TIMESTAMP"
            mainClass = application.mainClassName
            ports = listOf("8080", "8443")
            labels = mapOf("version" to "${project.version}", "name" to project.name, "group" to "${project.group}")
            format = ImageFormat.OCI
        }
    }
}
