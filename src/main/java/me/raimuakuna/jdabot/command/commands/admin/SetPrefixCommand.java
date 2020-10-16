package me.raimuakuna.jdabot.command.commands.admin;

import me.raimuakuna.jdabot.VeryBadDesign;
import me.raimuakuna.jdabot.command.CommandContext;
import me.raimuakuna.jdabot.command.ICommand;
import me.raimuakuna.jdabot.database.SQLiteDataSource;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if(!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You are missing the permission `MANAGE_SERVER`.").queue();
            return;
        }

        if(args.isEmpty()) {
            channel.sendMessage("Missing arguments.").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("The prefix has been set to `%s`", newPrefix).queue();

    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix of this server\n" +
                "Usage: `~setprefix <prefix>`";
    }

    private void updatePrefix(long guildId, String newPrefix) {
        VeryBadDesign.PREFIXES.put(guildId, newPrefix);

        try(PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                //language=SQLite
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
