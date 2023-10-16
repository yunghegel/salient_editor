package scene.graph

import assets.AssetManager
import assets.types.ModelAsset
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.math.Matrix4
import scene.graph.components.*

class GameObjectProducer(val sceneGraph: SceneGraph, val name: String, val id: Int,  transform: Matrix4 = Matrix4()) {

    var gameObject: GameObject = GameObject(sceneGraph,name,id,transform)

    fun emitGameObject(): GameObject {
        return this.gameObject
    }

    fun applyComponent(component: AbstractComponent<*>) : GameObjectProducer {
        gameObject.addComponent(component)
        return this
    }

    fun applyComponents( vararg components:AbstractComponent<*>) : GameObjectProducer {
        components.forEach { gameObject.addComponent(it) }
        return this
    }

    fun applyScale( x: Float, y: Float, z: Float) : GameObjectProducer {
        gameObject.scale(x,y,z)
        return this
    }

    fun applyTranslation( x: Float, y: Float, z: Float) : GameObjectProducer {
        gameObject.translate(x,y,z)
        return this
    }

    fun applyRotation( x: Float, y: Float, z: Float, angle: Float) : GameObjectProducer {
        gameObject.rotate(x,y,z,angle)
        return this
    }

    fun addChildren(vararg children: GameObject) : GameObjectProducer {
        children.forEach { gameObject.addChild(it) }
        return this
    }

    fun childOf( parent: GameObject) : GameObjectProducer {
        parent.addChild(gameObject)
        return this
    }

}

object ComponentProducer {



}

object AssetImporer {



}

class ModelImporter(val model: Model, val asset: ModelAsset, val sceneGraph: SceneGraph, assetManager: AssetManager) {

    fun buildGo() : GameObject {
        val go = GameObject(sceneGraph, asset.meta.properties.name, SceneGraph.count, Matrix4())

        val modelComponent = ModelComponent(model,go)
        val materialComponents = model.materials.map { MaterialComponent(it,go) }
//        val meshComponents = model.meshes.map { MeshComponent(it,go) }
//        val textureComponents = Array<TextureComponent>()
//        val mapOfMap = hashMapOf<Material,Map<TextureAttribute,Texture>>()
//        for (material in model.materials) {
//            val map = MaterialUtils.getTexturesFromMaterial(material)
//            map.forEach { (t, u) -> textureComponents.add(TextureComponent(u,go)) }
//            mapOfMap.put(material,map)
//        }
        val modelI = ModelInstance(model)
        val transformC = TransformComponent(modelI.transform,go)
        val renderableC = RenderableComponent(modelI,go)
        for (material in modelI.materials) {
            val materialComponent = MaterialComponent(material,go)
            go.addComponent(materialComponent)
        }
        for(mesh in modelI.model.meshes) {
            val meshComponent = MeshComponent(mesh,go)
            go.addComponent(meshComponent)
        }
        go.addComponents(modelComponent, transformC, renderableC)
//        go.addComponents(*materialComponents.toTypedArray())
//        go.addComponents(*meshComponents.toTypedArray())
//        go.addComponents(*textureComponents.items)
        return go
    }

}

class PrimitiveImporter(val primitive: PrimitiveModel, val sceneGraph: SceneGraph) {

    fun buildGo() : GameObject {
        val go = GameObject(sceneGraph, primitive.primitive.name, SceneGraph.count, Matrix4())

        val modelComponent = ModelComponent(primitive.getModel(),go)
        val transformC = TransformComponent(primitive.getRenderable().transform,go)
        val renderableC = RenderableComponent(primitive.getRenderable(),go)
        for (material in primitive.getRenderable().materials) {
            val materialComponent = MaterialComponent(material,go)
            go.addComponent(materialComponent)
        }


        go.addComponents(modelComponent, transformC, renderableC)
        return go
    }

}