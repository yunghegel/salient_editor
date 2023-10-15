package events

import app.Salient
import assets.Asset
import org.yunghegel.gdx.utils.console.ConsoleUtils
import project.Project
import scene.Scene
import scene.graph.GameObject
import java.text.SimpleDateFormat
import java.util.*


open class Event()  {
    var record :String
    fun  time() : String {
        return (SimpleDateFormat("HH:mm:ss").format(Date()).toString())
    }
    var desc: String = " "
    var msg: String ?= null
    init {
        var src =ConsoleUtils.getCallingStackTraceElement(Event.Listener::class.java).toString()
        val ste = Thread.currentThread().stackTrace[2]
        val callerClass = ste.getClassName()
        val callerMethod = ste.methodName
        record = String.format("[%s] : %s", time(), src)
    }


    var listener: DefaultLogger = Salient.eventLogger




    init {
        listener.eventCreated(event = this)
    }

    fun post() {
        listener.eventPosted(event = this)
    }

    fun handle(): Boolean {
        return listener.eventHandled(event = this)
    }

    interface Listener {
        fun eventCreated(event: Event)
        fun eventPosted(event: Event)
        fun eventHandled(event: Event): Boolean
    }
}

enum class Type {
    IO,NATIVE,PROJECT,SCENE
}

open class ProjectEvent(val project: Project) : Event() {
    var type: Type = Type.PROJECT
}

open class SceneEvent(val scene: Scene) : Event() {
    var type: Type = Type.SCENE
}

open class NativeEvent : Event() {
    var type: Type = Type.NATIVE
}

open class GameObjectEvent(val gameObject: GameObject) : Event() {
    var type: Type = Type.SCENE
}

open class IOEvent : Event() {
    var type: Type = Type.IO
}

open class AssetEvent(val asset: Asset<*>) : Event() {
    var type: Type = Type.IO
}

open class LifecycleEvent() : Event() {
    var type: Type = Type.IO
}



