package xerus.setup.tabs

import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import java.io.File

class FileTab : SetupTab() {
	
	val fileName = TextField()
	val textArea = TextArea()
	
	init {
		addContent(HBox(Label("Filename: "), fileName))
		addContent(textArea)
	}
	
	override fun execute() {
		File(fileName.text).writeText(textArea.text)
	}
	
	override fun deserialize(string: String) {
		string.split('\n', limit = 2).let {
			fileName.text = it[0]
			textArea.text = it.getOrNull(1)
		}
	}
	
	override fun serialize(): String =
			fileName.text + "\n" + textArea.serialize()
	
}