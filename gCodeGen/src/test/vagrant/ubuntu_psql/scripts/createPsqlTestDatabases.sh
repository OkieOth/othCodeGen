#!/bin/bash

scriptPos=`dirname $0`

echo "create test databases from generated sql scripts"
cd "$scriptPos/../vagrant/ubuntu_psql" && vagrant up
