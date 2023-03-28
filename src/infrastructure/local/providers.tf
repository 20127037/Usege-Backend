provider "aws" {
  region = local.region
  profile = "mobile_group_1_dev"
  s3_use_path_style = true
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true
  endpoints {
    dynamodb = "http://localhost:4566"
    s3 = "http://localhost:4566"
  }
}