#include <jni.h>
#include <string>
#include <unistd.h>
#include <cstdlib>
#include <cstdio>
#include "fork_log.h"
#include "vdplive.h"
#include "my_fork.h"

//extern "C" JNIEXPORT jstring JNICALL
//Java_com_example_pluginlib_NativeLib_stringFromJNI(
//        JNIEnv* env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_pluginproject_duer_NativeStarter_start(JNIEnv *env, jobject thiz, jstring logPath) {
    // TODO: implement start()
//    std::string result = work_run();
    const char *log_path = env->GetStringUTFChars(logPath, 0);
    std::string log_work = std::string(log_path);
//    run(log_work);
    work_run();
    env->ReleaseStringUTFChars(logPath, log_path);
    return env->NewStringUTF("hello");
}