package me.mrmaurice.lib.packets;

public abstract class PacketHook {

	private static PacketHook instance;

	public PacketHook() {
		if (instance != null)
			throw new IllegalStateException("Cannot instantiate PacketListener twice");
		instance = this;
		startListening();
	}

	public void disable() {
		instance = null;
		stopListening();
	}

	protected abstract void startListening();

	protected abstract void stopListening();

}
