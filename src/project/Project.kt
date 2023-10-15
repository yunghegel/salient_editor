package project

import com.badlogic.gdx.files.FileHandle
import io.Serializer
import scene.Scene

class Project(val name: String, val path: String,val uid:String) {

    var scenes: MutableList<Scene> = mutableListOf()
    var currentScene: Scene? = null

    val projectFolder = FileHandle(path).parent()

    val assetsFolder: FileHandle = projectFolder.child("assets")
    val sceneFolder: FileHandle = projectFolder.child("scenes")

    fun save() {
        Serializer.serializeProject(this)
        Serializer.serializeTmpData()
    }

}