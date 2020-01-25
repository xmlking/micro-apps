node("build-pod") {
    container('jnlp') {
        try {
            // stages for ALL branches.
            stage('Checkout repository') {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                checkout([
                        $class                           : 'GitSCM',
                        branches                         : scm.branches,
                        doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                        extensions                       : scm.extensions + [
                                [$class: 'LocalBranch', localBranch: '**'],
                                [$class: 'CloneOption', noTags: false, reference: '', shallow: true]
                        ],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : scm.userRemoteConfigs
                ])

            }

            stage('Checks & SonarQube analysis') {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                ex_gradle(command: "currentVersion", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                ex_gradle(command: "check", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                sonarQube_scan(serviceName: "play-jenkins", buildType: 'gradle')
            }

            stage("Quality Gate") {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }

            // stages for FEATURE, HOTFIX branches.
            // env.BRANCH_NAME.startsWith("feature/")
            if (env.BRANCH_NAME =~ /^feature\/.*$|^hotfix\/.*$/) {
                stage('Build & Test for PRs') {
                    script {
                        env.FAILED_STAGE_NAME = env.STAGE_NAME
                    }

                    ex_gradle(command: "build --profile", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                    // currentBuild.result = 'SUCCESS'
                }
            }

            // stages for DEVELOP branch.
            if (env.BRANCH_NAME == "develop") {
                stage('Publish for Develop') {
                    script {
                        env.FAILED_STAGE_NAME = env.STAGE_NAME
                    }
                    ex_gradle(command: "publish", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                    // Our Nexus only allow SNAPSHOT
                    // ex_gradle(command: "publish -Prelease.forceSnapshot", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                    sh "ls -la build/repos"
                }
            }

            // stages for MASTER branch.
            if (env.BRANCH_NAME == "master") {
                stage('Deploy for Master') {
                    script {
                        env.FAILED_STAGE_NAME = env.STAGE_NAME
                    }
                    ex_gradle(command: "publish", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                    ex_gradle(command: "jibDockerBuild", nexusCredentials: "nexusAuthToken", gradleConfigFile: "initGradleCatalog")
                }
            }

            // stages for RELEASE branches.
            if (env.BRANCH_NAME =~ /^release\/.*$/) {
                echo "Doing RELEASE stages..."
                echo "deploying to GKE..."
            }

            chatNotification('SUCCESS', "Successful !")
        } catch (error) {
            stage('Notification') {
                switch (currentBuild.currentResult) {
                    case 'UNSTABLE':
                        chatNotification('WARN', "Unstable on stage *${env.FAILED_STAGE_NAME}* !")
                        break
                    case 'FAILED':
                        chatNotification('ERROR', "Failed on stage *${env.FAILED_STAGE_NAME}* !")
                        break
                    case 'ABORTED':
                        chatNotification('ERROR', "Aborted on stage *${env.FAILED_STAGE_NAME}* !")
                        break
                    default:
                        chatNotification('ERROR', "Error: ${error.message}")
                }
            }
        } finally {
            echo "All Done. Cleanup..."
        }
    }
}

def chatNotification(String level, String msg) {
    def color = '#439fe0'
    def message = "Build <${env.BUILD_URL}|${env.JOB_NAME}#${env.BUILD_NUMBER}>/<${env.RUN_DISPLAY_URL}|BlueOcean>: ${msg}" as Object
    if (level == 'SUCCESS') {
        color = '#27a21b'
    } else if (level == 'WARN') {
        color = '#ff4500'
    } else if (level == 'ERROR') {
        color = '#ce2231'
    }
    // TODO Send
    echo "${color} -  ${message}"
}