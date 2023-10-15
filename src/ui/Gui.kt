package ui

import app.Salient
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.MultiSplitPane
import com.kotcrab.vis.ui.widget.VisSplitPane
import graphics.Renderer
import ktx.scene2d.Scene2DSkin
import org.yunghegel.gdx.ui.UI
import org.yunghegel.gdx.ui.widgets.STable
import org.yunghegel.gdx.ui.widgets.viewport.ViewportPanel
import org.yunghegel.gdx.ui.widgets.viewport.ViewportWidget
import project.ProjectManager
import ui.project.ProjectInspector
import ui.scene.SceneHierarchy
import ui.scene.SceneInspector
import ui.viewport.ScenePreview
import ui.viewport.SceneTabPane
import ui.viewport.ViewportButtons
import ui.viewport.ViewportMenubar


open class Gui :Renderer {

     val skin: Skin
     val stage: Stage
     val root: STable

     lateinit var projTable : STable

    lateinit var viewportWidget: ViewportWidget
    lateinit var viewportPanel : ViewportPanel
    lateinit var sceneTabPane : SceneTabPane

    lateinit var projectWidget: ProjectInspector

    lateinit var middlePane: MultiSplitPane
    lateinit var rightLeftSplitPane: VisSplitPane
    lateinit var menuBar : SMenuBar

    lateinit var scene: SceneHierarchy
    lateinit var sceneInspector: SceneInspector



    lateinit var sceneTable : STable
    lateinit var tooltipManager : TooltipManager

    init {

        UI.load()
        skin = UI.getSkin()

        if(!VisUI.isLoaded()) {
            VisUI.load(skin)
        }

        tooltipManager = TooltipManager.getInstance()
        tooltipManager.enabled = true

        Scene2DSkin.defaultSkin =skin
        stage = Stage(ScreenViewport())
        root = STable()
        root.setBackground("button-down")
        sceneTable = STable()
        val g = Gdx.graphics as Lwjgl3Graphics
        val windowHandle = g.window.windowHandle
        createUI()
        root.layout()


    }

    fun createUI() {
        stage.addActor(root)
        root.setFillParent(true)
        createRenderPreview()
        createMenuBar()
        createProjectWidget()
        makeMiddlePane()
    }

    fun makeMiddlePane() {
        middlePane = MultiSplitPane( false)
        root.add(middlePane).colspan(2).grow();
        middlePane.setWidgets(projTable,viewportPanel,sceneTable)
        middlePane.setSplit(0,0.15f)
        middlePane.setSplit(1,0.85f)


    }

    private fun createProjectWidget() {
        projTable = STable()
        projectWidget = ProjectInspector()
        projTable.align(Align.topLeft)
        projTable.add(projectWidget).grow().row()

    }

    public fun createSceneHierarchy() {
        val projectManager : ProjectManager = Salient.projectManager
        sceneInspector = SceneInspector()
        scene = SceneHierarchy(projectManager.sceneManager.currentScene)
        sceneTable.align(Align.topLeft)
        sceneTable.add(sceneInspector).grow().fill().row()

    }

    lateinit var tabCell : Cell<SceneTabPane>

    private fun createRenderPreview() {
        viewportWidget = ViewportWidget(Salient.viewport,stage)
        viewportPanel = ScenePreview(viewportWidget)
        sceneTabPane = SceneTabPane(viewportPanel)
        viewportWidget.uiStage.addActor(viewportPanel   )
        viewportPanel.uiBody.add(ViewportMenubar().table).align(Align.topLeft)
        viewportPanel.uiBody.add(ViewportButtons.meshSelectionButtons).growX()
        viewportPanel.uiBody.add(ViewportButtons.configButtons).growX()
        viewportPanel.uiBody.add(ViewportButtons.renderOptionButtons).growX().row()
        viewportPanel.uiBody.add(ViewportButtons.toolButtons).left().padLeft(3f)

//        tabCell = menuBar.projectInfo.add(sceneTabPane)



//        root.add(viewportPanel).grow().row()

    }

    fun createMenuBar(){
        menuBar = SMenuBar()
        var headerTable = STable()
        headerTable.add(menuBar.table).fillX().growX()
        tabCell = menuBar.table.add(sceneTabPane).fillX().growX()
        sceneTabPane.align(Align.topLeft)
        headerTable.align(Align.topLeft)
        root.align(Align.topLeft)
        root.add(headerTable).growX().fillX().row()

    }

    override fun render(delta:Float) {
        val w = menuBar.computeWidth()
        var splitPaneWidth = middlePane.width*.15f
        var amountToAdjust = splitPaneWidth - w
        root.pack()
        sceneTabPane.layout()
            menuBar.table.width = w + amountToAdjust
            menuBar.itemsCell.width( amountToAdjust )



        stage.act(delta)
        stage.draw()






    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    fun adjustMenuBarWidth(){



    }



}