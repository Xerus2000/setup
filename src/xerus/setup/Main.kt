package xerus.setup

import com.dlsc.preferencesfx.PreferencesFx
import com.dlsc.preferencesfx.model.Category
import com.dlsc.preferencesfx.model.Setting
import com.terminalfx.TerminalTab
import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.ObservableList
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import xerus.ktutil.javafx.properties.listen
import xerus.ktutil.javafx.ui.App
import java.io.File
import java.util.prefs.Preferences

val pref = Preferences.userNodeForPackage(ExecTab::class.java)
val storageFile
	get() = File(pref.get("file", "")).takeIf { it.exists() }

val double = SimpleDoubleProperty(2.0)
lateinit var tabs: ObservableList<Tab>

fun main(args: Array<String>) {
	App.launch("Setup Utility", scene = {
		val core = TabPane()
		core.side = Side.LEFT
		tabs = core.tabs
		core.tabs.add(TerminalTab("Terminal"))
		val file = storageFile
		if (file != null) {
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
	}, stager = {
		it.focusedProperty().listen {
			if (!it) {
				storageFile?.bufferedWriter()?.use {
					for (tab in tabs) {
						it.appendln((tab as SetupTab).serialize())
					}
				}
			}
		}
	})
}

enum class SetupType(val tab: (String) -> SetupTab) {
	COMMANDS({ ExecTab(it) }),
	CREATEFILE({ FileTab(it) }),
	//REPLACE({ ReplaceTab() }),
	//LINK({ LinkTab() })
}
