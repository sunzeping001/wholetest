cmake_minimum_required(VERSION 3.18.1)
project(duer)
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
#set(CMAKE_BUILD_TYPE Debug)
#add_definitions("-Wall -g -O0")
#set(INCLUDE_LIST ${CMAKE_CURRENT_SOURCE_DIR}/include)
#message("--------------${INCLUDE_LIST}")
#set(SRC_LIST ${CMAKE_CURRENT_SOURCE_DIR}/src/duer.cpp)
#include_directories(
#        ${INCLUDE_LIST}
#)
#
#link_directories(
#        ${CMAKE_CURRENT_SOURCE_DIR}/../libs/armeabi-v7a
#)
#message("--------------${CMAKE_CURRENT_SOURCE_DIR}/../libs/armeabi-v7a")
#
#add_library(
#        duer SHARED
#        ${SRC_LIST}
#)
#
#TARGET_LINK_LIBRARIES(
#        duer
#        fork_engin
#        ${log-lib}
#)
set(PATH ${CMAKE_CURRENT_SOURCE_DIR}/..)
INCLUDE_DIRECTORIES(${PATH}/cpp/include)
add_library(
        duer
        SHARED
        src/duer.cpp
)
add_library(
        fork_engin SHARED IMPORTED
)

add_library(
        btvdplive SHARED IMPORTED
)

add_library(
        c++_shared SHARED IMPORTED
)

set_target_properties(
        fork_engin
        PROPERTIES IMPORTED_LOCATION
        ${PATH}/libs/${ANDROID_ABI}/libfork_engin.so
)

set_target_properties(
        btvdplive
        PROPERTIES IMPORTED_LOCATION
        ${PATH}/libs/${ANDROID_ABI}/libbtvdplive.so
)

set_target_properties(
        c++_shared
        PROPERTIES IMPORTED_LOCATION
        ${PATH}/libs/${ANDROID_ABI}/libc++_shared.so
)

add_library(
        live SHARED IMPORTED
)

set_target_properties(
        live
        PROPERTIES IMPORTED_LOCATION
        ${PATH}/libs/${ANDROID_ABI}/liblive.so
)

find_library(
        log-lib
        log
)
target_link_libraries(
        duer
        fork_engin
        btvdplive
        c++_shared
        live
        ${log-lib}
)

