#include "my_fork.h"
#include <unistd.h>
#include "fork_log.h"
#include <jni.h>
#include <curl/curl.h>

size_t WriteCallback(void *contents, size_t size, size_t nmemb, void *userp) {
    std::string result = std::string((char *) contents, size * nmemb);
//    std::cout << "Response received: " << result << std::endl;
    LOGI("Response received: %s\n", result.c_str());
    // wirte_log(result);
    return size * nmemb;
}

void request() {
    while (1) {
        CURL *curl;
        CURLcode res;

        curl_global_init(CURL_GLOBAL_DEFAULT);

        curl = curl_easy_init();
        if (curl) {
            curl_easy_setopt(curl, CURLOPT_URL, "http://qq.com");

            // 设置回调函数，用于接收响应数据
            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);

            res = curl_easy_perform(curl);

            // 清理工作
            curl_easy_cleanup(curl);
        }
        curl_global_cleanup();
//        sleep(2);
    }
}

std::string work_run() {
    std::string name = "hello";
    while (true) {
//        pid_t pid = getpid();
//        LOGI("pid is: %d, is running", pid);
        request();
        sleep(1);
    }
    return name;
}

int main() {
    pid_t pid = getpid();
    LOGI("main process pid is: %d,is running", pid);
    work_run();
}