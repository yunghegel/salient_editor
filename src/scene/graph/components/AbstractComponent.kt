package scene.graph.components

import org.yunghegel.gdx.scenegraph.component.Component
import scene.graph.GameObject

abstract class AbstractComponent<T : Any> (val obj: T, val go: GameObject): Component<T> {




    abstract fun update(obj: T, delta: Float)

    abstract fun render(obj: T, delta: Float)

    override fun update(delta: Float) {
        update(obj, delta)
    }

    override fun render(delta: Float) {
        render(obj, delta)
    }

}