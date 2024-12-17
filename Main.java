package xmltoJSON;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

//first we have to add json jar file
public class Main {

    public static final int PRETTY_PRINT_INDENT_FACTOR = 4;

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("XML to JSON Converter");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);

     //giving afile path to string
        String directoryPath = "C:/Users/LENOVO/Downloads/books.xml";
        
        try {
            Main converter = new Main();
            int result = converter.getPath(directoryPath);
            if (result == 1) {
                JOptionPane.showMessageDialog(null, "Conversion Completed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No XML Files Found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    
    void convertXmlToJson(String filePath) throws Exception {
        StringBuilder xmlContent = new StringBuilder();
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                xmlContent.append(line);
            }
            br.close();

         
            JSONObject xmlJSONObj = XML.toJSONObject(xmlContent.toString());
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

           
            String outputFilePath = filePath.replace(".xml", ".json");
            try (FileWriter fw = new FileWriter(outputFilePath)) {
                fw.write(jsonPrettyPrintString);
            }
        } catch (JSONException je) {
            System.out.println("Error in JSON Conversion: " + je.toString());
        } catch (IOException ioe) {
            System.out.println("File Read/Write Error: " + ioe.getMessage());
        }
    }

    //to converrt result into single file
    int getPath(String path) throws Exception {
        File dir = new File(path);
        if (dir.isDirectory()) {
            
            FilenameFilter filter = (directory, name) -> name.endsWith(".xml");
            String[] list = dir.list(filter);

            if (list == null || list.length == 0) {
                return 0; 
            }

            for (String fileName : list) {
                convertXmlToJson(path + "/" + fileName);
            }
            return 1;
        } else if (path.endsWith(".xml")) {
            //converting to single file  output will be produced in filename.json 
            convertXmlToJson(path);
            
            return 1;
        } else {
            return 0; 
        }
    }
}
