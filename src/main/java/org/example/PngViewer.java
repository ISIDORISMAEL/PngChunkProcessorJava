package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngViewer {

    public static void showImage(String filePath) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PNG Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);

            BufferedImage image;
            try {
                image = ImageIO.read(new File(filePath));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to load image: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JLabel label = new JLabel(new ImageIcon(image));
            JScrollPane scrollPane = new JScrollPane(label);
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            JButton downloadButton = new JButton("Download");
            downloadButton.addActionListener((ActionEvent e) -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("download.png"));
                int option = fileChooser.showSaveDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File saveFile = fileChooser.getSelectedFile();
                    try {
                        ImageIO.write(image, "png", saveFile);
                        JOptionPane.showMessageDialog(frame, "Image saved to: " + saveFile.getAbsolutePath());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Failed to save image: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            controlPanel.add(downloadButton);
            frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        showImage("output.png");
    }
}
