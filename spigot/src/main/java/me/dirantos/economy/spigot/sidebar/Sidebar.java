package me.dirantos.economy.spigot.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Sidebar {

    private static final Random RND = new Random();
    private final Scoreboard board;
    private final Objective objective;
    private final List<SidebarLine> lines = new ArrayList<>();
    private final int beginIndex;
    private final SidebarColorPool colorPool = new SidebarColorPool();

    public Sidebar(String displayName, int beginIndex) {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("s" + RND.nextInt(999999999), "dummy");
        objective.setDisplayName(colored(displayName));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.beginIndex = beginIndex;
    }

    public Sidebar(String displayName) {
        this(displayName, 0);
    }

    public void display(Player player) {
        player.setScoreboard(board);
    }

    public void setDisplayName(String displayName) {
        objective.setDisplayName(colored(displayName));
    }

    public void setLines(String... newLines) {
        if (newLines.length > 15) throw new IllegalStateException("size of lines cannot be higher than 15");
        Collections.reverse(Arrays.asList(newLines));

        generateLines(newLines.length);

        for (int i = 0; i < lines.size(); i++) {
            setText(newLines[i], lines.get(i));
            lines.get(i).setParameteredText(newLines[i]);
        }
    }

    public void setLines(List<String> lines, Object... replace) {
        setLines(lines.stream().map(s -> replace(s, replace)).collect(Collectors.toList()).toArray(new String[lines.size()]));
    }

    public void setLines(List<String> lines) {
        setLines(lines.toArray(new String[lines.size()]));
    }

    private void generateLines(int length) {
        if (lines.size() == length) return;
        colorPool.clear();
        List<SidebarLine> newLines = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            SidebarLine line = getLine(i + beginIndex);
            if (line == null) {
                line = getNewLine(i + beginIndex);
            } else {
                colorPool.add(line.getColor());
            }
            newLines.add(line);
        }
        lines.clear();
        newLines.forEach(lines::add);
    }

    public Scoreboard getBoard() {
        return board;
    }

    private void setText(String text, SidebarLine line) {
        if (text.length() > 30)
            throw new IllegalStateException("The text '" + text + "' is too long! The size must be less than 31");
        text = colored(text);
        if (text.equals(line.getText())) return;
        String prefix = text;
        String suffix = "";

        if (text.length() > 16) {
            prefix = text.substring(0, 16);
            if (prefix.endsWith(String.valueOf(ChatColor.COLOR_CHAR)) && Pattern.matches("[0-9a-fk-or]", text.substring(16, 17))) {
                prefix = text.substring(0, 15);
                suffix = String.valueOf(ChatColor.COLOR_CHAR)+ text.substring(16);
            } else {
                String colorCode = "";
                Pattern pattern = Pattern.compile(String.valueOf(ChatColor.COLOR_CHAR) + "[0-9a-fk-or]");
                Matcher matcher = pattern.matcher(prefix);
                while (matcher.find()) {
                    colorCode = matcher.group();
                }
                suffix = colorCode + text.substring(16);
            }
        }

        line.getTeam().setPrefix(prefix);
        line.getTeam().setSuffix(suffix);
        line.setText(text);
    }

    private void update(SidebarLine line) {
        objective.getScore(line.getEntry()).setScore(line.getIndex());
    }

    private SidebarLine getNewLine(int index) {
        Team team = board.registerNewTeam("t" + RND.nextInt(99999));
        ChatColor color = colorPool.getFreeColor();
        colorPool.add(color);
        String entry = color + "" + ChatColor.RESET;
        team.addEntry(entry);
        SidebarLine line = new SidebarLine(index, team, entry, color);
        update(line);
        return line;
    }

    private SidebarLine getLine(int index) {
        for (SidebarLine line : lines) {
            if (line.getIndex() == index) return line;
        }
        return null;
    }

    private String replace(String text, Object... replace) {
        List<String> r = new ArrayList<>();
        List<String> o = new ArrayList<>();

        for (int i = 0; i < replace.length; i++) {
            if (i % 2 == 0) {
                r.add(String.valueOf(replace[i]));
            } else {
                o.add(String.valueOf(replace[i]));
            }
        }
        return replaceList(text, r, o);
    }

    private String replaceList(String text, List<String> r, List<String> o) {
        if (r.size() != o.size()) throw new IllegalArgumentException("length of replace must be even");
        String output = text;
        for (String s : r) {
            output = output.replaceAll(s, o.get(r.indexOf(s)));
        }
        return colored(output);
    }

    private String colored(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}