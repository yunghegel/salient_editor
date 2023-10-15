package ui.scene

import app.Salient
import events.sceneSelected
import org.yunghegel.gdx.ui.widgets.STable
import project.ProjectManager

class SceneInspector : STable() {

    val projectManager: ProjectManager
    val sceneInspectorHeading: SceneInspectorHeading
    val sceneHierarchy: SceneHierarchy

    init {
        projectManager=Salient.projectManager
        sceneInspectorHeading = SceneInspectorHeading(projectManager)
        sceneHierarchy = SceneHierarchy(projectManager.sceneManager.currentScene)

        add(sceneInspectorHeading).growX().row()
        add(sceneHierarchy).grow().row()

        sceneSelected { event ->
            sceneHierarchy.setCurrent(event) }
    }


}