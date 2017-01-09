#!/usr/bin/expect
. conf.sh
set timeout 100
spawn ./backup.sh
expect "password"
send "${password}\r"
interact
