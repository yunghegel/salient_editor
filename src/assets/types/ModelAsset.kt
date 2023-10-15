package assets.types

import app.App
import app.App.perf
import assets.Asset
import assets.AssetManager
import assets.AssetService
import assets.AssetType
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g3d.Model
import events.assets.AssetFinalizedEvent
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import net.mgsx.gltf.scene3d.scene.SceneAsset
import util.L
import util.Sal
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ModelAsset(file: FileHandle) : Asset<Model>(file) {

    init {
        this.meta.properties.type = AssetType.MODEL


        }

    suspend fun assetCoroutine() : SceneAsset {
        return suspendCoroutine {

                AssetService.load(meta.properties.path, SceneAsset::class.java)
                AssetService.finishLoading()
                val asset = AssetService.get<SceneAsset>(meta.properties.path)
                while (!AssetService.update()) {
                    L.info("Loading ${meta.properties.path} ${AssetService.progress * 100}%")
                }
                it.resume(asset)



        }
    }


    override fun load(assetManager: AssetManager) {
        App.perf("ModelAsset.load async") {
            val coroutine = KtxAsync.launch {

                    val co: suspend () -> Unit = suspend {
                        obj = assetCoroutine().scene.model
                    }

                    co.invoke()


            }
            coroutine.invokeOnCompletion {
                Sal.postEvent(AssetFinalizedEvent(this))
            }
        }
    }
    override fun get(assetStorage: AssetStorage): Model {
        obj = AssetService.get<SceneAsset>(meta.properties.path).scene.model




        return obj!!
    }




}