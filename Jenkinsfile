pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        DOCKER_USERNAME = 'sid118'
        DOCKER_PASSWORD = credentials('dockerhub-token')
        IMAGE_TAG = "latest"
    }


    stages {

        stage('Checkout Code From Git') {
            steps {
                git branch: 'Docker-siddarth',
                    url: 'https://github.com/devyanitus/MediQuick-Project.git'
            }
        }

//        stage('Code Compilation') {
//            steps {
//                bat 'mvn clean install -DskipTests'
//            }
//        }
//
//        stage('Test Auth Service') {
//            steps {
//                dir('auth-service') {
//                    bat 'mvn test'
//                }
//            }
//        }
//
//        stage('Test Consultant Service') {
//            steps {
//                dir('consultant-service') {
//                    bat 'mvn test'
//                }
//            }
//        }
//
//        stage('Code Coverage Report') {
//            steps {
//                jacoco()
//            }
//        }
//
//        stage('SonarQube Analysis') {
//            steps {
//                withSonarQubeEnv('SonarQube') {
//                    bat '''
//                    mvn clean verify -pl !karate-tests -am sonar:sonar ^
//                      -Dsonar.projectKey=mediquick ^
//                      -Dsonar.projectName=MediQuick ^
//                      -Dsonar.host.url=http://localhost:9000 ^
//                      -Dsonar.exclusions=**/karate-tests/**
//                    '''
//                }
//            }
//        }

        stage('Build Docker Images') {
            steps {
                bat '''
                docker build -t %DOCKER_USERNAME%/auth-service:%IMAGE_TAG% auth-service
                docker build -t %DOCKER_USERNAME%/consultant-service:%IMAGE_TAG% consultant-service
                docker build -t %DOCKER_USERNAME%/api-gateway:%IMAGE_TAG% Api-Gateway
                docker build -t %DOCKER_USERNAME%/config-server:%IMAGE_TAG% config-server
                docker build -t %DOCKER_USERNAME%/eureka-server:%IMAGE_TAG% eureka-server
                '''
            }
        }

        stage('Docker Login') {
            steps {
                bat '''
                echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                '''
            }
        }

        stage('Push Docker Images') {
//            options {
//                timeout(time: 10, unit: 'MINUTES')
//            }
            steps {
                bat '''
                docker push %DOCKER_USERNAME%/auth-service:%IMAGE_TAG%
                docker push %DOCKER_USERNAME%/consultant-service:%IMAGE_TAG%
                docker push %DOCKER_USERNAME%/api-gateway:%IMAGE_TAG%
                docker push %DOCKER_USERNAME%/config-server:%IMAGE_TAG%
                docker push %DOCKER_USERNAME%/eureka-server:%IMAGE_TAG%
                '''
            }
        }

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