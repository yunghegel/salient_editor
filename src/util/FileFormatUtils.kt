package util

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle
import java.io.File
import java.util.*

const val FORMAT_3D_G3DJ = "g3dj"
const val FORMAT_3D_COLLADA = "dae"
const val FORMAT_3D_WAVEFONT = "obj"
const val FORMAT_3D_FBX = "fbx"
const val FORMAT_3D_G3DB = "g3db"
const val FORMAT_3D_GLTF = "gltf"
const val FORMAT_3D_GLB = "glb"

const val FORMAT_IMG_PNG = "png"
const val FORMAT_IMG_JPG = "jpg"
const val FORMAT_IMG_JPEG = "jpeg"
const val FORMAT_IMG_TGA = "tga"

fun Files.isG3DB(filename: String): Boolean {
    return filename.lowercase(Locale.getDefault()).endsWith(FORMAT_3D_G3DB)
}

fun Files.isG3DB(file: FileHandle): Boolean {
    return isG3DB(file.name())
}

fun Files.isGLTF(filename: String): Boolean {
    return filename.lowercase(Locale.getDefault()).endsWith(FORMAT_3D_GLTF)
}

fun Files.isGLB(filename: String): Boolean {
    return filename.lowercase(Locale.getDefault()).endsWith(FORMAT_3D_GLB)
}

fun Files.isGLTF(file: FileHandle): Boolean {
    return isGLTF(file.name())
}

fun Files.isGLB(file: FileHandle): Boolean {
    return isGLB(file.name())
}

fun Files.isWavefont(filename: String) = filename.lowercase().endsWith(FORMAT_3D_WAVEFONT)
fun Files.isWavefont(file: FileHandle) = isWavefont(file.name())
fun Files.isCollada(filename: String) = filename.lowercase().endsWith(FORMAT_3D_COLLADA)
fun Files.isCollada(file: FileHandle) = isCollada(file.name())
fun Files.isFBX(filename: String) = filename.lowercase().endsWith(FORMAT_3D_FBX)
fun Files.isFBX(file: FileHandle) = isFBX(file.name())
fun Files.isG3DJ(filename: String) = filename.lowercase().endsWith(FORMAT_3D_G3DJ)
fun Files.isG3DJ(file: FileHandle) = isG3DJ(file.name())
fun Files.isPNG(file: FileHandle) = isPNG(file.name())
fun Files.isPNG(filename: String) = filename.lowercase().endsWith(FORMAT_IMG_PNG)
fun Files.isJPG(file: FileHandle) = isJPG(file.name())
fun Files.isTGA(filename: String) = filename.lowercase().endsWith(FORMAT_IMG_TGA)
fun Files.isTGA(file: FileHandle) = isTGA(file.name())
fun Files.is3DFormat(file: FileHandle) = is3DFormat(file.name())
fun Files.isImage(file: FileHandle) = isImage(file.name())

fun Files.isJPG(filename: String): Boolean {
    val fn = filename.lowercase()
    return (fn.endsWith(FORMAT_IMG_JPG) || fn.endsWith(FORMAT_IMG_JPEG))
}

fun Files.is3DFormat(filename: String): Boolean {
    val fn = filename.lowercase()
    return fn.endsWith(FORMAT_3D_WAVEFONT) || fn.endsWith(FORMAT_3D_COLLADA)
            || fn.endsWith(FORMAT_3D_G3DB) || fn.endsWith(FORMAT_3D_G3DJ) || fn.endsWith(FORMAT_3D_FBX)
}

fun Files.isImage(filename: String): Boolean {
    val fn = filename.lowercase()
    return fn.endsWith(FORMAT_IMG_TGA) || fn.endsWith(FORMAT_IMG_JPEG)
            || fn.endsWith(FORMAT_IMG_JPG) || fn.endsWith(FORMAT_IMG_PNG)
}

