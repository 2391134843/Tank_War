
import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
//由于CPU和磁盘占用率过大....就不在界面里面添加音乐了
public class Music {
    public Music()
    {
        // URL url = getClass().getResource("start.wav");
        // AudioClip clip = java.applet.Applet.newAudioClip(url);
        // clip.play();
    }
    static void playMusic(){//背景音乐播放
        try {
            URL cb;
            File f = new File("start.wav"); // 引号里面的是音乐文件所在的路径
            cb = f.toURL();
            AudioClip aau;
            aau = Applet.newAudioClip(cb);

            aau.play();
            aau.loop();//循环播放
            System.out.println("可以播放");
            // 循环播放 aau.play()
            //单曲 aau.stop()停止播放

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
    }

}
