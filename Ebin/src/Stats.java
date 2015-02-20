public class Stats {
	private SoxStats soxStats;
	private FfmpegStats ffmpegStats;
	private String path;

	Stats(String path, SoxStats soxStats, FfmpegStats ffmpegStats) {
		this.path = path;
		this.soxStats = soxStats;
		this.ffmpegStats = ffmpegStats;
	}

	public float getPeak() {
		return this.soxStats.peakLevel;
	}

	public float getSnr() {
		return this.soxStats.snr;
	}

	public float getLufs() {
		return this.ffmpegStats.lufs;
	}
	
	public String getPath() {
		return this.path;
	}
}
