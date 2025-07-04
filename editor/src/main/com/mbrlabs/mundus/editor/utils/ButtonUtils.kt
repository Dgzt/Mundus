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

package com.mbrlabs.mundus.editor.utils

object ButtonUtils {

    fun buttonToString(buttonCode: Int): String {
        return when (buttonCode) {
            0 -> "LEFT"
            1 -> "RIGHT"
            2 -> "MIDDLE"
            3 -> "BACK"
            4 -> "FORWARD"
            else -> "UNKNOWN"
        }
    }

    fun buttonStringToButtonKey(text: String): Int {
        return when(text) {
            "LEFT" -> 0
            "RIGHT" -> 1
            "MIDDLE" -> 2
            "BACK" -> 3
            "FORWARD" -> 4
            else -> -1
        }
    }
}
