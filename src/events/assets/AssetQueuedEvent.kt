package events.assets

import assets.Asset
import events.AssetEvent
import events.Subscribe

class AssetQueuedEvent(asset: Asset<*>) : AssetEvent(asset) {

    init {
        this.msg = "${asset.meta.properties.path} queued for loading"
    }

    interface Listener {
        @Subscribe
        fun onAssetQueuedEvent(event: AssetQueuedEvent)
    }

}