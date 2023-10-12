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
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-address/'){
                    sh 'mvn clean install' 
                }
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-company/'){
                    sh 'mvn clean install' 
                }
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-employee/'){
                    sh 'mvn clean install' 
                }
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-project/'){
                    sh 'mvn clean install' 
                }
                dir('Utility-Services/microservices-config-service/'){
                    sh 'mvn clean install' 
                }
                dir('Utility-Services/microservices-registry-service/'){
                    sh 'mvn clean install' 
                }
                dir('Utility-Services/gateway-service/'){
                    sh 'mvn clean install' 
                }
            }
            post {
                success {
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-address/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-company/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-employee/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-project/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                    dir('Utility-Services/microservices-config-service/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                    dir('Utility-Services/microservices-registry-service/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                    dir('Utility-Services/gateway-service/'){
                        archiveArtifacts '**/target/*.jar'
                    }
                }
            }
            
        }
        stage ('Test business microservices'){
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back.git']])
                echo 'starting run unit test'
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-address/'){
                    sh 'mvn test' 
                }
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-company/'){
                    sh 'mvn test' 
                }
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-employee/'){
                    sh 'mvn test' 
                }
                dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-project/'){
                    sh 'mvn test' 
                }
            }
            post {
                always {
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-address/'){
                        junit '**/target/surefire-reports/TEST-*.xml'
                    }
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-company/'){
                        junit '**/target/surefire-reports/TEST-*.xml'
                    }
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-employee/'){
                        junit '**/target/surefire-reports/TEST-*.xml'
                    }
                    dir('AEPC-Bs-Ms-Backend/k8s-kafka-avro-aepc-clean-archi-bs-micros-project/'){
                        junit '**/target/surefire-reports/TEST-*.xml'
                    }
                }
            }
        }
        stage ('Build appli stack docker images'){
            steps {
                echo 'Starting to build docker images of the application'
                script {
                    sh 'docker compose -f application-stack-compose.yml down'
                    sh 'docker compose -f application-stack-compose.yml build'
                    sh 'docker system prune -f'
                }
            }
        }
        stage ('Publish appli stack docker images') {
            steps {
                echo 'Starting to publish docker images into docker registry'
                script {
                    withDockerRegistry([ credentialsId: 'dockerhub-credentials', url: '' ]) {
                        sh 'docker compose -f application-stack-compose.yml push'
                    }
                }
            }
        }
    }
}