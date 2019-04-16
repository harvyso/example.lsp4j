package example.lsp4j.chat.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import example.lsp4j.chat.shared.ChatClient;
import example.lsp4j.chat.shared.SocketLauncher;

public class ChatServerLauncher {

	public static void main(String[] args) throws Exception {
		// create the chat server
		ChatServerImpl chatServer = new ChatServerImpl();
		ExecutorService threadPool = Executors.newCachedThreadPool();

		Integer port = Integer.valueOf(args[0]);
		// create the socket server
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("The chat server is running on port " + port);
			threadPool.submit(() -> {
				while (true) {
					// wait for clients to connect
					Socket socket = serverSocket.accept();
					// create a JSON-RPC connection for the accepted socket
					SocketLauncher<ChatClient> launcher = new SocketLauncher<>(chatServer, ChatClient.class, socket);
					// connect a remote chat client proxy to the chat server
					Runnable removeClient = chatServer.addClient(launcher.getRemoteProxy());
                    /*
                     * Start listening for incoming messages.
                     * When the JSON-RPC connection is closed
                     * disconnect the remote client from the chat server.
                     */
					launcher.startListening().thenRun(removeClient);
				}
			});
			System.out.println("Enter any character to stop");
			System.in.read();
			System.exit(0);
		}
	}

}