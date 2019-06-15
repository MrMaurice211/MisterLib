package me.mrmaurice.lib.gui.spinners;

import lombok.Getter;
import lombok.Setter;
import me.mrmaurice.lib.gui.MenuGUI;

@Getter
@Setter
public abstract class AbstractSpinner<T> extends MenuGUI {

	protected T value;

}
