package me.mrmaurice.lib.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.mrmaurice.lib.MisterLib;
import me.mrmaurice.lib.config.Message;

public class TextGUI extends GUI<Conversation> {

	private ConversationFactory factory;
	private Player player;
	private Conversation conversation;

	public TextGUI(Player player) {
		this.player = player;
		factory = new ConversationFactory(MisterLib.getPlugin());
	}

	public TextGUI prefix() {
		factory.withPrefix(arg -> Message.of("").pref().color().toString());
		return this;
	}

	public TextGUI prompt(Prompt prompt) {
		factory.withFirstPrompt(prompt);
		return this;
	}

	public <T> TextGUI onEnd(BiConsumer<T, ConversationAbandonedEvent> func, T obj) {
		factory.addConversationAbandonedListener(event -> func.accept(obj, event));
		return this;
	}

	public TextGUI onEnd(Consumer<ConversationAbandonedEvent> func) {
		factory.addConversationAbandonedListener(event -> func.accept(event));
		return this;
	}

	public TextGUI timeout(int timeout) {
		factory.withTimeout(timeout);
		return this;
	}

	public TextGUI echo(boolean echo) {
		factory.withLocalEcho(echo);
		return this;
	}

	public TextGUI exitKey(String sequence) {
		factory.withEscapeSequence(sequence);
		return this;
	}

	@Override
	public Conversation build() {
		if (conversation != null)
			return conversation;
		return conversation = factory.buildConversation(player);
	}

	@Override
	public void close(Player player) {
		player.abandonConversation(conversation);
	}

}
