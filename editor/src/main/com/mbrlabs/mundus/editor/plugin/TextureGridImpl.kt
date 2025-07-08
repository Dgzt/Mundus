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

import com.kotcrab.vis.ui.VisUI
import com.mbrlabs.mundus.commons.utils.TextureProvider
import com.mbrlabs.mundus.pluginapi.ui.TextureGrid

class TextureGridImpl : com.mbrlabs.mundus.editor.ui.widgets.TextureGrid<TextureProvider>(40, 5), TextureGrid {

    init {
        background = VisUI.getSkin().getDrawable("menu-bg")
    }

    override fun addTexture(texture: TextureProvider) {
        val textureWithPosition = TextureGridItemWithPosition(texture, countTextures())
        super<com.mbrlabs.mundus.editor.ui.widgets.TextureGrid>.addTexture(textureWithPosition)
    }

    override fun removeTextures() {
        super.removeTextures()
    }

    override fun highlight(id: Int) {
        super.highlight(id)
    }

}
