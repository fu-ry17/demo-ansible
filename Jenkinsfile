pipeline {
    agent any
    
    environment {
        ANSIBLE_PATH = '/etc/ansible/pipeline'
        VAULT_PASS_PATH = '/var/lib/jenkins/.vault_pass'
        SSH_KEY = '/var/lib/jenkins/.ssh/id_ed25519'
        ANSIBLE_HOST_KEY_CHECKING = 'False'
        ANSIBLE_SSH_RETRIES = '5'
        ANSIBLE_TIMEOUT = '60' 
        TARGET_SERVER = '10.0.3.85'
        ANSIBLE_STDOUT_CALLBACK = 'yaml'
        ANSIBLE_DISPLAY_SKIPPED_HOSTS = 'False'
        ANSIBLE_DISPLAY_OK_HOSTS = 'True'
        ANSIBLE_SHOW_CUSTOM_STATS = 'True'
    }
    
    stages {
        stage('Check Environment') {
            steps {
                sh '''
                    echo "Current user: $(whoami)"
                    echo "Which java: $(which java)"
                '''
            }
        }
        
        stage('SSH Test') {
            steps {
                script {
                    try {
                        sh '''
                            echo "Testing SSH connection..."
                            timeout 10 ssh -i ${SSH_KEY} \
                                -o ConnectTimeout=10 \
                                -o StrictHostKeyChecking=no \
                                -o UserKnownHostsFile=/dev/null \
                                yewa@${TARGET_SERVER} 'echo "SSH connection successful"'
                        '''
                    } catch (Exception e) {
                        echo "SSH test failed but continuing: ${e.getMessage()}"
                    }
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
                                -e \"ansible_connection_timeout=60\" \
                                -e \"ansible_ssh_common_args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null'\" \
                                --limit staging-server
                        """
                    } catch (Exception e) {
                        echo "Pipeline failed: ${e.getMessage()}"
                        sh 'ss -tulpn | grep :22 || true'
                        throw e
                    }
                }
            }
        }
    }
    
    post {
        failure {
            echo "Pipeline failed - check network connectivity and SSH access"
        }
        success {
            echo "Pipeline completed successfully"
        }
    }
}