set mods=autoadvance camera core lootfilter noloading pandora pocketshop showping stayonline teleport

md target\code-mods\

for %%i in (%mods%) do copy "%cd%\%%i\target\*.jar" "%cd%\target\code-mods\"

