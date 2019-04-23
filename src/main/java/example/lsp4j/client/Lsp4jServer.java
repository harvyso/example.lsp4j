package example.lsp4j.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import example.lsp4j.server.TextDocumentServiceImpl;
import example.lsp4j.server.WorkspaceServiceImpl;

public class Lsp4jServer implements LanguageServer {

	private static final Scanner scanner = new Scanner(System.in);
	
	WorkspaceService workspaceService = new WorkspaceServiceImpl();
	
	static class ByteArrayInputStreamCustom extends ByteArrayInputStream {
		public ByteArrayInputStreamCustom(byte[] buf) {
			super(buf);
			this.pos = 0;
			this.count = 0;
		}

		public synchronized void write(byte b[]) {
			System.arraycopy( b, 0, buf, 0, b.length );
			this.pos = 0;
			this.count = b.length;
		}
		
	    public synchronized int read() {
	        //return (pos < count) ? (buf[pos++] & 0xff) : -1;
	        return (pos < count) ? (buf[pos++] & 0xff) : 0;
	    }
	};
	
	public static void main(String[] args) {
		
		java.util.logging.Logger.getGlobal().info( args.toString() );
		
		byte serverBuffer[] = new byte[1024];
		byte clientBuffer[] = new byte[1024];
		
		Terminal terminal = null;
		try {
			terminal = TerminalBuilder.terminal();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		NonBlockingReader r = terminal.reader();
		PrintWriter w = terminal.writer();
		
		ByteArrayInputStreamCustom server_is = new ByteArrayInputStreamCustom( serverBuffer );
		OutputStream server_os = new ByteArrayOutputStream( 1024 );
		
		ByteArrayInputStreamCustom client_is = new ByteArrayInputStreamCustom( clientBuffer );
		OutputStream os = new ByteArrayOutputStream( 1024 );
		//ReadableByteChannel ch = new ;
		
		// implementation LSP4J listen only when none -1 returned
		// may be better to use synchronize (server_is) {
		// but according java InputStream.read should not be blocked!!
		
			// create server launcher to send request to client
			Lsp4jServer server = new Lsp4jServer();
			Launcher<LanguageClient> serverLauncher = LSPLauncher.createServerLauncher( server, server_is, server_os );
			serverLauncher.startListening();
			
			// create client launcher to send request to server
			Lsp4jClient client = new Lsp4jClient();
			Launcher<LanguageServer> clientLauncher = LSPLauncher.createClientLauncher( client, client_is, os );
			clientLauncher.startListening();

		// negotiation
		try {
			//serverLauncher.getRemoteEndpoint().notify( "initialize", null );
			
			InitializeParams params = new InitializeParams();
			params.setProcessId( 11 ); params.setRootUri( "/tmp/" );
			CompletableFuture<?> f = clientLauncher.getRemoteEndpoint().request( "initialize", params );
			
			System.out.println( "os.toString()" );
			String msg = os.toString();
			System.out.println( msg );
			//System.arraycopy( msg.getBytes(), 0, serverBuffer, 0, msg.getBytes().length );
			int tt = 0;
			synchronized (server_is) {
				server_is.write( msg.getBytes() );
				tt = server_is.available();
				server_is.notifyAll();	
			}

			tt = server_is.available();
			System.out.println( "os.toString()" );
			
			System.out.println( "server_os.toString()" );
			msg = server_os.toString();
			System.out.println( msg );
			
			tt = client_is.available();
			synchronized (client_is) {
				client_is.write( msg.getBytes() );
				tt = client_is.available();
				client_is.notifyAll();	
			}

			Object resp = f.get( 100, TimeUnit.SECONDS );
			System.out.println( resp );
			
			f = serverLauncher.getRemoteEndpoint().request( "hello", null );
			resp = f.get( 1, TimeUnit.SECONDS );
			System.out.println( resp );

			//serverLauncher.getRemoteEndpoint().request( "workspace/workspaceFolders", null );
			//server.
			//client.
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} // catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		try {
//			serverThread.join( );
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
		java.util.logging.Logger.getGlobal().info( params.toString() );
		InitializeResult res = new InitializeResult();
		res.setCapabilities( new ServerCapabilities() );
		CompletableFuture<InitializeResult> future = new CompletableFuture<InitializeResult>( );
		future.complete( res );
		return future;
	}

	@Override
	public CompletableFuture<Object> shutdown() {
		java.util.logging.Logger.getGlobal().info("");
		return null;
	}

	@Override
	public void exit() {
		java.util.logging.Logger.getGlobal().info("");
	}

	@Override
	public TextDocumentService getTextDocumentService() {
		java.util.logging.Logger.getGlobal().info("");
		return new TextDocumentServiceImpl();
	}

	@Override
	public WorkspaceService getWorkspaceService() {
		java.util.logging.Logger.getGlobal().info("");
		return workspaceService;
	}

}
