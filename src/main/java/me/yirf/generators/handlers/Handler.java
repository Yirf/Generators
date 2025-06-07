package me.yirf.generators.handlers;

import me.yirf.generators.commands.ArgumentCommand;

import java.util.List;

public interface Handler {

    String key();

    List<ArgumentCommand> commands();
}
