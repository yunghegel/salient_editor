package assets

import app.App.log
import app.App.perf
import app.Salient
import assets.types.ModelAsset
import assets.types.OtherAsset
import assets.types.TextureAsset
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ray3k.stripe.FreeTypeSkinLoader
import events.assets.AssetFinalizedEvent
import events.assets.AssetLoadingFinishedEvent
import events.assets.AssetLoadingInitalizedEvent
import events.assets.AssetQueuedEvent
import io.FileService
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import net.mgsx.gltf.loaders.glb.GLBAssetLoader
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader
import net.mgsx.gltf.loaders.gltf.GLTFLoader
import net.mgsx.gltf.scene3d.scene.SceneAsset
import project.Project
import scene.graph.ModelImporter
import sys.Log
import java.util.*


class AssetManager(val project: Project) :AssetFinalizedEvent.Listener {

    val registry = AssetRegistry()

    val defaultAssets = arrayOf(
        "provided/gltf/models/logo.gltf",
    )

    val textureFolder: FileHandle
    val modelFolder: FileHandle
    val shaderFolder: FileHandle
    val otherFolder: FileHandle
    val assetStorage = AssetStorage(asyncContext = newAsyncContext(threads = 2))
    val queuedAssets = Stack<Asset<*>>()

    init {
        textureFolder = project.assetsFolder.child("textures")
        modelFolder = project.assetsFolder.child("models")
        shaderFolder = project.assetsFolder.child("shaders")
        otherFolder = project.assetsFolder.child("other")
        Salient.registerListener(this)
        assetStorage.apply {
           setLoader<Skin>(".json",) {
                FreeTypeSkinLoader(assetStorage.fileResolver)
           }
           setLoader<SceneAsset>(".gltf", ) {
               GLTFAssetLoader()
           }
            setLoader<SceneAsset>(".glb") {
                GLBAssetLoader()
            }
            setLoader<Model>(".obj") {
                ObjLoader()
            }
        }

//        loadDefault()
    }

    fun initialize(){
        Salient.postEvent(AssetLoadingInitalizedEvent())
        loadExisting()
        Salient.postEvent(AssetLoadingFinishedEvent())
    }

    fun loadExisting() {
        for (file in textureFolder.list()) {
            load(file)
        }
        for (file in modelFolder.list()) {
            load(file)
        }
    }

    fun load(file: FileHandle) {
        val type = AssetType.fromFile(file)
        Log.info("Loading asset: " + file.name() + " of type: " + type.name)

        perf("Name")  {

            log { "hello" }


        }


        log(block = {" Hello! "})
        log { "hello" }


        when (type) {
            AssetType.TEXTURE -> {
                val asset = TextureAsset(file)
                Salient.postEvent(AssetQueuedEvent(asset))
                queuedAssets.add(asset)
                asset.load(this)
            }

            AssetType.MODEL -> {
                val asset = ModelAsset(file)
                Salient.postEvent(AssetQueuedEvent(asset))
                queuedAssets.add(asset)
                asset.load(this)
            }

            AssetType.SHADER -> TODO()
            AssetType.SOUND -> TODO()
            AssetType.FONT -> TODO()
            AssetType.OTHER -> {
                val asset = OtherAsset(file)
                queuedAssets.add(asset)
            }
        }

        AssetService.update()
    }

   inline fun <reified T:Any>  loadAsync(asset:Asset<*>) {
       KtxAsync.launch {
           assetStorage.apply {
             val asyncAsset = loadAsync<T>(asset.meta.properties.path)
               val obj = asyncAsset.await()
               loadingFinalized(asset)

           }
       }

   }

    inline fun <reified T:Any>  getAsync(asset:Asset<*>)  {
        KtxAsync.launch {
            assetStorage.apply {
                val asyncAsset = getAsync<T>(asset.meta.properties.path).await()
                val obj = asyncAsset;
                val mdlAsset = asset as ModelAsset
                mdlAsset.obj = obj as Model

            }
        }
    }

    fun <T : Any> retrievalFinalized(asset: Asset<*>, obj: T) {
        when (asset) {
            is ModelAsset -> {
                val modelAsset = asset as ModelAsset
               registry.assets.add(modelAsset)
                obj as Model
                project.currentScene?.let { it1 -> ModelImporter(obj, modelAsset, it1.sceneGraph,this).buildGo() }
            }
            is TextureAsset -> {}
        }
    }

    fun loading():Boolean{
     val finishedLoading =  AssetService.update()
        if(finishedLoading){
        }
        return finishedLoading
    }


    fun loadingFinalized(asset: Asset<*>){


        Salient.postEvent(AssetFinalizedEvent(asset))

    }

     fun add(asset: Asset<*>) {
        registry.assets.add(asset)
    }

    fun <T> get(asset: Asset<T>): T {
        return registry.get(asset)
    }

    private fun loadInternal(internalFile: FileHandle,type: AssetType) {


        when (type) {
            AssetType.TEXTURE -> {
                val destination = textureFolder.child(internalFile.name())
                FileService.Operations.copy(internalFile, textureFolder)
                load(destination)

            }

            AssetType.MODEL -> {
                val destination = modelFolder.child(internalFile.name())
                FileService.Operations.copy(internalFile, modelFolder)
                load(destination)
            }

            AssetType.SHADER -> TODO()
            AssetType.SOUND -> TODO()
            AssetType.FONT -> TODO()
            AssetType.OTHER -> {
                val destination = FileHandle(project.assetsFolder.path() + "/" + internalFile.name()+"/")
                FileService.Operations.copy(internalFile, modelFolder)
                load(destination)
            }
        }

    }

    fun loadDefault() {
        for (path in AssetService.defaultAssets) {
            val loader = GLTFLoader()
            val model = loader.load(Gdx.files.internal(path)).scene.model
            project.currentScene?.sceneRenderer!!.add(ModelInstance(model))
        }
    }

    override fun onAssetFinalized(event: AssetFinalizedEvent) {
//        val asset = event.asset
//        registry.assets.add(asset)
//        if(Gdx.files.isGLTF(asset.meta.properties.path)){
//
//
//            val modelAsset = asset as ModelAsset
//            val model = modelAsset.obj as Model
//            project.currentScene?.let { it1 -> ModelImporter(model, modelAsset, it1.sceneGraph,this).buildGo() }
//            project.currentScene?.sceneRenderer!!.addScene(Scene(model))
//        }

    }


}