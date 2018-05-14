package sk.upjs.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class Analyza3DModelu_Form extends JFrame {

	private static final long serialVersionUID = 4521250931961455886L;

	private Analyza3DModelu_SwingWorker worker;

	private JTextField zdrojovySuborText;
	private JButton zdrojovySuborButton;

	private JLabel nacitanieLabel;
	private JButton nacitanieRobitButton;
	private JButton nacitanieZrusitButton;

	private JTextField cielovyAdresarText;
	private JButton cielovyAdresarButton;

	private JTextField nazovNovehoSuboruText;
	private JLabel nazovNovehoSuboruLabel;

	private JComboBox<String> algoritmyComboBox;
	private JButton algoritmyPouziButton;
	private JButton algoritmyZrusButton;
	private JLabel algoritmyLabel;

	private JSpinner parameterUholSpinner;
	private JSpinner parameterVyskaUsekuSpinner;
	// vrchol zo stropu je napevno
	private JCheckBox parameterStropCheckBox;

	private JCheckBox parameterNormalyCheckBox;
	private JCheckBox parameterJedenSuborCheckBox;
	private JCheckBox parameterOsamoteneBodyCheckBox;

	public Analyza3DModelu_Form() {

		JPanel komponenty = new JPanel();
		komponenty.setLayout(new GridLayout(10, 1));
		komponenty.setBorder(new EmptyBorder(10, 10, 10, 10));

		// --------------------------------------
		// vybratie zdrojoveho suboru
		JLabel zdrojovySuborLabel = new JLabel("S˙bor s 3D modelom: ");
		zdrojovySuborLabel.setPreferredSize(new Dimension(150, 40));

		zdrojovySuborText = new JTextField();
		zdrojovySuborText.setPreferredSize(new Dimension(100, 25));
		zdrojovySuborText.setBounds(261, 68, 80, 20);
		zdrojovySuborText.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		zdrojovySuborText.setMaximumSize(new Dimension(Integer.MAX_VALUE, zdrojovySuborText.getPreferredSize().height));
		zdrojovySuborText.setEditable(false);
		zdrojovySuborText.setText(" Vyber subor PLY, s ktor˝m sa bude pracovaù ");

		zdrojovySuborButton = new JButton("Prehæad·vaù");
		zdrojovySuborButton.setBounds(261, 68, 80, 20);

		Box zdrojovySuborBox = Box.createHorizontalBox();
		zdrojovySuborBox.add(zdrojovySuborLabel);
		zdrojovySuborBox.add(Box.createHorizontalGlue());
		zdrojovySuborBox.add(zdrojovySuborText);
		zdrojovySuborBox.add(Box.createHorizontalStrut(5));
		zdrojovySuborBox.add(zdrojovySuborButton);

		komponenty.add(zdrojovySuborBox);

		// ----------------------
		// nacitanie suboru ply
		nacitanieLabel = new JLabel("");

		nacitanieZrusitButton = new JButton("Zruö naËÌtanie");
		nacitanieRobitButton = new JButton("NaËÌtaj");
		nacitanieRobitButton.setPreferredSize(new Dimension(107, 40));

		Box nacitanieBox = Box.createHorizontalBox();
		nacitanieBox.add(nacitanieLabel);
		nacitanieBox.add(Box.createHorizontalGlue());
		nacitanieBox.add(nacitanieZrusitButton);
		nacitanieBox.add(Box.createHorizontalStrut(5));
		nacitanieBox.add(nacitanieRobitButton);

		komponenty.add(nacitanieBox);

		// ----------------------
		// Vybratie cieloveho adresara
		JLabel cielovyAdresarLabel = new JLabel("Adres·r pre novÈ s˙bory: ");
		cielovyAdresarLabel.setPreferredSize(new Dimension(150, 40));

		cielovyAdresarText = new JTextField();
		cielovyAdresarText.setPreferredSize(new Dimension(100, 25));
		cielovyAdresarText.setBounds(195, 68, 166, 20);
		cielovyAdresarText.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		cielovyAdresarText
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, cielovyAdresarText.getPreferredSize().height));
		cielovyAdresarText.setEditable(false);

		cielovyAdresarButton = new JButton("Prehæad·vaù");
		cielovyAdresarButton.setBounds(261, 68, 80, 20);

		Box cielovyAdresarBox = Box.createHorizontalBox();
		cielovyAdresarBox.add(cielovyAdresarLabel);
		cielovyAdresarBox.add(Box.createHorizontalGlue());
		cielovyAdresarBox.add(cielovyAdresarText);
		cielovyAdresarBox.add(Box.createHorizontalStrut(5));
		cielovyAdresarBox.add(cielovyAdresarButton);

		komponenty.add(cielovyAdresarBox);

		// ----------------------
		// Zadanie nazvu noveho suboru
		JLabel nazovNovehoSuboruTextLabel = new JLabel("N·zov novÈho s˙boru: ");
		nazovNovehoSuboruTextLabel.setPreferredSize(new Dimension(150, 40));

		nazovNovehoSuboruText = new JTextField();
		nazovNovehoSuboruText.setPreferredSize(new Dimension(100, 25));
		nazovNovehoSuboruText.setBounds(195, 68, 166, 20);
		nazovNovehoSuboruText.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		nazovNovehoSuboruText
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, nazovNovehoSuboruText.getPreferredSize().height));
		nazovNovehoSuboruText.setEditable(true);

		nazovNovehoSuboruLabel = new JLabel("");
		nazovNovehoSuboruLabel.setPreferredSize(new Dimension(106, 40));

		Box nazovNovehoSuboruBox = Box.createHorizontalBox();
		nazovNovehoSuboruBox.add(nazovNovehoSuboruTextLabel);
		nazovNovehoSuboruBox.add(Box.createHorizontalGlue());
		nazovNovehoSuboruBox.add(nazovNovehoSuboruText);
		nazovNovehoSuboruBox.add(Box.createHorizontalStrut(5));
		nazovNovehoSuboruBox.add(nazovNovehoSuboruLabel);

		komponenty.add(nazovNovehoSuboruBox);

		// ----------------------
		// obsluha algoritmov
		algoritmyComboBox = new JComboBox<>();
		algoritmyComboBox
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, nazovNovehoSuboruText.getPreferredSize().height));
		algoritmyComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		algoritmyComboBox.setMaximumRowCount(9);

		algoritmyPouziButton = new JButton("Pouûi");
		algoritmyPouziButton.setPreferredSize(new Dimension(107, 40));

		Box algoritmyPouziBox = Box.createHorizontalBox();
		algoritmyPouziBox.add(algoritmyComboBox);
		algoritmyPouziBox.add(Box.createHorizontalGlue());
		algoritmyPouziBox.add(Box.createHorizontalStrut(5));
		algoritmyPouziBox.add(algoritmyPouziButton);

		komponenty.add(algoritmyPouziBox);

		// ----------------------
		// parametre algoritmov

		// 1. riadok
		SpinnerModel model = new SpinnerNumberModel(50.0, 0, 100, 0.1);
		parameterUholSpinner = new JSpinner(model);
		parameterUholSpinner.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		parameterUholSpinner
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, parameterUholSpinner.getPreferredSize().height));
		JFormattedTextField txt = ((JSpinner.NumberEditor) parameterUholSpinner.getEditor()).getTextField();
		NumberFormatter formatter = (NumberFormatter) txt.getFormatter();
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		formatter.setFormat(decimalFormat);
		formatter.setAllowsInvalid(false);

		parameterNormalyCheckBox = new JCheckBox();
		parameterNormalyCheckBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

		JPanel parametre1 = new JPanel();
		GridLayout gl1 = new GridLayout(1, 2);
		gl1.setHgap(20);
		parametre1.setLayout(gl1);

		Box b11 = Box.createHorizontalBox();
		b11.add(parameterUholSpinner);
		b11.add(Box.createHorizontalGlue());
		b11.add(Box.createHorizontalStrut(5));
		b11.add(new JLabel("uhol maxim·lnej odch˝lky"));
		parametre1.add(b11);

		Box b12 = Box.createHorizontalBox();
		b12.add(parameterNormalyCheckBox);
		b12.add(Box.createHorizontalStrut(5));
		b12.add(new JLabel("zohæadÚovaù norm·ly"));
		parametre1.add(b12);

		komponenty.add(parametre1);

		// 2. riadok
		SpinnerModel model2 = new SpinnerNumberModel(1.000, 0, Float.MAX_VALUE, 0.001);
		parameterVyskaUsekuSpinner = new JSpinner(model2);
		parameterVyskaUsekuSpinner.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		parameterVyskaUsekuSpinner
				.setPreferredSize(new Dimension(2, parameterVyskaUsekuSpinner.getPreferredSize().height));
		parameterVyskaUsekuSpinner
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, parameterVyskaUsekuSpinner.getPreferredSize().height));

		JFormattedTextField txt2 = ((JSpinner.NumberEditor) parameterVyskaUsekuSpinner.getEditor()).getTextField();
		NumberFormatter formatter2 = (NumberFormatter) txt2.getFormatter();
		DecimalFormat decimalFormat2 = new DecimalFormat("0.000");
		formatter2.setFormat(decimalFormat2);
		formatter2.setAllowsInvalid(false);

		parameterJedenSuborCheckBox = new JCheckBox();
		parameterJedenSuborCheckBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

		JPanel parametre2 = new JPanel();
		GridLayout gl2 = new GridLayout(1, 2);
		gl2.setHgap(20);
		parametre2.setLayout(gl2);

		Box b21 = Box.createHorizontalBox();
		b21.add(parameterVyskaUsekuSpinner);
		b21.add(Box.createHorizontalStrut(5));
		JLabel l = new JLabel("v˝öka ˙seku pre objem");
		l.setPreferredSize(new Dimension(145, 40));
		b21.add(l);
		parametre2.add(b21);

		Box b22 = Box.createHorizontalBox();
		b22.add(parameterJedenSuborCheckBox);
		b22.add(Box.createHorizontalStrut(5));
		b22.add(new JLabel("do jednÈho s˙boru"));
		parametre2.add(b22);

		komponenty.add(parametre2);

		// 3. riadok
		parameterOsamoteneBodyCheckBox = new JCheckBox();
		parameterOsamoteneBodyCheckBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

		JPanel parametre3 = new JPanel();
		GridLayout gl3 = new GridLayout(1, 2);
		gl3.setHgap(20);
		parametre3.setLayout(gl3);

		Box b31 = Box.createHorizontalBox();
		JTextField tf = new JTextField("glob·lne maximum");
		tf.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
		tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, tf.getPreferredSize().height));
		tf.setEditable(false);
		b31.add(tf);
		b31.add(Box.createHorizontalGlue());
		b31.add(Box.createHorizontalStrut(5));
		JLabel l3 = new JLabel("vrchol patriaci stropu");
		l3.setPreferredSize(new Dimension(145, 40));
		b31.add(l3);
		parametre3.add(b31);

		Box b32 = Box.createHorizontalBox();
		b32.add(parameterOsamoteneBodyCheckBox);
		b32.add(Box.createHorizontalStrut(5));
		b32.add(new JLabel("aj osamotenÈ vrcholy"));
		parametre3.add(b32);

		komponenty.add(parametre3);

		// 4. riadok
		parameterStropCheckBox = new JCheckBox();
		parameterStropCheckBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

		JPanel parametre4 = new JPanel();
		GridLayout gl4 = new GridLayout(1, 2);
		gl4.setHgap(20);
		parametre4.setLayout(gl4);

		Box b41 = Box.createHorizontalBox();
		b41.add(Box.createHorizontalGlue());
		b41.add(parameterStropCheckBox);
		b41.add(Box.createHorizontalStrut(5));
		JLabel l4 = new JLabel("aj s˙bor so stropom");
		l4.setPreferredSize(new Dimension(145, 40));
		b41.add(l4);
		parametre4.add(b41);

		Box b42 = Box.createHorizontalBox();
		b42.add(new JLabel(""));
		parametre4.add(b42);

		komponenty.add(parametre4);

		// ----------------------
		// priebeh a zastavenie algoritmu
		algoritmyLabel = new JLabel("");

		algoritmyZrusButton = new JButton("Zruö akciu");
		algoritmyZrusButton.setPreferredSize(new Dimension(107, 40));

		Box algoritmyZrusBox = Box.createHorizontalBox();
		algoritmyZrusBox.add(algoritmyLabel);
		algoritmyZrusBox.add(Box.createHorizontalGlue());
		algoritmyZrusBox.add(Box.createHorizontalStrut(5));
		algoritmyZrusBox.add(algoritmyZrusButton);

		komponenty.add(algoritmyZrusBox);

		// ------------------------------

		add(komponenty);
		setSize(new Dimension(600, 400));
		setMinimumSize(new Dimension(600, 400));
		setTitle("Spracovanie 3D modelu stropu jaskyne");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Analyza3DModelu_Form form = new Analyza3DModelu_Form();
			form.setIconImage(new javax.swing.ImageIcon(form.getClass().getResource("3Dmodel.png")).getImage());
			form.setVisible(true);
			form.obsluhaKomponentov();
		});
	}

	private void obsluhaKomponentov() {
		worker = new Analyza3DModelu_SwingWorker(zdrojovySuborText, zdrojovySuborButton, nacitanieLabel,
				nacitanieRobitButton, nacitanieZrusitButton, cielovyAdresarText, cielovyAdresarButton,
				nazovNovehoSuboruText, nazovNovehoSuboruLabel, algoritmyComboBox, algoritmyPouziButton,
				algoritmyZrusButton, algoritmyLabel, parameterUholSpinner, parameterStropCheckBox,
				parameterNormalyCheckBox, parameterJedenSuborCheckBox, parameterOsamoteneBodyCheckBox,
				parameterVyskaUsekuSpinner);
		worker.execute();
	}

}
