package xerus.setup

import xerus.setup.tabs.*
import kotlin.reflect.KClass

enum class SetupType(val tab: KClass<out SetupTab>) {
	COMMANDS(ExecTab::class),
	CREATEFILE(FileTab::class),
	REPLACE(ReplaceTab::class),
	LINK(LinkTab::class);
	
	companion object {
		fun newTab(type: String) = valueOf(type.toUpperCase()).tab.java.newInstance()
	}
}