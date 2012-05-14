package rpgSession

import com.vaadin.*
import com.vaadin.ui.*
import com.vaadin.ui.Button.ClickListener

class IndexApplication extends Application implements ClickListener {

	void init(){
		def window = new Window("RPG Sessions")
		setMainWindow window
	}

	public void buttonClick(Button.ClickEvent event) {
		final Button source = event.getButton()

	}
}

