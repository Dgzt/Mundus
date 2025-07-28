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

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.pluginapi.ui.TextureGridListener
import com.mbrlabs.mundus.pluginapi.ui.TextureGridRightClickMenuItem

class TextureGridRightClickMenu(rightClickMenuItems: Array<TextureGridRightClickMenuItem>, val listener: TextureGridListener) : PopupMenu() {

    private var pos = -1

    init {
        for (rightClickMenuItem in rightClickMenuItems) {
            val menuItem = MenuItem(rightClickMenuItem.name)
            addItem(menuItem)

            menuItem.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    rightClickMenuItem.onClick(pos)
                }
            })
        }
    }

    fun show(pos: Int) {
        this.pos = pos
        showMenu(UI, Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat())
    }
}
