pipeline {
    agent any

    tools {
        jdk 'jdk-8'
        maven 'mvn-3.8.6'
    }

    stages {
        stage("build") {
            steps {
                sh 'mvn clean install'
            }
        }

        stage("deploy") {
            steps {
                sh 'java -jar target/javalin-example-1.0-SNAPSHOT-jar-with-dependencies.jar'
            }
        }
    }
}