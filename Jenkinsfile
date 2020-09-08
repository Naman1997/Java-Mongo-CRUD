pipeline {
    agent any
    tools {
        gradle 'Gradle'
        docker 'Docker'
    }
    stages {
        stage("build") {
            steps{
                sh "gradle build"
            }
        }
        stage("test") {
            steps{
                sh "docker build --tag testImage:latest ."
            }
        }
    }
}
