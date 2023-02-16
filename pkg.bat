set mods=command pandora pocketshop showping stayonline teleport

md target\code-mods\

for %%i in (%mods%) do copy "%cd%\%%i\target\*.jar" "%cd%\target\code-mods\"

cd ./target && zip -rmv ./code-mods.zip ./code-mods/
