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
                sleep 1
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
//            environment {
//                HEROKU_API_KEY = credentials('HEROKU_API_KEY')
//            }
            steps {
                sleep 1
//                unstash 'app'
//                gradlew('deployHeroku')
            }
        }
    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}