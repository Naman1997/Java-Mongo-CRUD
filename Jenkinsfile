pipeline {
    agent any
    stages {
        stage("build") {
            steps{
                mvn clean install
            }
        }
        stage("test") {
            steps{
                mvn dependency:tree
            }
        }
    }
}
