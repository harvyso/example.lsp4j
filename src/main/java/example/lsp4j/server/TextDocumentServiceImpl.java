package example.lsp4j.server;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.services.TextDocumentService;

public class TextDocumentServiceImpl implements TextDocumentService {

	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		System.out.println( params );
	}

	@Override
	public void didChange(DidChangeTextDocumentParams params) {
		System.out.println( params );
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		System.out.println( params );
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		System.out.println( params );
	}

}
