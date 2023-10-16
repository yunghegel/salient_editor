package scene

import app.Salient
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.crashinvaders.vfx.VfxManager
import com.crashinvaders.vfx.effects.FxaaEffect
import events.scene.GameObjectDeselectedEvent
import events.scene.GameObjectSelectedEvent
import input.EditorCamera
import input.InputManager
import io.Serializer
import ktx.collections.GdxArray
import org.yunghegel.gdx.scenegraph.scene3d.BaseScene
import org.yunghegel.gdx.ui.widgets.viewport.ViewportWidget
import project.Project
import scene.graph.GameObject
import scene.graph.SceneGraph
import scene.systems.SceneIterator
import tools.ToolManager

/**
 * Default subclass of the [BaseScene] class - this is the main scene class used by the editor.
 *
 * Render context (environment) and configuration is provided by the [SceneContext] class.
 * Handling of the actual rendering is done by the [SceneRenderer] class which is configured by the [SceneContext].
 *
 * Only required parameter is [project], a scene must be associated with a project.
 */
class Scene( var name: String = "New Scene", val project: Project, var uid : String = "-1") : BaseScene(name,project.sceneFolder.path()+'/'+name.plus(".scene")), ViewportWidget.Renderer,GameObjectSelectedEvent.Listener,GameObjectDeselectedEvent.Listener {




    var perspectiveCamera : PerspectiveCamera = Salient.inject()
    var orthographicCamera : OrthographicCamera = Salient.inject()
    var controller : EditorCamera = Salient.inject()
    var inputs: InputManager = Salient.inject()
    var viewport : ScreenViewport = Salient.inject()

    var modelBatch : ModelBatch
    var depthBatch : ModelBatch

    var sceneRenderer : SceneRenderer
    var sceneGraph : SceneGraph
    var sceneContext : SceneContext

    var sceneIterators: GdxArray<SceneIterator> = GdxArray()

    var selectedGameObjects: GdxArray<GameObject> = GdxArray()
    var selectedGameObject: GameObject? = null

    lateinit var effectsManager : VfxManager


    lateinit var data : SceneData

    init {
        sceneRenderer = SceneRenderer()
        sceneGraph = SceneGraph(this)
        sceneContext = SceneContext()

        sceneRenderer.configureFromScene(this)

        modelBatch = sceneRenderer.batch
        depthBatch = sceneRenderer.depthBatch


        effectsManager= VfxManager(Pixmap.Format.RGBA8888)
        effectsManager.addEffect(FxaaEffect())

        Salient.registerListener(this)
    }

    fun hasSelection(): Boolean {
        return selectedGameObjects.size > 0
    }


    override fun dispose() {
        sceneRenderer.dispose()
        sceneContext.dispose()
    }

    override fun update(delta: Float) {
        sceneGraph.update(delta)
        sceneRenderer.update(delta)
        Salient.compass.update()

        perspectiveCamera.update()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        sceneRenderer.render()
        sceneRenderer.renderSceneContext(delta,sceneContext)
        sceneRenderer.renderSceneGraph(delta,sceneGraph)
        controller.perspectiveCameraController.render()
        ToolManager.renderTool()
    }

    override fun resize(width: Int, height: Int) {

    }

   companion object {
       fun createDefault(project: Project): Scene {
           return Scene("Default Scene", project)
       }

   }

    fun save() {
        Serializer.serializeScene(this)
    }

    fun getRenderer(): ViewportWidget.Renderer {
        return ViewportRenderer(this)
    }

    class ViewportRenderer(val scene: Scene) : ViewportWidget.Renderer {
        override fun render(delta: Float) {
            scene.render(delta)
        }
    }

    override fun onGameObjectDeselected(event: GameObjectDeselectedEvent) {
        selectedGameObjects.removeValue(event.gameObject,true)
    }

    override fun onGameObjectSelected(event: GameObjectSelectedEvent) {
        if(!event.inclusive) {
            selectedGameObjects.clear()
        }
        selectedGameObjects.add(event.gameObject)
        selectedGameObject = event.gameObject
    }

}