cmake_minimum_required(VERSION 3.18.1)
project(fork_engin)
set(FORK_ENGIN fork_engin)
#set(CMAKE_CXX_STANDARD 11)
set(PATH ${CMAKE_CURRENT_SOURCE_DIR})
# 定义交叉编译链文件路径
#set(ANDROID_NDK /home/szp/Android/Sdk/ndk/22.1.7171670)
# 定义工具链目标系统
set(CMAKE_SYSTEM_NAME Android)
#set(CMAKE_CXX_FLAGS "-g -ggdb -O0 -std=c++11 -frtti -fexceptions")
# 定义工具链目标系统版本
#set(CMAKE_SYSTEM_VERSION 21)  # Android 5.0
#set(CMAKE_ANDROID_API_MIN 23)
#set(CMAKE_ANDROID_STL "c++_shared")
# 定义工具链目标架构
set(CMAKE_ANDROID_ARCH_ABI armeabi-v7a)
#-DCMAKE_TOOLCHAIN_FILE=/home/szp/Android/Sdk/ndk/21.0.6113669/build/cmake/android.toolchain.cmake)
set(CMAKE_MAKE_PROGRAM /home/szp/Android/Sdk/cmake/3.18.1/bin)

# 设置工具链编译器路径
set(CMAKE_C_COMPILER ${ANDROID_NDK}/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi23-clang)
set(CMAKE_C_FLAGS "-std=gnu11 -O0 -g -ggdb -fPIC")
set(CMAKE_CXX_COMPILER ${ANDROID_NDK}/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi23-clang++)
set(CMAKE_CXX_FLAGS "-std=c++11 -O0 -g -ggdb -fPIC")

add_definitions(
        -DANDROID_PLATFORM=android-21
        -DCMAKE_TOOLCHAIN_FILE=/home/szp/Android/Sdk/ndk/22.1.7171670/build/cmake/android.toolchain.cmake
)

set(SRCS_LIST
        ${PATH}/src/my_fork.cpp
        )

set(INCLUDE_LIST
        ${PATH}/include
        ${PATH}/include/curl
        ${PATH}/include/openssl)

include_directories(
        ${INCLUDE_LIST}
)

link_directories(
        ${PATH}/libs/armeabi-v7a
)

add_library(
        live SHARED IMPORTED
)

set_target_properties(
        live
        PROPERTIES IMPORTED_LOCATION
        ${PATH}/libs/${ANDROID_ABI}/liblive.so
)

add_library(
        ${FORK_ENGIN} SHARED
        ${SRCS_LIST}
)

#add_executable(
#        ${FORK_ENGIN}
#        ${SRCS_LIST}
#)

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log
        )
target_link_libraries(${FORK_ENGIN}
        ${log-lib}
#        pthread
        #        rt
        #        pthread
        #        stdc++
        libssl.a
        libcrypto.a
        libcurl.a
        libz.a
        live
        )