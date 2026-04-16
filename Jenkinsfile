pipeline {
    agent any

//    tools {
//        maven 'Maven3'
//        jdk 'JDK17'
//    }

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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat '''
                    mvn clean verify sonar:sonar ^
                      -Dsonar.projectKey=mediquick ^
                      -Dsonar.projectName=MediQuick ^
                      -Dsonar.host.url=http://localhost:9000 ^
                      -Dsonar.login=YOUR_TOKEN
                    '''
                }
            }
        }

//        stage('Quality Gate') {
//            steps {
//                timeout(time: 5, unit: 'MINUTES') {
//                    waitForQualityGate abortPipeline: true
//                }
//            }
//        }
//
//        stage('Test Auth + Consultant Services') {
//            steps {
//                bat '''
//                mvn -pl auth-service,consultant-service -am clean test
//                '''
//            }
//        }
//
//        stage('Run Karate Tests') {
//            steps {
//                dir('karate-tests') {
//                    bat 'mvn clean test -Dkarate.env=local'
//                }
//            }
//        }
//    }
//
//    post {
//        always {
//            junit '**/target/surefire-reports/*.xml'
//        }
//        success {
//            echo '✅ Sonar + Unit Tests + Karate Passed'
//        }
//        failure {
//            echo '❌ Pipeline Failed'
//        }
    }
}