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

resource "aws_dynamodb_table" "userAlbumsTable" {
  name                        = "userAlbums"
  deletion_protection_enabled = false
  hash_key                    = "userId"
  #id                          = "userFiles"
  range_key                   = "name"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "userId"
    type = "S"
  }
  attribute {
    name = "name"
    type = "S"
  }
  point_in_time_recovery {
    enabled = false
  }
}

resource "aws_dynamodb_table" "userFilesInAlbumTable" {
  name                        = "userFilesInAlbum"
  deletion_protection_enabled = false
  hash_key                    = "userId"
  range_key                   = "updated"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "userId"
    type = "S"
  }
  attribute {
    name = "updated"
    type = "S"
  }
  attribute {
    name = "albumName"
    type = "S"
  }
  attribute {
    name = "fileName"
    type = "S"
  }
  local_secondary_index {
    name            = "album-name-index"
    projection_type = "ALL"
    range_key       = "albumName"
  }
  local_secondary_index {
    name            = "file-name-index"
    projection_type = "ALL"
    range_key       = "fileName"
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
  range_key                   = "fileName"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "userId"
    type = "S"
  }
  attribute {
    name = "updated"
    type = "S"
  }
  attribute {
    name = "fileName"
    type = "S"
  }

  local_secondary_index {
    name            = "updated-index"
    projection_type = "ALL"
    range_key       = "updated"
  }

  point_in_time_recovery {
    enabled = false
  }
}

resource "aws_dynamodb_table" "userDeletedFilesTale" {
  name                        = "userDeletedFiles"
  deletion_protection_enabled = false
  hash_key                    = "userId"
  #id                          = "userFiles"
  range_key                   = "fileName"
  read_capacity               = 20
  write_capacity              = 20
  stream_enabled              = false

  attribute {
    name = "userId"
    type = "S"
  }
  attribute {
    name = "updated"
    type = "S"
  }
  attribute {
    name = "fileName"
    type = "S"
  }

  local_secondary_index {
    name            = "updated-index"
    projection_type = "ALL"
    range_key       = "updated"
  }

  point_in_time_recovery {
    enabled = false
  }
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
}

resource "aws_s3_bucket" "fileStorage" {
  bucket              = "usege"
  object_lock_enabled = false
}