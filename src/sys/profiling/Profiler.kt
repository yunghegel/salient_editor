package sys.profiling

import java.lang.reflect.Method

@Suppress("UNCHECKED_CAST")
class Profiler(val obj: Any) {



    companion object Factory {



    }

}

object Performance {

}

class Metric(val name: String,val type: Class<*>,val method: Method) {

    var start: Long = 0
    var end: Long = 0
    var duration: Long = 0
    var calls: Int = 0

    fun start() {
        start = System.nanoTime()
    }

    fun end() {
        end = System.nanoTime()
        duration = end - start
        calls++
    }

}



