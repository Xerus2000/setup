package xerus.setup.tabs

import javafx.scene.layout.VBox
import xerus.ktutil.javafx.ui.controls.ExpandableView
import java.nio.file.Files

class LinkTab : SetupTab() {
	
	val view = ExpandableView({
		arrayOf(VBox(it.first.createHBox(), it.second.createHBox()))
	}, { createLink() })
	
	init {
		addContent(view)
	}
	
	fun createLink(location: String = "", target: String = "") =
			chooser("Location", location) to chooser("Target", target)
	
	override fun execute() {
		for (pair in view.rows)
			Files.createSymbolicLink(pair.first.path, pair.second.path)
	}
	
	override fun deserialize(string: String) {
		string.split('\n').forEach {
			val split = it.split('"')
			view.createRow(row = createLink(split[1], split[3]))
		}
	}
	
	override fun serialize() =
			view.rows.joinToString("\n") {
				"\"${it.first.selectedFile}\" \"${it.second.selectedFile}\""
			}
	
}