node {
    stage('Checkout') {
        checkout scm
    }
    
    stage('Run Pipeline') {
        dir('/etc/ansible/pipeline') {
            sh 'ansible-playbook -i inventory.ini pipeline.yml --vault-password-file ~/.vault_pass'
        }
    }
} 