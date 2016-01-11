#!/bin/bash

scriptPos=`dirname $0`

echo "stop vagrant vm"
cd "$scriptPos/../vagrant/ubuntu_psql" && vagrant halt