pipeline {
    agent any
    tools {
        maven 'Maven-3.6.3'
        jdk 'OpenJdk-17'
    }
    stages {
        stage ('Build application services'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back.git']])
                dir('K8s-Kafka-Avro-AEPC-Back/k8s-kafka-avro-aepc-clean-archi-bs-ms-address/'){
                    sh 'mvn clean install' 
                }
            }
            post {
                success {
                    dir('K8s-Kafka-Avro-AEPC-Back/k8s-kafka-avro-aepc-clean-archi-bs-ms-address/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                }
            }
            
        }
        stage ('Test business microservices'){
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back.git']])
                echo 'starting run unit test'
                dir('K8s-Kafka-Avro-AEPC-Back/k8s-kafka-avro-aepc-clean-archi-bs-ms-address/'){
                    sh 'mvn test' 
                }
            }
            post {
                always {
                    dir('K8s-Kafka-Avro-AEPC-Back/k8s-kafka-avro-aepc-clean-archi-bs-ms-address/'){
                        junit '**/target/surefire-reports/TEST-*.xml'
                    }
                }
            }
        }
        stage ('Build docker images'){
            steps {
                echo 'Starting to build docker image'
                script {
                    sh 'docker compose -f kafka-avro-ms-docker-compose.yml down'
                    sh 'docker compose -f kafka-avro-ms-docker-compose.yml build'
                    sh 'docker system prune -f'
                }
            }
        }
        stage ('Publish docker images') {
            steps {
                echo 'Starting to publish docker images into docker registry'
                script {
                    withDockerRegistry([ credentialsId: 'dockerhub-credentials', url: '' ]) {
                        sh 'docker compose -f kafka-avro-ms-docker-compose.yml push'
                    }
                }
            }
        }
        stage ('Run the stack'){
            steps {
                echo 'Start running microservices containers of the application'
                script {
                    sh 'docker compose -f kafka-avro-ms-docker-compose.yml up -d'
                }
            }
        }
    }
}