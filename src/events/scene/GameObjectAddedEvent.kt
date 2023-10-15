package events.scene

import events.GameObjectEvent
import events.Subscribe
import scene.graph.GameObject

class GameObjectAddedEvent(gameObject: GameObject) : GameObjectEvent(gameObject) {

    init {
        var sb = StringBuilder().apply {
            gameObject.components.forEach { append(it::class.java.simpleName).append(", ") }
        }
            msg = "GameObject added to scene graph; { id: ${gameObject.id}, components: ${sb} }"

    }

    interface Listener {
        @Subscribe
        fun onGameObjectAdded(event: GameObjectAddedEvent)
    }



}