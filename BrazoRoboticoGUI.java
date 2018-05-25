package Chapi;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public final class BrazoRoboticoGUI extends JFrame implements ActionListener{
    PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
    JPanel PanelPrincipal;
    JButton GarraOpen, GarraClose, CodoBoton, HombroBoton, BaseBoton, 
            Restaurar, Automatico;
    JTextField BaseTextField, HombroTextField, CodoTextField;
        
    public BrazoRoboticoGUI(){
        try {
            //Se inicia la comunicación con el Puerto Serie
            ino.arduinoTX("COM11", 9600); 
        } catch (ArduinoException ex) {
            Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        PanelPrincipal = new JPanel();
        setVisible(true);
        panelPrincipal();
        
    }

    public void frame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new AbsoluteLayout());        
        getContentPane().add(PanelPrincipal, 
                new AbsoluteConstraints(0, 0, 550, 500));
        pack();
    }
    
	//manda llamar a los metodos de cada boton
    public void panelPrincipal(){
        PanelPrincipal.setLayout(new AbsoluteLayout());
        componentesGarra();
        componentesCodo();
        componentesHombro();
        componentesBase();
        restaurar();
        automatico();
        frame();
    }
    
	//GARRA: al dar click en OPEN, la garra se abre, manda al arduino A180 para identificar el servo y los grados a girar
    public void componentesGarra(){
        GarraOpen = new JButton();
        GarraClose = new JButton();
        JLabel GarraImage = new JLabel();       
        GarraOpen.setText("Open");
        GarraOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GarraOpenActionPerformed(evt);
            }

            private void GarraOpenActionPerformed(ActionEvent evt) {
                try {
                    ino.sendData("A180");
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
		//GARRA: al dar click en CLOSE, la garra se cierra, manda al arduino A0 para identificar el servo y los grados a girar
        PanelPrincipal.add(GarraOpen, new AbsoluteConstraints(20, 40, 70, -1));
        GarraClose.setText("Close");
        GarraClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GarraCloseActionPerformed(evt);
            }
            private void GarraCloseActionPerformed(ActionEvent evt) {
                try {
                    ino.sendData("A0");
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }     
            }
        });
        PanelPrincipal.add(GarraClose, new AbsoluteConstraints(20, 70, 70, -1));

        GarraImage.setIcon(new ImageIcon(getClass().getResource("garra2.jpg")));
        PanelPrincipal.add(GarraImage, new AbsoluteConstraints(100, 30, 100, 70));
        JTextArea GarraText = new JTextArea();
        GarraText.setEditable(false);
        GarraText.setText("Pulsa el botón abrir o cerrar para activar la "
                + "\nGarra del Brazo Robotico");
        PanelPrincipal.add(GarraText, new AbsoluteConstraints(220, 30, 290, 70));        
    }
    
    public void componentesCodo(){
        CodoBoton = new JButton();
        CodoBoton.setText("Girar");
        CodoBoton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CodoBotonActionPerformed(evt);
            }

            private void CodoBotonActionPerformed(ActionEvent evt) {
                try {
                    ino.sendData("B"+CodoTextField.getText());
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
        PanelPrincipal.add(CodoBoton, new AbsoluteConstraints(20, 140, 70, -1));
        CodoTextField = new JTextField();
        PanelPrincipal.add(CodoTextField, new AbsoluteConstraints(20, 170, 70, -1));
        JLabel CodoImage = new JLabel();
        CodoImage.setIcon(new ImageIcon(getClass().getResource("codo2.jpg"))); 
        PanelPrincipal.add(CodoImage, new AbsoluteConstraints(100, 120, 100, 70));
        JTextArea CodoText = new JTextArea();
        CodoText.setEditable(false);
        CodoText.setText("Ingresa los grados que desees que gire el "
                + "\nCodo del Brazo Robotico");
        PanelPrincipal.add(CodoText, new AbsoluteConstraints(220, 120, 290, 70));        
    }
    
    public void componentesHombro(){
        HombroBoton = new JButton();
        HombroBoton.setText("Girar");
        HombroBoton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                HombroBotonActionPerformed(evt);
            }
            private void HombroBotonActionPerformed(ActionEvent evt) {
                try {
                    ino.sendData("C"+HombroTextField.getText());
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
        PanelPrincipal.add(HombroBoton, new AbsoluteConstraints(20, 230, 70, -1));
        HombroTextField = new JTextField();
        PanelPrincipal.add(HombroTextField, new AbsoluteConstraints(20, 260, 70, -1));
        JLabel HombroImage = new JLabel();    
        HombroImage.setIcon(new ImageIcon(getClass().getResource("hombro2.jpg"))); 
        PanelPrincipal.add(HombroImage, new AbsoluteConstraints(100, 210, 100, 70));
        JTextArea HombroText = new JTextArea();
        HombroText.setEditable(false);
        HombroText.setText("Ingresa los grados que desees que gire el "
                + "\nHombro del Brazo Robotico");
        PanelPrincipal.add(HombroText, new AbsoluteConstraints(220, 210, 290, 70));        
    }
    
    public void componentesBase(){
        BaseBoton = new JButton();
        BaseBoton.setText("Girar");
        BaseBoton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                BaseBotonActionPerformed(evt);
            }

            private void BaseBotonActionPerformed(ActionEvent evt) {
                try {
                    ino.sendData("D"+BaseTextField.getText());
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
        PanelPrincipal.add(BaseBoton, new AbsoluteConstraints(20, 320, 70, -1));
        BaseTextField = new JTextField();
        PanelPrincipal.add(BaseTextField, new AbsoluteConstraints(20, 350, 70, -1));
        JLabel BaseImage = new JLabel();
        BaseImage.setIcon(new ImageIcon(getClass().getResource("base2.jpg"))); 
        PanelPrincipal.add(BaseImage, new AbsoluteConstraints(100, 300, 100, 70));
        JTextArea BaseText = new JTextArea();
        BaseText.setEditable(false);
        BaseText.setText("Ingresa los grados que desees que gire la "
                + "\nBase del Brazo Robotico");
        PanelPrincipal.add(BaseText, new AbsoluteConstraints(220, 300, 290, 70));        
    }
    
    public void restaurar(){
        Restaurar = new JButton();
        Restaurar.setText("Restaurar");
        Restaurar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CodoBotonActionPerformed(evt);
            }
            private void CodoBotonActionPerformed(ActionEvent evt) {
                try {
                    ino.sendData("E");
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
        PanelPrincipal.add(Restaurar, new AbsoluteConstraints(20, 410, 110, -1));
    }
    
	//al tener guardado algo en la EEPROM del arduino, se meter a la memoria mandando la orden F
    public void automatico(){
        Automatico = new JButton();
        Automatico.setText("Automatico");
        Automatico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CodoBotonActionPerformed(evt);
            }
            private void CodoBotonActionPerformed(ActionEvent evt) {
                try {   
                    ino.sendData("F");
                } catch (ArduinoException | SerialPortException ex) {
                    Logger.getLogger(BrazoRoboticoGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
        PanelPrincipal.add(Automatico, new AbsoluteConstraints(200, 410, 120, -1));        
    }
    
    public static void main(String[] args) {
        BrazoRoboticoGUI o = new BrazoRoboticoGUI();        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
    }
    
}