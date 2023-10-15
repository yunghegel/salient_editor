package events

import assets.Asset
import events.assets.AssetFinalizedEvent
import events.assets.AssetLoadingFinishedEvent
import events.assets.AssetQueuedEvent
import events.proj.ProjectChangedEvent
import events.proj.ProjectCreatedEvent
import events.proj.ProjectInitializedEvent
import events.proj.ProjectLoadedEvent
import events.scene.*
import project.Project
import scene.Scene
import scene.graph.GameObject
import util.Sal

inline fun <T: Any> T.sceneCreated(crossinline listener: T.(Scene) -> Unit): SceneCreatedEvent.Listener {
    val onEvent = object : SceneCreatedEvent.Listener {
        override fun onSceneCreated(event: SceneCreatedEvent) = listener(event.scene)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T: Any> T.sceneSelected(noinline listener: (Scene) -> Unit): SceneSelectionEvent.Listener {
    val onEvent = object : SceneSelectionEvent.Listener {
        override fun onSceneSelection(event: SceneSelectionEvent) = listener(event.scene)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T: Any> T.sceneLoaded(crossinline listener: (Scene) -> Unit): SceneLoadedEvent.Listener {
    val onEvent = object : SceneLoadedEvent.Listener {
        override fun onSceneLoaded(event: SceneLoadedEvent) = listener(event.scene)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T: Any> T.code(crossinline codeBlock: T.() -> Unit) {
    codeBlock()
}

inline fun <T: Any> T.sceneClosed(crossinline listener: T.(Scene) -> Unit): SceneClosedEvent.Listener {
    val onEvent = object : SceneClosedEvent.Listener {
        override fun onSceneClosed(event: SceneClosedEvent) = listener(event.scene)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.gameObjectedAdded(crossinline listener: T.(GameObject) -> Unit): GameObjectAddedEvent.Listener {
    val onEvent = object : GameObjectAddedEvent.Listener {
        override fun onGameObjectAdded(event: GameObjectAddedEvent) = listener(event.gameObject)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.gameObjectSelected(crossinline listener: T.(GameObject) -> Unit): GameObjectSelectedEvent.Listener {
    val onEvent = object : GameObjectSelectedEvent.Listener {
        override fun onGameObjectSelected(event: GameObjectSelectedEvent) = listener(event.gameObject)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.gameObjectDeselected(crossinline listener: T.(GameObject) -> Unit): GameObjectDeselectedEvent.Listener {
    val onEvent = object : GameObjectDeselectedEvent.Listener {
        override fun onGameObjectDeselected(event: GameObjectDeselectedEvent) = listener(event.gameObject)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.gameObjectRemoved(crossinline listener: T.(GameObject) -> Unit): GameObjectRemovedEvent.Listener {
    val onEvent = object : GameObjectRemovedEvent.Listener {
        override fun onGameObjectRemoved(event: GameObjectRemovedEvent) = listener(event.gameObject)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.gameObjectTransformed(crossinline listener: T.(GameObject) -> Unit): GameObjectTransformedEvent.Listener {
    val onEvent = object : GameObjectTransformedEvent.Listener {
        override fun onGameObjectTransformed(event: GameObjectTransformedEvent) = listener(event.gameObject)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.projectCreated(crossinline listener: T.(Project) -> Unit): ProjectCreatedEvent.Listener {
    val onEvent = object : ProjectCreatedEvent.Listener {
        override fun onProjectCreated(event: ProjectCreatedEvent) = listener(event.project)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.projectLoaded(crossinline listener: T.(Project) -> Unit): ProjectLoadedEvent.Listener {
    val onEvent = object : ProjectLoadedEvent.Listener {
        override fun onProjectLoaded(event: ProjectLoadedEvent) = listener(event.project)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.projectChanged(crossinline listener: T.(Project) -> Unit): ProjectChangedEvent.Listener {
    val onEvent = object : ProjectChangedEvent.Listener {
        override fun onProjectChanged(event: ProjectChangedEvent) = listener(event.project)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.projectInitialized(crossinline listener: T.(Project) -> Unit): ProjectInitializedEvent.Listener {
    val onEvent = object : ProjectInitializedEvent.Listener {
        override fun onInitialized(event: ProjectInitializedEvent) = listener(event.project)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.assetFinalized(crossinline listener: T.(Asset<*>) -> Unit): AssetFinalizedEvent.Listener {
    val onEvent = object : AssetFinalizedEvent.Listener {
        override fun onAssetFinalized(event: AssetFinalizedEvent) = listener(event.asset)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.assetQueued(crossinline listener: T.(Asset<*>) -> Unit): AssetQueuedEvent.Listener {
    val onEvent = object : AssetQueuedEvent.Listener {
        override fun onAssetQueuedEvent(event: AssetQueuedEvent) = listener(event.asset)
    }
    Sal.registerListener(onEvent)
    return onEvent
}

inline fun <T:Any> T.assetsFinished(crossinline listener: T.() -> Unit): AssetLoadingFinishedEvent.Listener {
    val onEvent = object : AssetLoadingFinishedEvent.Listener {
        override fun onAssetLoadingFinished(event: AssetLoadingFinishedEvent) = listener()
    }
    Sal.registerListener(onEvent)
    return onEvent
}

//object Events {
//
//    init {
//        runBlocking {
//
//
//
//
//
//            coroutine {
//
//                for (i in 0..100) {
//                    Log.info("test")
//                }
//                complete {
//                    Thread.sleep(5000)
//                    Log.info(System.nanoTime().toString())
//                }
//
//
//
//
//            }
//
//        }
//
//    }
//}



