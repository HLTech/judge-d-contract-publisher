@echo off

set CMD_LINE_ARGS=%*
set CP_HOME=%~dp0..\
set JAVA=%JAVA_HOME%\bin\java


if not "%JAVA_HOME%" == "" goto SET_CLASSPATH

set JAVA=java
echo JAVA_HOME is not set, unexpected results may occur.
echo Set JAVA_HOME to the directory of your local JDK to avoid this message.

:SET_CLASSPATH

set CLASSPATH=%CP_HOME%lib\*
set CP_CMD_LINE_ARGS=%CMD_LINE_ARGS%

:START
set COMMAND="%JAVA%" -classpath "%CLASSPATH%"  com.hltech.contracts.judged.publisher.ContractPublisher %CP_CMD_LINE_ARGS%


@echo on
@echo +--------------------------------------------
@echo  JAVA_HOME=%JAVA_HOME%
@echo  CP_HOME=%CP_HOME%
@echo  CP_CMD_LINE_ARGS=%CP_CMD_LINE_ARGS%
@echo  COMMAND=%COMMAND%
@echo +--------------------------------------------

@echo off

cmd /c "cd /d %CP_HOME% && %COMMAND%"

