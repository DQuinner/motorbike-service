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
        stage('Unit Tests') {
            steps {
                gradlew('test', 'jacocoTestReport')
                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/reports/tests/test/',
                        reportFiles: 'index.html',
                        reportName: 'Test Summary Report'
                ]
                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/reports/jacoco/test/html/',
                        reportFiles: 'index.html',
                        reportName: 'Code Coverage Report'
                ]
                publishHTML target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'build/cucumber/',
                        reportFiles: 'index.html',
                        reportName: 'Cucumber Features Report'
                ]
            }
        }
//        stage('Long-running Verification') {
////            environment {
////                SONAR_LOGIN = credentials('SONARCLOUD_TOKEN')
////            }
//            parallel {
//                stage('Integration Tests') {
////                    steps {
////                        gradlew('integrationTest')
////                    }
////                    post {
////                        always {
////                            junit '**/build/test-results/integrationTest/TEST-*.xml'
////                        }
////                    }
//                }
//                stage('Code Analysis') {
////                    steps {
////                        gradlew('sonarqube')
////                    }
//                }
//            }
//        }
//        stage('Assemble') {
////            steps {
////                gradlew('assemble')
////                stash includes: '**/build/libs/*.war', name: 'app'
////            }
//        }
//        stage('Promotion') {
////            steps {
////                timeout(time: 1, unit:'DAYS') {
////                    input 'Deploy to Production?'
////                }
////            }
//        }
//        stage('Deploy to Production') {
////            environment {
////                HEROKU_API_KEY = credentials('HEROKU_API_KEY')
////            }
////            steps {
////                unstash 'app'
////                gradlew('deployHeroku')
////            }
//        }
    }
//    post {
//        failure {
//            mail to: 'donalq@gmail.com', subject: 'Build failed', body: 'Please fix!'
//        }
//    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}