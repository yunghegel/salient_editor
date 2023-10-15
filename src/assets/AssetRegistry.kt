package assets

import assets.types.ModelAsset
import assets.types.TextureAsset
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model

class AssetRegistry {

    var assets = mutableListOf<Asset<*>>()

    val modelMap: AssetMap<Model> = AssetMap()

    val textureMap: AssetMap<Texture> = AssetMap()



    fun add(asset: Asset<*>){
        assets.add(asset)
        when(asset){
            is ModelAsset -> modelMap.add(asset, asset.obj!!)
            is TextureAsset -> textureMap.add(asset, asset.obj!!)
        }
    }

    fun remove(asset: Asset<*>){
        assets.remove(asset)
        when(asset){
            is ModelAsset -> modelMap.remove(asset)
            is TextureAsset -> textureMap.remove(asset)
        }
    }

    fun clear(){
        assets.clear()
        modelMap.clear()
        textureMap.clear()
    }

    fun <T> get(asset: Asset<T>): T {
        return when(asset){
            is ModelAsset -> modelMap.get(asset) as T
            is TextureAsset -> textureMap.get(asset) as T
            else -> throw Exception("Asset type not supported")
        }
    }



    public inner class AssetMap<T> {
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