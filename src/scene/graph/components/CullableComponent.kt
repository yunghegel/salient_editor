package scene.graph.components

import scene.graph.GameObject


class CullableComponent(obj: Boolean,  go: GameObject) : AbstractComponent<Boolean>(obj, go) {

    override fun update(obj: Boolean, delta: Float) {

    }

    override fun render(obj: Boolean, delta: Float) {

    }

}