package scene.graph.components

import scene.graph.GameObject
import scene.graph.PrimitiveModel

class PrimitiveComponent(primitive: PrimitiveModel, go: GameObject) : AbstractComponent<PrimitiveModel>(primitive, go) {


    override fun update(obj: PrimitiveModel, delta: Float) {
    }

    override fun render(obj: PrimitiveModel, delta: Float) {
    }
}