pipeline {
    agent any
    
    environment {
        ANSIBLE_PATH = '/etc/ansible/pipeline'
        VAULT_PASS_PATH = '/var/lib/jenkins/.vault_pass'
        SSH_KEY = '/var/lib/jenkins/.ssh/id_ed25519'
        ANSIBLE_HOST_KEY_CHECKING = 'False'
        ANSIBLE_SSH_RETRIES = '5'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Verify Connection') {
            steps {
                script {
                    sh '''
                        # Quick connection test to first server
                        echo "Testing SSH connection..."
                        ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no yewa@10.0.3.74 'echo "SSH connection successful"' || echo "SSH connection failed but continuing..."
                        
                        # Verify ansible inventory
                        ansible all -i ${ANSIBLE_PATH}/inventory.ini --list-hosts
                    '''
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
                                -e \"ansible_ssh_common_args='-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o ServerAliveInterval=30 -o ServerAliveCountMax=5'\" \
                                -vvv
                        """
                    } catch (Exception e) {
                        echo "Pipeline failed: ${e.getMessage()}"
                        
                        // Collect connection diagnostics
                        sh '''
                            echo "=== Connectivity Test ==="
                            ping -c 1 10.0.3.74 || true
                            ping -c 1 10.0.3.85 || true
                            ping -c 1 10.0.4.212 || true
                            
                            echo "=== SSH Test ==="
                            ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no -vvv yewa@10.0.3.74 'exit' 2>&1 || true
                        '''
                        
                        throw e
                    }
                }
            }
        }
    }
    
    post {
        failure {
            echo "Pipeline failed - check SSH connectivity and server availability"
        }
        success {
            echo "Pipeline completed successfully"
        }
    }
}