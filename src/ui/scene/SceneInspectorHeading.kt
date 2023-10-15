package ui.scene

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip
import events.sceneCreated
import ktx.scene2d.*
import ktx.scene2d.vis.visTooltip
import org.yunghegel.gdx.ui.widgets.STable
import project.ProjectManager

class SceneInspectorHeading(val projectManager: ProjectManager) : STable() {

    val sceneSelector : SelectBox<String>
    val sceneName : Label
    val searchBar: TextField

    init {
        val project = projectManager.currentProject
        sceneSelector = scene2d.selectBox<String> {
            items.apply { project.scenes.forEach { add(it.name) } }

            sceneCreated { scene ->
                run {
                    items.add(scene.name)
                    refreshItems()
                }
            }


        }


        val mytooltip = tooltip {
            scene2d.table {
                listWidget {
                    items.apply { project.scenes.forEach { add(it.name) } }
                }
            }
        }
        scene2d.table {


        }

        sceneName = scene2d.label("Scene") {



            tooltip {
                scene2d.table {
                    listWidget {
                        items.apply { project.scenes.forEach { add(it.name) } }
                    }
                }


            }

        }
        searchBar = scene2d.textField {
            messageText = "Search"





                background = skin.getDrawable("border")
                val sceneNames = project.scenes.forEach {
                    if (it.name.contains(messageText)) {

                    }
                }


        }

        searchBar.addListener(TextTooltip("Test",skin))
        val toolTip = scene2d.table {
            listWidget {
                items.apply { project.scenes.forEach { add(it.name) } }
                sceneCreated { scene ->
                    items.add(scene.name)
                    refreshItems()
                }

            }
            defaults().pad(0f)


        }
        searchBar.visTooltip(toolTip) {
        }




        add(sceneName).growX().pad(5f)
        add(searchBar).growX().pad(5f).maxWidth(70f).left()
        add(sceneSelector).growX().pad(5f)



    }


}