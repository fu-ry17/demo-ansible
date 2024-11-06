node {
    stage('Checkout') {
        checkout scm
    }

    ANSIBLE_PATH = '/etc/ansible/pipeline'
    VAULT_PASS_PATH = '/var/lib/jenkins/.vault_pass'
    
    stage('Run Pipeline') {
        sh """
            # Debug SSH key
            ls -l /var/lib/jenkins/.ssh/id_ed25519
            
            # Run ansible with verbose output
            ANSIBLE_DEBUG=1 ansible-playbook \\
                -i ${ANSIBLE_PATH}/inventory.ini \\
                ${ANSIBLE_PATH}/pipeline.yml \\
                -e "branch_name=${env.BRANCH_NAME}" \\
                -vvv
                # --vault-password-file ${VAULT_PASS_PATH} \\
                # --extra-vars '@${ANSIBLE_PATH}/vars/vault.yml'
        """
    }
} 