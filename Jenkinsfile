pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    environment {
       
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
