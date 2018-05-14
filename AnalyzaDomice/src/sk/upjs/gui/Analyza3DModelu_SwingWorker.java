package sk.upjs.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import sk.upjs.gui.objemUlohy.VykonavacObjemuUloh;
import sk.upjs.gui.ulohy.VykonavacUloh;
import sk.upjs.pracaSDatami.VyrobcaANacitavacSuboruPLY;
import sk.upjs.service.AnalyzaServiceImplementacia;

/**
 * Obsluhuje komponenty v okne aplikacie
 */
public class Analyza3DModelu_SwingWorker extends SwingWorker<Void, Integer> {

	private AnalyzaServiceImplementacia service;
	private VykonavaSaUlohaAStav vykonavaSaUloha;
	private ExecutorService exekutor;

	private String nazovZdrojovehoSuboru = "";
	private String prebiehaNacitanieSuboruSprava = "NaËÌtava sa ";
	private String prebiehaAlgoritmusSprava = "Prebieha v˝poËet ";

	private boolean vykonavaSaNacitanie = false;
	private boolean vykonavaSaAlgoritmus = false;
	private boolean vykonavaSaAlgoritmusObjemu = false;
	
	private String[] nazvyAlgoritmov = { "Lok·lne minim· celÈho 3D modelu",
			"Stalaktity po prvÈ rozvetvenie zo vöetk˝ch lok·lnych minÌm",
			"Stalaktity po prvÈ rozvetvenie z lok·lnych minÌm nepatriacich stropu", "Stalaktity po oddelenÌ stropu",
			"Lok·lne minim· 3D modelu, ktorÈ nepatria stropu", "S˙vislÈ Ëasti 3D modelu do osobitn˝ch s˙borov",
			"Identifikovanie stalaktitov 3D modelu", "Objem identifikovan˝ch stalaktitov (identifikuje a vypoËÌta objem)",
			"Objem stalaktitov (ak naËÌtan˝ s˙bor obsahuje len stalaktity)" };
	private int idxVybranehoAlgoritmu = 0;

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
	// vrchol zo stropu je napevno
	private JCheckBox parameterStropCheckBox;
	private JSpinner parameterVyskaUsekuSpinner;

	private JCheckBox parameterNormalyCheckBox;
	private JCheckBox parameterJedenSuborCheckBox;
	private JCheckBox parameterOsamoteneBodyCheckBox;

	public Analyza3DModelu_SwingWorker(JTextField zdrojovySuborText, JButton zdrojovySuborButton, JLabel nacitanieLabel,
			JButton nacitanieRobitButton, JButton nacitanieZrusitButton, JTextField cielovyAdresarText,
			JButton cielovyAdresarButton, JTextField nazovNovehoSuboruText, JLabel nazovNovehoSuboruLabel,
			JComboBox<String> algoritmyComboBox, JButton algoritmyPouziButton, JButton algoritmyZrusButton,
			JLabel algoritmyLabel, JSpinner parameterUholSpinner, JCheckBox parameterStropCheckBox,
			JCheckBox parameterNormalyCheckBox, JCheckBox parameterJedenSuborCheckBox,
			JCheckBox parameterOsamoteneBodyCheckBox, JSpinner parameterVyskaUsekuSpinner) {

		super();
		this.zdrojovySuborText = zdrojovySuborText;
		this.zdrojovySuborButton = zdrojovySuborButton;
		this.nacitanieLabel = nacitanieLabel;
		this.nacitanieRobitButton = nacitanieRobitButton;
		this.nacitanieZrusitButton = nacitanieZrusitButton;
		this.cielovyAdresarText = cielovyAdresarText;
		this.cielovyAdresarButton = cielovyAdresarButton;
		this.nazovNovehoSuboruText = nazovNovehoSuboruText;
		this.nazovNovehoSuboruLabel = nazovNovehoSuboruLabel;
		this.algoritmyComboBox = algoritmyComboBox;
		this.algoritmyPouziButton = algoritmyPouziButton;
		this.algoritmyZrusButton = algoritmyZrusButton;
		this.algoritmyLabel = algoritmyLabel;
		this.parameterUholSpinner = parameterUholSpinner;
		this.parameterStropCheckBox = parameterStropCheckBox;
		this.parameterNormalyCheckBox = parameterNormalyCheckBox;
		this.parameterJedenSuborCheckBox = parameterJedenSuborCheckBox;
		this.parameterOsamoteneBodyCheckBox = parameterOsamoteneBodyCheckBox;
		this.parameterVyskaUsekuSpinner = parameterVyskaUsekuSpinner;

		inicializaciaKomponentov();
	}

	private void inicializaciaKomponentov() {

		// zdrojovySuborText.setEnabled(false);
		zdrojovySuborButton.setEnabled(true);
		// nacitanieLabel.setEnabled(false);
		nacitanieRobitButton.setEnabled(true);
		nacitanieZrusitButton.setEnabled(false);
		cielovyAdresarText.setEnabled(false);
		cielovyAdresarButton.setEnabled(false);
		nazovNovehoSuboruText.setEnabled(false);
		// nazovNovehoSuboruLabel.setEnabled(false);
		algoritmyComboBox.setEnabled(false);
		algoritmyPouziButton.setEnabled(false);
		algoritmyZrusButton.setEnabled(false);
		// algoritmyLabel.setEnabled(false);
		parameterUholSpinner.setEnabled(false);
		parameterStropCheckBox.setEnabled(false);
		parameterNormalyCheckBox.setEnabled(false);
		parameterJedenSuborCheckBox.setEnabled(false);
		parameterOsamoteneBodyCheckBox.setEnabled(false);
		parameterVyskaUsekuSpinner.setEnabled(false);

		vykonavaSaUloha = new VykonavaSaUlohaAStav();

		zdrojovySuborButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(new FileFilter() {

					public String getDescription() {
						return "PLY Files (*.ply)";
					}

					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						} else {
							String filename = f.getName().toLowerCase();
							return filename.endsWith(".ply");
						}
					}
				});
				int vratilo = jfc.showDialog(null, "Select file");
				if (vratilo == JFileChooser.APPROVE_OPTION) {
					File subor = jfc.getSelectedFile();
					zdrojovySuborText.setText(subor.getAbsolutePath());
				}
			}
		});

		nacitanieLabel.setForeground(Color.GRAY);
		nacitanieLabel.setText("Treba naËÌtaù s˙bor");

		nacitanieRobitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				File zdrojovySubor = new File(zdrojovySuborText.getText());
				if (zdrojovySubor.exists()) {
					nacitanieLabel.setForeground(Color.BLACK);
					nazovZdrojovehoSuboru = zdrojovySubor.getName();
					service = new AnalyzaServiceImplementacia(zdrojovySubor);
					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(vykonavaSaUloha, service));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaNacitanie = true;
					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(true);
				} else {
					nacitanieLabel.setForeground(Color.RED);
					nacitanieLabel.setText("Vyberte s˙bor!");
				}
			}
		});

		nacitanieZrusitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (vykonavaSaNacitanie) {
					exekutor.shutdownNow();
				} else {
					nacitanieLabel.setForeground(Color.GRAY);
					nacitanieLabel.setText("Treba naËÌtaù s˙bor");
					zdrojovySuborButton.setEnabled(true);
					nacitanieRobitButton.setEnabled(true);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyLabel.setText("");
				}
			}
		});

		cielovyAdresarText.setText(System.getProperty("user.dir"));
		cielovyAdresarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				jfc.setDialogTitle("Choose output directory");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int vratilo = jfc.showDialog(null, "Choose");
				if (vratilo == JFileChooser.APPROVE_OPTION) {
					File subor = jfc.getSelectedFile();
					cielovyAdresarText.setText(subor.getAbsolutePath());
				}
			}
		});

		nazovNovehoSuboruText.setText("novySubor");
		nazovNovehoSuboruLabel.setText(".ply");

		for (int i = 0; i < nazvyAlgoritmov.length; i++) {
			algoritmyComboBox.addItem(nazvyAlgoritmov[i]);
		}
		algoritmyComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String vybranyAlgoritmus = (String) algoritmyComboBox.getSelectedItem();
				algoritmyLabel.setText("");

				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[0])) {
					// lokalne minima celeho
					povolitKomponentyPreAlgoritmus(0);
					idxVybranehoAlgoritmu = 0;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[1])) {
					// Stalaktity po prvÈ rozvetvenie zo vöetk˝ch lok·lnych minÌm
					povolitKomponentyPreAlgoritmus(1);
					idxVybranehoAlgoritmu = 1;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[2])) {
					// Stalaktity po prvÈ rozvetvenie z lok·lnych minÌm nepatriacich stropu
					povolitKomponentyPreAlgoritmus(2);
					idxVybranehoAlgoritmu = 2;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[3])) {
					// Stalaktity po oddelenÌ stropu
					povolitKomponentyPreAlgoritmus(3);
					idxVybranehoAlgoritmu = 3;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[4])) {
					// Lok·lne minim· 3D modelu, ktorÈ nepatria stropu
					povolitKomponentyPreAlgoritmus(4);
					idxVybranehoAlgoritmu = 4;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[5])) {
					// Komponenty s˙vislosti do osobitn˝ch s˙borov
					povolitKomponentyPreAlgoritmus(5);
					idxVybranehoAlgoritmu = 5;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[6])) {
					// Vyseparovanie stalaktitov 3D modelu
					povolitKomponentyPreAlgoritmus(6);
					idxVybranehoAlgoritmu = 6;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[7])) {
					// Objem po vyseparovani
					povolitKomponentyPreAlgoritmus(7);
					idxVybranehoAlgoritmu = 7;
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[8])) {
					// Objem zo suboru len so stalaktitmi
					povolitKomponentyPreAlgoritmus(8);
					idxVybranehoAlgoritmu = 8;
					return;
				}
			}

		});

		algoritmyPouziButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String cielovyAdresar = cielovyAdresarText.getText();
				File cielovyAdresarFile = new File(cielovyAdresar);
				if (!cielovyAdresarFile.isDirectory()) {
					nazovNovehoSuboruLabel.setForeground(Color.RED);
					nazovNovehoSuboruLabel.setText("Nezn·my adres·r!");
					return;
				}

				String nazovSuboru = nazovNovehoSuboruText.getText();

				if (!VyrobcaANacitavacSuboruPLY.suLenPovoleneZnaky(nazovSuboru)) {
					algoritmyLabel.setForeground(Color.RED);
					algoritmyLabel.setText("N·zov s˙boru nesmie obsahovaù tieto znaky: \\ / : * ? \" < > |");
					return;
				}

				String vybranyAlgoritmus = (String) algoritmyComboBox.getSelectedItem();

				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[0])) {
					// lokalne minima celeho
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(0, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru,
							zohladnovatNormaly));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[1])) {
					// Stalaktity po prvÈ rozvetvenie zo vöetk˝ch lok·lnych minÌm
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();
					boolean doJednehoSuboru = parameterJedenSuborCheckBox.isSelected();
					boolean ajOsamoteneBody = parameterOsamoteneBodyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(1, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru,
							zohladnovatNormaly, doJednehoSuboru, ajOsamoteneBody));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[2])) {
					// Stalaktity po prvÈ rozvetvenie z lok·lnych minÌm nepatriacich stropu
					float uholMaxOdchylky = Float.parseFloat(parameterUholSpinner.getValue().toString());
					int idxVrcholuZoStropu = service.idxGlobalnehoMax();
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();
					boolean doJednehoSuboru = parameterJedenSuborCheckBox.isSelected();
					boolean ajOsamoteneBody = parameterOsamoteneBodyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(2, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru,
							zohladnovatNormaly, doJednehoSuboru, ajOsamoteneBody, uholMaxOdchylky, idxVrcholuZoStropu));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[3])) {
					// Stalaktity po oddelenÌ stropu
					float uholMaxOdchylky = Float.parseFloat(parameterUholSpinner.getValue().toString());
					int idxVrcholuZoStropu = service.idxGlobalnehoMax();
					boolean ajSuborSOddelenymStropom = parameterStropCheckBox.isSelected();
					boolean doJednehoSuboru = parameterJedenSuborCheckBox.isSelected();
					boolean ajOsamoteneBody = parameterOsamoteneBodyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(
							new VykonavacUloh(3, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru, uholMaxOdchylky,
									idxVrcholuZoStropu, ajSuborSOddelenymStropom, doJednehoSuboru, ajOsamoteneBody));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[4])) {
					// Lok·lne minim· 3D modelu, ktorÈ nepatria stropu
					float uholMaxOdchylky = Float.parseFloat(parameterUholSpinner.getValue().toString());
					int idxVrcholuZoStropu = service.idxGlobalnehoMax();
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(4, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru,
							uholMaxOdchylky, idxVrcholuZoStropu, zohladnovatNormaly));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[5])) {
					// Komponenty s˙vislosti do osobitn˝ch s˙borov
					boolean ajOsamoteneBody = parameterOsamoteneBodyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(5, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru,
							ajOsamoteneBody));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[6])) {
					// Vyseparovanie stalaktitov 3D modelu
					float uholMaxOdchylky = Float.parseFloat(parameterUholSpinner.getValue().toString());
					int idxVrcholuZoStropu = service.idxGlobalnehoMax();
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();
					boolean ajSuborSOddelenymStropom = parameterStropCheckBox.isSelected();
					boolean doJednehoSuboru = parameterJedenSuborCheckBox.isSelected();
					boolean ajOsamoteneBody = parameterOsamoteneBodyCheckBox.isSelected();

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacUloh(6, vykonavaSaUloha, service, cielovyAdresar, nazovSuboru,
							uholMaxOdchylky, idxVrcholuZoStropu, zohladnovatNormaly, ajSuborSOddelenymStropom,
							doJednehoSuboru, ajOsamoteneBody));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmus = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[7])) {
					// Objem vyseparovan˝ch stalaktitov
					float uholMaxOdchylky = Float.parseFloat(parameterUholSpinner.getValue().toString());
					int idxVrcholuZoStropu = service.idxGlobalnehoMax();
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();
					float vyskaUseku = Float.parseFloat(parameterVyskaUsekuSpinner.getValue().toString());

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacObjemuUloh(vykonavaSaUloha, service, uholMaxOdchylky,
							idxVrcholuZoStropu, zohladnovatNormaly, vyskaUseku));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmusObjemu = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
				if (vybranyAlgoritmus.equals(nazvyAlgoritmov[8])) {
					// Objem stalaktitov v subore len so stalaktitmi					
					boolean zohladnovatNormaly = parameterNormalyCheckBox.isSelected();
					float vyskaUseku = Float.parseFloat(parameterVyskaUsekuSpinner.getValue().toString());

					exekutor = Executors.newSingleThreadExecutor();
					exekutor.execute(new VykonavacObjemuUloh(vykonavaSaUloha, service, zohladnovatNormaly, vyskaUseku));
					vykonavaSaUloha.setPrebiehaVykonavanie(true);
					vykonavaSaAlgoritmusObjemu = true;

					zdrojovySuborButton.setEnabled(false);
					nacitanieRobitButton.setEnabled(false);
					nacitanieZrusitButton.setEnabled(false);
					povolitZakazatKomponentyNaVyberAlgoritmu(false);
					algoritmyZrusButton.setEnabled(true);
					return;
				}
			}
		});

		algoritmyZrusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (vykonavaSaAlgoritmus) {
					exekutor.shutdownNow();
				}
				if (vykonavaSaAlgoritmusObjemu) {
					exekutor.shutdownNow();
				}
			}
		});

	}

	@Override
	protected Void doInBackground() throws Exception {

		int pocetBodiek = 1;
		while (!Thread.currentThread().isInterrupted()) {
			if (vykonavaSaNacitanie) {
				algoritmyLabel.setForeground(Color.BLACK);
				while (vykonavaSaUloha.isPrebiehaVykonavanie()) {
					switch (pocetBodiek) {
					case 1:
						nacitanieLabel.setText(prebiehaNacitanieSuboruSprava + ".");
						break;
					case 2:
						nacitanieLabel.setText(prebiehaNacitanieSuboruSprava + "..");
						break;
					case 3:
						nacitanieLabel.setText(prebiehaNacitanieSuboruSprava + "...");
						break;
					}
					pocetBodiek = (pocetBodiek + 1) % 3 + 1;
					Thread.sleep(200);
				}
				if (vykonavaSaUloha.isVysledokVykonania()) {
					nacitanieLabel.setText("NaËÌtan˝ s˙bor: " + nazovZdrojovehoSuboru);
					vykonavaSaNacitanie = false;
					povolitKomponentyPreAlgoritmus(idxVybranehoAlgoritmu);
				} else {
					nacitanieLabel.setText("Nepodarilo sa naËÌtaù s˙bor: " + nazovZdrojovehoSuboru);
					vykonavaSaNacitanie = false;
					zdrojovySuborButton.setEnabled(true);
					nacitanieRobitButton.setEnabled(true);
					nacitanieZrusitButton.setEnabled(false);
				}
			}

			if (vykonavaSaAlgoritmus) {
				algoritmyLabel.setForeground(Color.BLACK);
				while (vykonavaSaUloha.isPrebiehaVykonavanie()) {
					switch (pocetBodiek) {
					case 1:
						algoritmyLabel.setText(prebiehaAlgoritmusSprava + ".");
						break;
					case 2:
						algoritmyLabel.setText(prebiehaAlgoritmusSprava + "..");
						break;
					case 3:
						algoritmyLabel.setText(prebiehaAlgoritmusSprava + "...");
						break;
					}
					pocetBodiek = (pocetBodiek + 1) % 3 + 1;
					Thread.sleep(200);
				}
				if (vykonavaSaUloha.isVysledokVykonania()) {
					algoritmyLabel.setForeground(Color.BLACK);
					algoritmyLabel.setText("V˝poËet prebehol ˙speöne");
					vykonavaSaAlgoritmus = false;
					povolitKomponentyPreAlgoritmus(idxVybranehoAlgoritmu);
					nacitanieZrusitButton.setEnabled(true);
				} else {
					algoritmyLabel.setForeground(Color.BLACK);
					algoritmyLabel.setText("Nepodarilo sa dokonËiù v˝poËet");
					vykonavaSaAlgoritmus = false;
					povolitKomponentyPreAlgoritmus(idxVybranehoAlgoritmu);
					nacitanieZrusitButton.setEnabled(true);
				}
			}

			if (vykonavaSaAlgoritmusObjemu) {
				algoritmyLabel.setForeground(Color.BLACK);
				while (vykonavaSaUloha.isPrebiehaVykonavanie()) {
					switch (pocetBodiek) {
					case 1:
						algoritmyLabel.setText(prebiehaAlgoritmusSprava + ".");
						break;
					case 2:
						algoritmyLabel.setText(prebiehaAlgoritmusSprava + "..");
						break;
					case 3:
						algoritmyLabel.setText(prebiehaAlgoritmusSprava + "...");
						break;
					}
					pocetBodiek = (pocetBodiek + 1) % 3 + 1;
					Thread.sleep(200);
				}
				if (vykonavaSaUloha.getObjem() == -1) {
					algoritmyLabel.setForeground(Color.BLACK);
					algoritmyLabel.setText("Objem sa nepoderilo vypoËÌtaù");
					vykonavaSaAlgoritmusObjemu = false;
					povolitKomponentyPreAlgoritmus(idxVybranehoAlgoritmu);
					nacitanieZrusitButton.setEnabled(true);
				} else {
					algoritmyLabel.setForeground(Color.BLACK);
					double objem = vykonavaSaUloha.getObjem();
					DecimalFormat decimalFormat = new DecimalFormat("0.00000000");
					String objemString = decimalFormat.format(objem);
					algoritmyLabel.setText("Objem je: " + objemString+" kubick˝ch dÂûkov˝ch jednotiek");
					vykonavaSaAlgoritmusObjemu = false;
					povolitKomponentyPreAlgoritmus(idxVybranehoAlgoritmu);
					nacitanieZrusitButton.setEnabled(true);
				}
			}
			Thread.sleep(100);
		}
		return null;
	}

	private void povolitZakazatKomponentyNaVyberAlgoritmu(boolean povolit) {

		if (povolit) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(true);
			parameterStropCheckBox.setEnabled(true);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(true);
			parameterOsamoteneBodyCheckBox.setEnabled(true);
			parameterVyskaUsekuSpinner.setEnabled(true);
		} else {
			cielovyAdresarText.setEnabled(false);
			cielovyAdresarButton.setEnabled(false);
			nazovNovehoSuboruText.setEnabled(false);
			algoritmyComboBox.setEnabled(false);
			algoritmyPouziButton.setEnabled(false);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(false);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(false);
			parameterJedenSuborCheckBox.setEnabled(false);
			parameterOsamoteneBodyCheckBox.setEnabled(false);
			parameterVyskaUsekuSpinner.setEnabled(false);
		}
	}

	private void povolitKomponentyPreAlgoritmus(int idxAlgoritmu) {
		// Lok·lne minim· celÈho 3D modelu
		if (idxAlgoritmu == 0) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(false);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(false);
			parameterOsamoteneBodyCheckBox.setEnabled(false);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// Stalaktity po prvÈ rozvetvenie zo vöetk˝ch lok·lnych minÌm
		if (idxAlgoritmu == 1) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(false);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(true);
			parameterOsamoteneBodyCheckBox.setEnabled(true);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// Stalaktity po prvÈ rozvetvenie z lok·lnych minÌm nepatriacich stropu
		if (idxAlgoritmu == 2) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(true);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(true);
			parameterOsamoteneBodyCheckBox.setEnabled(true);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// Stalaktity po oddelenÌ stropu
		if (idxAlgoritmu == 3) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(true);
			parameterStropCheckBox.setEnabled(true);
			parameterNormalyCheckBox.setEnabled(false);
			parameterJedenSuborCheckBox.setEnabled(true);
			parameterOsamoteneBodyCheckBox.setEnabled(true);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// Lok·lne minim· 3D modelu, ktorÈ nepatria stropu
		if (idxAlgoritmu == 4) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(true);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(false);
			parameterOsamoteneBodyCheckBox.setEnabled(false);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// Komponenty s˙vislosti do osobitn˝ch s˙borov
		if (idxAlgoritmu == 5) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(false);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(false);
			parameterJedenSuborCheckBox.setEnabled(false);
			parameterOsamoteneBodyCheckBox.setEnabled(true);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// Vyseparovanie stalaktitov 3D modelu
		if (idxAlgoritmu == 6) {
			cielovyAdresarText.setEnabled(true);
			cielovyAdresarButton.setEnabled(true);
			nazovNovehoSuboruText.setEnabled(true);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(true);
			parameterStropCheckBox.setEnabled(true);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(true);
			parameterOsamoteneBodyCheckBox.setEnabled(true);
			parameterVyskaUsekuSpinner.setEnabled(false);
			return;
		}
		// objem vyseparovanych stalaktitov 3D modelu
		if (idxAlgoritmu == 7) {
			cielovyAdresarText.setEnabled(false);
			cielovyAdresarButton.setEnabled(false);
			nazovNovehoSuboruText.setEnabled(false);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(true);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(false);
			parameterOsamoteneBodyCheckBox.setEnabled(false);
			parameterVyskaUsekuSpinner.setEnabled(true);
			return;
		}
		// objem z 3D modelu len so stalaktitmi
		if (idxAlgoritmu == 8) {
			cielovyAdresarText.setEnabled(false);
			cielovyAdresarButton.setEnabled(false);
			nazovNovehoSuboruText.setEnabled(false);
			algoritmyComboBox.setEnabled(true);
			algoritmyPouziButton.setEnabled(true);
			algoritmyZrusButton.setEnabled(false);
			parameterUholSpinner.setEnabled(false);
			parameterStropCheckBox.setEnabled(false);
			parameterNormalyCheckBox.setEnabled(true);
			parameterJedenSuborCheckBox.setEnabled(false);
			parameterOsamoteneBodyCheckBox.setEnabled(false);
			parameterVyskaUsekuSpinner.setEnabled(true);
			return;
		}
	}

}
