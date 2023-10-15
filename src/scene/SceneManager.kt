package scene

import app.Salient
import events.scene.SceneCreatedEvent
import events.scene.SceneLoadedEvent
import events.scene.SceneSelectionEvent
import io.FileService
import io.Serializer
import project.Project
import sys.Log
import util.CameraUtils

class SceneManager(project: Project) : SceneLoadedEvent.Listener, SceneCreatedEvent.Listener, SceneSelectionEvent.Listener {

    private var proj = project
    lateinit var currentScene: Scene

    init {
        Salient.eventBus.register(this)
    }

    fun initalize() {
        FileService.listScenes(proj.name).forEach {
            if (proj.scenes.find { scene -> scene.name == it.substringBefore('.') } == null) {
                proj.scenes.add(Serializer.deserializeScene(proj.name, it.substringBefore('.'), proj))
            }
        }
        if(proj.scenes.isEmpty()) {
            Log.info("No scenes found, creating default scene")
            initDefaultScene()
        } else {
            loadScene(proj.scenes[0])
            selectScene(proj.scenes[0])
        }
    }
    fun selectScene(scene: Scene){
        currentScene = scene
        if(!proj.scenes.contains(scene)){
            addScene(scene)
            setScene(scene)
        }
        Salient.postEvent(SceneSelectionEvent(scene))
    }

    fun closeScene(){
        saveScene()
        currentScene.dispose()
    }

    fun saveScene(){
        Serializer.serializeScene(currentScene)
    }

    fun loadScene(scene: Scene){
        currentScene = scene
        Salient.postEvent(SceneLoadedEvent(scene))

    }
    fun addScene(scene: Scene) {

        proj.scenes.add(scene)
    }
    fun setScene(scene: Scene){
        proj.currentScene = scene
    }

    fun removeScene(scene: Scene){
        proj.scenes.remove(scene)
    }


    fun newScene(name:String,makeCurrent: Boolean?) : Scene {
        var scene = Scene(name, proj)
        scene.sceneContext = SceneContext()
        scene.perspectiveCamera= Salient.camera

        Serializer.serializeScene(scene)
        if(makeCurrent == true){

            selectScene(scene)
        }
        Salient.postEvent(SceneCreatedEvent(scene))
        return scene
    }




    fun initDefaultScene() : Scene {
       currentScene =  newScene("New Scene",true)
        addScene(currentScene)
        setScene(currentScene)
        return currentScene
    }

    override fun onSceneLoaded(event: SceneLoadedEvent) {
//        Salient.cameraController.perspectiveCamera=(event.scene.camera)

    }

    override fun onSceneCreated(event: SceneCreatedEvent) {
        Serializer.serializeScene(event.scene)
    }

    override fun onSceneSelection(event: SceneSelectionEvent) {

        var lookAt = CameraUtils.cameraRayXZPlaneIntersection(event.scene.perspectiveCamera.position, event.scene.perspectiveCamera.direction)
//        Salient.cameraController.perspectiveCameraController.config.target.(lookAt)

    }


}