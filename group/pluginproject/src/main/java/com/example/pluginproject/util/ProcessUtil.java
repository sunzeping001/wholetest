package com.example.pluginproject.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProcessUtil {

    private static final String TAG = "ProcessUtil";

    /**
     * 运行二进制程序
     *
     * @param context
     * @param fileName 二进制文件的路径
     */
    public static void runBinary(Context context, String fileName, String... params) {
        try {
//            String fileName = context.getFilesDir().getAbsolutePath() + File.separator + name;
            Log.i(TAG, fileName);
            File file = new File(fileName);
            if (file.canExecute()) {
                Log.d(TAG, fileName + "have execute permission");
            } else {
                Log.e(TAG, fileName + "have not execute permission");
                if (file.setExecutable(true)) {
                    Log.d(TAG, fileName + "have no permission, set permission success");
                } else {
                    Log.e(TAG, fileName + "have no permission, set permission error");
                }
            }
            StringBuffer sb = new StringBuffer();
            for (String param : params) {
                sb.append(param + " ");
            }
            Process process = Runtime.getRuntime().exec("./" + fileName + sb.toString());
            int pid = getProcessId(process);
            Log.i(TAG, "run: binary pid is: " + pid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取进程的 PID
     *
     * @param process
     * @return
     */
    private static int getProcessId(Process process) {
        try {
            // 获取进程信息
            Field field = process.getClass().getDeclaredField("pid");
            field.setAccessible(true);
            return field.getInt(process);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取当前进程的所有子进程的 PID
     * 获取的不太准确，会有多余数据展示
     *
     * @param parentProcessId
     * @return
     */
    @Deprecated
    public static List<Integer> getChildProcessIds(int parentProcessId) {
        List<Integer> childProcessIds = new ArrayList<>();
        File taskDir = new File("/proc/" + parentProcessId + "/task");
        if (taskDir.exists() && taskDir.isDirectory()) {
            File[] taskDirs = taskDir.listFiles();
            if (taskDirs != null) {
                for (File task : taskDirs) {
                    if (task.isDirectory()) {
                        try {
                            int childPid = Integer.parseInt(task.getName());
                            childProcessIds.add(childPid);
                        } catch (NumberFormatException e) {
                            // 忽略无效的子目录名称
                        }
                    }
                }
            }
        }
        return childProcessIds;
    }

    /**
     * 获取当前app进程中所有的进程信息
     *
     * @param pid 父进程的pid
     */
    public static List<Integer> getAllProcessInfo(int pid, boolean isPrint) {
        List<Integer> processList = new ArrayList<>();
        try {
            ProcessBuilder processBuilder;
            //查看进程归属测试用的
            if (isPrint) {
                processBuilder = new ProcessBuilder("sh", "-c", "ps -ef | awk -F \" \" '{print $2,$3,$7,$8}'");
            } else {
                processBuilder = new ProcessBuilder("sh", "-c", "ps -ef | awk -F \" \" '{print " +
                        "$2}'");
            }
            Log.d(TAG, "cmd is " + processBuilder.command().toString());
            Process process = processBuilder.start();
            // 读取命令执行的输出
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            String line;
            while ((line = lineNumberReader.readLine()) != null) {
                // 处理命令执行的输出
                Log.i(TAG, line);
                try {
                    if (isPrint) {
                        int result = Integer.parseInt(line);
                        if (result != pid) {
                            processList.add(result);
                        }
                    }
                } catch (NumberFormatException ex) {
                    Log.e(TAG, "is not number");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return processList;
    }

    /**
     * 停止所有进程
     * @param processList
     */
    public static void stopAllProcess(List<Integer> processList) {
        try {
            for (int i = 0; i < processList.size(); i++) {
                ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9",
                        Integer.toString(processList.get(i)));
                Process process = processBuilder.start();
                Log.d(TAG, "stopAllProcess: pid is: " + processList.get(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
