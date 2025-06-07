package com.mbrlabs.mundus.editor.plugin

import com.badlogic.gdx.graphics.Texture
import com.mbrlabs.mundus.commons.utils.TextureProvider

data class TextureGridItemWithPosition(val texture: TextureProvider, val pos: Int) : TextureProvider {
    override fun getTexture(): Texture = texture.texture
}
