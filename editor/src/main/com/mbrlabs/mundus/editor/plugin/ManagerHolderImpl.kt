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

package com.mbrlabs.mundus.editor.plugin

import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.pluginapi.manager.AssetManager
import com.mbrlabs.mundus.pluginapi.manager.ManagerHolder
import com.mbrlabs.mundus.pluginapi.manager.PluginEventManager
import com.mbrlabs.mundus.pluginapi.manager.ToasterManager
import com.mbrlabs.mundus.pluginapi.manager.ToolManager
import com.mbrlabs.mundus.pluginapi.manager.ViewportManager

class ManagerHolderImpl : ManagerHolder {
    override fun getEventManager(): PluginEventManager {
        return PluginEventManager { listener ->
            Mundus.registerEventListener(listener)
        }
    }

    override fun getAssetManager(): AssetManager = AssetManagerImpl()

    override fun getToasterManager(): ToasterManager = ToasterManagerImpl()

    override fun getToolManager(): ToolManager = PluginToolManagerImpl()

    override fun getViewportManager(): ViewportManager = ViewportManagerImpl()
}
