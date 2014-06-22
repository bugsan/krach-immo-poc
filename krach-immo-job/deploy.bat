@echo off

set CLASSPATH=
set PATH=%JAVA_HOME%\bin;%GAE_HOME%\bin

call appcfg.cmd backends target/krach-immo-job-0.0.1-SNAPSHOT update job

pause