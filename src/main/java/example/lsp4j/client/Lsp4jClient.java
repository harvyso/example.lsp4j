package example.lsp4j.client;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public class Lsp4jClient implements LanguageClient {


	private final Scanner scanner = new Scanner(System.in);
	
	/**
	 * 1. Ask the user for a name
     * 2. Fetch existing messages from the remote server and display them
     * 3. Ask the user for a next message
     * 4. Post a new message to the chat server, continue with step 3
	 */
	public void start(LanguageServer server) throws Exception {
		//System.out.print("Enter your name: ");
		//String user = scanner.nextLine();
		
		InitializeParams params = new InitializeParams();
		server.initialize(params);
		
		//WorkspaceService workspace = server.getWorkspaceService();
		
		//TextDocumentService document = server.getTextDocumentService();
		
//		server.fetchMessages().get().forEach(message -> this.didPostMessage(message));
//		while (true) {
//			String content = scanner.nextLine();
//			server.postMessage(new UserMessage(user, content));
//		}

	}
	
	public static void main(String[] args) {
		Lsp4jClient client = new Lsp4jClient();
		Launcher<LanguageServer> launcher = LSPLauncher.createClientLauncher( client, System.in, System.out );
		launcher.startListening();
	}

	@Override
	public void telemetryEvent(Object object) {
		java.util.logging.Logger.getAnonymousLogger().fine( object.toString() );
	}

	@Override
	public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {
		java.util.logging.Logger.getGlobal().info( diagnostics.toString() );
	}

	@Override
	public void showMessage(MessageParams messageParams) {
		java.util.logging.Logger.getGlobal().info( messageParams.toString() );
	}

	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {
		java.util.logging.Logger.getGlobal().info( requestParams.toString() );
		return null;
	}

	@Override
	public void logMessage(MessageParams message) {
		java.util.logging.Logger.getGlobal().info( message.toString() );
	}

}
