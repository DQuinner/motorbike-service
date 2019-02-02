pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Compile') {
            steps {
                gradlew('clean', 'classes')
            }
        }
        stage('Test') {
            parallel {
                stage('Unit') {
                    steps {
                        gradlew('test', 'jacocoTestReport')

                        publishHTML target: [
                                allowMissing: false,
                                alwaysLinkToLastBuild: false,
                                keepAll: true,
                                reportDir: 'build/reports/tests/test/',
                                reportFiles: 'index.html',
                                reportName: 'Unit Test Report'
                        ]
                        publishHTML target: [
                                allowMissing: false,
                                alwaysLinkToLastBuild: false,
                                keepAll: true,
                                reportDir: 'build/reports/jacoco/test/html/',
                                reportFiles: 'index.html',
                                reportName: 'Code Coverage Report'
                        ]
                    }
                    post {
                        always {
                            junit 'build/test-results/test/**/*.xml'
                        }
                    }
                }
                stage('Integration') {
                    steps {
                        gradlew('integrationTest')

                        publishHTML target: [
                                allowMissing: false,
                                alwaysLinkToLastBuild: false,
                                keepAll: true,
                                reportDir: 'build/reports/tests/integrationTest/',
                                reportFiles: 'index.html',
                                reportName: 'Integration Test Report'
                        ]
                    }
                    post {
                        always {
                            junit 'build/test-results/integrationTest/**/*.xml'
                        }
                    }
                }
            }
        }
        stage('Code Analysis') {
            parallel {
                stage('Sonar'){
                    environment {
                        SONAR_LOGIN = credentials('SONARCLOUD_TOKEN')
                    }
                    steps {
                        gradlew('sonarqube')
                    }
                }
                stage('Coverage'){
                    steps {
                        gradlew('jacocoTestCoverageVerification')
                    }
                }
            }
        }
        stage('Assemble') {
            steps {
                gradlew('assemble')
                stash includes: '**/build/libs/*.jar', name: 'app'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
                }
            }
        }
        stage('Publish') {
            steps {
                gradlew('install')
                stash includes: '**/build/libs/*.jar', name: 'app'
            }
        }
        stage('Acceptance Test') {
            steps {
                startApp()

                gradlew('acceptanceTest')

                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/reports/tests/acceptanceTest/',
                        reportFiles: 'index.html',
                        reportName: 'Acceptance Test Report'
                ]
                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/cucumber/',
                        reportFiles: 'index.html',
                        reportName: 'Cucumber Report'
                ]
            }
            post {
                always {
                    junit 'build/test-results/acceptanceTest/**/*.xml'
                    stopApp()
                }
            }
        }
        stage('Promote') {
            steps {
                timeout(time: 1, unit:'DAYS') {
                    input 'Deploy?'
                }
            }
        }
        stage('Deploy') {
            steps {
                sleep 1 //2DO feature/cloud-foundry
            }
        }
    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}

def startApp() {
    sh "java -jar build/libs/motorbike-service-*.jar -s"
    return
}

def stopApp() {
    sh "curl -X POST localhost:8080/actuator/shutdown"
    return
}