#!/bin/bash

mods="command pandora pocketshop showping stayonline teleport"

mkdir -p ./target/code-mods/
chmod -R 777 ./target/code-mods/
rm -rf ./target/code-mods/*.jar

for i in $mods;
do
cp ./$i/target/*.jar ./target/code-mods/
done

cd ./target && zip -rmv ./code-mods.zip ./code-mods/