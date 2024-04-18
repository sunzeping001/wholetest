cmake_minimum_required(VERSION 3.18.1)
project(fork_engin)
set(FORK_ENGIN fork_engin)
set(PATH ${CMAKE_CURRENT_SOURCE_DIR})
# 定义交叉编译链文件路径
set(ANDROID_NDK /home/szp/Android/Sdk/ndk/21.0.6113669)
# 定义工具链目标系统
set(CMAKE_SYSTEM_NAME Android)
# 定义工具链目标系统版本
set(CMAKE_SYSTEM_VERSION 21)  # Android 5.0
# 定义工具链目标架构
set(CMAKE_ANDROID_ARCH_ABI armeabi-v7a)
#-DCMAKE_TOOLCHAIN_FILE=/home/szp/Android/Sdk/ndk/21.0.6113669/build/cmake/android.toolchain.cmake)
set(CMAKE_MAKE_PROGRAM /home/szp/Android/Sdk/cmake/3.18.1/bin)
# 设置工具链编译器路径
set(CMAKE_C_COMPILER ${ANDROID_NDK}/toolchains/llvm/prebuilt/linux-x86_64/bin/clang)
set(CMAKE_CXX_COMPILER ${ANDROID_NDK}/toolchains/llvm/prebuilt/linux-x86_64/bin/clang++)

set(SRCS_LIST
        ${PATH}/src/my_fork.cpp
        )

set(INCLUDE_LIST ${PATH}/include)

include_directories(
        ${INCLUDE_LIST}
)

add_library(
        ${FORK_ENGIN} SHARED
        ${SRCS_LIST}
)

target_link_libraries(${FORK_ENGIN}
        ${log-lib}
        )