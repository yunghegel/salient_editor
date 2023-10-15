package assets.types

import assets.Asset
import assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import ktx.assets.async.AssetStorage

class OtherAsset(file: FileHandle) : Asset<FileHandle>(file) {
    override fun load(assetManager: AssetManager) {

    }

    override fun get(assetStorage: AssetStorage): FileHandle {
        return file
    }


}