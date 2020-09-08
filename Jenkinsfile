pipeline {
    agent { dockerfile true }
    stages {
        stage('List Files') {
            steps {
                sh 'ls'
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
