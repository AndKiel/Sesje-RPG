package rpgApp.windows

import com.vaadin.terminal.Sizeable
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent

public class YesNoDialog extends Window implements Button.ClickListener {
	private Callback callback;
	private Button yes = new Button("Yes", this);
	private Button no = new Button("No", this);

	public YesNoDialog(String caption, String question, Callback callback) {
		// Window settings
		super(caption);
		setCaption(caption)
		setModal(true);
		setResizable(false)
		setDraggable(false)

		this.callback = callback;

		if (question != null) {
			addComponent(new Label(question));
		}

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(yes);
		hl.addComponent(no);
		hl.setSpacing(true)
		addComponent(hl);
		VerticalLayout mainLayout = this.getContent()
		mainLayout.setSpacing(true)
		mainLayout.setComponentAlignment(hl, Alignment.BOTTOM_RIGHT);
		mainLayout.setMargin(true)

		setWidth(300, Sizeable.UNITS_PIXELS)
		center();
	}

	public void buttonClick(ClickEvent event) {
		if (getParent() != null) {
			((Window) getParent()).removeWindow(this);
		}
		callback.onDialogResult(event.getSource() == yes);
	}

	public interface Callback {

		public void onDialogResult(boolean resultIsYes);
	}

}