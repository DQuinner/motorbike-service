pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                sleep(1)
//                checkout([
//                        $class: 'GitSCM',
//                        branches: scm.branches,
//                        extensions: scm.extensions + [[$class: 'LocalBranch']]
//                ])
            }
        }
        stage('Compile') {
            steps {
                sleep(1)
                //gradlew('clean', 'classes')
            }
        }
        stage('Test') {
            parallel {
                stage('Unit') {
                    steps {
                        sleep(1)
//                        gradlew('test', 'jacocoTestReport')
//
//                        publishHTML target: [
//                                allowMissing         : false,
//                                alwaysLinkToLastBuild: false,
//                                keepAll              : true,
//                                reportDir            : 'build/reports/tests/test/',
//                                reportFiles          : 'index.html',
//                                reportName           : 'Unit Test Report'
//                        ]
//                        publishHTML target: [
//                                allowMissing         : false,
//                                alwaysLinkToLastBuild: false,
//                                keepAll              : true,
//                                reportDir            : 'build/reports/jacoco/test/html/',
//                                reportFiles          : 'index.html',
//                                reportName           : 'Code Coverage Report'
//                        ]
                    }
                    post {
                        always {
                            sleep(1)
                            //junit 'build/test-results/test/**/*.xml'
                        }
                    }
                }
                stage('Integration') {
                    steps {
                        sleep(1)
//                        gradlew('integrationTest')
//
//                        publishHTML target: [
//                                allowMissing         : false,
//                                alwaysLinkToLastBuild: false,
//                                keepAll              : true,
//                                reportDir            : 'build/reports/tests/integrationTest/',
//                                reportFiles          : 'index.html',
//                                reportName           : 'Integration Test Report'
//                        ]
                    }
                    post {
                        always {
                            sleep(1)
                            //junit 'build/test-results/integrationTest/**/*.xml'
                        }
                    }
                }
            }
        }
        stage('Quality Gate') {
            parallel {
                stage('Sonar Analysis') {
                    environment {
                        SONAR_LOGIN = credentials('SONARCLOUD_TOKEN')
                    }
                    steps {
                        sleep(1)
                        //gradlew('sonarqube')
                    }
                }
                stage('Code Coverage') {
                    steps {
                        sleep(1)
                        //gradlew('jacocoTestCoverageVerification')
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sleep(1)
//                gradlew('assemble')
//                stash includes: '**/build/libs/*.jar', name: 'app'
//                gradlew('docker')
            }
            post {
                always {
                    sleep(1)
                    //archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
                }
            }
        }
        stage('Publish') {
            steps {
                sleep(1)
//                gradlew('install')
//                gradlew('dockerPush')
            }
        }
        stage('Acceptance Test') {
            steps {
                sleep(1)
//                startApp()
//                sleep(30) //wait for application to start
//                gradlew('acceptanceTest aggregate')
//
//                publishHTML target: [
//                        allowMissing         : false,
//                        alwaysLinkToLastBuild: false,
//                        keepAll              : true,
//                        reportDir            : 'build/reports/tests/acceptanceTest/',
//                        reportFiles          : 'index.html',
//                        reportName           : 'Acceptance Test Report'
//                ]
//                publishHTML target: [
//                        allowMissing         : false,
//                        alwaysLinkToLastBuild: false,
//                        keepAll              : true,
//                        reportDir            : 'build/cucumber/',
//                        reportFiles          : 'index.html',
//                        reportName           : 'Cucumber Report'
//                ]
//                publishHTML target: [
//                        allowMissing         : false,
//                        alwaysLinkToLastBuild: false,
//                        keepAll              : true,
//                        reportDir            : 'target/site/serenity/',
//                        reportFiles          : 'index.html',
//                        reportName           : 'Serenity Report'
//                ]
            }
            post {
                always {
                    sleep(1)
//                    junit 'build/test-results/acceptanceTest/**/*.xml'
//                    stopApp()
                }
            }
        }
        stage('Deployment Gate') {
            steps {
                timeout(time: 1, unit: 'DAYS') {
                    input 'Deploy docker tag '+dockerImage()
                }
            }
        }
        stage('Deploy') {
            steps {
                createDockerRunFile()
                createEnvironment()
                environmentHealthCheck()
            }
        }
    }
}
def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}

def startApp() {
    def appProps = readProperties  file:'src/main/resources/application.properties'
    def testProps = readProperties  file:'src/test/resources/application-test.properties'
    sh "docker run -p "+testProps['acceptance.test.port']+":"+testProps['acceptance.test.port']+" -t dquinner/motorbike-service:"+appProps['info.app.version']+currentTag()+" &"
}

def stopApp() {
    def testProps = readProperties  file:'src/test/resources/application-test.properties'
    sh "curl -X POST "+testProps['acceptance.test.host']+":"+testProps['acceptance.test.port']+"/actuator/shutdown"
}

def currentTag(){
    def branch = env.BRANCH_NAME
    if(branch.contains('feature/')){
        return '-'+branch.replace('feature/','')
    }else if (branch.equals('master')){
        return '-latest'
    }else {
        return ''
    }
}

def createDockerRunFile(){
    def json = readJSON file: 'DockerRunTemplate.json'
    json.Image.Name=dockerImage()
    writeJSON file: 'Dockerrun.aws.json', json: json, pretty: 4
}

def environmentName(){
    def appProps = readProperties  file:'src/main/resources/application.properties'
    def appVersion = appProps['info.app.version']
    return appVersion.replace('.','')+currentTag()
}

def dockerImage(){
    def appProps = readProperties  file:'src/main/resources/application.properties'
    return "dquinner/motorbike-service:"+appProps['info.app.version']+currentTag()
}

def createEnvironment(){
    def resp = sh (script: "eb create "+environmentName()+" -s", returnStdout: true)
    echo 'createEnvironment resp = '+resp
    if(resp.contains('Successfully launched environment: '+environmentName())){
        echo 'environment created'
    }else{
        echo 'environment not created'
    }
}

def environmentHealthCheck(){
    def response = sh (script: "aws elasticbeanstalk describe-environments --environment-names "
            +environmentName()+" --no-include-deleted --output json", returnStdout: true)
    writeJSON file: 'Env_'+environmentName()+'.json', json: response, pretty: 4
    def jsonEnvironment = readJSON file: 'Env_'+environmentName()+'.json'
    echo 'jsonEnvironment = '+jsonEnvironment
    echo 'Environments = '+jsonEnvironment.Environments
//    echo 'Status = '+jsonEnvironment.Environments[0].Status
//    echo 'HealthStatus = '+jsonEnvironment.Environments[0].HealthStatus
//    echo 'CNAME = '+jsonEnvironment.Environments[0].CNAME
}