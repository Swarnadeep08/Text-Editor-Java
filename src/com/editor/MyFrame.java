package com.editor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MyFrame extends JFrame implements ActionListener
{
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JSpinner fontSizeSpinner;
    private JButton fontColorButton;
    private JComboBox fontBox;
    private JComboBox fontStyle;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem closeItem;

    MyFrame()
    {
        super.setTitle("Text Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 660);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.MAGENTA);
        this.setLayout(new FlowLayout());
        this.setLocation(200, 100);

        /* Text Area */
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));

        /* Scroll Pane */
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(550, 550));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        /* Font Size Spinner */
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(18);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int fontSize = (int)fontSizeSpinner.getValue();
                if(fontStyle.getSelectedItem() == "Plain")
                    textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, fontSize));
                else if (fontStyle.getSelectedItem() == "Bold")
                    textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.BOLD, fontSize));
                else if(fontStyle.getSelectedItem() == "Italic")
                    textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.ITALIC, fontSize));
            }
        });

        /* Font Color Button */
        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        /* Font Box */
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Monospaced");

        /* Font Style [Plain, Bold, Italic] */
        String fontStyles[] = {"Plain", "Bold", "Italic"};
        fontStyle = new JComboBox(fontStyles);
        fontStyle.setSelectedItem("Plain");
        fontStyle.addActionListener(this);

        /* Menu Bar */
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        closeItem = new JMenuItem("Close");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(closeItem);
        menuBar.add(fileMenu);

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        closeItem.addActionListener(this);

        this.setJMenuBar(menuBar);
        this.add(fontBox);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontStyle);
        this.add(scrollPane);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fontColorButton){
            JColorChooser colorChooser = new JColorChooser();
            Color color = colorChooser.showDialog(null, "Choose a color", Color.BLACK);
            textArea.setForeground(color);
        }

        if(e.getSource()==fontBox){
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        if(e.getSource()==fontStyle){
            if(fontStyle.getSelectedItem() == "Plain")
                textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
            else if (fontStyle.getSelectedItem() == "Bold")
                textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.BOLD, textArea.getFont().getSize()));
            else if (fontStyle.getSelectedItem() == "Italic")
                textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.ITALIC, textArea.getFont().getSize()));
        }

        if(e.getSource()==openItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);
            if(response==JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;
                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()){
                        while(fileIn.hasNextLine()){
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileIn.close();
                }
            }
        }

        if(e.getSource()==saveItem){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);
            if(response==JFileChooser.APPROVE_OPTION){
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    fileOut.close();
                }
            }
        }

        if(e.getSource()==closeItem){
            System.exit(0);
        }
    }
}
