#include <jni.h>
#include <string>
#include "my_fork.h"
#include <unistd.h>
#include <cstdlib>
#include <cstdio>
#include "fork_log.h"

//extern "C" JNIEXPORT jstring JNICALL
//Java_com_example_pluginlib_NativeLib_stringFromJNI(
//        JNIEnv* env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_pluginproject_duer_NativeStarter_start(JNIEnv *env, jobject thiz) {
    // TODO: implement start()
    std::string result = work_run();
    return env->NewStringUTF(result.c_str());
}