import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType; import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer; 
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.*;
//import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.*;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.table.TableColumn;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.imageio.ImageIO;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.*;
import javax.swing.table.AbstractTableModel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ImageProcessing1 {

  public static BufferedImage greyScaleConverter(BufferedImage bufferedImg) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    String input = "./image.jpg";
    Mat matrixSrc = new Mat(); // 入力画像(Mat型)
    Mat matrixDst = new Mat(); // 出力画像(Mat型)

    // 入力画像の読み込み
    matrixSrc = Imgcodecs.imread(input);
    
    // カラー画像をグレー画像に変換
    Imgproc.cvtColor(matrixSrc, matrixDst, Imgproc.COLOR_BGR2GRAY);
   
    return Mat2Image(matrixDst);
  }

  public static BufferedImage cannyConverter(BufferedImage bufferedImg) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    String input = "./image.jpg";
    Mat matrixSrc = new Mat(); // 入力画像(Mat型)
    Mat matrixDst = new Mat(); // 出力画像(Mat型)

    // 入力画像の読み込み
    matrixSrc = Imgcodecs.imread(input);
    
    Mat gray = new Mat();
    // カラー画像をグレー画像に変換
    Imgproc.cvtColor(matrixSrc, gray, Imgproc.COLOR_BGR2GRAY);
    Imgproc.Canny(gray, matrixDst, 100, 200, 3, true); // Cannyアルゴリズムで輪郭検出

    return Mat2Image(matrixDst);		
  }

  public static void crackMapConverter() {
        ArrayList<String> sr = new ArrayList<String>();
             
        try{
            ArrayList<String> arrText = new ArrayList<String>();
            File file = new File("./crack_map/crack_map_setting.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";            
            while( (line = br.readLine()) != null){                
                sr.add(line); // addメソッドで追記               
            }
            br.close();
            System.out.println(sr.get(0));
            System.out.println(sr.get(1));
        }catch(FileNotFoundException e){
             System.out.println(e);
        }catch(IOException e){
             System.out.println(e);
        }

        String command = "./crack_map/crack_map.exe" + " --in ./crack_map/in.png --intervals "+sr.get(0)+" --colors "+sr.get(1)+" --out ./crack_map/out.png";
        
        System.out.println(command);
        try {
            //runtime.exec(Command); // 指定したコマンドを実行する
            Process p = Runtime.getRuntime().exec(command);
            int ret = p.waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
  }

   
   // MatをBufferedImageに変換するメソッド
   public static BufferedImage Mat2BufferedImage(Mat matrix) throws IOException {
    MatOfByte mob=new MatOfByte();
    Imgcodecs.imencode(".jpg", matrix, mob);
    return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
   }

   

  // MatをBufferedImageに変換するメソッド
  public static BufferedImage matToBufferedImage(Mat matrix, BufferedImage bimg) {
    if ( matrix != null ) { 
        int cols = matrix.cols();  
        int rows = matrix.rows();  
        int elemSize = (int)matrix.elemSize();  
        byte[] data = new byte[cols * rows * elemSize];  
        int type;  
        matrix.get(0, 0, data);  
        switch (matrix.channels()) {  
        case 1:  
            type = BufferedImage.TYPE_BYTE_GRAY;  
            break;  
        case 3:  
            type = BufferedImage.TYPE_3BYTE_BGR;  
            // bgr to rgb  
            byte b;  
            for(int i=0; i<data.length; i=i+3) {  
                b = data[i];  
                data[i] = data[i+2];  
                data[i+2] = b;  
            }  
            break;  
        default:  
            return null;  
        }  

        // Reuse existing BufferedImage if possible
        if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
            bimg = new BufferedImage(cols, rows, type);
        }        
        bimg.getRaster().setDataElements(0, 0, cols, rows, data);
    } else { // mat was null
        bimg = null;
    }
    return bimg;  
  }   

  // MatをBufferedImageに変換するメソッド
  public static BufferedImage Mat2Image(Mat src) {

        //Mat srcのチャネル数を取得
        int type = 0;
        if (src.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (src.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else {
            return null;
        }
        //新規BufferedImage型をsrcの幅，縦，チャネル数で作成．
        BufferedImage image = new BufferedImage(src.width(), src.height(), type);
        //作成したBufferedImageからRasterを抜き出す.
        WritableRaster raster = image.getRaster();
        //抜き出したRasterからバッファを抜き出す．
        DataBufferByte Buf = (DataBufferByte) raster.getDataBuffer();
        byte[] data = Buf.getData();
        src.get(0, 0, data);

        return image;
    }
}