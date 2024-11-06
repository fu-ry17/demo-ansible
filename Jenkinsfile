node {
    stage('Checkout') {
        checkout scm
    }

    ANSIBLE_PATH = '/etc/ansible/pipeline'
    
    stage('Run Pipeline') {
        // First ensure SSH key has correct permissions
        sh """
            sudo chown jenkins:jenkins /var/lib/jenkins/.ssh/id_ed25519
            sudo chmod 600 /var/lib/jenkins/.ssh/id_ed25519
            
            # Test SSH connection first
            ssh -i /var/lib/jenkins/.ssh/id_ed25519 -o StrictHostKeyChecking=no ubuntu@10.0.3.85 'echo "SSH connection successful"'
            
            # Run ansible playbook
            ansible-playbook \\
                -i ${ANSIBLE_PATH}/inventory.ini \\
                ${ANSIBLE_PATH}/pipeline.yml \\
                -e "branch_name=${env.BRANCH_NAME}"
        """
    }
} 