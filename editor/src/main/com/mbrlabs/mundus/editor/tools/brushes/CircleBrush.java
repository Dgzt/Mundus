/*
 * Copyright (c) 2016. See AUTHORS file.
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

package com.mbrlabs.mundus.editor.tools.brushes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mbrlabs.mundus.editor.core.project.ProjectManager;
import com.mbrlabs.mundus.editor.history.CommandHistory;
import com.mbrlabs.mundus.editor.tools.picker.GameObjectPicker;
import com.mbrlabs.mundus.editor.utils.Fa;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcus Brummer
 * @version 05-02-2016
 */
public class CircleBrush extends TerrainBrush {

    public CircleBrush(ProjectManager projectManager, CommandHistory history, GameObjectPicker goPicker) {
        super(projectManager, history, Gdx.files.internal("brushes/circle.png"), goPicker);
    }

    @NotNull
    @Override
    public String getName() {
        return "Circle brush";
    }

    @NotNull
    @Override
    public Drawable getIcon() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String getIconFont() {
        return Fa.Companion.getCIRCLE();
    }


}
