package ui.scene

import app.Salient
import com.badlogic.gdx.utils.Align
import events.sceneSelected
import org.yunghegel.gdx.ui.widgets.STable
import project.ProjectManager

class SceneInspector : STable() {

    val projectManager: ProjectManager
    val sceneInspectorHeading: SceneInspectorHeading
    val sceneHierarchy: SceneHierarchy
    val objectInspector: ObjectInspector = ObjectInspector()

    init {
        align(Align.topLeft)
        projectManager=Salient.projectManager
        sceneInspectorHeading = SceneInspectorHeading(projectManager)
        sceneHierarchy = SceneHierarchy(projectManager.sceneManager.currentScene)

        add(sceneInspectorHeading).growX().row()
        add(sceneHierarchy).maxHeight(500f).top().growX().minHeight(250f).row()

        sceneSelected { event ->
            sceneHierarchy.setCurrent(event) }

        add(objectInspector).grow().fill().top()
    }


}