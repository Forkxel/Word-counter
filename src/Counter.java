import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Counter extends JFrame {

    private JButton button1;
    private JPanel mainPanel;
    private JLabel jlCount;
    private String fileType;

    public Counter() {
        setContentPane(mainPanel);
        setTitle("Word counter");
        button1.setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = getjFileChooser();

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName().toLowerCase();

                    if (fileName.endsWith(".pdf")) {
                        fileType = ".pdf";
                        readPDF(selectedFile);
                    } else if (fileName.endsWith(".docx")) {
                        fileType = ".docx";
                        readDocx(selectedFile);
                    }
                }
            }
        });
    }

    private static JFileChooser getjFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(".pdf") || fileName.endsWith(".docx");
            }

            @Override
            public String getDescription() {
                return "PDF and DOCX files";
            }
        });
        return fileChooser;
    }

    private void readPDF(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String filteredText = text.replaceAll("\\.+", "");
            String[] words = filteredText.trim().split("\\s+");
            document.close();
            jlCount.setText(String.valueOf(words.length));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDocx(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fileInputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String text = extractor.getText();
            String filteredText = text.replaceAll("\\.+", "");
            String[] words = filteredText.split("\\s+");
            document.close();
            jlCount.setText(String.valueOf(words.length));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
