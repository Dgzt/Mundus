/*
 * Copyright (c) 2021. See AUTHORS file.
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

package com.mbrlabs.mundus.editor.core.converter;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.dto.CustomComponentDTO;
import com.mbrlabs.mundus.commons.dto.GameObjectDTO;
import com.mbrlabs.mundus.commons.dto.TerrainComponentDTO;
import com.mbrlabs.mundus.commons.mapper.CustomComponentConverter;
import com.mbrlabs.mundus.commons.mapper.CustomPropertiesComponentConverter;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.SceneGraph;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.CustomPropertiesComponent;
import com.mbrlabs.mundus.commons.scene3d.components.LightComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainManagerComponent;
import com.mbrlabs.mundus.commons.utils.AssetUtils;
import com.mbrlabs.mundus.editor.scene3d.components.PickableModelComponent;
import com.mbrlabs.mundus.editor.scene3d.components.PickableTerrainComponent;
import com.mbrlabs.mundus.editor.scene3d.components.PickableWaterComponent;

import java.util.Map;

/**
 * The converter for game object.
 */
public class GameObjectConverter {

    private static final Vector3 tempVec = new Vector3();
    private static final Quaternion tempQuat = new Quaternion();

    /**
     * Converts {@link GameObjectDTO} to {@link GameObject}.
     */
    public static GameObject convert(GameObjectDTO dto, SceneGraph sceneGraph,
                                     Map<String, Asset> assets,
                                     Array<CustomComponentConverter> customComponentConverters) {
        final GameObject go = new GameObject(sceneGraph, dto.getName(), dto.getId());
        go.active = dto.isActive();

        // transformation
        final float[] transform = dto.getTransform();
        go.translate(transform[0], transform[1], transform[2]);
        go.rotate(transform[3], transform[4], transform[5], transform[6]);
        go.scale(transform[7], transform[8], transform[9]);

        // convert tags
        if (dto.getTags() != null || !dto.getTags().isEmpty()) {
            for (String tag : dto.getTags()) {
                go.addTag(tag);
            }
        }

        // convert components
        if (dto.getModelComponent() != null) {
            go.getComponents().add(ModelComponentConverter.convert(dto.getModelComponent(), go, assets));
        } else if (dto.getTerrainComponent() != null) {
            go.getComponents().add(TerrainComponentConverter.convert(dto.getTerrainComponent(), go, assets));
        } else if (dto.getTerrainManagerComponent() != null) {
            go.getComponents().add(TerrainManagerComponentConverter.convert(dto.getTerrainManagerComponent(), go));
        } else if (dto.getWaterComponent() != null) {
            go.getComponents().add(WaterComponentConverter.convert(dto.getWaterComponent(), go, assets));
        }

        // Convert custom properties component
        if (dto.getCustomPropertiesComponent() != null) {
            go.getComponents().add(CustomPropertiesComponentConverter.convert(dto.getCustomPropertiesComponent(), go));
        }

        // Convert light component
        if (dto.getLightComponent() != null) {
            LightComponent component = PickableLightComponentConverter.convert(dto.getLightComponent(), go);
            go.getComponents().add(component);
        }

        // Custom components
        if (dto.getCustomComponents() != null) {
            for (int i = 0; i < dto.getCustomComponents().size; ++i) {
                final CustomComponentDTO customComponentDTO = dto.getCustomComponents().get(i);
                final String componentType = customComponentDTO.getComponentType();

                for (int ii = 0; ii < customComponentConverters.size; ++ii) {
                    final CustomComponentConverter converter = customComponentConverters.get(ii);

                    if (componentType != null && componentType.equals(converter.getComponentType())) {
                        final Array<String> assetIds = customComponentDTO.getAssetIds();
                        final ObjectMap<String, Asset> assetMap = AssetUtils.getAssetsById(assetIds, assets);
                        final Component customComponent = converter.convert(go, customComponentDTO.getProperties(), assetMap);

                        if (customComponent != null) {
                            go.getComponents().add(customComponent);
                        }
                    }
                }

            }
        }

        // recursively convert children
        if (dto.getChilds() != null) {
            for (GameObjectDTO c : dto.getChilds()) {
                go.addChild(convert(c, sceneGraph, assets, customComponentConverters));
            }

            setupNeighborTerrains(dto, go);
        }

        return go;
    }

    /**
     * Setups neighbor terrains for all children if child has {@link TerrainComponent} component and it has set neighbor.
     *
     * @param goDto The DTO of game object.
     * @param go    The game object.
     */
    private static void setupNeighborTerrains(final GameObjectDTO goDto, final GameObject go) {
        for (final GameObjectDTO childDto : goDto.getChilds()) {
            final GameObject child = go.findChildById(childDto.getId());

            final TerrainComponentDTO terrainComponentDTO = childDto.getTerrainComponent();
            final TerrainComponent terrainComponent = (TerrainComponent) child.findComponentByType(Component.Type.TERRAIN);
            if (terrainComponent == null) return; // Happens if terra files deleted in file system

            if (terrainComponentDTO != null) {
                final Integer topNeighborId = terrainComponentDTO.getTopNeighborID();
                if (topNeighborId != null) {
                    final GameObject topNeighbor = go.findChildById(topNeighborId);
                    if (topNeighbor != null) {
                        terrainComponent.setTopNeighbor((TerrainComponent) topNeighbor.findComponentByType(Component.Type.TERRAIN));
                    }
                }

                final Integer rightNeighborId = terrainComponentDTO.getRightNeighborID();
                if (rightNeighborId != null) {
                    final GameObject rightNeighbor = go.findChildById(rightNeighborId);
                    if (rightNeighbor != null) {
                        terrainComponent.setRightNeighbor((TerrainComponent) rightNeighbor.findComponentByType(Component.Type.TERRAIN));
                    }
                }

                final Integer bottomNeighborId = terrainComponentDTO.getBottomNeighborID();
                if (bottomNeighborId != null) {
                    final GameObject bottomNeighbor = go.findChildById(bottomNeighborId);
                    if (bottomNeighbor != null) {
                        terrainComponent.setBottomNeighbor((TerrainComponent) bottomNeighbor.findComponentByType(Component.Type.TERRAIN));
                    }
                }

                final Integer leftNeighborId = terrainComponentDTO.getLeftNeighborID();
                if (leftNeighborId != null) {
                    final GameObject leftNeighbor = go.findChildById(leftNeighborId);
                    if (leftNeighbor != null) {
                        terrainComponent.setLeftNeighbor((TerrainComponent) leftNeighbor.findComponentByType(Component.Type.TERRAIN));
                    }
                }
            }
        }
    }

    /**
     * Converts {@link GameObject} to {@link GameObjectDTO}.
     */
    public static GameObjectDTO convert(
            GameObject go,
            Array<CustomComponentConverter> customComponentConverters
    ) {
        GameObjectDTO descriptor = new GameObjectDTO();
        descriptor.setName(go.name);
        descriptor.setId(go.id);
        descriptor.setActive(go.active);

        // translation
        go.getLocalPosition(tempVec);
        final float[] transform = descriptor.getTransform();
        transform[0] = tempVec.x;
        transform[1] = tempVec.y;
        transform[2] = tempVec.z;

        // rotation
        go.getLocalRotation(tempQuat);
        transform[3] = tempQuat.x;
        transform[4] = tempQuat.y;
        transform[5] = tempQuat.z;
        transform[6] = tempQuat.w;

        // scaling
        go.getLocalScale(tempVec);
        transform[7] = tempVec.x;
        transform[8] = tempVec.y;
        transform[9] = tempVec.z;

        // convert components
        for (Component c : go.getComponents()) {
            if (Component.Type.MODEL.name().equals(c.getType())) {
                descriptor.setModelComponent(ModelComponentConverter.convert((PickableModelComponent) c));
            } else if (Component.Type.TERRAIN.name().equals(c.getType())) {
                descriptor.setTerrainComponent(TerrainComponentConverter.convert((PickableTerrainComponent) c));
            } else if (Component.Type.WATER.name().equals(c.getType())) {
                descriptor.setWaterComponent(WaterComponentConverter.convert((PickableWaterComponent) c));
            } else if (Component.Type.LIGHT.name().equals(c.getType())) {
                descriptor.setLightComponent(PickableLightComponentConverter.convert((LightComponent) c));
            } else if (Component.Type.CUSTOM_PROPERTIES.name().equals(c.getType())) {
                descriptor.setCustomPropertiesComponent(CustomPropertiesComponentConverter.convert((CustomPropertiesComponent) c));
            } else if (Component.Type.TERRAIN_MANAGER.name().equals(c.getType())) {
                descriptor.setTerrainManagerComponent(TerrainManagerComponentConverter.convert((TerrainManagerComponent) c));
            } else if (c.getType() != null) {
                for (int i = 0; i < customComponentConverters.size; ++i) {
                    final CustomComponentConverter converter = customComponentConverters.get(i);

                    if (c.getType().equals(converter.getComponentType())) {
                        final OrderedMap<String, String> customComponentProperties = converter.convert(c);

                        if (customComponentProperties != null) {
                            if (descriptor.getCustomComponents() == null) {
                                descriptor.setCustomComponents(new Array<>());
                            }

                            final CustomComponentDTO customComponentDTO = new CustomComponentDTO();
                            customComponentDTO.setComponentType(c.getType());
                            customComponentDTO.setProperties(customComponentProperties);
                            customComponentDTO.setAssetIds(converter.getAssetIds(c));

                            descriptor.getCustomComponents().add(customComponentDTO);
                        }
                    }
                }
            }
        }

        // convert tags
        if (go.getTags() != null && !go.getTags().isEmpty()) {
            for (String tag : go.getTags()) {
                descriptor.getTags().add(tag);
            }
        }

        // recursively convert children
        if (go.getChildren() != null) {
            for (GameObject c : go.getChildren()) {
                descriptor.getChilds().add(convert(c, customComponentConverters));
            }
        }

        return descriptor;
    }
}
