// pipeline {
//     agent any
//
//     stages {
//         stage('Run Tests') {
//             steps {
//                  bat 'mvn clean test'
//             }
//         }
//         post {
//                         always {
//                             junit '**/surefire-reports/*.xml'
//                         }
//                     }
//     }
// }


pipeline {
    agent any

    stages {
        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
