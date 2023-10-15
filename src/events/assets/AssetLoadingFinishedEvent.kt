package events.assets

import events.Event
import events.Subscribe

class AssetLoadingFinishedEvent() : Event() {

    interface Listener {
        @Subscribe
        fun onAssetLoadingFinished(event: AssetLoadingFinishedEvent)
    }

}