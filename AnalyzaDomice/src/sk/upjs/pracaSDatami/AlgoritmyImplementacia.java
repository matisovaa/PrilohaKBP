package sk.upjs.pracaSDatami;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import sk.upjs.entity.Elipsa;
import sk.upjs.entity.NacitanySubor;
import sk.upjs.entity.Vertex;

public class AlgoritmyImplementacia implements Algoritmy {

	private final NacitanySubor nacitanySubor;

	public AlgoritmyImplementacia(NacitanySubor nacitanySubor) {
		this.nacitanySubor = nacitanySubor;
	}

	public boolean[] lokalneMinimaCeleho(boolean zohladnovatNormaly) {
		if (nacitanySubor == null) {
			return null;
		}

		int pocetVrcholov = nacitanySubor.getPocetVrcholov();
		boolean[] minima = new boolean[pocetVrcholov];

		// algoritmus z literatury upraveny
		for (int i = 0; i < pocetVrcholov; i++) {
			Vertex vrchol = nacitanySubor.getVrchol(i);
			List<Integer> susedia = vrchol.getSusedia();
			boolean jeMinimum = true;
			for (int j = 0; j < susedia.size(); j++) {
				if (!(vrchol.getSuradnicaZ() <= nacitanySubor.getVrchol(susedia.get(j)).getSuradnicaZ())) {
					jeMinimum = false;
					break;
				}
				if (zohladnovatNormaly && !(vrchol.getSuradnicaZNorm() < 0)) {
					jeMinimum = false;
					break;
				}
			}

			if (jeMinimum) {
				minima[vrchol.getIdxZPLY()] = true;
			}
		}

		return minima;
	}

	public List<Vertex> jedenStalaktitPoPrveRozvetvenie(int idxMinimaStalaktitu) {
		if (nacitanySubor == null) {
			return null;
		}

		// pomocou tree set, aby boli usporiadane podla z
		TreeSet<Vertex> rad = new TreeSet<>();
		boolean[] bolVRade = new boolean[nacitanySubor.getPocetVrcholov()];
		// najnisi bod ohybu okolo stalaktitu
		boolean nastalOhyb = false;
		// vrcholy tvoriace stalaktit
		List<Vertex> stalaktit = new ArrayList<>();

		if (idxMinimaStalaktitu >= nacitanySubor.getPocetVrcholov() || idxMinimaStalaktitu < 0) {
			return null;
		}

		Vertex minimum = nacitanySubor.getVrchol(idxMinimaStalaktitu);
		rad.add(minimum);
		bolVRade[idxMinimaStalaktitu] = true;

		// pokial nie je nad urovnou vyseku stalaktitu
		while (!nastalOhyb) {
			if (rad.isEmpty()) {
				break;
			}

			Vertex vybraty = rad.pollFirst();

			stalaktit.add(vybraty);
			List<Integer> susedia = vybraty.getSusedia();
			for (Integer idxSuseda : susedia) {
				if (nastalOhyb) {
					break;
				}
				if (!bolVRade[idxSuseda]) {
					// oznacime, ze sme ho prehodnocovali na pridanie
					bolVRade[idxSuseda] = true;
					Vertex sused = nacitanySubor.getVrchol(idxSuseda);

					rad.add(sused);

					// hlada suseda, ktory nebol v rade a ma mensiu Z (ohyb, ktory znamena
					// rozvetvenie)
					if (sused.getSuradnicaZ() < vybraty.getSuradnicaZ()) {
						nastalOhyb = true;
					}
				}
			}
		}
		return stalaktit;
	}

	public boolean[] oddelitStropAStalaktity(float uholMaxOdchylky, int idxVrcholuZoStropu) {
		if (nacitanySubor == null) {			
			return null;
		}

		// rad vrcholov, ktore este nemali prehodnotene svoje okolite steny
		LinkedList<Vertex> radPreStrop = new LinkedList<>();
		// v rade pre strop, takze patri stropu
		boolean[] bolVRade = new boolean[nacitanySubor.getPocetVrcholov()];

		if (idxVrcholuZoStropu >= nacitanySubor.getPocetVrcholov() || idxVrcholuZoStropu < 0) {			
			return null;
		}

		radPreStrop.add(nacitanySubor.getVrchol(idxVrcholuZoStropu));
		bolVRade[idxVrcholuZoStropu] = true;

		while (!radPreStrop.isEmpty()) {
			Vertex vybraty = radPreStrop.pollFirst();

			List<int[]> pary = vybraty.getParySusedovPreTrojuholniky();

			for (int[] par : pary) {
				if (bolVRade[par[0]] && bolVRade[par[1]]) {
					continue;
				}

				Vertex vrchol0 = nacitanySubor.getVrchol(par[0]);
				Vertex vrchol1 = nacitanySubor.getVrchol(par[1]);

				// suradnice vektora z vybraneho do vrcholu 0
				float[] vektorVybratyV0 = new float[] { vrchol0.getSuradnicaX() - vybraty.getSuradnicaX(),
						vrchol0.getSuradnicaY() - vybraty.getSuradnicaY(),
						vrchol0.getSuradnicaZ() - vybraty.getSuradnicaZ() };
				// suradnice vektora z vybraneho do vrcholu 1
				float[] vektorVybratyV1 = new float[] { vrchol1.getSuradnicaX() - vybraty.getSuradnicaX(),
						vrchol1.getSuradnicaY() - vybraty.getSuradnicaY(),
						vrchol1.getSuradnicaZ() - vybraty.getSuradnicaZ() };

				float[] normalovyTrojuholnika = new float[] {
						vektorVybratyV0[1] * vektorVybratyV1[2] - vektorVybratyV0[2] * vektorVybratyV1[1],
						vektorVybratyV0[2] * vektorVybratyV1[0] - vektorVybratyV0[0] * vektorVybratyV1[2],
						vektorVybratyV0[0] * vektorVybratyV1[1] - vektorVybratyV0[1] * vektorVybratyV1[0] };

				float uhol = uholDvochVektorov(normalovyTrojuholnika, new float[] { 0, 0, -1 });

				if (uhol <= uholMaxOdchylky) {
					if (!bolVRade[par[0]]) {
						radPreStrop.add(vrchol0);
						bolVRade[par[0]] = true;
					}
					if (!bolVRade[par[1]]) {
						radPreStrop.add(vrchol1);
						bolVRade[par[1]] = true;
					}
				}
			}
		}
		return bolVRade;
	}

	/**
	 * Vyrata uhol medzi dvoma zadanymi vektormi, ktore maju tri suradnice
	 * 
	 * @param vektor0
	 * @param vektor1
	 * @return uhol v stupnoch medzi vektorom 0 a 1
	 */
	private float uholDvochVektorov(float[] vektor0, float[] vektor1) {
		return (float) (Math
				.acos(Math.abs(vektor0[0] * vektor1[0] + vektor0[1] * vektor1[1] + vektor0[2] * vektor1[2])
						/ (Math.sqrt(vektor0[0] * vektor0[0] + vektor0[1] * vektor0[1] + vektor0[2] * vektor0[2]) * Math
								.sqrt(vektor1[0] * vektor1[0] + vektor1[1] * vektor1[1] + vektor1[2] * vektor1[2])))
				* 180 / Math.PI);
	}

	public List<Vertex> minimaNepatriaceStropu(boolean[] lokalneMinimaCeleho, boolean[] stropAStalaktity) {
		if (nacitanySubor == null) {
			return null;
		}

		// vrcholy, kde je lokalne min a neni to oznacene ako strop
		List<Vertex> minima = new ArrayList<>();

		for (int i = 0; i < nacitanySubor.getPocetVrcholov(); i++) {			
			if (lokalneMinimaCeleho[i] && !stropAStalaktity[i]) {
				minima.add(nacitanySubor.getVrchol(i));
			}
		}

		return minima;
	}

	public boolean[] vyseparovanieStalaktitov(List<Vertex> minimaNepatriaceStropu, boolean[] stropAStalaktity) {

		// pole kde bude nakonci false na indexoch vrcholov, ktore patria finalnym
		// stalaktitom
		boolean[] finalneStalaktity = new boolean[nacitanySubor.getPocetVrcholov()];
		Arrays.fill(finalneStalaktity, true);

		LinkedList<Vertex> rad = new LinkedList<>();
		rad.addAll(minimaNepatriaceStropu);
		// true maju vrcholy, ktore patria stropu takze ich nechame ako zaznacene, ze uz
		// boli v rade
		boolean[] bolVRade = Arrays.copyOf(stropAStalaktity, stropAStalaktity.length);
		for (Vertex minimum : minimaNepatriaceStropu) {
			bolVRade[minimum.getIdxZPLY()] = true;
		}

		while (!rad.isEmpty()) {
			Vertex vybrany = rad.pollFirst();
			int idxVybraneho = vybrany.getIdxZPLY();
			float suradnicaZVybraneho = vybrany.getSuradnicaZ();

			if (stropAStalaktity[idxVybraneho]) {
				// Vrchol je vrcholom stropu
				continue;
			} else {
				// Vrchol nie je vrcholom stropu, takze patri stalaktitom
				finalneStalaktity[idxVybraneho] = false;
			}

			List<Integer> susedia = vybrany.getSusedia();
			for (Integer idxSuseda : susedia) {
				Vertex sused = nacitanySubor.getVrchol(idxSuseda);
				// do radu pridame suseda, ktory ma vacsiu alebo rovnu suradnicu Z ako vybrany
				// vrchol a este nebol v rade
				if (sused.getSuradnicaZ() >= suradnicaZVybraneho && !bolVRade[idxSuseda]) {
					rad.add(sused);
					bolVRade[idxSuseda] = true;
				}
			}
		}

		return finalneStalaktity;
	}

	public Set<List<Vertex>> komoponentySuvislosti(boolean[] vrcholyPreRozdelenie) {

		Set<List<Vertex>> komponentySuvislosti = new HashSet<>();

		int dlzka = vrcholyPreRozdelenie.length;
		boolean[] uzZaradeny = Arrays.copyOf(vrcholyPreRozdelenie, dlzka);
		int idxUzPrejdene = 0;

		while (idxUzPrejdene < dlzka) {
			// ak ma vrchol true tak ho neriesime a ideme na dalsi
			if (uzZaradeny[idxUzPrejdene]) {
				idxUzPrejdene++;
				continue;
			}

			// ked sme sa dostali tu tak sme nasli vrchol, ktory patri novemu, zatial
			// nezaradenemu komponentu suvislosti

			LinkedList<Vertex> radKomponentu = new LinkedList<>();
			radKomponentu.add(nacitanySubor.getVrchol(idxUzPrejdene));
			uzZaradeny[idxUzPrejdene] = true;
			List<Vertex> komponentSuvislosti = new ArrayList<>();

			while (!radKomponentu.isEmpty()) {
				Vertex vybrany = radKomponentu.pollFirst();
				komponentSuvislosti.add(vybrany);

				List<Integer> susedia = vybrany.getSusedia();
				for (Integer idxSuseda : susedia) {
					if (!uzZaradeny[idxSuseda]) {
						radKomponentu.add(nacitanySubor.getVrchol(idxSuseda));
						uzZaradeny[idxSuseda] = true;
					}
				}
			}

			komponentySuvislosti.add(komponentSuvislosti);
		}

		return komponentySuvislosti;
	}

	/**
	 * Vyrata objem zadaneho stalaktitu nasledovne: Pre kazdy usek stalaktitu, ktory ma
	 * vysku podla osi Z ako je zadana vyska useku prejde suradnicami vsetkych
	 * bodov, ktore danemu useku patria (ich suradnica Z ma hodnotu medzi Z
	 * suradnicami vysky useku) a zistime maximalnu a minimalnu suradnicu X a Y zo
	 * vsetkych tychto bodov. Z tychto zistenych hodnot maximalnej a minimalnej
	 * suradnice X a Y vyrobime elipsu, ktorej obsah by mal byt obsahom prierezu na
	 * vrchu useku. Vysledny objem je suctom objemov telies, ktore maju ako podstavy
	 * dve elipsy po sebe masledujucich usekov a vyska tohto telesa je vyska useku
	 * (pri najvrchnejsom telese je vyska rozdiel suradnice Z bodu s najvyssou
	 * suradnicou Z a suradnicou Z predchadzajucej elipsy)
	 * 
	 * @param stalaktit
	 *            zoznam vrcholov 3D modelu, ktore predpokladame, ze patria jednemu
	 *            stalaktitu, ktory nema rozvetvenia
	 * @param minimumStalaktitu
	 *            vrchol tohto stalaktitu, ktory je jeho minimom
	 * @param vyskaUseku
	 *            rozsah suradnic Z vrcholov, ktore este budeme povazovat,ze patria
	 *            jednemu useku stalaktitu
	 * @return objem zadaneho stalaktitu
	 */
	private double objemStalaktituBezRozvetveni(List<Vertex> stalaktit, Vertex minimumStalaktitu, float vyskaUseku) {

		List<Float> maxVyskyUsekov = new ArrayList<>();
		List<Elipsa> elipsyUseku = new ArrayList<>();

		// prva elipsa prislucha minimu stalaktitu a je to len jeden bod
		float suradnicaZMinima = minimumStalaktitu.getSuradnicaZ();
		maxVyskyUsekov.add(suradnicaZMinima);
		elipsyUseku.add(new Elipsa(minimumStalaktitu.getSuradnicaX(), minimumStalaktitu.getSuradnicaX(),
				minimumStalaktitu.getSuradnicaY(), minimumStalaktitu.getSuradnicaY(), suradnicaZMinima));

		// najdenie suradnic elips na vsetkych hraniciach usekov
		for (Vertex bod : stalaktit) {
			// ak predosle z suradnice nemam tak vsetky do listu pridam a k nim pridam
			// elipsi s min a max value
			// elipsyUseku.

			float suradnicaZ = bod.getSuradnicaZ();

			float vzdialenostOdMinima = suradnicaZ - suradnicaZMinima;
			int indexElipsy = (int) (Math.floor(vzdialenostOdMinima / vyskaUseku) + 1);

			if (indexElipsy < maxVyskyUsekov.size()) {
				// uz na tomto useku mame elipsu
				Elipsa e = elipsyUseku.get(indexElipsy);
				e.setMaxX(bod.getSuradnicaX());
				e.setMinX(bod.getSuradnicaX());
				e.setMaxY(bod.getSuradnicaY());
				e.setMinY(bod.getSuradnicaY());
				e.setMaxZ(bod.getSuradnicaZ());
			} else {
				// na tomto useku este nemame elipsu, lebo ma vyssiu suradnicu naz sme doteraz
				// mali
				// dame hodnoty medzi aktualne pridavanu elipsu a tie co uz su
				for (int i = maxVyskyUsekov.size(); i < indexElipsy; i++) {
					maxVyskyUsekov.add(maxVyskyUsekov.get(i - 1) + vyskaUseku);
					elipsyUseku.add(new Elipsa(Float.MAX_VALUE, Float.MIN_VALUE, Float.MAX_VALUE, Float.MIN_VALUE,
							Float.MIN_VALUE));
				}
				maxVyskyUsekov.add(maxVyskyUsekov.get(maxVyskyUsekov.size() - 1) + vyskaUseku);
				elipsyUseku.add(new Elipsa(bod.getSuradnicaX(), bod.getSuradnicaX(), bod.getSuradnicaY(),
						bod.getSuradnicaY(), bod.getSuradnicaZ()));
			}
		}

		// Odstranenie defaultnych elips s hodnotami Float.MAX_VALUE, Float.MIN_VALUE
		// prva a posledna nebudu defaultne, lebo defaultne sa vkladaju len medzi
		// existujuce elipsy
		List<Elipsa> defaultne = new ArrayList<>();
		for (int i = 1; i < elipsyUseku.size() - 1; i++) {

			if (elipsyUseku.get(i).getMaxZ() == Float.MIN_VALUE) {
				defaultne.add(elipsyUseku.get(i));
			} else {
				// nastavime vsetkym elipsam okrem prvej a poslednej suradnicu Z na vysku,
				// ktorej prierez predstavuju posledne teleso bude mat mensiu vysku nez vyska
				// useku
				elipsyUseku.get(i).setMaxZ(suradnicaZMinima + i * vyskaUseku);
			}
		}

		elipsyUseku.removeAll(defaultne);

		// zratanie samotneho objemu
		double objem = 0;
		for (int i = 1; i < elipsyUseku.size(); i++) {
			Elipsa eNissie = elipsyUseku.get(i - 1);
			Elipsa eVyssie = elipsyUseku.get(i);
			objem += objemTelesa(eNissie, eVyssie, eVyssie.getMaxZ() - eNissie.getMaxZ());
		}

		return objem;
	}

	/**
	 * Rata objem zrezaneho kuzela s elipsovimi podstavami. Rata to podla vzorca na
	 * vypocet objemu zrezaneho kuzela, lebo podla Cavalieriho principu mozeme
	 * elipsove podstavy zamenit za kruhy s rovnaky obsahom a pri tej istej vyske
	 * tychto dvoch telies sa objem nezmeni.
	 * 
	 * @param podstava1
	 *            Elipsa, ktora predstavuje prvu podstavu telesa
	 * @param podstava2
	 *            Elipsa, ktora predstavuje druhu podstavu telesa
	 * @param vyska
	 *            telesa, ktoreho objem chceme vyratat
	 * @return objem telesa, ktore vznikne so zadanych parametrov
	 */
	private double objemTelesa(Elipsa podstava1, Elipsa podstava2, float vyska) {

		double obsahPodstavy1 = Math.PI * podstava1.getPoloosX() * podstava1.getPoloosY();
		double obsahPodstavy2 = Math.PI * podstava2.getPoloosX() * podstava2.getPoloosY();

		// polomer kruhu s rovnakym obsahom ako elipsova podstava
		double r1 = Math.sqrt(obsahPodstavy1 / Math.PI);
		double r2 = Math.sqrt(obsahPodstavy2 / Math.PI);

		// je to taky objem ako keby sme nechali elipsy, vdaka Cavalieriho principu
		// objem zrezaneho kuzela
		return (1.0 / 3) * Math.PI * vyska * (r1 * r1 + r1 * r2 + r2 * r2);
	}

	/**
	 * Vrati skupiny bodov, kde kazda skupina bodov by mala predstavovat jeden
	 * stalaktit (odnos stalaktitu) bez rozvetveni.
	 * 
	 * @param stropAStalaktity
	 *            true je na indexoch vrcholov, ktore by mali predstavovat strop
	 * @param minimaZadanychStalaktitov
	 *            minima, ktore su minimami stalaktitov, kde z kazdeho sa bude
	 *            vyhladavat jeden stalaktit bez rozvetveni
	 * @return mapu, kde ku kazdemu vrcholu zo zadanych minimaZadanychStalaktitov je
	 *         namapovany zoznam vrcholov, ktore patria stalaktitu bez rozvetveni
	 *         prislusneho lokalneho minima
	 */
	private Map<Vertex, List<Vertex>> rozkuskovanieStalaktitov(boolean[] stropAStalaktity, List<Vertex> minimaZadanychStalaktitov) {

		// true maju vrcholy, ktore patria stropu takze ich nechame ako zaznacene, ze uz
		// boli v rade
		boolean[] bolVRade = Arrays.copyOf(stropAStalaktity, stropAStalaktity.length);

		// minima z ktorych zacne hladanie jednotlivych kuskov stalaktitov
		TreeSet<Vertex> minimaBezStropu = new TreeSet<>(minimaZadanychStalaktitov);

		// stalaktit ide od minima a patria do neho vsetci susedia vybraneho vrcholu, ktore
		// maju vecsiu rovnu suradnicu ako vybrany vrchol
		// teoreticky by to malo zobrat hlavny stalaktit a odnose dat ako dalsie stalaktity
		Map<Vertex, List<Vertex>> stalaktityAIchMinima = new HashMap<>();

		while (!minimaBezStropu.isEmpty()) {
			Vertex minStalaktitu = minimaBezStropu.pollFirst();

			TreeSet<Vertex> radJednehoStalaktitu = new TreeSet<>();
			List<Vertex> jedenStalaktit = new ArrayList<>();
			radJednehoStalaktitu.add(minStalaktitu);
			bolVRade[minStalaktitu.getIdxZPLY()] = true;

			while (!radJednehoStalaktitu.isEmpty()) {

				// tie co boli v rade vsetky patria stalaktitu
				Vertex vybrany = radJednehoStalaktitu.pollFirst();
				jedenStalaktit.add(vybrany);

				List<Integer> susedia = vybrany.getSusedia();

				for (Integer idxSuseda : susedia) {
					// ak sused nebol v rade a jeho suradnica z je >= ako Z vybraneho tak ho pridame
					// do stalaktitu
					if (!bolVRade[idxSuseda]
							&& nacitanySubor.getVrchol(idxSuseda).getSuradnicaZ() >= vybrany.getSuradnicaZ()) {

						Vertex sused = nacitanySubor.getVrchol(idxSuseda);
						radJednehoStalaktitu.add(sused);
						bolVRade[idxSuseda] = true;
					}
				}
			}

			stalaktityAIchMinima.put(minStalaktitu, jedenStalaktit);
		}

		return stalaktityAIchMinima;
	}

	public double objemStalaktitov(boolean[] stropAStalaktity, List<Vertex> minimaZadanychStalaktitov, float vyskaUseku) {
		Map<Vertex, List<Vertex>> rozkuskovaneStalaktity = rozkuskovanieStalaktitov(stropAStalaktity, minimaZadanychStalaktitov);
		double vyslednyObjem = 0;
		Set<Vertex> minima = rozkuskovaneStalaktity.keySet();		
		for (Vertex minimum : minima) {
			if (rozkuskovaneStalaktity.get(minimum).size() < 3) {
				continue;
			}
			vyslednyObjem += objemStalaktituBezRozvetveni(rozkuskovaneStalaktity.get(minimum), minimum, vyskaUseku);
		}
		return vyslednyObjem;
	}

}
