terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.60.0"
    }
  }
}

resource "aws_dynamodb_table" "userInfoTable" {
  name                        = "userInfo"
  deletion_protection_enabled = false
  hash_key                    = "userId"
  #id                          = "userInfo"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "email"
    type = "S"
  }
  attribute {
    name = "userId"
    type = "S"
  }

  global_secondary_index {
    hash_key           = "email"
    name               = "email-index"
    non_key_attributes = []
    projection_type    = "KEYS_ONLY"
    read_capacity      = 20
    write_capacity     = 20
  }

  point_in_time_recovery {
    enabled = false
  }
}

resource "aws_dynamodb_table" "userFilesTable" {
  name                        = "userFiles"
  deletion_protection_enabled = false
  hash_key                    = "userId"
  #id                          = "userFiles"
  range_key                   = "fileId"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "fileId"
    type = "S"
  }
  attribute {
    name = "userId"
    type = "S"
  }
  attribute {
    name = "contentType"
    type = "S"
  }
  local_secondary_index {
    name            = "content-type-index"
    projection_type = "ALL"
    range_key       = "contentType"
  }

  point_in_time_recovery {
    enabled = false
  }

  depends_on = [aws_dynamodb_table.userInfoTable]
}


resource "aws_dynamodb_table" "storagePlanTable" {
  name                        = "storagePlans"
  deletion_protection_enabled = false
  hash_key                    = "name"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "name"
    type = "S"
  }
  point_in_time_recovery {
    enabled = false
  }

  provisioner "local-exec" {
    command = "bash populate_plan.sh"
  }

  depends_on = [aws_dynamodb_table.userFilesTable]
}

resource "aws_dynamodb_table" "paymentHistoriesTable" {
  name                        = "paymentHistories"
  deletion_protection_enabled = false
  hash_key                    = "userId"
  range_key                   = "planName"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "userId"
    type = "S"
  }
  attribute {
    name = "planName"
    type = "S"
  }
  point_in_time_recovery {
    enabled = false
  }
  depends_on = [aws_dynamodb_table.storagePlanTable]
}

resource "aws_s3_bucket" "fileStorage" {
  bucket                      = "usege"
  #bucket_domain_name          = "usege.s3.amazonaws.com"
  #bucket_regional_domain_name = "usege.s3.ap-southeast-1.amazonaws.com"
  #hosted_zone_id              = "Z3O0J2DXBE1FTB"
  #id                          = "usege"
  object_lock_enabled         = false
  #region                      = "ap-southeast-1"
}

#resource "aws_cognito_user_pool" "user_pool" {
#  auto_verified_attributes  = [
#    "email",
#  ]
#  deletion_protection       = "ACTIVE"
#  estimated_number_of_users = 0
#  mfa_configuration         = "OFF"
#  name                      = "Usege"
#  tags                      = {}
#  tags_all                  = {}
#  username_attributes       = [
#    "email",
#  ]
#
#  account_recovery_setting {
#    recovery_mechanism {
#      name     = "admin_only"
#      priority = 1
#    }
#  }
#
#  admin_create_user_config {
#    allow_admin_create_user_only = true
#  }
#
#  email_configuration {
#    email_sending_account = "COGNITO_DEFAULT"
#  }
#
#  password_policy {
#    minimum_length                   = 8
#    require_lowercase                = true
#    require_numbers                  = true
#    require_symbols                  = true
#    require_uppercase                = true
#    temporary_password_validity_days = 7
#  }
#
#  schema {
#    attribute_data_type      = "String"
#    developer_only_attribute = false
#    mutable                  = true
#    name                     = "email"
#    required                 = true
#
#    string_attribute_constraints {
#      max_length = "2048"
#      min_length = "0"
#    }
#  }
#
#  user_attribute_update_settings {
#    attributes_require_verification_before_update = [
#      "email",
#    ]
#  }
#
#  username_configuration {
#    case_sensitive = false
#  }
#
#  verification_message_template {
#    default_email_option = "CONFIRM_WITH_CODE"
#  }
#}
#
#resource "aws_cognito_user_pool_client" "user_pool_client" {
#  access_token_validity                         = 60
#  allowed_oauth_flows_user_pool_client          = false
#  auth_session_validity                         = 3
#  enable_propagate_additional_user_context_data = false
#  enable_token_revocation                       = true
#  explicit_auth_flows                           = [
#    "ALLOW_ADMIN_USER_PASSWORD_AUTH",
#    "ALLOW_CUSTOM_AUTH",
#    "ALLOW_REFRESH_TOKEN_AUTH",
#    "ALLOW_USER_SRP_AUTH",
#  ]
#  id_token_validity                             = 60
#  name                                          = "UsegeBackend"
#  prevent_user_existence_errors                 = "ENABLED"
#  read_attributes                               = [
#    "address",
#    "birthdate",
#    "email",
#    "email_verified",
#    "family_name",
#    "gender",
#    "given_name",
#    "locale",
#    "middle_name",
#    "name",
#    "nickname",
#    "phone_number",
#    "phone_number_verified",
#    "picture",
#    "preferred_username",
#    "profile",
#    "updated_at",
#    "website",
#    "zoneinfo",
#  ]
#  refresh_token_validity                        = 30
#  user_pool_id                                  = aws_cognito_user_pool.user_pool.id
#  write_attributes                              = [
#    "address",
#    "birthdate",
#    "email",
#    "family_name",
#    "gender",
#    "given_name",
#    "locale",
#    "middle_name",
#    "name",
#    "nickname",
#    "phone_number",
#    "picture",
#    "preferred_username",
#    "profile",
#    "updated_at",
#    "website",
#    "zoneinfo",
#  ]
#
#  token_validity_units {
#    access_token  = "minutes"
#    id_token      = "minutes"
#    refresh_token = "days"
#  }
#
#}
#
#resource "aws_cognito_identity_pool" "identity_pool" {
#
#  allow_classic_flow               = false
#  allow_unauthenticated_identities = false
#  identity_pool_name               = "Cognito Usege Identity"
#  openid_connect_provider_arns     = []
#  saml_provider_arns               = []
#  supported_login_providers        = {}
#  tags                             = {}
#  tags_all                         = {}
#
#  cognito_identity_providers {
#    client_id               = aws_cognito_user_pool_client.user_pool_client.id
#    provider_name           = aws_cognito_user_pool.user_pool.endpoint
#    server_side_token_check = false
#  }
#
#}