pipeline {

    agent {
        label 'build-pod'
    }
    parameters {
        booleanParam(name: 'RELEASE', defaultValue: false, description: 'Enable this value to generate a release on this build')
        choice(name: 'RELEASE_TYPE', choices: 'Minor\nMajor\nPatch', description: 'Increment major, minor, or patch version for release')
    }
    options {
        timeout(time: 1, unit: "HOURS")
        parallelsAlwaysFailFast()
    }
    environment {
        CI = 'true'
    }

    stages {
        stage('Preparing') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                    echo("IsRelease: ${params.RELEASE}, Releasing with increment: ${params.RELEASE_TYPE}")
                }
                echo "Running Preparing..."
                // Default pipeline checkout behaviour does not fetch tags so we need to get them now
                sh "git fetch --tags"
                sh './gradlew currentVersion'
            }
        }

        stage('Test') {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }
                echo "Running Tests..."
                sh './gradlew check --profile'
            }
            post {
                always {
                    jacoco classPattern: "**/build/classes", execPattern: "**/build/jacoco/*.exec", sourcePattern: "**/src/main/kotlin"
                    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
                }
            }
        }

        stage('Static Code Analysis') {
            parallel {
                stage('SonarQube') {
                    steps {
                        script {
                            env.FAILED_STAGE_NAME = env.STAGE_NAME
                        }
                        sonarQube_scan(serviceName: "play-jenkins", buildType: 'gradle')
                    }
                }
                stage('NexusIQ') {
                    steps {
                        echo "TODO - NexusIQ is not implemented"
                    }
                }
                stage('Fortify') {
                    steps {
                        echo "TODO - Fortify is not implemented"
                    }
                }
            }
        }

        stage("Quality Gate") {
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }

                timeout(time: 30, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build for F/H/R/*') {
            when {
                anyOf {
                    branch 'feature/*'
                    branch 'hotfix/*'
                    branch 'release/*'
                    changeRequest()
                }
            }
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }
                echo "Running Build for PR/Feature/Hotfix/Release/*..."
                sh './gradlew build --profile'
            }
        }

        stage('Publish for Develop') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                    if (params.RELEASE) {
                        echo("Releasing with increment ${params.RELEASE_TYPE}")
                    }
                }
                echo "Running Publish for Develop..."
                sh './gradlew publish'
                timeout(time: 10, unit: "MINUTES") {
                    input(message: 'Build docker image?')
                    sh './gradlew jibBuildTar'
                }
            }
        }

        stage('Generate for Release') {
            when {
                branch 'release/*'
            }
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }
                echo "TODO: Generate Changelog"
                echo "TODO: Generate proto code"
            }
        }

        stage('Deploy for Master') {
            when {
                branch 'master'
            }
            steps {
                script {
                    env.FAILED_STAGE_NAME = env.STAGE_NAME
                }
                echo "Running Deploy for Master..."
                sh './gradlew publish'
                timeout(time: 1, unit: "HOURS") {
                    chatNotification('PROMPT', "*ACTION REQUIRED* Pipeline build ${env.BUILD_TAG} requesting input to deploy to *PRODUCTION*. Click _'Proceed'_  to deploy, click _'Abort'_  to skip deployment. Link to build: ${env.BUILD_URL}console")
                    input(message: 'Deploy this build to Production?')
                    sh './gradlew jib'
                }
            }
        }
    }

    post {
        success {
            chatNotification('SUCCESS', "Successful !")
        }
        unstable {
            chatNotification('UNSTABLE', "Unstable on stage *${env.FAILED_STAGE_NAME}* !")
        }
        failure {
            chatNotification('FAILURE', "Failed on stage *${env.FAILED_STAGE_NAME}* !")
        }
        aborted {
            chatNotification('ABORTED', "Aborted on stage *${env.FAILED_STAGE_NAME}* !")
        }
        always {
            echo "Archive JARs:"
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true

            echo "Publish Gradle Test Report:"
            publishHTML([
                    allowMissing         : false,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : 'build/reports/tests/test',
                    reportFiles          : 'index.html',
                    reportName           : 'Gradle Test Report',
                    reportTitles         : ''
            ])

            echo "Publish Gradle Profile Reports:"
            publishHTML([
                    allowMissing         : false,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : 'build/reports/profile',
                    reportFiles          : 'profile-*.html',
                    reportName           : 'Gradle Profile Reports',
                    reportTitles         : ''
            ])
        }
    }
}

def chatNotification(String level, String msg) {
    def GoogleWebhook = 'https://chat.googleapis.com/v1/spaces/xyz/messages?key=xyz&token=xyz'

    def message = "Build <${env.BUILD_URL}|${env.JOB_NAME}#${env.BUILD_NUMBER}>/<${env.RUN_DISPLAY_URL}|BlueOcean>: ${msg}" as Object

    if (level == 'SUCCESS') {
        googlechatnotification notifySuccess: true, message: message, sameThreadNotification: true, url: GoogleWebhook;
    } else if (level == 'UNSTABLE') {
        googlechatnotification notifyUnstable: true, message: message, sameThreadNotification: true, url: GoogleWebhook;
    } else if (level == 'FAILURE') {
        googlechatnotification notifyFailure: true, message: message, sameThreadNotification: true, url: GoogleWebhook;
    } else if (level == 'ABORTED') {
        googlechatnotification notifyAborted: true, message: message, sameThreadNotification: true, url: GoogleWebhook;
    } else if (level == 'PROMPT') {
        googlechatnotification message: msg as Object, sameThreadNotification: true, url: GoogleWebhook;
    }

}