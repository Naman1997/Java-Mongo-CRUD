pipeline {
    agent { dockerfile true }
    stages {
        stage('List Files') {
            steps {
                ls
            }
        }
        stage('Test') {
            steps {
                sh 'git --version'
                sh 'python3 --version'
            }
        }
    }
}
