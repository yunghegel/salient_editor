package scene.graph.components


import com.badlogic.gdx.graphics.g3d.environment.SpotLight
import scene.graph.GameObject

class SpotLightComponent(obj: SpotLight, go: GameObject) : AbstractComponent<SpotLight>(obj, go) {

    override fun update(obj: SpotLight, delta: Float) {

    }

    override fun render(obj: SpotLight, delta: Float) {

    }

}