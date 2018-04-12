package xerus.setup

import com.pty4j.PtyProcess
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import xerus.ktutil.dump
import xerus.ktutil.javafx.ui.controls.ExpandableView
import java.io.File

fun TextArea.serialize() = text.replace("\n\n", "\n \n")

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
	
	override fun serialize() = textArea.serialize()
	
}

class FileTab(text: String? = null) : SetupTab() {
	
	val fileName = TextField()
	val textArea = TextArea()
	
	init {
		addContent(HBox(Label("Filename: "), fileName))
		addContent(textArea)
		text?.split('\n', limit = 2)?.let {
			fileName.text = it[0]
			textArea.text = it.getOrNull(1)
		}
	}
	
	override fun execute() {
		File(fileName.text).writeText(textArea.text)
	}
	
	override fun serialize(): String =
			fileName.text + "\n" + textArea.serialize()
	
}

class LinkTab : SetupTab() {
	
	val view = ExpandableView()
	
	override fun execute() {
	}
	
	override fun serialize(): String {
	}
	
}

/*
class ReplaceTab : SetupTab() {
	
	
	
	override fun execute() {
	}
	
}
*/
