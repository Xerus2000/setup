package xerus.setup

import com.terminalfx.TerminalTab
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.ObservableList
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import xerus.ktutil.javafx.properties.listen
import xerus.ktutil.javafx.ui.App
import xerus.setup.tabs.*
import java.io.File
import java.util.prefs.Preferences
import kotlin.reflect.KClass

val pref = Preferences.userNodeForPackage(ExecTab::class.java)
val storageFile
	get() = File(pref.get("file", "")).takeIf { it.exists() }

val double = SimpleDoubleProperty(2.0)

lateinit var tabs: ObservableList<Tab>
inline fun <reified T: SerializableTab> tab(name: String = ""): Collection<T> =
		tabs.filterIsInstance(T::class.java).filter { it.text.contains(name, true) }

fun main(args: Array<String>) {
	App.launch("Setup Utility", scene = {
		val core = TabPane()
		core.side = Side.LEFT
		tabs = core.tabs
		tabs.add(TerminalTab("Terminal"))
		val file = storageFile
		if (file != null) {
			file.readText().split("\n\n").forEach {
				val split = it.split('\n', limit = 3)
				val tab = SetupType.newTab(split[0])
				tab.deserialize(split[2])
				tab.text = split[1]
			}
		} else
			core.tabs.add(ExecTab("fdisk -l\nwhereami"))
		
		//PreferencesFx.of(App::class.java, Category.of("Bla", Setting.of("double", double)))
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
		it.setOnHiding { Platform.exit() }
	})
}

