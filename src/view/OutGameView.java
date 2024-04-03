package view;

// 아웃게임 View를 위한 super class
import javax.swing.*;
import java.awt.*;
import IO.ImportSettings;

public class OutGameView extends JFrame {
    protected JPanel mainPanel;

    protected JLabel titleLabel;

    protected int curFocus;

    protected JLabel[] arrowLabel = { new JLabel("["), new JLabel("]") };

    public OutGameView() {
        setLocationRelativeTo(null);
    }


    public void initPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
    }

    public void initTitle() {
        String fullTitle = "";
        titleLabel = new JLabel(fullTitle);
        titleLabel.setFont(new Font("malgun gothic", Font.BOLD, 20));
    }

    public void initWindow() {
        setTitle("");
        setSize(
                Integer.parseInt(ImportSettings.getSetting("ResoultionSizeX")),
                Integer.parseInt(ImportSettings.getSetting("ResoultionSizeY"))
        );
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void paintFocus() { }
    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "경고",
                JOptionPane.WARNING_MESSAGE);
    }

    public int showQuestion(String message) {
        return JOptionPane.showConfirmDialog(this, message,
                "경고", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}
