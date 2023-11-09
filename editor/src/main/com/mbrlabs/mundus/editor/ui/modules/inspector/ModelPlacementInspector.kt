package com.mbrlabs.mundus.editor.ui.modules.inspector

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextField
import com.mbrlabs.mundus.commons.assets.ModelAsset

class ModelPlacementInspector : VisTable() {

    private val nameLabel = VisTextField()

    init {
        initUI()
    }

    fun setup(model: ModelAsset) {
        nameLabel.text = model.name
    }

    fun getModelName(): String = nameLabel.text

    private fun initUI() {
        align(Align.top)

        add(VisLabel("Name:")).pad(7f, 7f, 7f, 4f)
        add(nameLabel).pad(7f, 0f, 7f, 7f).growX()
    }

}
