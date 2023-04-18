#!/usr/bin/env bash

aws dynamodb batch-write-item --endpoint-url http://localhost:4566 --request-items file://storage_plan.json