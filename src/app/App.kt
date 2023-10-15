package app

import com.badlogic.gdx.*
import com.badlogic.gdx.Gdx.*
import com.badlogic.gdx.graphics.GL20
import org.yunghegel.gdx.utils.StringUtils
import org.yunghegel.gdx.utils.console.ConsoleUtils
import sys.profiling.MemoryProfile
import sys.profiling.OpenGLProfile
import util.Vec2
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object App {

    val i: Input = input
    val f : Files = files
    val g : Graphics = graphics
    val a : Application = app
    val gl : GL20 = Gdx.gl

    fun key(keycode: Int): Boolean {
        return i.isKeyPressed(keycode)
    }

    fun keyDown(keycode: Int): Boolean {
        return i.isKeyPressed(keycode)
    }

    fun btn(button: Int): Boolean {
        return i.isButtonPressed(button)
    }

    fun btnDown(button: Int): Boolean {
        return i.isButtonPressed(button)
    }

    fun mouse(): Vec2 {
        return Vec2(i.x.toFloat(), i.y.toFloat())
    }

    fun pos(): Vec2 {
        return Vec2(i.x.toFloat(), i.y.toFloat())
    }

    fun delta(): Vec2 {
        return Vec2(i.deltaX.toFloat(), i.deltaY.toFloat())
    }

    val memoryProfile: MemoryProfile = MemoryProfile
    val glProfile: OpenGLProfile = OpenGLProfile

    object Profiler {

        fun delegateToProxy(clazz: Class<*>,obj:Any): Any {
            return obj
        }

    }

    inline fun <T: Any> T.perf(name: String,crossinline block: () -> Unit) {
        val caller = ConsoleUtils.getClassMethodLine(App.javaClass::class.java)
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        val time = (end - start) / 1000000f
        println(StringUtils.colorize("PERF: [$name took $time ms]", StringUtils.Ansi.PURPLE) + StringUtils.colorize(" $caller", StringUtils.Ansi.CYAN))
    }
    inline fun <T: Any> T.perf(crossinline block: T.()-> Unit, crossinline log: ()-> String,name: String?="UNAMED") {
        val caller = ConsoleUtils.getClassMethodLine(App.javaClass::class.java)
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        val time = (end - start) / 1000000f
        println(StringUtils.colorize("PERF: [${name} took $time ms]", StringUtils.Ansi.PURPLE) + StringUtils.colorize(" $caller", StringUtils.Ansi.CYAN))
        log()
    }

    inline fun <T: Any> T.code(crossinline codeBlock: T.() -> Unit) {
        codeBlock()
    }

    inline fun <T: Any> T.log(crossinline block: () -> String) {
        val msg = block()
        val caller = ConsoleUtils.getClassMethodLine(App.javaClass::class.java)
        println(StringUtils.colorize("LOG MSG: [${msg}]", StringUtils.Ansi.GREEN) + StringUtils.colorize(" $caller", StringUtils.Ansi.CYAN))
    }

    interface Callback {
        fun complete(func: () -> Unit = {}) = func()
    }

    abstract class Coroutine (val callbackFnc: () -> Unit) {

        val callback =  object :Callback {
            override fun complete(func: () -> Unit): Unit = func()
        }

        abstract fun runBlocked(callback: Callback) : Unit


    }

    suspend inline fun <T : Any> T.coroutine(crossinline block: Callback.(T) -> Unit,crossinline callback:()->Unit ): Unit {
       val onEnd = object : Callback {
            override fun complete(func: () -> Unit): Unit = func()
           }
        var continuation = suspendCoroutine<Unit> { t ->
            onEnd.block(this)
            t.resume(onEnd.complete())
        }

        block(onEnd,this)
        onEnd.complete()
    }

    suspend inline fun <T : Any> T.coroutine(crossinline block: Callback.(T) -> Unit): T {


        var continuation = suspendCoroutine<Unit> { t ->
            block(object : Callback {
                override fun complete(func: () -> Unit): Unit = func()
            },this)
                t.resume(Unit)
        }


        return this
    }
//    example

    class Example {

        init {}


    }











}