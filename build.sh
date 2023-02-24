#!/bin/bash

mods="autoadvance camera core noloading pandora pocketshop showping stayonline teleport"

mkdir -p ./target/code-mods/
chmod -R 777 ./target/code-mods/
rm -rf ./target/code-mods/*.jar

for i in $mods;
do
cp ./$i/target/*.jar ./target/code-mods/
done

