#!/usr/bin/env bash
mkdir ../SQLScript
mv ../SQLScript/MiniLibrary.sql ../SQLScript/MiniLibrary.sql.back
mv ../SQLScript/wordpress.sql ../SQLScript/wordpress.sql.back
rm -rf ../SQLScript/wordpress.sql
rm -rf ../SQLScript/MiniLibrary.sql
mysqldump -u test MiniLibrary > ../SQLScript/MiniLibrary.sql
mysqldump -h 127.0.0.1 -u wordpress -p wordpress > ../SQLScript/wordpress.sql

