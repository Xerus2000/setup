package xerus.setup.tabs

import com.pty4j.PtyProcess
import javafx.scene.control.TextArea
import xerus.ktutil.dump

class ExecTab(text: String = "") : SetupTab() {
	
	val textArea = TextArea(text)
	
	init {
		addContent(textArea)
	}
	
	override fun execute() {
		for (line in textArea.paragraphs) {
			val exec = PtyProcess.exec(line.split(' ').toTypedArray())
			exec.errorStream.dump()
			exec.inputStream.dump()
		}
		println("Done")
	}
	
	override fun deserialize(string: String) {
		textArea.text = string
	}
	
	override fun serialize() = textArea.serialize()
	
}