/*
 * Copyright (c) 2023. See AUTHORS file.
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


package com.mbrlabs.mundus.editor.tools.terrain;

import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush;

/**
 * @author JamesTKhan
 * @version June 28, 2023
 */
public abstract class RadiusTerrainTool implements TerrainTool {
    protected static final TerrainBrush.TerrainModifyComparison radiusDistanceComparison = (terrainBrush, vertexPos, localBrushPos) -> {
        float distance = vertexPos.dst(localBrushPos);
        return distance <= terrainBrush.getRadius();
    };

}
