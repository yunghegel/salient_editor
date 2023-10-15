package app

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ScreenViewport
import events.DefaultLogger
import events.Event
import events.EventBus
import input.EditorCamera
import input.InputManager
import io.DirectoryMappings
import io.FileService
import io.Serializer
import ktx.inject.Context
import ktx.inject.register
import ktx.reflect.Reflection
import ktx.scene2d.Scene2DSkin
import org.yunghegel.gdx.gizmo.core.utility.CompassGizmo
import project.Project
import project.ProjectManager
import sys.Natives
import sys.WindowSubscriber
import ui.Gui
import util.DebugDrawer


object Salient {
    val context = Context()
    val eventBus: EventBus;

    val modelBatch: ModelBatch
    val depthBatch: ModelBatch
    val spriteBatch: SpriteBatch
    val shapeRenderer: ShapeRenderer
    val debugDrawer: DebugDrawer
    val font: BitmapFont
    val viewport: ScreenViewport
    var cameraController: EditorCamera
    var camera: PerspectiveCamera
    var orthoCam: OrthographicCamera
    val ui : Gui
    val natives: Natives
    val inputManager : InputManager
    var projectManager: ProjectManager = ProjectManager()
    var windowSubscriber: WindowSubscriber = WindowSubscriber()
    var eventLogger: DefaultLogger = DefaultLogger()
    var compass: CompassGizmo




init {
    eventBus = EventBus()
    modelBatch = ModelBatch()
    depthBatch = ModelBatch()
    camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    viewport = ScreenViewport(camera)
    viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    orthoCam = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    cameraController = EditorCamera(camera, orthoCam, viewport)

    ui = Gui()
    Scene2DSkin.defaultSkin = ui.skin
    natives = Natives()
    spriteBatch = SpriteBatch()
    shapeRenderer = ShapeRenderer()
    debugDrawer = DebugDrawer()
    font = ui.skin.getFont("default")
    inputManager = InputManager()
    inputManager.addProcessor(ui.stage)
    inputManager.addProcessor(cameraController.perspectiveCameraController)
    inputManager.addProcessor(ui.viewportWidget.uiStage)
    inputManager.set()

    compass = CompassGizmo(inputManager.inputMultiplexer, modelBatch, camera, viewport,-1)
    @OptIn(Reflection::class)
    context.register {
        bindSingleton(projectManager)

        bindSingleton(camera)
        bindSingleton(orthoCam)
        bindSingleton(viewport)
        bindSingleton(cameraController)
        bindSingleton(ui)
        bindSingleton(natives)
        bindSingleton(spriteBatch)
        bindSingleton(shapeRenderer)
        bindSingleton(debugDrawer)
        bindSingleton(font)
        bindSingleton(inputManager)
        bindSingleton(eventLogger)
        bindSingleton(depthBatch)
    }


    eventBus.register(windowSubscriber)
    eventBus.register(eventLogger)





}

    inline fun <reified Type : Any> inject(): Type = context.inject()

    inline fun keyVal(pos: Vector2, key: String, value: String) {
        debugDrawer.keyValue(pos, key, value,font)
    }

    inline fun keyVal(x:Float,y:Float, key: String, value: String) {
        debugDrawer.keyValue(Vector2(x,y), key, value,font)
    }

    fun initializeProjectContext(){
        var projs = FileService.listProjects()
        if(projs.isEmpty()) {
            firstTimeSetup()
        }
        else{
            projectManager.initialize(projs[0])
        }
    }

    fun firstTimeSetup(){
        FileService.createDirectoryIfNotExists(DirectoryMappings.PROJECTS_DIR)
        FileService.getOrCreateFile(DirectoryMappings.getProjectPath("default"))
        FileService.createDirectoryIfNotExists(DirectoryMappings.getAssetsPath("default"))
        FileService.createAssetFolders("default")
        FileService.createDirectoryIfNotExists(DirectoryMappings.getScenesPath("default"))
        Serializer.serializeProject(Project("default", DirectoryMappings.getProjectPath("default"),"-1"))
        projectManager.initialize("default")
    }

    fun postEvent(event: Any) {
        if(event is Event) {
            event.listener.eventPosted(event)

        }

        eventBus.post(event)
    }

    fun registerListener(listener: Any) = eventBus.register(listener)

    fun unregisterListener(listener: Any) = eventBus.unregister(listener)




}