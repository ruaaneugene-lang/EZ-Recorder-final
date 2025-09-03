()
// create virtual display
val metrics = DisplayMetrics()
windowManager.defaultDisplay.getRealMetrics(metrics)
val density = metrics.densityDpi
val width = metrics.widthPixels
val height = metrics.heightPixels


virtualDisplay = mediaProjection?.createVirtualDisplay(
"EZRecorderDisplay",
width, height, density,
android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
mediaRecorder?.surface, null, null
)


mediaRecorder?.start()
isRecording = true


findViewById<TextView>(R.id.tvStatus).text = "Recording..."
findViewById<Button>(R.id.btnRecord).text = "Stop Recording"


} catch (e: Exception) {
e.printStackTrace()
}
}


private fun setupMediaRecorder() {
val metrics = DisplayMetrics()
windowManager.defaultDisplay.getRealMetrics(metrics)
val width = metrics.widthPixels
val height = metrics.heightPixels


val folder = File(getExternalFilesDir(null), "EZRecordings")
if (!folder.exists()) folder.mkdirs()


val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
val filePath = File(folder, "ez_recording_$timeStamp.mp4").absolutePath


mediaRecorder = MediaRecorder()
mediaRecorder?.apply {
setAudioSource(MediaRecorder.AudioSource.MIC)
setVideoSource(MediaRecorder.VideoSource.SURFACE)
setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
setOutputFile(filePath)
setVideoEncoder(MediaRecorder.VideoEncoder.H264)
setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
setVideoSize(width, height)
setVideoFrameRate(30)
setVideoEncodingBitRate(8 * 1000 * 1000) // 8Mbps, good for gaming
}
}


private fun stopRecording() {
try {
mediaRecorder?.stop()
mediaRecorder?.reset()
virtualDisplay?.release()
mediaProjection?.stop()
} catch (e: Exception) {
e.printStackTrace()
}
isRecording = false
}


override fun onDestroy() {
super.onDestroy()
tts.shutdown()
}
}
