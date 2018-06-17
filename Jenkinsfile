pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    environment {

        MYSQL_ROOT_PASSWORD = ''
        MYSQL_ALLOW_EMPTY_PASSWORD = 'yes'
        MYSQL_DATABASE = 'cicdpoc'
        SPRING_JPA_HIBERNATE_DDL = 'none'
        EFS_RESOURCE_TEST_PATH = "${env.WORKSPACE}" + "/efs-resource"
        E2E_DOCKER_MACHINE_NAME = 'e2e'
        SPRING_PROFILES_ACTIVE = 'docker'
        MYSQL_DATA_DIR = '/home/ubuntu/data'
        MYSQL_INIT_FILES_DIR = '/home/ubuntu/db-init-scripts'
    }
    stages {
        stage('Build') {
            steps {
                echo "spring active profile: " + " $SPRING_PROFILES_ACTIVE"
               sh './gradlew clean build docker'
               sh "docker-machine create --driver amazonec2 --amazonec2-open-port 8080  --amazonec2-instance-type t2.micro --amazonec2-region ap-south-1 $E2E_DOCKER_MACHINE_NAME"
                               sh "eval \$(docker-machine env $E2E_DOCKER_MACHINE_NAME) && ./gradlew docker"
                               sh "export "export AWS_IP=\$(docker-machine ip $E2E_DOCKER_MACHINE_NAME) && " +
                                       "export LOG_DIRECTORY=/home/ubuntu/logs && " +
                                       "eval \$(docker-machine env $E2E_DOCKER_MACHINE_NAME) && " +
                                       "docker-compose up -d"
            }

        }
        stage('Check E2E instance is up') {
            steps {
                sh "wget -O /dev/null --retry-connrefused --waitretry=10 --read-timeout=20 --timeout=15 -t 60 \$(docker-machine ip $E2E_DOCKER_MACHINE_NAME):8080/index.html"
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
