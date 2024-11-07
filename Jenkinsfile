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
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('SSH Test') {
            steps {
                script {
                    try {
                        sh '''
                            echo "Testing direct SSH connection..."
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
                            # Set extended Ansible SSH timeout
                            export ANSIBLE_TIMEOUT=60
                            export ANSIBLE_SSH_TIMEOUT=60
                            export ANSIBLE_CONNECT_TIMEOUT=60
                            
                            ansible-playbook \
                                -i ${ANSIBLE_PATH}/inventory.ini \
                                ${ANSIBLE_PATH}/pipeline.yml \
                                -e \"branch_name=${env.BRANCH_NAME ?: 'main'}\" \
                                -e \"ansible_ssh_private_key_file=${SSH_KEY}\" \
                                -e \"ansible_user=yewa\" \
                                -e \"ansible_connection_timeout=60\" \
                                -e \"ansible_ssh_common_args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o ServerAliveInterval=30 -o ServerAliveCountMax=10 -o ConnectTimeout=60'\" \
                                --limit staging-server \
                                -vvv
                        """
                    } catch (Exception e) {
                        echo "Pipeline failed: ${e.getMessage()}"
                        sh '''
                            echo "=== Final Diagnostics ==="
                            ss -tulpn | grep :22 || true
                            journalctl -u sshd -n 50 || true
                        '''
                        throw e
                    }
                }
            }
        }
    }
    
    post {
        always {
            script {
                sh '''
                    echo "=== Environment Information ==="
                    env | grep -i ansible || true
                    echo "=== SSH Configuration ==="
                    ssh -V
                '''
            }
        }
        failure {
            echo "Pipeline failed - check network connectivity and SSH access"
        }
        success {
            echo "Pipeline completed successfully"
        }
    }
}