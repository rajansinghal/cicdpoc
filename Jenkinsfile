pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    environment {
        AWS_ACCESS_KEY_ID = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
        EFS_RESOURCE_TEST_PATH = "${env.WORKSPACE}" + "/efs-resource"
        E2E_DOCKER_MACHINE_NAME = 'e2e'
        MYSQL_DATA_DIR = '/home/ubuntu/data'
        MYSQL_INIT_FILES_DIR = '/home/ubuntu/db-init-scripts'
    }
    stages {
        stage('Build') {
            steps {
               sh './gradlew --profile clean build'
            }
            post {
                always {
                    publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'backend/core/build/reports/tests/test', reportFiles: 'index.html', reportName: 'Tests Report', reportTitles: ''])
                    junit allowEmptyResults: true, testResults: '**/build/reports/**/*.xml'
                }
                success {
                    archiveArtifacts artifacts: '**/build/libs/**/*.jar', fingerprint: true, allowEmptyArchive: true
                }
            }
        }
        stage('Provision E2E Environment') {
            steps {
                sh "docker-machine rm $E2E_DOCKER_MACHINE_NAME -f"
                sh "docker-machine create --driver amazonec2 --amazonec2-open-port 9090  --amazonec2-instance-type t2.micro --amazonec2-region ap-south-1 $E2E_DOCKER_MACHINE_NAME"
                sh "eval \$(docker-machine env $E2E_DOCKER_MACHINE_NAME) && ./gradlew docker"
                sh "eval \$(docker-machine env $E2E_DOCKER_MACHINE_NAME) && docker-machine scp -r -d efs-resource/ $E2E_DOCKER_MACHINE_NAME:/home/ubuntu/efs-resources"
                sh "eval \$(docker-machine env $E2E_DOCKER_MACHINE_NAME) && docker-machine scp -r -d db/ $E2E_DOCKER_MACHINE_NAME:$MYSQL_INIT_FILES_DIR"
                sh "export EFS_RESOURCE_HOST_PATH=/home/ubuntu/efs-resources && " +
                        "export AWS_IP=\$(docker-machine ip $E2E_DOCKER_MACHINE_NAME) && " +
                        "export LOG_DIRECTORY=/home/ubuntu/logs && " +
                        "eval \$(docker-machine env $E2E_DOCKER_MACHINE_NAME) && " +
                        "docker-compose up -d"
            }
        }
        stage('Check E2E instance is up') {
            steps {
                sh "wget -O /dev/null --retry-connrefused --waitretry=10 --read-timeout=20 --timeout=15 -t 60 \$(docker-machine ip $E2E_DOCKER_MACHINE_NAME):9090"
            }
        }

    }

    post {
        success {
            mail to: 'rsinghal@xebia.com',
                    subject: "Pipeline Job Successful: ${currentBuild.fullDisplayName}",
                    body: "Your last pipeline job was successful :). You can view it here ${env.BUILD_URL}"
        }
        failure {
            mail to: 'rsinghal@xebia.com',
                    subject: "Pipeline Job Failed: ${currentBuild.fullDisplayName}",
                    body: "Something went wrong with the build :(. Please look at the logs here ${env.BUILD_URL}"
        }
    }
}
