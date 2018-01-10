package databaseDownloader;

public interface GUIUpdater {
	public void fireProgressUpdate(int start, int end, int current);
	public void fireTaskComplete();
}
