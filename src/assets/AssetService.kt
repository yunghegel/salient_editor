package assets

import app.Salient
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ray3k.stripe.FreeTypeSkinLoader
import events.assets.AssetFinalizedEvent
import net.mgsx.gltf.loaders.glb.GLBAssetLoader
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader
import net.mgsx.gltf.scene3d.scene.SceneAsset


object AssetService : com.badlogic.gdx.assets.AssetManager() {

    val defaultAssets = arrayOf(
        "provided/gltf/models/logo.gltf")

    init {
        setLoader(Skin::class.java,".json", FreeTypeSkinLoader(fileHandleResolver))
        setLoader(SceneAsset::class.java, ".gltf", GLTFAssetLoader())
        setLoader(SceneAsset::class.java, ".glb", GLBAssetLoader())
        setLoader(Model::class.java,".obj", ObjLoader()
        )
    }

    fun processQueue(manager: AssetManager) {
        if(manager.queuedAssets.isNotEmpty()){
            val asset = manager.queuedAssets.lastElement()

//            watch for asset loading finished event



           val finished= update()
            if(!finished){
                watchAsset(asset,manager)

            }


        }

//        if that was the last asset, post event


    }

    fun watchAsset(asset: Asset<*>, manager: AssetManager){
        if(isLoaded(asset.meta.properties.path)){
            Salient.postEvent(AssetFinalizedEvent(asset))
        }
    }

    override fun <T : Any?> load(fileName: String?, type: Class<T>?) {
        setLoader(Skin::class.java,".json", FreeTypeSkinLoader(fileHandleResolver))
        setLoader(SceneAsset::class.java, ".gltf", GLTFAssetLoader())
        setLoader(SceneAsset::class.java, ".glb", GLBAssetLoader())
        setLoader(Model::class.java,".obj", ObjLoader()
        )
        super.load(fileName, type)
        finishLoading()

    }

}


