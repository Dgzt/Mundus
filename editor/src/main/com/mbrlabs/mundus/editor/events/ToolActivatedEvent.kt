package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editor.tools.ToolType

class ToolActivatedEvent(val type: ToolType) {

    interface ToolActivatedEventListener {
        @Subscribe
        fun onToolActivatedEvent(event: ToolActivatedEvent)
    }
}
