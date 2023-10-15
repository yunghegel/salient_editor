package assets

import com.badlogic.gdx.files.FileHandle
import java.io.File

enum class AssetType(val directoryName: String,val validExtensions:Array<String>) {

    TEXTURE("textures", arrayOf("png","jpg","jpeg","bmp","gif")),
    MODEL("models", arrayOf("g3db","g3dj","obj","fbx","gltf")),
    SHADER("shaders", arrayOf("vert","frag","glsl","vs","fs")),
    SOUND("sounds", arrayOf("wav","mp3","ogg")),
    FONT("fonts", arrayOf("ttf","otf")),
    OTHER("other", arrayOf(""));

    fun filter(): java.io.FileFilter {
        return Filter(this)
    }

    companion object {
        fun fromExtension(extension: String): AssetType {
            for (type in values()) {
                if (type.matches(extension)) {
                    return type
                }
            }
            return OTHER
        }
        fun fromFile(file: File): AssetType {
            return fromExtension(file.extension)
        }

        fun fromFile(file: FileHandle): AssetType {
            if(file.isDirectory){
                when(file.name()){
                    TEXTURE.directoryName -> return TEXTURE
                    MODEL.directoryName -> return MODEL
                    SHADER.directoryName -> return SHADER
                    SOUND.directoryName -> return SOUND
                    FONT.directoryName -> return FONT
                    OTHER.directoryName -> return OTHER
                }
            }

            return fromExtension(file.extension())
        }

    }

    fun matches(file: FileHandle): Boolean {
        return file.extension() in validExtensions
    }

    fun matches(file: File): Boolean {
        return file.extension in validExtensions
    }

    fun matches(extension: String): Boolean {
        return extension in validExtensions
    }

    private inner class Filter(val type: AssetType) : java.io.FileFilter {
        override fun accept(file: File): Boolean {
            return file.extension in type.validExtensions
        }
    }

}
