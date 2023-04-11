# Usege backend

## Prerequisites

### AWS setup
1. Download AWS Cli: [Download link](https://awscli.amazonaws.com/AWSCLIV2.msi)
2. Test installation:
   `aws -version`
### AWS sso credential
1. Configure
```
aws configure --profile mobile_group_1_dev sso
```
Then insert
```
session name: mobile_group_1_dev
start URL: https://d-96677c108a.awsapps.com/stars
region: ap-southeast-1
scopes: sso:account:access
```
2. Then, a webpage will ask you to login using the provided **dev** account (Teams)
```
region: ap-southeast-1
output format: json
profile name: mobile_group_1_dev
```
3. Run the command to check aws profile `aws s3 ls --profile mobile_group_1_dev`

### Terraform setup
1. [Download Terraform](https://developer.hashicorp.com/terraform/downloads?product_intent=terraform)
2. Insert the installed terraform folder to **PATH** environment
> ![img.png](gitRes/install_terraform_0.png)
> ![img.png](gitRes/install_terraform_1.png)
3. Run the command `terraform -v`

### Initialize the project
1. Go to the [infrastructure/local](src/infrastructure/local) folder in cmd
2. Init terraform: `terraform init`

### Run services after first time initializing
1. Check aws using command `aws s3 ls --profile mobile_group_1_dev` is expired or not. If the credential is expired, try to get a new one [Credential step](#aws-sso-credential)
2. Pull new images: `docker compose pull`
3. Run the compose file: `docker compose up -d --remove-orphans`
4. Plan terraform: `terraform plan`
5. Apply terraform: `terraform apply -auto-approve`
6. (Optional) Remove out-dated images and volumes
```
docker image prune
docker volume prune
```