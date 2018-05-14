package sk.upjs.pracaSDatami;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import sk.upjs.entity.NacitanySubor;
import sk.upjs.entity.Vertex;

/**
 * Ma metody na nacitanie a vyrabanie suboru PLY
 */
public class VyrobcaANacitavacSuboruPLY {

	/**
	 * Zo zadaneho suboru PLY vyberie informacie o vrcholoch co su tu ulozene
	 * 
	 * @param suborPLY
	 *            z 3D modelom ktory chceme nacitat
	 * @return NacitanySubor s informaciami z nacitaneho 3D modelu
	 */
	public static NacitanySubor nacitajSubor(File suborPLY) {

		String hlavickaSuboru = "";
		Vertex[] vrcholy;
		int pocetVrcholov = -1;
		int pocetHran = -1;
		int idxGlobalnehoMaxima = -1;
		float maxZ = Float.NEGATIVE_INFINITY;
		int idxGlobalnehoMinima = -1;
		float minZ = Float.POSITIVE_INFINITY;

		try (Scanner scanner = new Scanner(suborPLY)) {

			String riadok = scanner.nextLine();
			hlavickaSuboru = hlavickaSuboru + riadok + "\n";
			if (!riadok.equals("ply")) {
				return null;
			}

			int pocetKX = 0;
			int pocetKY = 0;
			int pocetKZ = 0;
			int pocetKXNormaly = 0;
			int pocetKYNormaly = 0;
			int pocetKZNormaly = 0;

			int stlpecSuradniceX = -1;
			int stlpecSuradniceY = -1;
			int stlpecSuradniceZ = -1;
			int stlpecSuradniceXNorm = -1;
			int stlpecSuradniceYNorm = -1;
			int stlpecSuradniceZNorm = -1;

			boolean suNormaly = false;

			// nacitanie poctu vrcholov, hran a stlpcou so suradnicami
			while (!riadok.equals("end_header")) {
				riadok = scanner.nextLine();
				hlavickaSuboru = hlavickaSuboru + riadok + "\n";

				if (riadok.startsWith("element vertex ")) {
					String pocet = riadok.replace("element vertex ", "");
					pocetVrcholov = Integer.parseInt(pocet);
					continue;
				}

				if (riadok.startsWith("element face ")) {
					String pocet = riadok.replace("element face ", "");
					pocetHran = Integer.parseInt(pocet);
					continue;
				}

				if (riadok.startsWith("property ")) {

					switch (riadok) {
					case "property float x":
						pocetKX++;
						stlpecSuradniceX = pocetKX;
						break;
					case "property float y":
						pocetKY++;
						stlpecSuradniceY = pocetKY;
						break;
					case "property float z":
						pocetKZ++;
						stlpecSuradniceZ = pocetKZ;
						break;
					case "property float nx":
						pocetKXNormaly++;
						stlpecSuradniceXNorm = pocetKXNormaly;
						break;
					case "property float ny":
						pocetKYNormaly++;
						stlpecSuradniceYNorm = pocetKYNormaly;
						break;
					case "property float nz":
						pocetKZNormaly++;
						stlpecSuradniceZNorm = pocetKZNormaly;
						break;
					}
					// vzdy prirata 1 ku kazdemu stlpcu
					pocetKX++;
					pocetKY++;
					pocetKZ++;
					pocetKXNormaly++;
					pocetKYNormaly++;
					pocetKZNormaly++;
					continue;

				}

			}
			hlavickaSuboru.trim();

			// overenie ci sa podarilo vsetko nacitat
			if (pocetVrcholov == -1 || pocetHran == -1 || stlpecSuradniceX == -1 || stlpecSuradniceY == -1
					|| stlpecSuradniceZ == -1) {
				return null;
			}

			if (stlpecSuradniceXNorm == -1 && stlpecSuradniceYNorm == -1 && stlpecSuradniceZNorm == -1) {
				suNormaly = false;
			} else {
				if (stlpecSuradniceXNorm != -1 && stlpecSuradniceYNorm != -1 && stlpecSuradniceZNorm != -1) {
					suNormaly = true;
				} else {
					// nie je pripad ze su vsetky alebo ziadna
					return null;
				}
			}

			vrcholy = new Vertex[pocetVrcholov];

			// nacitanie suradnic
			for (int i = 0; i < pocetVrcholov; i++) {
				riadok = scanner.nextLine();

				String[] hodnoty = riadok.split(" ");
				float suradnicaX = Float.parseFloat(hodnoty[stlpecSuradniceX - 1]);
				float suradnicaY = Float.parseFloat(hodnoty[stlpecSuradniceY - 1]);
				float suradnicaZ = Float.parseFloat(hodnoty[stlpecSuradniceZ - 1]);

				if (suNormaly) {
					float suradnicaXNormaly = Float.parseFloat(hodnoty[stlpecSuradniceXNorm - 1]);
					float suradnicaYNormaly = Float.parseFloat(hodnoty[stlpecSuradniceYNorm - 1]);
					float suradnicaZNormaly = Float.parseFloat(hodnoty[stlpecSuradniceZNorm - 1]);

					vrcholy[i] = new Vertex(i, riadok, suradnicaX, suradnicaY, suradnicaZ, suradnicaXNormaly,
							suradnicaYNormaly, suradnicaZNormaly);
				} else {
					vrcholy[i] = new Vertex(i, riadok, suradnicaX, suradnicaY, suradnicaZ, 0, 0, 0);
				}

				// hladanie globalneho maxima a minima
				if (maxZ < suradnicaZ) {
					maxZ = suradnicaZ;
					idxGlobalnehoMaxima = i;
				}
				if (minZ > suradnicaZ) {
					minZ = suradnicaZ;
					idxGlobalnehoMinima = i;
				}
			}

			// nacitanie susedov podla informacii o tom,
			// ze ktore indexi vrcholov tvoria spolu trojuholnik
			for (int i = 0; i < pocetHran; i++) {
				riadok = scanner.nextLine();
				String[] hodnoty = riadok.split(" ");
				int vrchol1 = Integer.parseInt(hodnoty[1]);
				int vrchol2 = Integer.parseInt(hodnoty[2]);
				int vrchol3 = Integer.parseInt(hodnoty[3]);

				// poradie pri paroch susedov je dolezite aby bola zachovana informacia o ich
				// poradi pre orientaciu steny
				// stena ma spravne poradie: vrchol, prvy z paru, druhy z paru
				vrcholy[vrchol1].pridajSuseda(vrchol2);
				vrcholy[vrchol1].pridajSuseda(vrchol3);
				vrcholy[vrchol1].pridajParSusedov(new int[] { vrchol2, vrchol3 });

				vrcholy[vrchol2].pridajSuseda(vrchol1);
				vrcholy[vrchol2].pridajSuseda(vrchol3);
				vrcholy[vrchol2].pridajParSusedov(new int[] { vrchol3, vrchol1 });

				vrcholy[vrchol3].pridajSuseda(vrchol1);
				vrcholy[vrchol3].pridajSuseda(vrchol2);
				vrcholy[vrchol3].pridajParSusedov(new int[] { vrchol1, vrchol2 });

			}

		} catch (FileNotFoundException e) {
			System.err.println(e.toString());
			return null;
		} catch (Exception e) {
			System.err.println(e.toString());
			return null;
		}

		return new NacitanySubor(hlavickaSuboru, vrcholy, idxGlobalnehoMaxima, idxGlobalnehoMinima);
	}

	/**
	 * Vyrobi jeden alebo viac suborov, do ktorych zapise zadane vrcholy.
	 * 
	 * @param hlavickaSuboru
	 *            hlavicka povodeho suboru PLY z ktoreho su zadane vrcholy
	 * @param cielovyAdresar
	 *            do ktoreho sa maju ulozit novovytvorene subory
	 * @param nazovSuboru
	 *            nazov noveho suboru do ktoreho sa ulozia zadane vrcholi
	 * @param skupinyVrcholov
	 *            medzi vrcholmi, ktore su vramci toho isteho zoznamu sa vedia
	 *            zapisat aj steny, ktore medzi nimi boli v povodnom subore PLY
	 *            (podla parametra ajSteny)
	 * @param ajSteny
	 *            true ak ma do suboru zapisat aj steny medzi zapisovanymi vrcholmi
	 *            z povodneho 3D modelu
	 * @param comment
	 *            text, ktory sa ma zapisat do comentara v hlacicke suboru PLY
	 * @param doJednehoSuboru
	 *            true, ak chceme skupinyVrcholov zapisat spolu do jedneho suboru
	 *            (steny medzi vrcholmi z dvoch roznych skupin sa nezapisu); false,
	 *            ak chceme vyrobit pre kazdu skupinu vrcholov zo zadanej mnoziny
	 *            osobitny subor
	 * @return true ak sa podarilo vyrobit vsetky pozadovane subory
	 */
	public static boolean vyrobSuborPLY(String hlavickaSuboru, String cielovyAdresar, String nazovSuboru,
			Set<List<Vertex>> skupinyVrcholov, boolean ajSteny, String comment, boolean doJednehoSuboru) {

		if (!new File(cielovyAdresar).isDirectory()) {
			return false;
		}

		if (doJednehoSuboru) {
			// da skupiny vrcholov do jedneho suboru a necha len steny medzi vrcholmi

			File zapisovanySubor = najdiNepouzituCestuKSuboru(cielovyAdresar + "\\" + nazovSuboru);

			return vyrobenieAZapisVrcholovAStienSkupinyVrcholovDoJedneho(hlavickaSuboru, zapisovanySubor,
					skupinyVrcholov, ajSteny, comment);

		} else {
			// da kazdu skupinu vrcholov do samostatneho suboru
			boolean podariloSaVyrobitVsetky = true;

			int pocitadloSuborov = 1;
			for (List<Vertex> vrcholy : skupinyVrcholov) {

				File zapisovanySubor = najdiNepouzituCestuKSuboru(
						cielovyAdresar + "\\" + nazovSuboru + "_" + pocitadloSuborov);

				if (!vyrobenieAZapisVrcholovAStien(hlavickaSuboru, zapisovanySubor, vrcholy, ajSteny, comment)) {
					podariloSaVyrobitVsetky = false;
				}
				pocitadloSuborov++;
			}

			return podariloSaVyrobitVsetky;
		}
	}

	/**
	 * Vyrobi subor, do ktoreho zapise zadane vrcholy.
	 * 
	 * @param hlavickaSuboru
	 *            hlavicka povodeho suboru PLY z ktoreho su zadane vrcholy
	 * @param cielovyAdresar
	 *            do ktoreho sa ma ulozit novovytvoreny subor
	 * @param nazovSuboru
	 *            nazov noveho suboru do ktoreho sa ulozia zadane vrcholi
	 * @param vrcholy
	 *            ktore chceme zapisat
	 * @param ajSteny
	 *            true ak ma do suboru zapisat aj steny medzi zapisovanymi vrcholmi
	 *            z povodneho 3D modelu
	 * @param comment
	 *            text, ktory sa ma zapisat do comentara v hlacicke suboru PLY
	 * @return true ak sa podarilo vyrobit subor
	 */
	public static boolean vyrobSuborPLY(String hlavickaSuboru, String cielovyAdresar, String nazovSuboru,
			List<Vertex> vrcholy, boolean ajSteny, String comment) {

		if (!new File(cielovyAdresar).isDirectory()) {
			return false;
		}

		File zapisovanySubor = najdiNepouzituCestuKSuboru(cielovyAdresar + "\\" + nazovSuboru);

		if (!vyrobenieAZapisVrcholovAStien(hlavickaSuboru, zapisovanySubor, vrcholy, ajSteny, comment)) {
			return false;
		}
		return true;
	}

	/**
	 * Zistuje, ze ci subor so zadanym nazvom v zadanom adresari uz existuje, a ak
	 * ano tak zvisuje cislo za nazvom suboru az kym nenajde taky nazov, ktory este
	 * v tomto adresari nie je
	 * 
	 * @param adresarANazovBezKoncovky
	 *            cesta k suboru, ktory chceme vyrobit, zadana bez koncovky suboru
	 * @return cestu k suboru, ktory este neexistuje takze taky subor mozeme vyrobit
	 *         a zapisovat do neho bez toho aby sme nieco premazali
	 */
	private static File najdiNepouzituCestuKSuboru(String adresarANazovBezKoncovky) {

		int pocitadloRovnakych = 1;
		String cestaKZapisovanemuSuboru = adresarANazovBezKoncovky + ".ply";
		File zapisovanySubor = new File(cestaKZapisovanemuSuboru);

		// hladame take aby este neexistoval
		while (zapisovanySubor.isFile()) {
			cestaKZapisovanemuSuboru = adresarANazovBezKoncovky + "_(" + pocitadloRovnakych + ")" + ".ply";
			zapisovanySubor = new File(cestaKZapisovanemuSuboru);
			pocitadloRovnakych++;
		}

		return zapisovanySubor;
	}

	/**
	 * Vyrobi zoznam vrcholov pre zapis (dokopi vsetky zo vsetkych skupin vrcholov)
	 * a mnozinu stien pre zapis (ak je zadane, ze treba a len tie steny, ktore su
	 * medzi vrcholmi z tej istej skupiny). Zavola metodu pre samotne vyrobenie
	 * suboru a zapis mnoziny vrcholov a stien so suboru.
	 * 
	 * @param hlavickaSuboru
	 *            hlavicka povodeho suboru z ktoreho su zadane vrcholy
	 * @param novySubor
	 *            ktory sa ma vyrobit
	 * @param skupinyVrcholov
	 *            medzi vrcholmi, ktore su vramci toho isteho zoznamu sa vedia
	 *            zapisat aj steny, ktore medzi nimi boli v povodnom subore PLY
	 *            (podla parametra ajSteny)
	 * @param ajSteny
	 *            true ak ma do suboru zapisat aj trojuholniky medzi zapisovanymi
	 *            vrcholmi z povodneho 3D modelu
	 * @param comment
	 *            text, ktory sa ma zapisat do comentara v hlacicke suboru PLY
	 * @return true ak vyrobilo subor
	 */
	private static boolean vyrobenieAZapisVrcholovAStienSkupinyVrcholovDoJedneho(String hlavickaSuboru, File novySubor,
			Set<List<Vertex>> skupinyVrcholov, boolean ajSteny, String comment) {

		List<Vertex> vrcholyPreZapis = new ArrayList<>();
		Set<List<Integer>> steny = new HashSet<>();

		if (!ajSteny) {
			for (List<Vertex> jedenStalaktit : skupinyVrcholov) {
				for (Vertex vrchol : jedenStalaktit) {
					vrcholyPreZapis.add(vrchol);
				}
			}
		} else {// naplni steny stenami, ktore patria len jednemu stalaktitu

			// index vrchola vo vystupnom subore PLY
			int aktualnyIdx = -1;
			for (List<Vertex> jedenStalaktit : skupinyVrcholov) {

				Map<Integer, Integer> preindexovanie = new HashMap<>();
				Set<List<Integer>> stenyPovodneIdx = new HashSet<>();

				for (Vertex vrchol : jedenStalaktit) {

					vrcholyPreZapis.add(vrchol);
					aktualnyIdx++;

					preindexovanie.put(vrchol.getIdxZPLY(), aktualnyIdx);

					List<int[]> parySusedov = vrchol.getParySusedovPreTrojuholniky();

					// naplnenie stenami tak, aby najmensi idx bol na prvom mieste,
					// koli tomu aby list idx hrany bol od kazdeho vrcholu rovnaky, takze sa do
					// mnoziny dostane kazda stena len raz.
					for (int j = 0; j < parySusedov.size(); j++) {
						int idxVrchol = vrchol.getIdxZPLY();
						int idxPar0 = parySusedov.get(j)[0];
						int idxPar1 = parySusedov.get(j)[1];

						// vrchol s najmensim indexom je na zaciatku
						if (idxVrchol < idxPar0 && idxVrchol < idxPar1) {
							stenyPovodneIdx.add(Arrays.asList(idxVrchol, idxPar0, idxPar1));
						} else {
							if (idxPar0 < idxPar1) {
								stenyPovodneIdx.add(Arrays.asList(idxPar0, idxPar1, idxVrchol));
							} else {
								stenyPovodneIdx.add(Arrays.asList(idxPar1, idxVrchol, idxPar0));
							}
						}
					}
				}

				// naplnenie mnoziny stien s indexmi pre zapis
				for (List<Integer> list : stenyPovodneIdx) {
					List<Integer> stenaPreZapis = new ArrayList<>();

					stenaPreZapis.add(preindexovanie.get(list.get(0)));
					stenaPreZapis.add(preindexovanie.get(list.get(1)));
					stenaPreZapis.add(preindexovanie.get(list.get(2)));

					if (!stenaPreZapis.contains(null)) {
						steny.add(stenaPreZapis);
					}
				}
			}
		}

		return zapisanieDoSuboruPLY(hlavickaSuboru, novySubor, vrcholyPreZapis, steny, comment);
	}

	/**
	 * Vyrobi mnozinu stien pre zapis (ak je zadane, ze treba). Zavola metodu pre
	 * samotne vyrobenie suboru a zapis mnoziny vrcholov a stien so suboru.
	 * 
	 * @param hlavickaSuboru
	 *            hlavicka povodeho suboru z ktoreho su zadane vrcholy
	 * @param novySubor
	 *            ktory sa ma vyrobit
	 * @param vrcholy
	 *            ktore maju byt zapisane
	 * @param ajSteny
	 *            true ak ma do suboru zapisat aj trojuholniky medzi zapisovanymi
	 *            vrcholmi z povodneho 3D modelu
	 * @param comment
	 *            text, ktory sa ma zapisat do comentara v hlacicke suboru PLY
	 * @return true ak vyrobilo subor
	 */
	private static boolean vyrobenieAZapisVrcholovAStien(String hlavickaSuboru, File novySubor, List<Vertex> vrcholy,
			boolean ajSteny, String comment) {

		Set<List<Integer>> steny = new HashSet<>();

		// naplni mnozinu stien ak ich treba pre zapis do suboru
		if (ajSteny) {
			// mapovanie z indexov vrcholov z povodneho suboru PLY na indexi, ktore budu mat
			// po zapise do PLY
			// koli tomu, aby sedeli indexi pre zapis hran
			Map<Integer, Integer> preindexovanie = new HashMap<>();

			// mnozina stien v povodnom indexovani
			Set<List<Integer>> stenyPovodneIdx = new HashSet<>();

			for (int i = 0; i < vrcholy.size(); i++) {
				Vertex vrchol = vrcholy.get(i);
				preindexovanie.put(vrchol.getIdxZPLY(), i);
				List<int[]> parySusedov = vrchol.getParySusedovPreTrojuholniky();

				// naplnenie stenami tak, aby najmensi idx bol na prvom mieste,
				// koli tomu aby list idx hrany bol od kazdeho vrcholu rovnaky, takze sa do
				// mnoziny dostane kazda stena len raz.
				for (int j = 0; j < parySusedov.size(); j++) {
					int idxVrchol = vrchol.getIdxZPLY();
					int idxPar0 = parySusedov.get(j)[0];
					int idxPar1 = parySusedov.get(j)[1];

					// vrchol s najmensim indexom je na zaciatku
					if (idxVrchol < idxPar0 && idxVrchol < idxPar1) {
						stenyPovodneIdx.add(Arrays.asList(idxVrchol, idxPar0, idxPar1));
					} else {
						if (idxPar0 < idxPar1) {
							stenyPovodneIdx.add(Arrays.asList(idxPar0, idxPar1, idxVrchol));
						} else {
							stenyPovodneIdx.add(Arrays.asList(idxPar1, idxVrchol, idxPar0));
						}
					}
				}
			}

			// naplnenie mnoziny stien s indexmi pre zapis
			for (List<Integer> list : stenyPovodneIdx) {
				List<Integer> stenaPreZapis = new ArrayList<>();

				stenaPreZapis.add(preindexovanie.get(list.get(0)));
				stenaPreZapis.add(preindexovanie.get(list.get(1)));
				stenaPreZapis.add(preindexovanie.get(list.get(2)));

				if (!stenaPreZapis.contains(null)) {
					steny.add(stenaPreZapis);
				}
			}

		}

		return zapisanieDoSuboruPLY(hlavickaSuboru, novySubor, vrcholy, steny, comment);

	}

	/**
	 * Vyrobi novy subor PLY, ktoremu vyrobi hlavicku podla ulozenej hlavicky a
	 * zapise do neho vrcholy a steny, ktore su zadane v parametri
	 * 
	 * @param novySubor
	 *            ktory sa ma vyrobit
	 * @param vrcholyPreZapis
	 *            zoznam vrcholov, ktore treba zapisat do suboru
	 * @param steny
	 *            zoznam stien, ktore treba zapisat do suboru
	 * @param comment
	 *            text, ktory sa ma zapisat do comentara v hlacicke suboru PLY
	 * @return true ak sa podarilo vyrobit subor
	 */
	private static boolean zapisanieDoSuboruPLY(String hlavickaSuboru, File novySubor, List<Vertex> vrcholyPreZapis,
			Set<List<Integer>> steny, String comment) {

		try (Scanner scanner = new Scanner(hlavickaSuboru); PrintWriter zapisovac = new PrintWriter(novySubor)) {

			// zapis hlavicky suboru
			while (scanner.hasNextLine()) {
				String riadok = scanner.nextLine();

				if (riadok.startsWith("ply") || riadok.startsWith("format") || riadok.startsWith("property")
						|| riadok.startsWith("end_header")) {
					zapisovac.println(riadok);
				}

				if (riadok.startsWith("element vertex")) {
					zapisovac.println("element vertex " + vrcholyPreZapis.size());
				}

				if (riadok.startsWith("element face")) {
					zapisovac.println("element face " + steny.size());
				}

				if (riadok.startsWith("comment")) {
					zapisovac.println("comment " + comment);
				}
			}

			// zapis vrcholov stalaktitu
			for (int i = 0; i < vrcholyPreZapis.size(); i++) {
				zapisovac.println(vrcholyPreZapis.get(i).getRiadokZaznamuVrcholaZPLY());
			}
			// zapis stien
			for (List<Integer> stena : steny) {
				zapisovac.print("3 " + stena.get(0) + " " + stena.get(1) + " " + stena.get(2));
				zapisovac.println();
			}

		} catch (FileNotFoundException e) {
			return false;
		}

		return true;
	}

	public static boolean suLenPovoleneZnaky(String nazov) {

		for (int i = 0; i < nazov.length(); i++) {
			char znak = nazov.charAt(i);

			if (znak == '\\' || znak == '/' || znak == ':' || znak == '*' || znak == '?' || znak == '\"' || znak == '<'
					|| znak == '>' || znak == '|') {
				return false;
			}
		}

		return true;
	}

}
