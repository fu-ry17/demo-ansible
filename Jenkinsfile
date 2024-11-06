node {
    stage('Checkout') {
        checkout scm
    }

    ANSIBLE_PATH = '/etc/ansible/pipeline'
    VAULT_PASS_PATH = '/var/lib/jenkins/.vault_pass'
    SSH_KEY = '/var/lib/jenkins/.ssh/id_ed25519'
    
    stage('Run Pipeline') {
            sh """
                ansible-playbook \\
                    -i ${ANSIBLE_PATH}/inventory.ini \\
                    ${ANSIBLE_PATH}/pipeline.yml \\
                    -e "branch_name=${env.BRANCH_NAME}" \\
                    -e "ansible_ssh_private_key_file=${SSH_KEY}" \\
                    -vvv
                    # --vault-password-file ${VAULT_PASS_PATH} \\
                    # --extra-vars '@${ANSIBLE_PATH}/vars/vault.yml'
            """
    }
} 