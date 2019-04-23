package example.lsp4j.server;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;

public class WorkspaceServiceImpl implements WorkspaceService {

	@Override
	public void didChangeConfiguration(DidChangeConfigurationParams params) {
		System.out.println( params );
	}

	@Override
	public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
		System.out.println( params );
	}

}
