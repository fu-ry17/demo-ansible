node {
    stage('Checkout') {
        checkout scm
    }

    ANSIBLE_PATH = '/etc/ansible/pipeline'
    VAULT_PASS_PATH = '/var/lib/jenkins/.vault_pass'
    
    stage('Run Pipeline') {
        sh """
            ansible-playbook \\
                -i ${ANSIBLE_PATH}/inventory.ini \\
                ${ANSIBLE_PATH}/pipeline.yml
                # --vault-password-file ${VAULT_PASS_PATH} \\
                # --extra-vars '@${ANSIBLE_PATH}/vars/vault.yml'
        """
    }
} 