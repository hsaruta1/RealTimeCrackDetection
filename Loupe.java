import java.awt.*;
import java.awt.Point;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.Timer; 
import javax.swing.*;
import javax.swing.border.Border;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.*;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
//import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;

import java.awt.*;
import javax.swing.*;
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
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.BevelBorder;
import java.util.List;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
 
public class Loupe extends JFrame {
   //private static final int SLIDER_ACCURACY = 5;
   //private static final int INITIAL_SCALE = 3;
   //private static final int MAX_SCALE = 6;
   //private static final int MIN_SCALE = 0;
   int count = 0;

   public static Net net;
   public static Mat im;

   public static String[] names = new String[]{
            "1","2","3","4","5","6"
   };

   public static void main(String[] args) throws AWTException{
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

      net = Dnn.readNetFromDarknet("./yolov3-voc_honda.cfg", "./yolov3-voc_honda.weights");
      if ( net.empty() ) {
          //System.out.println("Reading Net error");
      }

      //String image_file = "./DJI_0248.jpg";//IMG_9452.JPG
      //im = Imgcodecs.imread(image_file, Imgcodecs.IMREAD_COLOR);
      //if( im.empty() ) {
      //    System.out.println("Reading Image error");
      //}


      Loupe f = new Loupe();
      f.setAlwaysOnTop(true);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.pack();
      f.setLocationRelativeTo(null);
      f.setVisible(true);
   }

   BufferedImage image;
   private boolean captureEnable = true;
   private final Robot robot;
   private final JLabel captureLabel;
   //private final JSlider slider;
   //private final JTextField text;
   private GridMode gridMode = GridMode.GUIDE_LINE;
   boolean greyScaleFlag = false;
   boolean cannyFlag = false;
   boolean crackMapFlag = false;

   public Loupe() throws AWTException {
      super("Loupe");
 
      this.robot = new Robot();
      robot.setAutoDelay(0);
 
      this.captureLabel = new JLabel();
      captureLabel.setBorder(BorderFactory.createEtchedBorder());
      getContentPane().add(captureLabel, BorderLayout.CENTER);
      captureLabel.setPreferredSize(new Dimension(800, 800));
      
      initEventListener();
      initTimer();
   }
 
  
   protected void capture() {
      Point location = MouseInfo.getPointerInfo().getLocation();
 
      double scale = 1.0;
      
      int width = (int) (captureLabel.getWidth());
      int height = (int) (captureLabel.getHeight());
      if (width <= 0 || height <= 0) {
         return;
      }
 
      image = robot.createScreenCapture(new Rectangle(location.x - width / 2, location.y - width / 2, width, height));
      try {
            ImageIO.write(image, "jpeg", new File("image.jpg"));
            image = detect();
      } catch (IOException e) {
            e.printStackTrace();
      }

      if(greyScaleFlag) {
          //System.out.println("true");
          
          try {
              ImageIO.write(image, "jpeg", new File("image.jpg"));
              //image = Mat2BufferedImage( detect( bufferedImageToMat(image) ) );
          } catch (IOException e) {
              e.printStackTrace();
          }
          
          //image = ImageProcessing1.greyScaleConverter(image);
          //image = Mat2BufferedImage( detect( bufferedImageToMat(image) ) );
      }

      if(cannyFlag) {
          try {
              ImageIO.write(image, "jpeg", new File("image.jpg"));
          } catch (IOException e) {
              e.printStackTrace();
          }
          image = ImageProcessing1.cannyConverter(image);
      }

      if(crackMapFlag) {
          try {
              //ImageIO.write(image, "jpeg", new File("image.jpg"));
              BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
              Graphics g = newImg.createGraphics();
              g.drawImage(image, 0, 0, null);
              g.dispose();
              ImageIO.write(newImg, "png", new File("./crack_map/in.png"));
          } catch (IOException e) {
              e.printStackTrace();
          }
          ImageProcessing1.crackMapConverter();
          try {
              image = ImageIO.read(new File("./crack_map/out.png")); 
          } catch (IOException ioe) {
              ioe.printStackTrace();
          }                       
      }

      //image = robot.createScreenCapture(new Rectangle(location.x - width / 2, location.y - width / 2, width, height));
      captureLabel.setIcon(new ImageIcon(image));
      //captureLabel.setIcon(new ExpandImageIcon(image, scale, gridMode));
   }

   // BufferedImageをMatに変換するメソッド
   public static Mat bufferedImageToMat(BufferedImage bi) {
       Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
       byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
       mat.put(0, 0, data);
       return mat;
   }
    
 
   private void initEventListener() {
      Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
         public void eventDispatched(AWTEvent event) {
            if (event.getID() == KeyEvent.KEY_PRESSED) {
               KeyEvent ke = (KeyEvent) event;
               switch (ke.getKeyChar()) {
                   //case 'l': toggleCaptureEnable(); break;
                   case 'a': greyScaleFlag = true;
                   case 'b': crackMapFlag = false;
                   case 'c': crackMapFlag = true;    //copyToClipbord(); break;
                   case 'g': changeGridMode(); //break;greyScaleFlag = false;  //changeGridMode(); break;
                   //case 'f': capture(); break;
                   default: //pickUp();
               }
            }
            else if(event.getID() == MouseWheelEvent.MOUSE_WHEEL) {
                MouseWheelEvent mwe = (MouseWheelEvent) event;
                //System.out.println("回転");
                count = count + 1;
                //System.out.println(count);
                if(count==10) {
                    //crackMapFlag = true;
                } else if(count==20) {
                    //crackMapFlag = false;
                }
                //slider.setValue(slider.getValue() + mwe.getWheelRotation());
            }
         }
 
      }, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
   }
 
   private void initTimer() {
      Timer timer = new Timer(true);
      timer.schedule(new TimerTask() {
         @Override
         public void run() {
            if (captureEnable) {
                
                //capture();
                //if(greyScaleFlag) {
                //    image = ImageProcessing1.greyScaleConverter(image);
                //}
               capture();
            }
         }
      }, 500, 100); 
   } 
 
   protected void changeGridMode() {
      GridMode[] modes = GridMode.values();
      gridMode = modes[(gridMode.ordinal()+1)%modes.length];
 
      //text.setText("GridMode is chaged.");
   }
 
   enum GridMode {   GUIDE_LINE, GRID, NONE };
   
   static class ExpandImageIcon extends ImageIcon {
      private static final Color guidLineColor = new Color(0, 0, 255, 128);
      private static final Color gridColor = new Color(128, 128, 128, 128);
      private final double scale;
      private final GridMode gridMode;
 
      public ExpandImageIcon(Image image, double scale, GridMode gridMode) {
         super(image);
         this.scale = scale;
         this.gridMode = gridMode;
      }
 
      @Override
      public int getIconHeight() {
         return (int) (super.getIconHeight() * scale);
      }
 
      @Override
      public int getIconWidth() {
         return (int) (super.getIconWidth() * scale);
      }
 
      private void paintGuideLine(Graphics g, int x, int y, Image image) {
         g.setColor(guidLineColor);
         int w = image.getWidth(getImageObserver());
         int h = image.getHeight(getImageObserver());
         if (w < 0 || h < 0) {
            return;
         }
         int xc = x + (int) (scale * (w / 2));
         int yc = y + (int) (scale * (h / 2));
         
         g.drawLine(xc, y, xc, y + getIconHeight());
         g.drawLine(x, yc, x + getIconWidth(), yc);
         
         int xcPlus1 = xc + (int) scale;
         int ycPlus1 = yc + (int) scale;
         g.drawLine(xcPlus1, y, xcPlus1, y + getIconHeight());
         g.drawLine(x, ycPlus1, x + getIconWidth(), ycPlus1);
      }
 
      @Override
      public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
         Image image = getImage();
         g.drawImage(image, x, y, getIconWidth(), getIconHeight(), c);
         switch (gridMode) {
         case GUIDE_LINE : paintGuideLine(g, x, y, image); break;
         case GRID : paintGridLine(g, x, y); break;
         case NONE: break;
         default : throw new AssertionError();
         }
      }
 
      private void paintGridLine(Graphics g, int x, int y) {
         g.setColor(gridColor);
         for (int xi = 0; xi * scale < x + getIconWidth(); xi++) {
            g.drawLine(x + (int)(xi*scale), y,
                     x + (int)(xi*scale), y + getIconHeight());
         }
         for (int yi = 0; yi * scale < y + getIconHeight(); yi++) {
            g.drawLine(x, y + (int)(yi*scale),
                     x + getIconWidth(), y + (int)(yi*scale));
         }
      }
   }

   /*
   // BufferedImage型の画像オブジェクトをMat型に変換し、返すメソッド
   public static Mat bufferedImageToMat(BufferedImage bi) {
       Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
       byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
       mat.put(0, 0, data);
       return mat;
   }
   */

   /*
   // Mat型の画像オブジェクトをBufferedImage型に変換し、返すメソッド
   public static BufferedImage Mat2BufferedImage(Mat matrix) throws IOException {
       MatOfByte mob=new MatOfByte();
       Imgcodecs.imencode(".jpg", matrix, mob);
       return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
   }
   */
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
   public static BufferedImage detect() {
       im = Imgcodecs.imread("./image.jpg");
       //im = img;
       Mat frame = new Mat();
       Size sz1 = new Size(im.cols(),im.rows());
       Imgproc.resize(im, frame, sz1);

       Mat resized = new Mat();
       Size sz = new Size(416,416);
       Imgproc.resize(im, resized, sz);

       float scale = 1.0F / 255.0F;
       Mat inputBlob = Dnn.blobFromImage(im, scale, sz, new Scalar(0), false, false);
       net.setInput(inputBlob, "data"); 
       //Mat detectionMat = net.forward("detection_out");
       Mat detectionMat = net.forward();
       if( detectionMat.empty() ) {
           //System.out.println("No result");
       }

       for (int i = 0; i < detectionMat.rows(); i++)
       {           
           int probability_index = 5;
           int size = (int) (detectionMat.cols() * detectionMat.channels());

           float[] data = new float[size];
           detectionMat.get(i, 0, data);
           System.out.println("length: " + data.length);
           float confidence = data[4];//-1;
           int objectClass = -1;
           /*
           for (int j=0; j < detectionMat.cols();j++)
           {   
               if (j>=probability_index && confidence<data[j])
               {
                   confidence = data[j];
                   objectClass = j-probability_index;
               }
           }
           */
           if (confidence >= 0.01)
           {
               //System.out.println("Result Object: "+i);
               for (int j=0; j < detectionMat.cols();j++) {
                    //System.out.print(" "+j+":"+ data[j]);
               }
               //System.out.println("");
               float x = data[0];
               float y = data[1];
               float width = data[2];
               float height = data[3];
               float xLeftBottom = (x - width / 2) * frame.cols();
               float yLeftBottom = (y - height / 2) * frame.rows();
               float xRightTop = (x + width / 2) * frame.cols();
               float yRightTop = (y + height / 2) * frame.rows();

               //System.out.println("Class: "+ names[objectClass]);
               //System.out.println("Confidence: "+confidence);

               //System.out.println("ROI: "+xLeftBottom+" "+yLeftBottom+" "+xRightTop+" "+yRightTop+"\n");

               Imgproc.rectangle(frame, new org.opencv.core.Point(xLeftBottom, yLeftBottom),
                    new org.opencv.core.Point(xRightTop,yRightTop),new Scalar(0, 255, 0),3);
           }
        }
        return Mat2Image(frame);
   }
}