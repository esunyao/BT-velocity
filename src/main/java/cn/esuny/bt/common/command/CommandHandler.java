package cn.esuny.bt.common.command;

import cn.esuny.bt.BT;
import cn.esuny.bt.core.service.WebSocketService;
import cn.esuny.bt.util.Components;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.stream.Stream;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.LinearComponents.linear;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;

public class CommandHandler {
    public void help(Audience audience) {
        Stream.of(
                linear(Component.text("bt"), space(), text("BT" + " 的指令帮助", WHITE)),
                commandInfo("bt about", "查看一些bt的信息"),
                commandInfo("bt reload", "重载bt的配置文件"),
                commandInfo("bt help", "查看这个帮助菜单"),
                commandInfo("bt connect", "连接到bt"),
                commandInfo("bt on/off", "关闭/开启bt"),
                commandInfo("bt status", "bt状态"),
                commandInfo("bt chat on/off", "开启/关闭聊天转发")
        ).forEach(audience::sendMessage);
    }


    private static Component commandInfo(final String command, final String description) {
        return text()
                .content(" - ")
                .color(GRAY)
                .append(text('/', WHITE))
                .append(text(command, color(0x007FFF)))
                .append(text(':'))
                .append(space())
                .append(text(description, WHITE))
                .hoverEvent(text()
                        .content("点我执行 '")
                        .color(GRAY)
                        .append(text("/" + command, WHITE))
                        .append(text("'"))
                        .build())
                .clickEvent(runCommand("/" + command))
                .build();
    }

    public void about(Audience audience) {
        Stream.of(
                linear(Component.text("bt"), space(), text("BT" + " 的信息", WHITE)),
                text("作者: ", GRAY).append(text("esuny", color(0x007FFF))),
                text("版本: ", GRAY).append(text("2.1.0", color(0x007FFF)))
        ).forEach(audience::sendMessage);
    }

    public void reload(Audience audience) {
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("bt理论上是实时加载配置文件的的不存在重载~", RED)
        ));
    }

    public void connect(Audience audience) {
        WebSocketService.main();
        WebSocketService.client.connect();
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("连接成功", GREEN)
        ));
    }

    public void on(Audience audience) {
        BT.wbservice.setStatus(true);
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("进程守护开启！", GREEN)
        ));
    }

    public void off(Audience audience) {
        BT.wbservice.setStatus(false);
        if (WebSocketService.client.isOpen()) {
            WebSocketService.client.close();
        }
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("进程守护关闭！", RED)
        ));
    }

    public void status(Audience audience) {
        if (BT.wbservice.getStatus())
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("进程守护运行中！", GREEN)
            ));
        else {
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("进程守护已关闭！", RED)
            ));
        }
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("当前状态为" + BT.wbservice.getStatus(), GREEN)
        ));
        if (WebSocketService.client.isOpen()) {
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("WebSocket连接正常！", GREEN)
            ));
        } else {
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("WebSocket连接异常！", RED)
            ));
        }
    }

    public void chat_on(Audience audience) {
        if (audience instanceof Player) {
            Player player = (Player) audience;
            String playerName = player.getUsername();
            BT.player_personality.put(playerName, true);
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("设定 转发聊天 成功！", GREEN)
            ));
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("您发送的内容和q群内的内容将会被转发或看到", GREEN)
            ));
        }
    }

    public void chat_off(Audience audience) {
        if (audience instanceof Player) {
            Player player = (Player) audience;
            String playerName = player.getUsername();
            BT.player_personality.put(playerName, false);
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("设定 不转发聊天 成功！", GREEN)
            ));
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("您发送的内容和q群内的内容将不会被转发或看到", GREEN)
            ));
        }
    }

    public void chat_status(Audience audience) {
        help(audience);
        if (audience instanceof Player) {
            Player player = (Player) audience;
            String playerName = player.getUsername();
            if (BT.player_personality.getBooleanValue(playerName, true))
                audience.sendMessage(Components.ofChildren(
                        Component.text("bt"),
                        space(),
                        text("当前你的聊天信息会被转发，输入/bt chat off关闭转发", GREEN)
                ));
            else
                audience.sendMessage(Components.ofChildren(
                        Component.text("bt"),
                        space(),
                        text("当前你的聊天信息不会被转发，输入/bt chat on来开启转发", RED)
                ));
        }
    }

    @FunctionalInterface
    public interface Executor {
        void execute(Audience audience);
    }
}
