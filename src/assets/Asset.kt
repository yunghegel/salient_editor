package assets

import com.badlogic.gdx.files.FileHandle
import ktx.assets.async.AssetStorage

open class Asset<T>(val file: FileHandle) {

    var obj: T ?= null


    val service: AssetService = AssetService

    val meta: AssetMeta = AssetMeta(file)

    open fun load(assetManager: AssetManager) {
        assetManager.registry.assets.add(this)
    }

    open fun get(assetStorage: AssetStorage) : T {
        return service.get(meta.properties.path)
    }

//    abstract fun unload()
//
//
//    abstract fun save()
//
//    abstract fun delete()
//
//    abstract fun rename(newName: String)
//
//    abstract fun move(newPath: String)
//
//    abstract fun copy(newPath: String)

    inner class AssetMap<T> {
        val map: MutableMap<Asset<T>, T> = mutableMapOf()

        fun add(asset: Asset<T>, value: T){
            map[asset] = value
        }

        fun get(asset: Asset<T>): T? {
            return map[asset]
        }

        fun remove(asset: Asset<T>){
            map.remove(asset)
        }

        fun clear(){
            map.clear()
        }
    }



}



