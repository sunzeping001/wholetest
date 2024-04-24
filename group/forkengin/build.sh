cd ./build
rm -rf *
cmake .. \
    -DCMAKE_TOOLCHAIN_FILE=/home/szp/Android/Sdk/ndk/22.1.7171670/build/cmake/android.toolchain.cmake \
    -DANDROID_PLATFORM=android-22 \
    -DCMAKE_C_COMPILER=/home/szp/Android/Sdk/ndk/22.1.7171670/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi23-clang \
    -DCMAKE_CXX_COMPILER=/home/szp/Android/Sdk/ndk/22.1.7171670/toolchains/llvm/prebuilt/linux-x86_64/bin/armv7a-linux-androideabi23-clang++
#    -DCMAKE_CXX_FLAGS="-fPIC" \
#    -DCMAKE_C_FLAGS="-fPIC"
make