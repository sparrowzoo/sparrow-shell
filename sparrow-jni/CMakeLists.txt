cmake_minimum_required(VERSION 3.13)
set(CMAKE_C_STANDARD 99)
project(HelloWorld)
set(CMAKE_C_STANDARD 99)
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})
include_directories(jni)
add_library(SparrowJni SHARED  src/HelloWorld.c)

