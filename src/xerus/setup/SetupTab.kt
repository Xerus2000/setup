package xerus.setup

import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.layout.VBox
import xerus.ktutil.javafx.createButton

abstract class SetupTab : Tab() {
	
	protected val content = VBox(createButton("Run") { execute() })
	
	init {
		setContent(content)
	}
	
	fun addContent(vararg nodes: Node) {
		for (node in nodes)
			content.children.add(content.children.size - 1, node)
	}
	
	abstract fun execute()
	
	abstract fun serialize(): String
	
}