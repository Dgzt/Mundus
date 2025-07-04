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

import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.ModelComponent
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.commons.scene3d.components.WaterComponent
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.tools.picker.GameObjectPicker
import com.mbrlabs.mundus.pluginapi.manager.GameObjectPickerManager

class GameObjectPickerManagerImpl() : GameObjectPickerManager {

    private val goPicker = Mundus.inject<GameObjectPicker>()
    private val projectManager = Mundus.inject<ProjectManager>()

    override fun pick(
        screenX: Int,
        screenY: Int,
        vararg componentTypes: GameObjectPickerManager.ComponentType
    ): GameObject? {
        if (componentTypes.isNotEmpty()) {
            goPicker.setIgnoreFilter {
                component -> !componentTypes.contains(GameObjectPickerManager.ComponentType.TERRAIN) && component is TerrainComponent ||
                    !componentTypes.contains(GameObjectPickerManager.ComponentType.WATER) && component is WaterComponent ||
                    !componentTypes.contains(GameObjectPickerManager.ComponentType.MODEL) && component is ModelComponent
            }
        }

        val go = goPicker.pick(projectManager.current().currScene, screenX, screenY)

        if (componentTypes.isNotEmpty()) {
            goPicker.clearIgnoreFilter()
        }

        return go
    }
}
