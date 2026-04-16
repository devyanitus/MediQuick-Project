pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout Code From Git') {
            steps {
                git branch: 'Pipeline-siddarth',
                    url: 'https://github.com/devyanitus/MediQuick-Project.git'
            }
        }

        stage('Code Compilation') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Test Auth Service') {
            steps {
                dir('auth-service') {
                    bat 'mvn test'
                }
            }
        }

        stage('Test Consultant Service') {
            steps {
                dir('consultant-service') {
                    bat 'mvn test'
                }
            }
        }

        stage('Code Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat '''
                    mvn clean verify -pl !karate-tests -am sonar:sonar ^
                      -Dsonar.projectKey=mediquick ^
                      -Dsonar.projectName=MediQuick ^
                      -Dsonar.host.url=http://localhost:9000 ^
                      -Dsonar.login=YOUR_TOKEN ^
                      -Dsonar.exclusions=**/karate-tests/**
                    '''
                }
            }
        }
//docker stage


//        stage('Karate API Tests') {
//            steps {
//                dir('karate-tests') {
//                    bat 'mvn test'
//                }
//            }
//        }
//
//        stage('Publish Karate Results') {
//            steps {
//                junit '**/karate-tests/target/surefire-reports/*.xml'
//            }
//        }
    }
}