package assets.types

import assets.Asset
import assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import ktx.assets.async.AssetStorage

class TextureAsset(file: FileHandle) : Asset<Texture>(file) {

        override fun load(assetManager: AssetManager) {
            service.load(meta.properties.path, Texture::class.java)
        }

        override fun get(assetStorage: AssetStorage): Texture {
            return service.get(meta.properties.path, Texture::class.java)
        }
}