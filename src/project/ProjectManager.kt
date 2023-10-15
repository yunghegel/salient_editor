package project

import app.App.log
import app.App.perf
import app.Salient
import assets.AssetManager
import assets.types.ModelAsset
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Model
import events.assets.AssetFinalizedEvent
import events.assets.AssetLoadingFinishedEvent
import events.natives.WindowFocusEvent
import events.proj.ProjectCreatedEvent
import events.proj.ProjectInitializedEvent
import events.proj.ProjectLoadedEvent
import io.DirectoryMappings
import io.FileService
import io.Serializer
import scene.SceneManager
import scene.graph.ModelImporter
import sys.Log
import sys.profiling.Profile
import util.isGLTF

class ProjectManager() : AssetLoadingFinishedEvent.Listener,ProjectLoadedEvent.Listener,ProjectInitializedEvent.Listener,AssetFinalizedEvent.Listener
{
    lateinit var currentProject: Project

    lateinit var sceneManager: SceneManager

    lateinit var assetManager: AssetManager

     lateinit var tmpData: TmpData

    val recentProjects: List<String>
        get() = tmpData.recentProjects

    init {
    }



    @Profile
    fun initialize(name: String){

        Salient.registerListener(this)
        Log.info("Initializing project $name")

        loadAndSet(name)
        assetManager = AssetManager(currentProject)
        sceneManager = SceneManager(currentProject)
        sceneManager.initalize()

        assetManager.initialize()
        tmpData = Serializer.deserializeTmpData()





        Log.info("Retreived scene ${sceneManager.currentScene.name} from project ${currentProject.name}")
        Salient.postEvent(ProjectInitializedEvent(currentProject))





    }

    fun parseTmpInfo(tmp: TmpData) {

    }

    @Profile
    fun save(){
        currentProject.save()
        Serializer.serializeTmpData()
        Log.info("Saved project ${currentProject.name}")
    }

    @Profile
    fun load(name:String): Project {
        perf("ProjectManager.load") {
            Log.info("Loading project $name")
        }

        perf("ProjectManager") {
            Thread.sleep(100)
            log { "hello" }
        }


        if(FileService.fileExists(DirectoryMappings.getProjectPath(name))) {
            Log.info("Project $name exists, retrieving from disk")
            var project = Serializer.deserializeProject(name)
            Salient.postEvent(ProjectLoadedEvent(project))
            return project
        }
        else{
            Log.info("Project $name does not exist, creating new project")
            var project = create(name)
            Salient.postEvent(ProjectLoadedEvent(project))
            return project
        }
    }

    fun set(project: Project){
        currentProject = project
    }

    fun loadAndSet(name:String){
        set(load(name))
    }

    @Profile
    fun create(name:String): Project {
        var project = Project(name, DirectoryMappings.getProjectPath(name), Serializer.generateUID())
        FileService.createAssetFolders(name)
        Serializer.serializeProject(project)
        TmpData.appendRecentProject(name)
        currentProject = project
        Salient.postEvent(WindowFocusEvent(true))
        Salient.postEvent(ProjectCreatedEvent(project))
        return project
    }




        override fun onProjectLoaded(event: ProjectLoadedEvent) {

        }


    override fun onAssetLoadingFinished(event: AssetLoadingFinishedEvent) {

    }

    override fun onInitialized(event: ProjectInitializedEvent) {
        Salient.ui.createSceneHierarchy()
        Salient.ui.viewportWidget.setRenderer(sceneManager.currentScene.getRenderer())
        Salient.projectManager.tmpData = Serializer.deserializeTmpData()

        Salient.ui.sceneInspector.sceneHierarchy.refresh()


    }

    override fun onAssetFinalized(event: AssetFinalizedEvent) {
        val asset = event.asset
        assetManager.registry.assets.add(asset)
        if(Gdx.files.isGLTF(asset.meta.properties.path)){


            val modelAsset = asset as ModelAsset
            val model = modelAsset.obj as Model
            val go = ModelImporter(model, modelAsset, sceneManager.currentScene.sceneGraph,assetManager).buildGo()
            sceneManager.currentScene.sceneGraph.addGameObject(go)
        }
        if(Salient.ui.sceneInspector==null) {
            return
        }
        Salient.ui.sceneInspector.sceneHierarchy.refresh()

    }

}