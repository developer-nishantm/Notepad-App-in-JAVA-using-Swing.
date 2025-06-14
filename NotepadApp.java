import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadApp extends JFrame implements  ActionListener {
    JTextArea textArea;
    JScrollPane scrollPane;
    String fileName = "Untitled";
    JFileChooser fileChooser;
    JCheckBoxMenuItem wordWrap;
    int fontSize = 16;

    NotepadApp(){
        // Frame Setup
        setTitle(fileName +" - Notepad");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Text Area
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
        scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File Menu
        JMenu fileMenu = new JMenu("File");
        addMenuItem(fileMenu, "New");
        addMenuItem(fileMenu, "Open");
        addMenuItem(fileMenu, "Save");
        addMenuItem(fileMenu, "Save As");
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit");
        menuBar.add(fileMenu);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        addMenuItem(editMenu, "Cut");
        addMenuItem(editMenu, "Copy");
        addMenuItem(editMenu, "Paste");
        addMenuItem(editMenu, "Delete");
        editMenu.addSeparator();
        addMenuItem(editMenu, "Select All");
        menuBar.add(editMenu);

        // Format Menu
        JMenu formatMenu = new JMenu("Format");
        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        wordWrap.addActionListener(this);
        formatMenu.add(wordWrap);
        addMenuItem(formatMenu, "Font");
        menuBar.add(formatMenu);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        addMenuItem(viewMenu,"Zoom In");
        addMenuItem(viewMenu,"Zoom Out");
        menuBar.add(viewMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        addMenuItem(helpMenu, "About");
        menuBar.add(helpMenu);

        // File Chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        setVisible(true);
    }
    private void addMenuItem(JMenu menu, String title){
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(this);
        menu.add(item);
    }
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();

        switch(command) {
            case "New":
                textArea.setText("");
                setTitle("Untitled - Notepad");
                fileName = "Untitled";
                break;
            case "Open":
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        textArea.read(br, null);
                        fileName = file.getName();
                        setTitle(fileName + " - Notepad");
                    }  catch (IOException ex) {
                        showError("File could not be opened.");
                    }
                }
                break;
            case "Save" :
                saveFile(false);
                break;
            case "Save As" :
                saveFile(true);
                break;
            case "Exit" :
                dispose();
                break;
            case "Cut" :
                textArea.cut();
                break;
            case "Copy" :
                textArea.copy();
                break;
            case "Paste" :
                textArea.paste();
                break;
            case "Delete" :
                textArea.replaceSelection("");
                break;
            case "Select All" :
                textArea.selectAll();
                break;
            case "Font" :
                Font font = new Font("Courier New", Font.PLAIN, fontSize);
                textArea.setFont(font);
                break;
            case "Zoom In" :
                fontSize += 2;
                textArea.setFont(new Font(textArea.getFont().getName(), Font.PLAIN, fontSize));
                break;
            case "Zoom Out" :
                fontSize = Math.max(8, fontSize - 2);
                textArea.setFont(new Font(textArea.getFont().getName(), Font.PLAIN, fontSize));
                break;
            case "About" :
                JOptionPane.showMessageDialog(this, "Notepad App\nCreated by Kasmera technologies", "About", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        if (e.getSource() == wordWrap){
            boolean wrap = wordWrap.isSelected();
            textArea.setLineWrap(wrap);
            textArea.setWrapStyleWord(wrap);
        }
    }
    private void saveFile(boolean saveAs){
        if (saveAs || fileName.equals("Untitled")){
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
            {
            textArea.write(bw);
            fileName = file.getName();
            setTitle(fileName + " - Notepad");
            } catch (IOException ex){
                showError("File could not be saved.");
            }
            }
        } else {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))){
                textArea.write(bw);
            } catch (IOException ex) {
                showError("File could not be saved.");
            }
        }
    }
    private void showError(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args){
        new NotepadApp();
    }
}