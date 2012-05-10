package rpgSession

import com.vaadin.*
import com.vaadin.ui.*

class IndexApplication extends Application {
    
    void init(){
        def window = new Window("Hello Vaadin!")
        setMainWindow window
        Button button = new Button("Click Meee")
        window.addComponent(button)
		
	Button button2 = new Button("Click Meee")
	window.addComponent(button2)
		
	Button button3 = new Button("Click Meee")
	window.addComponent(button3)
    }
}

