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

package com.mbrlabs.mundus.editor.core.registry;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Global Mundus settings.
 *
 * @author Marcus Brummer
 * @version 07-06-2016
 */
public class Settings {

    private String fbxConvBinary = "";
    private KeyboardLayout keyboardLayout;
    private ObjectMap<String, String> customKeyboardShortcuts = new ObjectMap<>();

    public String getFbxConvBinary() {
        return fbxConvBinary;
    }

    public void setFbxConvBinary(String fbxConvBinary) {
        this.fbxConvBinary = fbxConvBinary;
    }

    public KeyboardLayout getKeyboardLayout() {
        return keyboardLayout;
    }

    public void setKeyboardLayout(KeyboardLayout keyboardLayout) {
        this.keyboardLayout = keyboardLayout;
    }

    public ObjectMap<String, String> getCustomKeyboardShortcuts() {
        return customKeyboardShortcuts;
    }

    public void setCustomKeyboardShortcuts(ObjectMap<String, String> customKeyboardShortcuts) {
        this.customKeyboardShortcuts = customKeyboardShortcuts;
    }
}
