//javah -jni -d ../../../jni  com.sparrow.jdk.jni.HelloWorld
//不要重命名，c实现与java 的包名要一致
#include <jni.h>

#ifndef _Included_com_sparrow_jdk_jni_HelloWorld
#define _Included_com_sparrow_jdk_jni_HelloWorld
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_sparrow_jdk_jni_HelloWorld_hi
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif
