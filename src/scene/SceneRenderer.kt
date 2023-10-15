package scene

import app.Salient
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider
import com.badlogic.gdx.utils.viewport.ScreenViewport
import input.EditorCamera
import input.InputManager
import net.mgsx.gltf.scene3d.scene.Scene
import net.mgsx.gltf.scene3d.scene.SceneManager
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider
import scene.graph.SceneGraph

class SceneRenderer(val numBones:Int = 64,val numSpotLights:Int =8,val numPointLights:Int=8,val numDirLights:Int=1) : SceneManager(){



init {
        val config = PBRShaderProvider.createDefaultConfig()
        val depthConfig = PBRShaderProvider.createDefaultDepthConfig()
        config.numBones = numBones
        config.numDirectionalLights = numDirLights
        config.numPointLights = numPointLights
        config.numSpotLights = numSpotLights


        depthConfig.numBones = numBones
        depthConfig.numDirectionalLights = numDirLights
        depthConfig.numPointLights = numPointLights
        depthConfig.numSpotLights = numSpotLights

        setShaderProvider(PBRShaderProvider(config))
        setDepthShaderProvider(DepthShaderProvider(depthConfig))
    }

    val perspectiveCamera : PerspectiveCamera = Salient.inject()
    val orthographicCamera : OrthographicCamera = Salient.inject()
    val controller : EditorCamera = Salient.inject()
    val inputs: InputManager = Salient.inject()
    val viewport : ScreenViewport = Salient.inject()




     fun renderSceneContext(delta: Float,sceneContext:SceneContext ){
        if(batch==null) return
        val ctx = sceneContext as SceneContext
        ctx.grid.render(perspectiveCamera)




    }

     fun renderSceneGraph(delta: Float, sceneGraph: SceneGraph?) {

         sceneGraph?.root?.children?.forEach{ it.render(delta) }
    }

    fun render(delta: Float,sceneGraph: SceneGraph){
        sceneGraph.render(delta)
    }

    fun configureFromScene(scene: scene.Scene): SceneRenderer {

        setEnvironment(scene.sceneContext.env)
        setCamera(scene.perspectiveCamera)
        setAmbientLight(scene.sceneContext.config.globalLighting.ambientLight)
        skyBox = scene.sceneContext.skybox

        return this
    }

    fun setEnvironment(env: Environment): SceneRenderer {
        environment = env
        return this
    }

    private fun add(modelInstance: ModelInstance): SceneRenderer {
        addIfMissing(modelInstance)
        return this
    }

    fun add(vararg modelInstance: ModelInstance): SceneRenderer {
        for (m in modelInstance) add(m)
        return this
    }

    private fun remove(modelInstance: ModelInstance): SceneRenderer {
        if (renderableProviders.contains(modelInstance, true)) renderableProviders.removeValue(modelInstance, true)
        return this
    }

    fun remove(vararg modelInstance: ModelInstance): SceneRenderer {
        for (m in modelInstance) remove(m)
        return this
    }

    private fun add(scene: Scene): SceneRenderer {
        addIfMissing(scene)
        return this
    }

    fun add(vararg scene: Scene): SceneRenderer {
        for (s in scene) add(s)
        return this
    }

    private fun remove(scene: Scene): SceneRenderer {
        removeScene(scene)
        return this
    }

    fun remove(vararg scene: Scene): SceneRenderer {
        for (s in scene) remove(s)
        return this
    }

    private fun add(pointLight: PointLight): SceneRenderer {
        environment.add(pointLight)
        return this
    }

    fun add(vararg pointLight: PointLight): SceneRenderer {
        for (p in pointLight) add(p)
        return this
    }

    private fun remove(pointLight: PointLight): SceneRenderer {
        environment.remove(pointLight)
        return this
    }

    fun remove(vararg pointLight: PointLight): SceneRenderer {
        for (p in pointLight) remove(p)
        return this
    }

    private fun addIfMissing(modelInstance: ModelInstance): SceneRenderer {
        if (!renderableProviders.contains(modelInstance, true)) renderableProviders.add(modelInstance)
        return this
    }

    private fun addIfMissing(scene: Scene): SceneRenderer {
        if (!renderableProviders.contains(scene, true)) add(scene)
        return this
    }

}