package view;

import model.OutGame.OutGameModel;
import model.VersusMode.VsTimeBoardModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;


public class TimeView extends JPanel {
    private SimpleAttributeSet styleSet;

    private JTextPane TimeText;
    private static final int TIME_LIMIT = VsTimeBoardModel.TIME_LIMIT;
    public TimeView(){
        this.setLayout(new BorderLayout());
        TimeText=new JTextPane();
        TimeText.setLayout(new BorderLayout());
        TimeText.setBorder(new LineBorder(Color.BLUE, 5));
        //해상도에 따라 크기 변경
        TimeText.setPreferredSize(new Dimension(OutGameModel.getResX(), OutGameModel.getResY()/15));
        TimeText.setBackground(Color.BLACK);
        TimeText.setForeground(Color.CYAN);
        TimeText.setFont(new Font("Courier New", Font.PLAIN, 30*(OutGameModel.getResX()/5)/80));
        //텍스트를 중간에 배치
        styleSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER);
        StyledDocument doc = TimeText.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);
        this.add(TimeText);
    }

    public void setTimeText(long time) {
        TimeText.setText("Time: " + (TIME_LIMIT-(System.currentTimeMillis()-time)/1000));
    }
}
