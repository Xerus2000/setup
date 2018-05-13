package xerus.setup.tabs

import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import xerus.ktutil.helpers.Serializable
import xerus.ktutil.javafx.createButton
import xerus.ktutil.javafx.ui.App
import xerus.ktutil.javafx.ui.FileChooser
import java.io.File

fun TextArea.serialize() = text.replace("\n\n", "\n \n")

fun chooser(name: String, file: String = "") =
		FileChooser(App.stage, File(file), "", name)

abstract class SerializableTab : Tab(), Serializable<String>

abstract class SetupTab : SerializableTab() {
	
	protected val content = VBox(createButton("Run") { execute() }.also { it.maxWidth = Double.MAX_VALUE })
	
	init {
		setContent(content)
	}
	
	fun addContent(vararg nodes: Node) {
		for (node in nodes)
			content.children.add(content.children.size - 1, node)
	}
	
	abstract fun execute()
	
}
