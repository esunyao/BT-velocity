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
                linear(Component.text("bt"), space(), text("BT" + " ��ָ�����", WHITE)),
                commandInfo("bt about", "�鿴һЩbt����Ϣ"),
                commandInfo("bt reload", "����bt�������ļ�"),
                commandInfo("bt help", "�鿴��������˵�"),
                commandInfo("bt connect", "���ӵ�bt"),
                commandInfo("bt on/off", "�ر�/����bt"),
                commandInfo("bt status", "bt״̬"),
                commandInfo("bt chat on/off", "����/�ر�����ת��")
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
                        .content("����ִ�� '")
                        .color(GRAY)
                        .append(text("/" + command, WHITE))
                        .append(text("'"))
                        .build())
                .clickEvent(runCommand("/" + command))
                .build();
    }

    public void about(Audience audience) {
        Stream.of(
                linear(Component.text("bt"), space(), text("BT" + " ����Ϣ", WHITE)),
                text("����: ", GRAY).append(text("esuny", color(0x007FFF))),
                text("�汾: ", GRAY).append(text("2.1.0", color(0x007FFF)))
        ).forEach(audience::sendMessage);
    }

    public void reload(Audience audience) {
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("bt��������ʵʱ���������ļ��ĵĲ���������~", RED)
        ));
    }

    public void connect(Audience audience) {
        WebSocketService.main();
        WebSocketService.client.connect();
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("���ӳɹ�", GREEN)
        ));
    }

    public void on(Audience audience) {
        BT.wbservice.setStatus(true);
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("�����ػ�������", GREEN)
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
                text("�����ػ��رգ�", RED)
        ));
    }

    public void status(Audience audience) {
        if (BT.wbservice.getStatus())
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("�����ػ������У�", GREEN)
            ));
        else {
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("�����ػ��ѹرգ�", RED)
            ));
        }
        audience.sendMessage(Components.ofChildren(
                Component.text("bt"),
                space(),
                text("��ǰ״̬Ϊ" + BT.wbservice.getStatus(), GREEN)
        ));
        if (WebSocketService.client.isOpen()) {
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("WebSocket����������", GREEN)
            ));
        } else {
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("WebSocket�����쳣��", RED)
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
                    text("�趨 ת������ �ɹ���", GREEN)
            ));
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("�����͵����ݺ�qȺ�ڵ����ݽ��ᱻת���򿴵�", GREEN)
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
                    text("�趨 ��ת������ �ɹ���", GREEN)
            ));
            audience.sendMessage(Components.ofChildren(
                    Component.text("bt"),
                    space(),
                    text("�����͵����ݺ�qȺ�ڵ����ݽ����ᱻת���򿴵�", GREEN)
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
                        text("��ǰ���������Ϣ�ᱻת��������/bt chat off�ر�ת��", GREEN)
                ));
            else
                audience.sendMessage(Components.ofChildren(
                        Component.text("bt"),
                        space(),
                        text("��ǰ���������Ϣ���ᱻת��������/bt chat on������ת��", RED)
                ));
        }
    }

    @FunctionalInterface
    public interface Executor {
        void execute(Audience audience);
    }
}
