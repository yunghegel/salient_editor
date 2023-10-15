package events.scene

import events.GameObjectEvent
import events.Subscribe
import scene.graph.GameObject

class GameObjectRemovedEvent(gameObject: GameObject) : GameObjectEvent(gameObject) {

    init {
        msg = "GameObject ${gameObject.name} removed from scene graph; { id: ${gameObject.id} }"
    }

    interface Listener {
        @Subscribe
        fun onGameObjectRemoved(event: GameObjectRemovedEvent)
    }

}