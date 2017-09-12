package me.dirantos.moneymaker.spigot.command;

public @interface CommandInfo {

    String name();

    String usage();

    String permission();

    String[] description() default {};

}
