pipeline {
    agent any
    stages {
        stage("build") {
            steps{
                withGradle() {
                    sh "./gradlew build"
                }
            }
        }
        stage("test") {
            steps{
                docker('Docker') {
                    sh "docker build --tag testImage:latest ."
                }
            }
        }
    }
}
