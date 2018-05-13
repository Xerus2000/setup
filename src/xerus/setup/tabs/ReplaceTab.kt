package xerus.setup.tabs

import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import xerus.ktutil.helpers.ArrayMap
import xerus.ktutil.javafx.ui.controls.ExpandableView

class ReplaceTab : SetupTab() {
	
	val regexCheck = CheckBox("Enable Regex")
	val files = ExpandableView({
		arrayOf(it.textField(), it.button())
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
