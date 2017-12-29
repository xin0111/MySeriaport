
set ndkPath=D:\Qt\android-ndk-r9b

REM project cpp file path
set jniDir=.
REM create so files path
set NDK_PROJECT_PATH=%jniDir%/../
REM Build file
%ndkPath%\ndk-build.cmd  "APP_BUILD_SCRIPT=%jniDir%/Android.mk" "NDK_APPLICATION_MK=%jniDir%/Application.mk"


