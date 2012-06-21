package rpgApp.windows

import java.text.SimpleDateFormat

import rpgApp.data.SessionItem
import rpgApp.main.IndexApplication
import rpgApp.utils.ChatEntry

import com.github.wolfie.refresher.Refresher
import com.github.wolfie.refresher.Refresher.RefreshListener
import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Window
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.themes.Reindeer

class ChatWindow extends Window implements Button.ClickListener {
	private IndexApplication app
	private SessionItem sessionItem

	private Panel chatLayout
	private TextField chatInput
	private Button send
	private Button showHistory
	private Button throwDice
	private Boolean historyFlag
	private Date chatStart
	private Date lastMessageDate
	
	Random rand = new Random(System.currentTimeMillis())

	ChatWindow(IndexApplication app, SessionItem s) {
		super("Chat")
		this.app = app
		this.setStyleName(Reindeer.WINDOW_BLACK)
		this.sessionItem = s
		setCaption("Chat #"+s.getId()+" - "+s.getSystem())
		setWidth("900px")
		setHeight("650px")
		setModal(true)
		setResizable(false)
		setDraggable(false)
		center()
		getContent().setMargin(true)
		getContent().setSpacing(true)

		historyFlag = false
		chatStart = new Date()
		lastMessageDate = new Date()

		VerticalLayout vl = this.getContent()
		vl.setSizeFull()

		chatLayout = new Panel()
		chatLayout.setStyleName(Reindeer.PANEL_LIGHT)
		chatLayout.getContent().setMargin(false, true, false, true)
		chatLayout.getContent().setSpacing(true)
		chatLayout.setSizeFull()
		chatInput = new TextField()
		chatInput.setWidth("100%")
		chatInput.focus()
		send = new Button("Send")
		send.setClickShortcut(KeyCode.ENTER);
		send.addStyleName("primary");
		send.addListener((Button.ClickListener) this)
		showHistory = new Button("Show history")
		showHistory.addListener((Button.ClickListener) this)
		throwDice = new Button("Throw a dice")
		throwDice.addListener((Button.ClickListener) this)

		HorizontalLayout footer = new HorizontalLayout()
		footer.setSizeFull()
		footer.setSpacing(true)
		footer.addComponent(chatInput)
		footer.addComponent(send)
		footer.addComponent(showHistory)
		footer.addComponent(throwDice)
		footer.setExpandRatio(chatInput, 1.0f)
		footer.setComponentAlignment(chatInput, Alignment.MIDDLE_CENTER)
		footer.setComponentAlignment(send, Alignment.MIDDLE_CENTER)

		vl.addComponent(chatLayout)
		Label grid = new Label("<hr/>", Label.CONTENT_XHTML)
		vl.addComponent(grid)
		vl.setComponentAlignment(grid, Alignment.MIDDLE_CENTER)
		vl.addComponent(footer)
		vl.setExpandRatio(chatLayout, 0.9f)
		vl.setExpandRatio(grid, 0.05f)
		vl.setExpandRatio(footer, 0.05f)

		final Refresher chatRefresher = new Refresher();
		chatRefresher.setRefreshInterval(500);
		chatRefresher.addListener(new ChatRefreshListener());
		addComponent(chatRefresher);

		refreshChat()
	}

	public class ChatRefreshListener implements RefreshListener {
		public void refresh(final Refresher source) {
			int index = Collections.binarySearch(app.roomIndexes, sessionItem.getId())
			List<ChatEntry> entries = app.chatEntries.get(index)
			Date lastEntryDate
			if(entries.size() > 0) {
				lastEntryDate = entries.get(entries.size()-1).getTimeStamp()

				if(lastMessageDate.compareTo(lastEntryDate) != 0) {
					refreshChat()
				}

				lastMessageDate = entries.get(entries.size()-1).getTimeStamp()
			} else {
				lastMessageDate = new Date()
			}
		}
	}

	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton()
		switch(source) {
			case send:
				if(chatInput.getValue() != null && !chatInput.getValue().equals("")) {
					int index = Collections.binarySearch(app.roomIndexes, sessionItem.getId())
					ChatEntry entry = new ChatEntry(app.security.getContextNickname(), chatInput.getValue(), new Date())
					app.chatEntries.get(index).add(entry)
					chatInput.setValue("")
					refreshChat()
				}
				break
			case showHistory:
				if(historyFlag) {
					showHistory.setCaption("Show history")
					historyFlag = false
				} else {
					showHistory.setCaption("Hide history")
					historyFlag = true
				}
				refreshChat()
				break
			case throwDice:
				// Dice range
				int n = 11
				int roll = rand.nextInt(n)+2
			
				int index = Collections.binarySearch(app.roomIndexes, sessionItem.getId())
				ChatEntry entry = new ChatEntry(app.security.getContextNickname(), "Throws "+roll+" on a dices", new Date())
				app.chatEntries.get(index).add(entry)
				break
		}
	}

	void refreshChat() {
		boolean flag = true

		chatLayout.removeAllComponents()
		int index = Collections.binarySearch(app.roomIndexes, sessionItem.getId())
		List<ChatEntry> entries = app.chatEntries.get(index)
		for(ChatEntry entry in entries) {
			if(!historyFlag) {
				if(chatStart.compareTo(entry.getTimeStamp()) > 0) {
					continue
				}
			}

			if(historyFlag && flag && (chatStart.compareTo(entry.getTimeStamp()) < 0)) {
				flag = false
				Panel p = new Panel()
				p.setStyleName("history")
				Label history = new Label("--------------------------------------------------------------------------------------- HISTORY ---------------------------------------------------------------------------------------")
				p.addComponent(history)
				VerticalLayout vl = p.getContent()
				vl.setMargin(false)
				chatLayout.addComponent(p)
			}

			if(entry.getNickname().equals(app.security.getContextNickname())) {
				print(entry, false)
			} else {
				print(entry, true)
			}
		}
	}

	private void print(final ChatEntry entry, boolean diffrent) {
		final Date timestamp = entry.getTimeStamp();
		final String name = entry.getNickname();
		final String message = entry.getMessage();

		final Panel p = new Panel()
		if(diffrent) {
			p.setStyleName("diffrent")
		}
		final HorizontalLayout chatLine = new HorizontalLayout();
		chatLine.setMargin(false, false, false, true)
		p.setContent(chatLine)

		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		final Label timeLabel = new Label("[ "+dateFormat.format(timestamp)+" ]");
		timeLabel.setWidth("120px");
		chatLine.addComponent(timeLabel);

		final Label nameLabel = new Label("<b>"+name + ": </b>", Label.CONTENT_XHTML);
		nameLabel.setWidth("100px");
		chatLine.addComponent(nameLabel);

		final Label messageLabel = new Label(message);
		messageLabel.setWidth("550px")
		chatLine.addComponent(messageLabel);
		chatLine.setExpandRatio(messageLabel, 1);

		chatLayout.addComponent(p);
		this.scrollIntoView(chatLine)
	}
}