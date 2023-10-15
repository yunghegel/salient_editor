package assets

import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.Serializable
import java.util.*

class AssetMeta (val file: FileHandle) {



    val properties: AssetData

    init {
        properties = AssetData(
            file.nameWithoutExtension(),
            AssetType.OTHER,
            file.path(),
            file.length(),
            file.lastModified(),
            UUID.randomUUID().toString()
        )
    }

}

@Serializable
data class AssetData (
    val name: String,
    var type: AssetType,
    val path: String,
    val size: Long,
    val lastModified: Long,
    val uuid: String
)