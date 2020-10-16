package me.raimuakuna.jdabot.command.commands;

import me.raimuakuna.jdabot.command.CommandContext;
import me.raimuakuna.jdabot.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageFormat("Reset ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Shows the current ping from the bot to the server";
    }

    @Override
    public String getName() {
        return "ping";
    }
}
