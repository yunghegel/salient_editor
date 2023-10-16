package ui.scene

import app.Salient
import events.scene.GameObjectAddedEvent
import events.scene.GameObjectDeselectedEvent
import events.scene.GameObjectRemovedEvent
import events.scene.GameObjectSelectedEvent

import scene.Scene


class SceneHierarchy(var scene: Scene) : SceneTree(scene.sceneGraph) , GameObjectAddedEvent.Listener,
    GameObjectRemovedEvent.Listener, GameObjectSelectedEvent.Listener, GameObjectDeselectedEvent.Listener {

    private var lastSelected: GameObjectNode? = null

    init {
        Salient.registerListener(this)
    }

    fun setCurrent(scene: Scene) {
        this.scene = scene
        refresh()
    }

    override fun onGameObjectAdded(event: GameObjectAddedEvent) {
        if(tree.selectedNode != null) {
            lastSelected = tree.selectedNode as GameObjectNode
        }
        refresh()
    }

    override fun onGameObjectDeselected(event: GameObjectDeselectedEvent) {
        deselect(event.gameObject)
        if(scene.selectedGameObjects.isEmpty()) {
            tree.selection.clear()
        }
    }

    override fun onGameObjectRemoved(event: GameObjectRemovedEvent) {
        remove(event.gameObject)
    }

    override fun onGameObjectSelected(event: GameObjectSelectedEvent) {
        select(event.gameObject)
    }

    override fun refresh() {

        super.refresh()
    }


}
