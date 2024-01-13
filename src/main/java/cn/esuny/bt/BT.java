package cn.esuny.bt;

import cn.esuny.bt.common.command.CommandHandler;
import cn.esuny.bt.core.service.WebSocketAsyncService;
import cn.esuny.bt.core.service.WebSocketService;
import cn.esuny.bt.util.websocket.client.WebSocketClient;
import com.alibaba.fastjson2.JSONObject;
import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import cn.esuny.bt.listener.PlayerChatListener;
import cn.esuny.bt.listener.PlayerDisconnectListener;
import cn.esuny.bt.listener.PlayerLoginServerListener;

import java.net.URISyntaxException;

@Plugin(id = "bt", name = "BT", version = "2.1.0",
        authors = "esuny", url = "")
public class BT {

    @Inject public static Logger logger;

    public static ProxyServer proxyServer;
    public final CommandManager commandManager;
    private final ProxyServer proxy;
    public static JSONObject player_personality = new JSONObject();
//    public static boolean thread = false;
    public static WebSocketAsyncService wbservice = new WebSocketAsyncService();

    @Inject
    public BT(ProxyServer proxyServer, Logger logger, CommandManager commandManager) {
        BT.proxyServer = proxyServer;
        this.proxy = proxyServer;
        BT.logger = logger;
        this.commandManager = commandManager;
//        JSONObject user = new JSONObject();
        // 初始化插件
//        Initialization.init(logger);
        wbservice.start();
        this.registerCommands();
        logger.info("§aBTAPI加载完成");
    }

    private void registerCommands() {
        final class WrappingExecutor implements Command<CommandSource> {
            private final CommandHandler.Executor handler;

            WrappingExecutor(final CommandHandler.@NotNull Executor handler) {
                this.handler = handler;
            }

            @Override
            public int run(final CommandContext<CommandSource> context) {
                this.handler.execute(context.getSource());
                return Command.SINGLE_SUCCESS;
            }
        }

        final CommandHandler handler = new CommandHandler();
        this.commandManager.register(this.commandManager.metaBuilder("bt").build(), new BrigadierCommand(
                LiteralArgumentBuilder.<CommandSource>literal("bt")
                        .executes(new WrappingExecutor(handler::help))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("chat").requires(source -> source.hasPermission("bt.player")).executes(new WrappingExecutor(handler::chat_status))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("on").requires(source -> source.hasPermission("bt.player")).executes(new WrappingExecutor(handler::chat_on)))
                                .then(LiteralArgumentBuilder.<CommandSource>literal("off").requires(source -> source.hasPermission("bt.player")).executes(new WrappingExecutor(handler::chat_off))))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("reload").requires(source -> source.hasPermission("bt.admin")).executes(new WrappingExecutor(handler::reload)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("connect").requires(source -> source.hasPermission("bt.admin")).executes(new WrappingExecutor(handler::connect)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("on").requires(source -> source.hasPermission("bt.admin")).executes(new WrappingExecutor(handler::on)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("off").requires(source -> source.hasPermission("bt.admin")).executes(new WrappingExecutor(handler::off)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("status").requires(source -> source.hasPermission("bt.admin")).executes(new WrappingExecutor(handler::status)))
        ));

    }

    void BTCommand(){

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // 注册玩家聊天消息监听器
        proxy.getEventManager().register(this, new PlayerChatListener(proxy));
        // 注册玩家连接服务器监听器
        proxy.getEventManager().register(this, new PlayerLoginServerListener(proxy));
        // 注册玩家断开服务器监听器
        proxy.getEventManager().register(this, new PlayerDisconnectListener(proxy));
    }
}
