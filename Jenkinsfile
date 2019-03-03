pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout([
                        $class: 'GitSCM',
                        branches: scm.branches,
                        extensions: scm.extensions + [[$class: 'LocalBranch']]
                ])
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
                                allowMissing         : false,
                                alwaysLinkToLastBuild: false,
                                keepAll              : true,
                                reportDir            : 'build/reports/tests/test/',
                                reportFiles          : 'index.html',
                                reportName           : 'Unit Test Report'
                        ]
                        publishHTML target: [
                                allowMissing         : false,
                                alwaysLinkToLastBuild: false,
                                keepAll              : true,
                                reportDir            : 'build/reports/jacoco/test/html/',
                                reportFiles          : 'index.html',
                                reportName           : 'Code Coverage Report'
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
                                allowMissing         : false,
                                alwaysLinkToLastBuild: false,
                                keepAll              : true,
                                reportDir            : 'build/reports/tests/integrationTest/',
                                reportFiles          : 'index.html',
                                reportName           : 'Integration Test Report'
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
        stage('Quality Gate') {
            parallel {
                stage('Sonar Analysis') {
                    environment {
                        SONAR_LOGIN = credentials('SONARCLOUD_TOKEN')
                    }
                    steps {
                        gradlew('sonarqube')
                    }
                }
                stage('Code Coverage') {
                    steps {
                        gradlew('jacocoTestCoverageVerification')
                    }
                }
            }
        }
        stage('Build') {
            steps {
                gradlew('assemble')
                stash includes: '**/build/libs/*.jar', name: 'app'
                gradlew('docker')
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
                gradlew('dockerPush')
            }
        }
        stage('Acceptance Test') {
            steps {
                startTestApp()
                sleep(30) //wait for application to start
                gradlew('acceptanceTest aggregate')

                publishHTML target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : 'build/reports/tests/acceptanceTest/',
                        reportFiles          : 'index.html',
                        reportName           : 'Acceptance Test Report'
                ]
                publishHTML target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : 'build/cucumber/',
                        reportFiles          : 'index.html',
                        reportName           : 'Cucumber Report'
                ]
                publishHTML target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : 'target/site/serenity/',
                        reportFiles          : 'index.html',
                        reportName           : 'Serenity Report'
                ]
            }
            post {
                always {
                    junit 'build/test-results/acceptanceTest/**/*.xml'
                    stopTestApp()
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
                healthCheck()
            }
        }
    }
}
def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}

def startTestApp() {
    def appProps = readProperties  file:'src/main/resources/application.properties'
    def testProps = readProperties  file:'src/test/resources/application-test.properties'
    sh "docker run -p "+testProps['acceptance.test.port']+":"+testProps['acceptance.test.port']+" -t dquinner/motorbike-service:"+appProps['info.app.version']+currentTag()+" &"
}

def stopTestApp() {
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
    sh (script: "eb create "+environmentName()+" -s", returnStdout: true)
}

def healthCheck(){
    def url = environmentURL()
    if(url!=null){
        def appHealth = applicationHealthCheck(url)
        if(!'UP'.equals(appHealth)){
            error('Failed to start application on '+url)
        }
    }else{
        error('Failed to create environment '+environmentName())
    }
}

def environmentURL(){
    def environment = readJSON text: sh (script: "aws elasticbeanstalk describe-environments --environment-names "+environmentName()+" --no-include-deleted --output json", returnStdout: true)
    if(('Ready').equals(environment.Environments[0].Status) && ('Ok').equals(environment.Environments[0].HealthStatus) && ('Green').equals(environment.Environments[0].Health)){
        return environment.Environments[0].CNAME
    }
}

def applicationHealthCheck(url){
    def jsonHealth = readJSON text: sh (script: "curl -X GET "+url+"/actuator/health", returnStdout: true)
    if('UP'.equals(jsonHealth.status)){
        return 'UP'
    }
}