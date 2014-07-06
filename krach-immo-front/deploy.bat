@echo off

set CLASSPATH=
set PATH=%JAVA_HOME%\bin;%GAE_HOME%\bin

call appcfg.cmd update target\krach-immo-front-0.0.1-SNAPSHOT

pause