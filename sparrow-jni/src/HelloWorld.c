// HelloWorld.c
#include "com_sparrow_jdk_jni_HelloWorld.h"
#ifdef __cplusplus
extern "C"
{
#endif

JNIEXPORT jstring JNICALL Java_com_sparrow_jdk_jni_HelloWorld_hi(
        JNIEnv *env, jclass cls, jstring j_str)
{
    const char *c_str = NULL;
    c_str = (*env)->GetStringUTFChars(env, j_str, NULL);
    printf( " c  recv  :[%s]\n" , c_str);

    if (c_str == NULL)
    {
        printf("error.\n");
        return NULL;
    }
    (*env)->ReleaseStringUTFChars(env, j_str, c_str);
    return j_str;
}

#ifdef __cplusplus
}
#endif