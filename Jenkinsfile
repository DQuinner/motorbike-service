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
        stage('Analyse and Test') {
            parallel {
                stage('Unit Test') {
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
                stage('Integration Test') {
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
                stage('Sonar Analysis'){
                    environment {
                        SONAR_LOGIN = credentials('SONARCLOUD_TOKEN')
                    }
                    steps {
                        gradlew('sonarqube')
                    }
                }
            }
        }
        stage('Quality Gate') {
            parallel {
                stage('Code Coverage'){
                    steps {
                        gradlew('jacocoTestCoverageVerification')
                    }
                }
                stage('Sonar'){
                    environment {
                        SONAR_LOGIN = credentials('SONARCLOUD_TOKEN')
                    }
                    steps {
                        sleep(1) //2DO check sonar results
                        //gradlew('sonarqube')
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
                sleep(10) //wait for application to start
                gradlew('acceptanceTest aggregate')

                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/cucumber/',
                        reportFiles: 'index.html',
                        reportName: 'Cucumber Report'
                ]
                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'target/site/serenity/',
                        reportFiles: 'index.html',
                        reportName: 'Serenity Report'
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
    sh "java -jar build/libs/motorbike-service-*.jar &"
}

def stopApp() {
    sh "curl -X POST localhost:8080/actuator/shutdown"
}