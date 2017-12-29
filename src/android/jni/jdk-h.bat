
set jdkPath=D:\Qt\jdk1.8.0_144\bin

REM header name
set outname=SerialPort
REM source path
set outfilepath=.
REM java file : src+package+class
set classpath=..\src
set package=com.example.myserialport.serialport
set class=SerialPort
REM cmd
%jdkPath%\javah -o %outfilepath%\%outname%.h -jni -classpath %classpath% %package%.%class%
REM todo : change the source function names