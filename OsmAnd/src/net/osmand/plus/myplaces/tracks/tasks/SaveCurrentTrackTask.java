package net.osmand.plus.myplaces.tracks.tasks;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import net.osmand.shared.gpx.GpxUtilities;
import net.osmand.shared.gpx.GpxFile;
import net.osmand.IndexConstants;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.configmap.tracks.TrackItem;
import net.osmand.plus.plugins.monitoring.SavingTrackHelper;
import net.osmand.plus.track.helpers.save.SaveGpxListener;

import java.io.File;
import java.util.Map;

public class SaveCurrentTrackTask extends AsyncTask<Void, Void, Boolean> {

	private final OsmandApplication app;
	private final GpxFile gpx;

	private final SaveGpxListener saveGpxListener;

	public SaveCurrentTrackTask(@NonNull OsmandApplication app, @NonNull GpxFile gpx, @NonNull SaveGpxListener listener) {
		this.app = app;
		this.gpx = gpx;
		saveGpxListener = listener;
	}

	@Override
	protected void onPreExecute() {
		if (saveGpxListener != null) {
			saveGpxListener.onSaveGpxStarted();
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		SavingTrackHelper savingTrackHelper = app.getSavingTrackHelper();
		Map<String, GpxFile> files = savingTrackHelper.collectRecordedData();
		File dir;
		boolean shouldClearPath = false;
		if (gpx.path.isEmpty()) {
			dir = app.getCacheDir();
			shouldClearPath = true;
		} else {
			dir = app.getAppCustomization().getTracksDir();
		}
		if (!dir.exists()) {
			dir.mkdir();
		}
		for (String f : files.keySet()) {
			File fout = new File(dir, f + IndexConstants.GPX_FILE_EXT);
			Exception exception = GpxUtilities.writeGpxFile(fout, gpx);
			if (exception == null) {
				app.getSavingTrackHelper().setLastTimeFileSaved(fout.lastModified());
				app.getSmartFolderHelper().addTrackItemToSmartFolder(new TrackItem(app, gpx));
			}
		}
		return shouldClearPath;
	}

	@Override
	protected void onPostExecute(Boolean shouldClearPath) {
		if (gpx != null) {
			if (saveGpxListener != null) {
				saveGpxListener.onSaveGpxFinished(null);
			}
			if (shouldClearPath) {
				gpx.path = "";
			}
		}
	}
}