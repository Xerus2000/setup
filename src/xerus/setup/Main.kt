package xerus.setup

import com.dlsc.preferencesfx.PreferencesFx
import com.dlsc.preferencesfx.model.Category
import com.dlsc.preferencesfx.model.Setting
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import xerus.ktutil.javafx.ui.App
import java.io.File
import java.util.prefs.Preferences

val pref = Preferences.userNodeForPackage(ExecTab::class.java)
val double = SimpleDoubleProperty(2.0)

fun main(args: Array<String>) {
	App.launch("Setup Utility", scene = {
		val core = TabPane()
		core.side = Side.LEFT
		core.tabs.add(TerminalTab("Terminal"))
		val file = File(pref.get("file", ""))
		if (file.exists()) {
			file.readText().split("\n\n").forEach {
				val split = it.split('\n', limit = 3)
				SetupType.valueOf(split[0]).tab(split[2]).text = split[1]
			}
		} else
			core.tabs.add(ExecTab("fdisk -l\nwhereami"))
		
		PreferencesFx.of(App::class.java,
				Category.of("Bla", Setting.of("double", double)))
		//core.tabs.add(Tab("Settings",))
		Scene(core)
	})
}

enum class SetupType(val tab: (String?) -> SetupTab) {
	COMMANDS({ ExecTab(it) }),
	CREATEFILE({ FileTab() }),
	REPLACE({ ReplaceTab() }),
	LINK({ LinkTab() })
}

val runtime = Runtime.getRuntime()

class ExecTab(text: String? = "") : SetupTab() {
	
	val textArea = TextArea(text)
	
	init {
		addContent(textArea)
		text?.split('\n')?.let {
			TextArea().paragraphs.addAll(it)
		}
	}
	
	override fun execute() {
		for (line in textArea.paragraphs) {
			val exec = PtyProcess.exec(line.split(' ').toTypedArray())
			exec.errorStream.dump()
			exec.inputStream.dump()
		}
		println("Done")
	}
	
}

