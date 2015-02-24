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
		return this.soxStats.getPeakLevel();
	}

	public float getSnr() {
		return this.soxStats.getSnr();
	}

	public float getLufs() {
		return this.ffmpegStats.getLufs();
	}

	public float getLra() {
		return this.ffmpegStats.getLra();
	}

	public String getPath() {
		return this.path;
	}
}
