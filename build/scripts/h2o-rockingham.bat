@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  h2o-rockingham startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and H2O_ROCKINGHAM_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\h2o-rockingham-1.0-SNAPSHOT.jar;%APP_HOME%\lib\icafe-1.1-SNAPSHOT.jar;%APP_HOME%\lib\pdfbox-1.8.12.jar;%APP_HOME%\lib\log4j-api-2.1.jar;%APP_HOME%\lib\log4j-core-2.1.jar;%APP_HOME%\lib\jai_imageio-1.1.jar;%APP_HOME%\lib\slf4j-api-1.7.12.jar;%APP_HOME%\lib\fontbox-1.8.12.jar;%APP_HOME%\lib\jempbox-1.8.12.jar;%APP_HOME%\lib\commons-logging-1.1.1.jar

@rem Execute h2o-rockingham
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %H2O_ROCKINGHAM_OPTS%  -classpath "%CLASSPATH%" com.krashidbuilt.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable H2O_ROCKINGHAM_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%H2O_ROCKINGHAM_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
