#!/bin/bash

scriptPos=`dirname $0`

echo "start vagrant vm"
cd "$scriptPos/../vagrant/ubuntu_psql" && vagrant up