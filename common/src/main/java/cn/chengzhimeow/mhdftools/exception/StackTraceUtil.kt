package cn.chengzhimeow.mhdftools.exception

object StackTraceUtil {
    @JvmStatic
    fun stackTraceToString(throwable: Throwable) = throwable.stackTraceToString()
}