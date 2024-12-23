/*
 * Copyright (c) 2024. See AUTHORS file.
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

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.mbrlabs.mundus.pluginapi.ui.LabelWidget
import com.mbrlabs.mundus.pluginapi.ui.WidgetAlign

class LabelWidgetImpl(val label: VisLabel) : LabelWidget {
    override fun setAlign(align: WidgetAlign): LabelWidget {
        when (align) {
            WidgetAlign.LEFT -> label.setAlignment(Align.left)
            WidgetAlign.CENTER -> label.setAlignment(Align.center)
            WidgetAlign.RIGHT -> label.setAlignment(Align.right)
        }

        return this
    }

    override fun setText(text: String): LabelWidget {
        label.setText(text)

        return this
    }
}
