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

package com.mbrlabs.mundus.editor.core.helperlines

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent
import com.mbrlabs.mundus.commons.terrain.Terrain

class HexagonHelperLineShape(width: Int,
                             counterOffsetX: Int,
                             counterOffsetY: Int,
                             terrainComponent: TerrainComponent) : HelperLineShape(width, counterOffsetX, counterOffsetY, terrainComponent) {

    enum class Vector {
        BOTTOM_RIGHT,
        TOP_RIGHT,
        RIGHT,
    }

    companion object {
        val PATTERN = arrayOf(
                arrayOf(arrayOf(Vector.TOP_RIGHT, Vector.BOTTOM_RIGHT), arrayOf(),             arrayOf(),                                      arrayOf(Vector.RIGHT)),
                arrayOf(arrayOf(),                                      arrayOf(Vector.RIGHT), arrayOf(Vector.TOP_RIGHT, Vector.BOTTOM_RIGHT), arrayOf()            )
        )
    }

    override fun calculateIndicesNum(width: Int, terrain: Terrain): Int {
        var i = 0
        calculate(width, terrain.vertexResolution) { ++i }

        return i
    }

    override fun fillIndices(width: Int, indices: ShortArray, vertexResolution: Int) {
        var i = 0
        calculate(width, vertexResolution) {pos -> indices[i++] = pos}
    }

//    override fun calculateCenterOfHelperObjects(): Array<HelperLineCenterObject> {
//        val centerOfHelperObjects = Array<HelperLineCenterObject>()
//
//        val terrain = terrainComponent.terrainAsset.terrain
//        val vertexResolution = terrain.vertexResolution
//
//        val widthOffset = width * terrain.terrainWidth.toFloat() / (vertexResolution - 1).toFloat()
//        val depthOffset = width * terrain.terrainDepth.toFloat() / (vertexResolution - 1).toFloat()
//
//        addTopHalfHexagonHelperLineObjects(centerOfHelperObjects, widthOffset)
//        addLeftHalfHexagonHelperLineObjects(centerOfHelperObjects, widthOffset, depthOffset)
//
//        var terrainY = 2 * depthOffset
//        var cellY = 0
//
//        while (terrainY - depthOffset <= terrain.terrainDepth) {
//            var terrainX = 0f
//            var cellX = 0
//
//            while (terrainX + 1 <= terrain.terrainWidth) {
//                val posX = terrainX + 1.5f * widthOffset
//                val posY = 0f
//                val posZ = if (cellX % 2 == 1) terrainY - depthOffset else terrainY
//                val fullCell = posZ + depthOffset <= terrain.terrainWidth && posX + 1.5f * widthOffset <= terrain.terrainWidth
//                val pos = Vector3(posX, posY, posZ)
//                // Convert to world position
//                pos.mul(terrainComponent.modelInstance.transform)
//
//                val helperLineCenterObject = helperLineCenterObjectPool.obtain().initialize(cellX + counterOffsetX, cellY + counterOffsetY, pos, fullCell)
//                centerOfHelperObjects.add(helperLineCenterObject)
//
//                ++cellX
//                terrainX += 2 * widthOffset
//            }
//
//            ++cellY
//            terrainY += 2 * depthOffset
//        }
//
//        return centerOfHelperObjects
//    }


    override fun calculateCenterOfHelperObjects(): Array<HelperLineCenterObject> {
        val centerOfHelperObjects = Array<HelperLineCenterObject>()

        val terrain = terrainComponent.terrainAsset.terrain

        val terrainSystemWidthVertexResolution = calculateTerrainSystemWidthVertexResolution()
        val terrainSystemDepthVertexResolution = calculateTerrainSystemDepthVertexResolution()
        val rightTerrainChunksVertexResolution = calculateRightTerrainChunksVertexResolution()
        val bottomTerrainChunksVertexResolution = calculateBottomTerrainChunksVertexResolution()
        val currentTerrainChunkVertexResolution = terrain.vertexResolution
        val gridWidthSize = terrain.terrainWidth.toFloat() / (currentTerrainChunkVertexResolution - 1).toFloat()

        if (bottomTerrainChunksVertexResolution == 0) {
            addBottomHalfHexagonHelperLineObjects(centerOfHelperObjects, terrainSystemWidthVertexResolution, rightTerrainChunksVertexResolution, currentTerrainChunkVertexResolution, gridWidthSize)
        }

        if (rightTerrainChunksVertexResolution == 0) {
            addRightHalfHexagonHelperLineObjects(centerOfHelperObjects, terrainSystemDepthVertexResolution, rightTerrainChunksVertexResolution, currentTerrainChunkVertexResolution, gridWidthSize)
        }

        for ((cellX, x) in ((1.5 * width).toInt() until terrainSystemWidthVertexResolution step (2 * width)).withIndex()) {
            val zInit = if (cellX % 2 == 0) 2 * width else width

            for ((cellZ, z) in (zInit until terrainSystemDepthVertexResolution step (2 * width)).withIndex()) {

                if (x in rightTerrainChunksVertexResolution..rightTerrainChunksVertexResolution+currentTerrainChunkVertexResolution &&
                        z in bottomTerrainChunksVertexResolution..bottomTerrainChunksVertexResolution+currentTerrainChunkVertexResolution) {
                    val posX = (x - rightTerrainChunksVertexResolution) * gridWidthSize
                    val posY = 0f
                    val posZ = (z - bottomTerrainChunksVertexResolution) * gridWidthSize

                    val pos = Vector3(posX, posY, posZ)
                    // Convert to world position
                    pos.mul(terrainComponent.modelInstance.transform)

                    val helperLineCenterObject = helperLineCenterObjectPool.obtain().initialize(cellX + counterOffsetX, cellZ + counterOffsetY, pos, true)
                    centerOfHelperObjects.add(helperLineCenterObject)
                }
            }
        }

        return centerOfHelperObjects
    }

    private fun addBottomHalfHexagonHelperLineObjects(centerOfHelperObjects: Array<HelperLineCenterObject>,
                                                      terrainSystemWidthVertexResolution: Int,
                                                      rightTerrainChunksVertexResolution: Int,
                                                      currentTerrainChunkVertexResolution: Int,
                                                      gridWidthSize: Float) {

        for ((cellX, x) in ((1.5 * width).toInt() until terrainSystemWidthVertexResolution step (4 * width)).withIndex()) {
            if (x in rightTerrainChunksVertexResolution..rightTerrainChunksVertexResolution + currentTerrainChunkVertexResolution) {
                val posX = (x - rightTerrainChunksVertexResolution) * gridWidthSize
                val posY = 0f
                val posZ = -0.5f * width * gridWidthSize

                val pos = Vector3(posX, posY, posZ)
                // Convert to world position
                pos.mul(terrainComponent.modelInstance.transform)

                val helperLineCenterObject = helperLineCenterObjectPool.obtain().initialize(cellX + counterOffsetX, 0 + counterOffsetY, pos, false)
                centerOfHelperObjects.add(helperLineCenterObject)
            }
        }
    }

    private fun addRightHalfHexagonHelperLineObjects(centerOfHelperObjects: Array<HelperLineCenterObject>,
                                                     terrainSystemDepthVertexResolution: Int,
                                                     bottomTerrainChunksVertexResolution: Int,
                                                     currentTerrainChunkVertexResolution: Int,
                                                     gridWidthSize: Float) {

        for ((cellZ, z) in (0 until terrainSystemDepthVertexResolution step (4 * width)).withIndex()) {
            if (z in bottomTerrainChunksVertexResolution..bottomTerrainChunksVertexResolution + currentTerrainChunkVertexResolution) {
                val posX = -0.5f * width * gridWidthSize
                val posY = 0f
                val posZ = (1f * width + (z - bottomTerrainChunksVertexResolution)) * gridWidthSize

                val pos = Vector3(posX, posY, posZ)
                // Convert to world position
                pos.mul(terrainComponent.modelInstance.transform)

                val helperLineCenterObject = helperLineCenterObjectPool.obtain().initialize(0 + counterOffsetX, cellZ + counterOffsetY, pos, false)
                centerOfHelperObjects.add(helperLineCenterObject)
            }
        }
    }

    private fun calculate(width: Int, vertexResolution: Int, method: (pos: Short) -> Unit) {
        val bottomTerrainChunksVertexResolution = calculateBottomTerrainChunksVertexResolution()
        val rightTerrainChunksVertexResolution = calculateRightTerrainChunksVertexResolution()

        val yUsedWidth = bottomTerrainChunksVertexResolution % width
        val xUsedWidth = rightTerrainChunksVertexResolution % width

        val yInit = if (yUsedWidth == 0) 0 else -yUsedWidth
        val xInit = if (xUsedWidth == 0) 0 else -xUsedWidth

        var patternY = ((bottomTerrainChunksVertexResolution - yUsedWidth) / width) % PATTERN.size

        for (y in yInit until vertexResolution + width step width) {
            var patternX = ((rightTerrainChunksVertexResolution - xUsedWidth) / width) % PATTERN.get(patternY).size

            for (x in xInit until vertexResolution step width) {
                val mainCurrent = y * vertexResolution + x

                for (pattern in PATTERN.get(patternY).get(patternX)) {
                    var current = mainCurrent
                    for (w in 0 until  width) {
                        val next = getNext(current, pattern, vertexResolution)

                        if (current >= 0 && next >= 0 && x + w >= 0) {
                            if (isOnMap(current, vertexResolution) && isOk(current, next, vertexResolution, pattern) && isOnMap(next, vertexResolution)) {
                                method.invoke(current.toShort())
                                method.invoke(next.toShort())
                            } else if (!isOk(current, next, vertexResolution, pattern)) {
                                break
                            }
                        } else if (current == -1 && next == 0) {
                            break
                        }

                        current = next
                    }
                }

                patternX = ++patternX % PATTERN.get(patternY).size
            }

            patternY = ++patternY % PATTERN.size
        }
    }

    private fun getNext(current: Int, vector: Vector, vertexResolution: Int): Int {
        return when(vector) {
            Vector.BOTTOM_RIGHT -> current + vertexResolution + 1
            Vector.TOP_RIGHT -> current - vertexResolution + 1
            Vector.RIGHT -> current + 1
        }
    }

    private fun isOnMap(current: Int, vertexResolution: Int): Boolean  = getRow(current, vertexResolution) in 0 until vertexResolution

    private fun isOk(current: Int, next: Int, vertexResolution: Int, pattern: Vector): Boolean {
        val currentRow = getRow(current, vertexResolution)
        val nextRow = getRow(next, vertexResolution)

        return when(pattern) {
            Vector.BOTTOM_RIGHT -> currentRow + 1 == nextRow
            Vector.TOP_RIGHT -> currentRow == nextRow + 1
            Vector.RIGHT -> currentRow == nextRow
        }
    }

    private fun getRow(cell: Int, vertexResolution: Int) = cell / vertexResolution + if (cell < 0) -1 else 0
}
