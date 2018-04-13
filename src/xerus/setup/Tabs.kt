package xerus.setup

import com.pty4j.PtyProcess
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import xerus.ktutil.dump
import xerus.ktutil.helpers.ArrayMap
import xerus.ktutil.javafx.ui.App
import xerus.ktutil.javafx.ui.FileChooser
import xerus.ktutil.javafx.ui.controls.ExpandableView
import java.io.File
import java.nio.file.Files

fun TextArea.serialize() = text.replace("\n\n", "\n \n")

fun chooser(name: String, file: String = ".") =
		FileChooser(App.stage, File(file), "", name)

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

class LinkTab : SetupTab() {
	
	val view = ExpandableView<Pair<FileChooser, FileChooser>>({
		arrayOf(VBox(it.first.createHBox(), it.second.createHBox()))
	}, { createLink() })
	
	init {
		addContent(view)
	}
	
	fun createLink(location: String = ".", target: String = ".") =
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

class ReplaceTab : SetupTab() {
	
	val regexCheck = CheckBox("Enable Regex")
	val files = ExpandableView({
		arrayOf(it.textField, it.button)
	}, {
		chooser("File")
	})
	val replaceTerms = ExpandableView {
		arrayOf(TextField(), TextField())
	}
	
	
	init {
		addContent(regexCheck)
		addContent(files)
		addContent(replaceTerms)
	}
	
	override fun execute() {
		val regexes = ArrayMap<Regex, String>()
		if (regexCheck.isSelected) {
			replaceTerms.rows.forEach {
				regexes[Regex(it[0].text)] = it[1].text
			}
		}
		files.rows.forEach { chooser ->
			val text = chooser.file.readText()
			if (regexCheck.isSelected)
				regexes.forEach { regex, replacement -> text.replace(regex, replacement) }
			else
				replaceTerms.rows.forEach { text.replace(it[0].text, it[1].text) }
		}
	}
	
	override fun deserialize(string: String) {
	}
	
	override fun serialize(): String {
		return ""
	}
	
}
