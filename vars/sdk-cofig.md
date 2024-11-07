1 # Create .sdkman directory for Jenkins
sudo mkdir -p /var/lib/jenkins/.sdkman
sudo cp -r /root/.sdkman/* /var/lib/jenkins/.sdkman/

# Set ownership
sudo chown -R jenkins:jenkins /var/lib/jenkins/.sdkman


2 # Create or edit Jenkins environment file
sudo tee /etc/systemd/system/jenkins.service.d/sdkman.conf << EOF
[Service]
Environment="SDKMAN_DIR=/var/lib/jenkins/.sdkman"
Environment="PATH=/var/lib/jenkins/.sdkman/candidates/java/current/bin:${PATH}"
Environment="JAVA_HOME=/var/lib/jenkins/.sdkman/candidates/java/current"
EOF

3 # Reload systemd and restart Jenkins
sudo systemctl daemon-reload
sudo systemctl restart jenkins

# Create or edit Jenkins bash profile
sudo tee /var/lib/jenkins/.bash_profile << EOF
export SDKMAN_DIR="/var/lib/jenkins/.sdkman"
[[ -s "/var/lib/jenkins/.sdkman/bin/sdkman-init.sh" ]] && source "/var/lib/jenkins/.sdkman/bin/sdkman-init.sh"
EOF

# Set ownership
sudo chown jenkins:jenkins /var/lib/jenkins/.bash_profile

sudo su - jenkins -s /bin/bash
echo $JAVA_HOME
java -version