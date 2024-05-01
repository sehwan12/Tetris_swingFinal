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

    private ImportSettings importSettings;  // ImportSettings 객체를 저장하기 위한 private 멤버 변수

    public OutGameView() {
        setLocationRelativeTo(null);
    }

    public void setImportSettings(ImportSettings settings) {
        this.importSettings = settings;  // 외부에서 전달받은 ImportSettings 인스턴스 저장
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
        /*if (importSettings != null) {  // importSettings가 null이 아니면 설정값을 사용하여 창 크기 설정
            String width = importSettings.getSetting("ResolutionSizeX");
            String height = importSettings.getSetting("ResolutionSizeY");
            setSize(Integer.parseInt(width), Integer.parseInt(height));
        }
        setTitle("");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);*/
    }
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
