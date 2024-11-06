## Quotation Service
Quotation Service
### Build System
* Gradle 

### Docker
```
  registry.turnkeyafrica.com:5000/turnquest/gis-quotation-service
```
To build image using gradle

```bash
    gradle jibDockerBuild 
```
To build and push image to registry

```bash
    gradle jib
```
### Ports
* 60232/tcp