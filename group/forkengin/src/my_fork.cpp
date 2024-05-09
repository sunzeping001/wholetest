#include "my_fork.h"
#include <unistd.h>
#include "fork_log.h"
#include <jni.h>
#include <curl/curl.h>
#include <thread>

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
        std::string response_data;  // 用于存储响应数据
        curl_global_init(CURL_GLOBAL_DEFAULT);

        curl = curl_easy_init();
        if (curl) {
            curl_easy_setopt(curl, CURLOPT_URL, "https://www.qq.com");
            // 设置回调函数，用于接收响应数据
            curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
            curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);  // 设置是否跟随重定向
            curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response_data);  // 设置响应数据的写入位置
            curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
            curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0L);

            res = curl_easy_perform(curl);
            if (res != CURLE_OK) {
                LOGI("xsgg---curl_easy_perform() failed: %s", curl_easy_strerror(res));
            } else {
                // 请求成功，处理响应数据
                LOGI("xsgg---Response: %s", response_data.c_str());
            }
            // 清理工作
            curl_easy_cleanup(curl);
        }
        curl_global_cleanup();
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

bool run_system(const std::string &command) {
    pid_t status;
    status = system(command.c_str());

    if (status == -1) {
        LOGI("run system error, command: %s", command.c_str());
    } else {
        if (WIFEXITED(status)) {
            if (WEXITSTATUS(status) == 0) {
                LOGI("run system successfully, command: %s", command.c_str());
                return true;
            } else {
                LOGI("run system error, script exit code: %d", WEXITSTATUS(status));
            }
        } else {
            LOGI("exit status = [%d]", WEXITSTATUS(status));
        }
    }

    return false;
}

std::shared_ptr<std::thread> net_request_th;

int main(int argc, char *argv[]) {
    if (argc > 0) {
        for (int i = 0; i < argc; i++) {
            LOGI("param is: %s", argv[i]);
        }
    }
    std::string flag = argv[1];
    std::string path = argv[0];
    pid_t pid = getpid();
    LOGI("main process pid is: %d,is running", pid);
    net_request_th.reset(new std::thread(&work_run));
//    work_run();
    if (!flag.empty()) {
        std::string cmd = "sh -c " + path;
        run_system(cmd);
    }
    if (net_request_th->joinable()) {
        net_request_th->join();
    }
}