node {
    stage('Checkout') {
        checkout scm
    }

    ANSIBLE_PATH = '/etc/ansible/pipeline'
    
    stage('Run Pipeline') {
        // Use Jenkins' built-in credential handling
        wrap([$class: 'BuildUser']) {
            sh """
                # Run ansible playbook
                ansible-playbook \\
                    -i ${ANSIBLE_PATH}/inventory.ini \\
                    ${ANSIBLE_PATH}/pipeline.yml \\
                    -e "branch_name=${env.BRANCH_NAME}"
            """
        }
    }
} 