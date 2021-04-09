package exportacao;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class SentencasInput {

    private ArrayList<Sentenca> sentencas;

    public SentencasInput(ArrayList<Sentenca> sentencas) {
        this.sentencas = sentencas;
    }

    public ArrayList<Sentenca> ler(File arquivo) {

        FileInputStream fis;

        try {

            fis = new FileInputStream(arquivo);
            
            XStream xStream = new XStream(new DomDriver("UTF-8"));

            sentencas = (ArrayList<Sentenca>) xStream.fromXML(fis);

        } catch (FileNotFoundException ex) {

            JOptionPane.showMessageDialog(null, "Arquivo de configuração corrompido ou inapropriado.", "PGCA...", JOptionPane.ERROR_MESSAGE);
        }

        return sentencas;
    }

    public void salvar(File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver());
            String string_jpanelEmXML = xstream.toXML(sentencas);
            OutputStream streamOut;
            if (!(caminho.exists())) {
                caminho.createNewFile();
            } else {
                caminho.delete();
                caminho.createNewFile();
            }
            streamOut = new FileOutputStream(caminho);
           
            xstream.toXML(sentencas, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }

    public void salvarComo(File caminho) {
        try {
            XStream xstream = new XStream(new DomDriver());
            String string_jpanelEmXML = xstream.toXML(sentencas);
            File xmlMap = caminho;
            OutputStream streamOut;
            if (!(xmlMap.exists())) {
                xmlMap.createNewFile();
            } else {
                String novoArquivo = xmlMap.getCanonicalPath();
                xmlMap.delete();
                System.out.println("Caminho do arquivo: " + novoArquivo);
                xmlMap = new File(novoArquivo);
              
            }
            streamOut = new FileOutputStream(xmlMap);
           
            xstream.toXML(sentencas, streamOut);
        } catch (FileNotFoundException exception) {
            System.out.println("FileNotFoundException: " + exception.getMessage());
            exception.getStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
            exception.getStackTrace();
        }
    }
}
