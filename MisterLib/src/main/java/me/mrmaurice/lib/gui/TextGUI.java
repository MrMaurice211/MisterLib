package me.mrmaurice.lib.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.mrmaurice.lib.MisterLib;
import me.mrmaurice.lib.config.Message;

public class TextGUI {

	private Player reader;
	private ConversationFactory conv;

	public TextGUI(Player player) {
		reader = player;
		conv = new ConversationFactory(MisterLib.getPlugin());
	}

	public TextGUI prefix() {
		conv.withPrefix(arg -> Message.of("").pref().color().toString());
		return this;
	}

	public TextGUI prompt(Prompt prompt) {
		conv.withFirstPrompt(prompt);
		return this;
	}

	public <T> TextGUI onEnd(BiConsumer<T, ConversationAbandonedEvent> func, T obj) {
		conv.addConversationAbandonedListener(event -> func.accept(obj, event));
		return this;
	}

	public TextGUI onEnd(Consumer<ConversationAbandonedEvent> func) {
		conv.addConversationAbandonedListener(event -> func.accept(event));
		return this;
	}

	public TextGUI timeout(int timeout) {
		conv.withTimeout(timeout);
		return this;
	}

	public TextGUI echo(boolean echo) {
		conv.withLocalEcho(echo);
		return this;
	}

	public TextGUI exitKey(String sequence) {
		conv.withEscapeSequence(sequence);
		return this;
	}

	public void start() {
		conv.buildConversation(reader).begin();
	}

}
