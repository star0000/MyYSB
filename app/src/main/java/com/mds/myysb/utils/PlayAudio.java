package com.mds.myysb.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

public class PlayAudio extends AsyncTask<Void, Integer, Void> {

    private static boolean isPlaying;
    private boolean isPut;
    private int frequency = 4000;
    private int audioEncoding = AudioFormat.ENCODING_PCM_8BIT;
    private int channelConfiguration = AudioFormat.CHANNEL_OUT_MONO;
    private byte[] audiodata = new byte[200];
    AudioTrack audioTrack;

    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub
        isPlaying = true;
        isPut = false;
        int bufferSize = AudioTrack.getMinBufferSize(frequency ,
                AudioFormat.CHANNEL_OUT_MONO, audioEncoding);

//        Log.e("MyRecord", "bufferSize="+bufferSize+", audiodataSize="+audiodata.length);
        //每次写入的数据先存入audiodata，audiodata是16位的short变量数据，长度为512,bufferSize=2048
        try {
/*            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(
                            recordingFile)));*/
//            Log.e("MyRecord", "播放准备");
            audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC, frequency,
                    channelConfiguration, audioEncoding, bufferSize*4,
                    AudioTrack.MODE_STREAM);
//            Log.e("MyRecord", "开始播放");
            audioTrack.play();
//            audioTrack.pause();
            while (isPlaying ) {//&& dis.available() > 0
                if(isPut){
                    if(AudioTrack.PLAYSTATE_PAUSED == audioTrack.getPlayState()){
                        audioTrack.play();
                    }
                    audioTrack.write(audiodata, 0, audiodata.length);
                    isPut = false;
                }
//            	Thread.sleep(20);
            }
//            Log.e("MyRecord", "播放结束");
        } catch (Throwable t) {
//            Log.e("AudioTrack", "Playback Failed");
        }
        return null;
    }

    public void putData(byte[] data){
        System.arraycopy(data, 0, audiodata, 0, 200);
        isPut = true;
    }

    public void stop(){
        isPlaying = false;
    }

}
