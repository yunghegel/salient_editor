package io

import app.Salient
import kotlinx.serialization.json.Json
import project.Project
import project.ProjectData
import project.ProjectManager
import project.TmpData
import scene.Scene
import scene.SceneData
import scene.context.env.CameraData
import util.Matrix4Data

object Serializer {
    val json = Json { prettyPrint = true }

    val projectManager:ProjectManager =Salient.projectManager

    fun serializeProject(project: Project) {
        var sceneData = mutableListOf<String>()
        project.scenes.forEach {
            sceneData.add(it.name)
            serializeScene(it)
        }
        var projectData = ProjectData(project.name, project.path,project.uid, sceneData)
        FileService.writeStringToFile(
            DirectoryMappings.getProjectPath(project.name),
            json.encodeToString(ProjectData.serializer(), projectData)
        )
    }

    fun deserializeProject(projectName: String): Project {
        var projectData = Json.decodeFromString(ProjectData.serializer(), FileService.getProjectFile(projectName).readString())
        var project = Project(projectData.name, projectData.path,projectData.uid)
        projectData.scenes.forEach {
//           if not present in project.scenes, add it
            if(project.scenes.find { scene -> scene.name == it } == null) project.scenes.add(deserializeScene(projectName, it,project))
        }
        return project
    }

    fun serializeScene(scene: Scene) {
        var sceneData = SceneData(scene.name, scene.path(),scene.uid,scene.sceneContext.config, CameraData.fromCamera(scene.perspectiveCamera)    )
        FileService.writeStringToFile(
            DirectoryMappings.getScenePath(scene.project.name, scene.name),
            json.encodeToString(SceneData.serializer(), sceneData)
        )
    }

    fun deserializeScene(projectName: String, sceneName: String,project: Project): Scene {
        val sceneData = Json.decodeFromString(SceneData.serializer(), FileService.getSceneFile(projectName, sceneName).readString())
        val scene = Scene(sceneData.name, project)
        scene.uid = sceneData.uid
        scene.sceneContext.config=sceneData.environment
        scene.perspectiveCamera = Salient.camera
        scene.perspectiveCamera.far = sceneData.cam.far
        scene.perspectiveCamera.near = sceneData.cam.near
        scene.perspectiveCamera.position.set(sceneData.cam.position.x,sceneData.cam.position.y,sceneData.cam.position.z)
        scene.perspectiveCamera.direction.set(sceneData.cam.rotation.x,sceneData.cam.rotation.y,sceneData.cam.rotation.z)
        scene.perspectiveCamera.view.set(Matrix4Data.toMat4(sceneData.cam.view))
        scene.perspectiveCamera.projection.set(Matrix4Data.toMat4(sceneData.cam.projection))
        scene.perspectiveCamera.up.set(0f,1f,0f)
        scene.perspectiveCamera.update()

        scene.data = sceneData

        return scene
    }

    fun serializeTmpData() {
        if(!FileService.fileExists(DirectoryMappings.TMP_FILEPATH)) {
            val tmp = TmpData(
                projectManager.currentProject.name, projectManager.sceneManager.currentScene!!.name,
                listOf(projectManager.currentProject.name))
            FileService.writeStringToFile(
                DirectoryMappings.TMP_FILEPATH,
                json.encodeToString(TmpData.serializer(), tmp)
            )
            val file = FileService.getOrCreateFile(DirectoryMappings.TMP_FILEPATH)
            if(!file.exists() || file.length() == 0L) {
                val tmp=TmpData()
                FileService.writeStringToFile(
                    DirectoryMappings.TMP_FILEPATH,
                    json.encodeToString(TmpData.serializer(), tmp)
                )
            }

        }
        else {
            val tmp = deserializeTmpData()
            val newRecentProjects = tmp.recentProjects.toMutableList()
            tmp.mostRecentProject = projectManager.currentProject.name
            tmp.mostRecentScene = projectManager.sceneManager.currentScene!!.name
            if(!tmp.recentProjects.contains(projectManager.currentProject.name)) {
                newRecentProjects.add(projectManager.currentProject.name)
            }
            tmp.recentProjects = newRecentProjects
            FileService.writeStringToFile(
                DirectoryMappings.TMP_FILEPATH,
                json.encodeToString(TmpData.serializer(), tmp)
            )
        }



    }

    fun deserializeTmpData(): TmpData {
       val tmpFile = FileService.getOrCreateFile(DirectoryMappings.TMP_FILEPATH)
        if(!tmpFile.exists() || tmpFile.length() == 0L) {

                val tmp=TmpData()
            return tmp
        }
            val tmp = TmpData(


                Salient.projectManager.currentProject.name, Salient.projectManager.sceneManager.currentScene.name,
                listOf(projectManager.currentProject.name))
            FileService.writeStringToFile(
                DirectoryMappings.TMP_FILEPATH,
                json.encodeToString(TmpData.serializer(), tmp)
            )


        return Json.decodeFromString(TmpData.serializer(), FileService.getTmpFile().readString())

    }

    fun generateUID():String {
        return java.util.UUID.randomUUID().toString()
    }


}

typealias files = FileService