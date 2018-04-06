package xerus.setup

import com.dlsc.preferencesfx.PreferencesFx
import com.dlsc.preferencesfx.model.Category
import com.dlsc.preferencesfx.model.Setting
import com.pty4j.PtyProcess
import com.terminalfx.TerminalTab
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import xerus.ktutil.dump
import xerus.ktutil.javafx.createButton
import xerus.ktutil.javafx.ui.App

val double = SimpleDoubleProperty(2.0)

fun main(args: Array<String>) {
	App.launch("Setup Utility", scene = {
		val core = TabPane()
		core.side = Side.LEFT
		core.tabs.add(TerminalTab("Terminal"))
		core.tabs.add(ExecTab("Test", "fdisk -l\nwhereami"))
		
		PreferencesFx.of(App::class.java,
				Category.of("Bla", Setting.of("double", double))).show()
		//core.tabs.add(Tab("Settings",))
		Scene(core)
	})
}

val runtime = Runtime.getRuntime()

class ExecTab(name: String, text: String = ""): Tab(name) {
	
	val textArea = TextArea(text)
	val box = VBox(textArea, createButton("Run") { execute() })
	
	init {
		content = box
		TextArea().paragraphs
	}
	
	fun execute() {
		for (line in textArea.paragraphs) {
			val exec = PtyProcess.exec(line.split(' ').toTypedArray())
			exec.errorStream.dump()
			exec.inputStream.dump()
		}
		println("Done")
	}
	
}