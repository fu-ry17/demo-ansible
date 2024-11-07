pipeline {
    agent any
    
    environment {
        ANSIBLE_PATH = '/etc/ansible/pipeline'
        VAULT_PASS_PATH = '/var/lib/jenkins/.vault_pass'
        SSH_KEY = '/var/lib/jenkins/.ssh/id_ed25519'
        ANSIBLE_HOST_KEY_CHECKING = 'False'
        ANSIBLE_SSH_RETRIES = '5'
        ENVIRONMENT = 'staging'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Verify Variables') {
            steps {
                script {
                    sh """
                        echo "Environment: ${env.ENVIRONMENT}"
                    """
                }
            }
        }
        
        stage('Run Pipeline') {
            steps {
                script {
                    try {
                        sh """
                            ansible-playbook \
                                -i ${ANSIBLE_PATH}/inventory.ini \
                                ${ANSIBLE_PATH}/pipeline.yml \
                                -e \"branch_name=${env.BRANCH_NAME ?: 'main'}\" \
                                -e \"ansible_ssh_private_key_file=${SSH_KEY}\" \
                                -e \"ansible_user=yewa\" \
                                -e \"production_tag=${env.PRODUCTION_TAG}\" \
                                -e \"environment=${env.ENVIRONMENT}\" \
                                -e \"ansible_ssh_common_args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o ServerAliveInterval=30 -o ServerAliveCountMax=5'\" \
                                -vvv
                        """
                    } catch (Exception e) {
                        echo "Pipeline failed: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                sh """
                    echo "=== Variables Debug ==="
                    echo "BUILD_NUMBER: ${env.BUILD_NUMBER}"
                    echo "BRANCH_NAME: ${env.BRANCH_NAME}"
                    echo "ENVIRONMENT: ${env.ENVIRONMENT}"
                    echo "PRODUCTION_TAG: ${env.PRODUCTION_TAG}"
                """
            }
        }
        failure {
            echo "Pipeline failed - check variable definitions and ansible configuration"
        }
        success {
            echo "Pipeline completed successfully"
        }
    }
}