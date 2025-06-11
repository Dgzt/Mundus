/*
 * Copyright (c) 2025. See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mbrlabs.mundus.editor.tools

import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.history.CommandHistory
import com.mbrlabs.mundus.pluginapi.listener.ToolListener

class PluginTool(projectManager: ProjectManager, history: CommandHistory) : Tool(projectManager, history) {

    lateinit var listener: ToolListener

    override val name: String
        get() = "Plugin Tool"

    override val icon: Drawable
        get() = throw UnsupportedOperationException()

    override val iconFont: String
        get() = throw UnsupportedOperationException()

    override fun render() {
        // NOOP
    }

    override fun act() {
        // NOOP
    }

    override fun onActivated() {
        // NOOP
    }

    override fun onDisabled() {
        listener.onDisabled()
    }

    override fun dispose() {
        // NOOP
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        listener.touchDown(screenX, screenY, button)
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        listener.mouseMoved(screenX, screenY)
        return true
    }

}
