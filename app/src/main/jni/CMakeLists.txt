project(mobileobjectdetection)

cmake_minimum_required(VERSION 3.4.1)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile-4.5.4-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20220420-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(
        mobileobjectdetection SHARED
        yolov5ncnn_jni.cpp
        scrfdncnn.cpp
        scrfd.cpp
        ndkcamera.cpp
) # should include all cpp files, according to jni

target_link_libraries(
    mobileobjectdetection
    ncnn
    ${OpenCV_LIBS}
    camera2ndk
    mediandk
    jnigraphics
)
