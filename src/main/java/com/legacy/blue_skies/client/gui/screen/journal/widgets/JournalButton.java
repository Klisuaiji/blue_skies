package com.legacy.blue_skies.client.gui.screen.journal.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.network.chat.Component;

public class JournalButton extends Button {
   public JournalButton(int x, int y, int width, int height, Component label, OnPress pressedAction) {
      super(x, y, width, height, label, pressedAction, Button.DEFAULT_NARRATION);
   }
}
